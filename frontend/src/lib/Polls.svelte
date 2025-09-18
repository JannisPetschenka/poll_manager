<script>
	import { onMount } from "svelte";
	import { user, polls, updatePolls, vote, deletePoll } from "./store.js";

	onMount(async () => {
		await updatePolls();
	});

	function pollBelongsToCurrentUser(poll) {
		if (typeof poll.createdBy == "number") {
			return poll.createdBy === $user.id;
		} else {
			return poll.createdBy.id === $user.id;
		}
	}

	function exec(pollId, voteOptionId) {
		const poll = $polls[pollId];
		const voteOption = poll.options[voteOptionId];
		vote(poll.id, voteOption);
	}

	function execPollDelete(pollId) {
		deletePoll(pollId);
	}
</script>

{#each $polls as poll, pollIndex}
	<fieldset>
		<legend>Poll number: {pollIndex}</legend>
		<h3>{poll.question}</h3>
		{#each poll.options as option, voteIndex}
			<div class="voteOption">
				<span>{option.caption}</span>
				<button
					type="button"
					class="btn btn-blue btn-vote-option"
					on:click={() => exec(poll.id, voteIndex)}
				>
					Vote
				</button>
			</div>
		{/each}
		{#if poll.votes}
			<div class="voteContainer">
				Votes:
				{#each Object.entries(poll.votes) as [caption, voteCount]}
					<div class="votes">
						{caption}: {voteCount} votes
					</div>
				{/each}
			</div>
		{/if}
		{#if pollBelongsToCurrentUser(poll)}
			<button
				class="btn btn-red btn-delete"
				on:click={() => execPollDelete(poll.id)}>Delete Poll</button
			>
		{/if}
	</fieldset>
{/each}

<style>
	fieldset {
		margin: 10px;
		background-color: #eeeeee;
	}

	legend {
		background-color: gray;
		color: white;
		padding: 5px 10px;
	}

	.voteOption {
		padding: 5px;
		background-color: darkgray;

		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.voteContainer {
		margin-top: 5px;
		border: 2px solid;
	}
	.votes {
		border-top: 1px solid;
	}

	.btn-delete {
		margin-top: 10px;
	}
</style>
