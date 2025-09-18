package no.hvl.poll_manager.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import no.hvl.poll_manager.controller.PollsController.PollRequest;
import no.hvl.poll_manager.controller.UsersController.UserRequest;
import no.hvl.poll_manager.controller.VotesController.VoteRequest;
import no.hvl.poll_manager.model.Poll;
import no.hvl.poll_manager.model.User;
import no.hvl.poll_manager.model.Vote;
import no.hvl.poll_manager.model.VoteOption;

@Component
public class PollManager {

	private EntityManagerFactory emf;

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
		User user = em.createQuery("select u from User u where u.id like :id", User.class)
				.setParameter("id", poll.creatorId()).getSingleResult();
		Poll p = user.createPoll(poll.question());
		for (VoteOption v : poll.voteOptions()) {
			// VoteOption vo = p.addVoteOption(v.getCaption());
			p.addVoteOption(v.getCaption());
		}

		em.persist(p);

		em.getTransaction().commit();
		em.close();

		return Optional.of(p);
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
		// if (votes.containsKey(id)) {
		// return votes.get(id);
		// }
		return Collections.emptyList();
	}

	public Optional<Vote> addVoteForPoll(VoteRequest vote) {
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		User u = em.find(User.class, vote.creatorId());

		em.persist(u.voteFor(vote.voteOption()));

		em.getTransaction().commit();
		em.close();

		// Optional<User> creator = users.stream().filter(user -> user.getId() ==
		// vote.creatorId()).findFirst();
		// if (creator.isPresent() && votes.containsKey(vote.pollId())) {
		// Vote tmp = new Vote(creator.get(), vote.voteOption());
		// votes.get(vote.pollId()).add(tmp);
		// return Optional.of(tmp);
		// }
		return Optional.empty();
	}
}
