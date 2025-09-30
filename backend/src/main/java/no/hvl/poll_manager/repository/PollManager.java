package no.hvl.poll_manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import redis.clients.jedis.UnifiedJedis;
import no.hvl.poll_manager.model.Vote;
import no.hvl.poll_manager.model.VoteOption;
import no.hvl.poll_manager.model.VoteOptionCount;

@Component
public class PollManager {

	private EntityManagerFactory emf;

	private UnifiedJedis jedis;

	private Gson gson;

	@Autowired
	RabbitTemplate rabbitTemplate;

	public PollManager() {
		EntityManagerFactory emf = new PersistenceConfiguration("polls")
				.managedClass(Poll.class)
				.managedClass(User.class)
				.managedClass(Vote.class)
				.managedClass(VoteOption.class)
				.property(PersistenceConfiguration.JDBC_URL, "jdbc:h2:file:./db")
				.property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, "update")
				.property(PersistenceConfiguration.JDBC_USER, "sa")
				.property(PersistenceConfiguration.JDBC_PASSWORD, "")
				.property("spring.h2.console.enabled", true)
				.createEntityManagerFactory();
		this.emf = emf;

		// Redis connection
		this.jedis = new UnifiedJedis("redis://localhost:6379");
		// Gson mapper to convert objects to string.
		// This simplifies saving objects with redis.
		this.gson = new Gson();
	}

	public Optional<User> addUser(String username, String email) {
		User tmp = new User(username, email);
		emf.runInTransaction(em -> {
			em.persist(tmp);
		});
		return Optional.of(tmp);
	}

	public List<User> getUsers() {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		List<User> users = em.createQuery("select u from User u", User.class).getResultList();

		em.getTransaction().commit();
		em.close();

		return users;
	}

	public Optional<User> updateUser(int id, UserRequest user) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		User u = em.find(User.class, id);

		if (u != null) {
			if (user.email() != null)
				u.setEmail(user.email());
			if (user.username() != null)
				u.setUsername(user.username());
		} else {
			return Optional.empty();
		}

		em.getTransaction().commit();
		em.close();

		return Optional.of(u);
	}

	public void deleteUser(int id) {
		emf.runInTransaction(em -> {
			User user = em.find(User.class, id);

			if (user != null) {
				em.remove(user);
			}
		});
	}

	public Optional<Poll> addPoll(PollRequest poll) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		User user = em.find(User.class, poll.creatorId());
		Poll p = user.createPoll(poll.question());
		for (VoteOption v : poll.voteOptions()) {
			// VoteOption vo = p.addVoteOption(v.getCaption());
			p.addVoteOption(v.getCaption());
		}

		em.persist(p);

		em.getTransaction().commit();
		em.close();

		createRabbitTopic(p.getId());

		return Optional.of(p);
	}

	private void createRabbitTopic(int pollId) {
		rabbitTemplate.convertAndSend("pollsExchange", "", "New poll created with ID: " + pollId);
	}

	public List<Poll> getPolls() {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		List<Poll> polls = em.createQuery("select p from Poll p", Poll.class).getResultList();

		em.getTransaction().commit();
		em.close();

		return polls;
	}

	public Optional<Poll> updatePoll(int id, PollRequest poll) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		Poll p = em.find(Poll.class, id);

		if (p != null) {
			if (poll.question() != null)
				if (poll.question() != null)
					p.setQuestion(poll.question());
			if (poll.validUntil() != null)
				p.setValidUntil(poll.validUntil());
			if (poll.voteOptions() != null)
				p.setOptions(poll.voteOptions());
		}

		em.getTransaction().commit();
		em.close();
		return Optional.of(p);
	}

	public void deletePoll(int id) {
		emf.runInTransaction(em -> {
			Poll poll = em.find(Poll.class, id);

			if (poll != null) {
				em.remove(poll);
			}
		});
	}

	public List<Vote> getVotesForPoll(int id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		List<Vote> votes = em.createQuery("SELECT v FROM Vote v WHERE v.poll.id = :id", Vote.class)
				.setParameter("id", id).getResultList();
		return votes;
	}

	private List<VoteOptionCount> retreiveVoteCountFromPollFromDB(int id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		List<VoteOptionCount> result = em.createQuery("""
				SELECT o.caption, COUNT(v.id)
				FROM VoteOption o
				INNER JOIN Vote v on o.id = v.votesOn.id
				WHERE o.poll.id = :poll_id
				GROUP BY o.presentationOrder
				ORDER BY o.presentationOrder
								""", VoteOptionCount.class).setParameter("poll_id", id).getResultList();

		em.getTransaction().commit();
		em.close();

		return result;
	}

	public VoteOptionCount[] getVoteCountForPoll(int id) {
		String cachedVoteOptionCount = jedis.get("poll:" + id);
		if (cachedVoteOptionCount == null) {
			List<VoteOptionCount> fromDB = retreiveVoteCountFromPollFromDB(id);
			String cacheString = gson.toJson(fromDB);
			// Set voteOptionCount in redis and set expiration to 200
			jedis.setex("poll:" + id, 200, cacheString);
			return fromDB.toArray(new VoteOptionCount[0]);
		} else {
			VoteOptionCount[] voc = gson.fromJson(cachedVoteOptionCount, VoteOptionCount[].class);
			return voc;
		}
	}

	public Optional<Vote> addVoteForPoll(VoteRequest vote) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		VoteOption vo = em
				.createQuery("SELECT o FROM VoteOption o WHERE o.poll.id = :poll_id AND o.caption = :caption",
						VoteOption.class)
				.setParameter("poll_id", vote.pollId()).setParameter("caption", vote.voteOption().getCaption())
				.getSingleResult();

		User u = em.find(User.class, vote.creatorId());

		Vote v = u.voteFor(vo);
		em.persist(v);

		// Invalidate/remove voteOptionCount for specific vote
		jedis.del("poll:" + vo.getPoll().getId());

		rabbitTemplate.convertAndSend("pollsExchange", "", "New vote on poll: " + vo.getPoll().getId() + " with option: "+vo.getCaption());

		em.getTransaction().commit();
		em.close();
		return Optional.of(v);
	}

	public Optional<Vote> addVoteForPollAnonymous(Integer pollId, String caption) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		VoteOption vo = em
				.createQuery("SELECT o FROM VoteOption o WHERE o.poll.id = :poll_id AND o.caption = :caption",
						VoteOption.class)
				.setParameter("poll_id", pollId)
				.setParameter("caption", caption)
				.getSingleResult();

		Vote v = new Vote(vo);
		em.persist(v);

		// Invalidate/remove voteOptionCount for specific vote
		jedis.del("poll:" + vo.getPoll().getId());

		em.getTransaction().commit();
		em.close();
		return Optional.of(v);
	}
}
