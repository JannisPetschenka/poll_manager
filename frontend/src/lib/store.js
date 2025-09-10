import { writable } from 'svelte/store';

export const users = writable([]);
export const user = writable({});
export const polls = writable([]);

let creatorId;
user.subscribe(u => creatorId = u.id);

export async function getUsers() {
	fetch("/api/v1/users")
		.then(response => response.json())
		.then(data => {
			users.set(data);
			user.set(data[0]);
		});
}

export async function updatePolls() {
	await fetch("/api/v1/polls")
            .then( response => response.json() )
            .then( data => {
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
	await updatePolls()
}
