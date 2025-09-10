<script>
	import { onMount } from "svelte";
	import { polls, updatePolls, vote } from "./store.js";

	onMount(async () => {
		await updatePolls();
	});

	polls.set([
		{
			question: "question",
			voteOptions: [{ caption: "hi", presentationOrder: 0 }],
			votes: { "Option A": 5, "Option B": 3 },
		},
	]);

	function exec(pollId, voteOptionId) {
		const poll = $polls[pollId];
		const voteOption = poll.voteOptions[voteOptionId];
		vote(poll.id, voteOption);
	}
</script>

{#each $polls as poll, pollIndex}
	<fieldset>
		<legend>Poll number: {pollIndex}</legend>
		<h3>{poll.question}</h3>
		{#each poll.voteOptions as option, voteIndex}
			<div class="voteOption">
				<span>{option.caption}</span>
				<button
					type="button"
					class="btn btn-blue btn-vote-option"
					on:click={() => exec(pollIndex, voteIndex)}
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
</style>
