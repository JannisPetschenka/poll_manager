<script>
	import { createPoll } from "./store.js";

	let question = "";
	let voteOptions = [
		{ caption: "", presentationOrder: 1 },
		{ caption: "", presentationOrder: 2 },
	];
	let validUntil;

	function addVoteOption() {
		voteOptions = [
			...voteOptions,
			{ caption: "", presentationOrder: voteOptions.length + 1 },
		];
	}

	function removeOption(index) {
		voteOptions = voteOptions
			.filter((_, i) => i !== index)
			.map((opt, i) => ({ ...opt, presentationOrder: i + 1 }));
	}

	function exec() {
		createPoll(validUntil, question, voteOptions);
	}
</script>

<form on:submit|preventDefault={exec}>
	<fieldset>
		<legend>Create Poll:</legend>
		<div>
			<label for="question">Question:</label>
			<input
				id="question"
				type="text"
				bind:value={question}
				required
				placeholder="Enter your question"
			/>
		</div>
		<div>
			<label for="validUntil">Valid until:</label>
			<input
				id="validUnitl"
				required
				type="datetime-local"
				bind:value={validUntil}
			/>
		</div>
		<div>
			<h3>Vote Options</h3>
			{#each voteOptions as option, index}
				<div>
					<input
						type="text"
						bind:value={option.caption}
						placeholder="Option caption"
						required
					/>
					<span>#{option.presentationOrder}</span>
					<button
						type="button"
						on:click={() => removeOption(index)}
						disabled={voteOptions.length <= 2}
					>
						Remove
					</button>
				</div>
			{/each}
			<button
				type="button"
				on:click={addVoteOption}
				class="bg-blue-500 text-white px-3 py-1 rounded"
			>
				+ Add Option
			</button>
		</div>
		<div>
			<button type="submit"> Submit </button>
		</div>
	</fieldset>
</form>

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

	input {
		margin: 5px;
	}

	input:invalid {
		border-color: red;
	}
</style>
