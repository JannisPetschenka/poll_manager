import { writable } from 'svelte/store';

export const user = writable({});
export const polls = writable([]);

let creatorId;
user.subscribe(u => creatorId = u.id);

export async function updatePolls() {
	await fetch("/api/v1/polls")
            .then( response => response.json() )
            .then( data => {
				// for (let poll of data) {
				// 	fetch("/api/v1/votes/" + poll.id)
				// 		.then(response => response.json())
				// 		.then(data => {
				// 			poll.votes = poll.votes ?? {};
				//
				// 			for (let vote of data) {
				// 				const caption = vote.voteOption.caption;
				// 				poll.votes[caption] = (poll.votes[caption] ?? 0) + 1;
				// 			}
				// 		});
				// }
				polls.set(data);
			})
}

export async function createPoll(validDate, question, voteOptions) {
	if (creatorId === null || creatorId === undefined) {
		window.alert("Select an User first");
		return;
	}
	const validUntil = new Date(validDate).toISOString();
	const poll = {
		creatorId,
		validUntil,
		question,
		voteOptions,
	};

	await fetch("/api/v1/polls", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify(poll),
	});
	await updatePolls()
}

export async function vote(pollId, voteOption) {
	if (creatorId === null || creatorId === undefined) {
		window.alert("Select an User first");
		return;
	}

	const vote = {
		creatorId,
		pollId,
		voteOption
	}

	await fetch("/api/v1/votes", {
		method: "POST",
		headers: {"Content-Type": "application/json"},
		body: JSON.stringify(vote),
	})
}
