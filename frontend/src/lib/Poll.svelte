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
		<div class="voteOptions">
			<h3>Vote Options</h3>
			{#each voteOptions as option, index}
				<div class="voteOption">
					<input
						type="text"
						bind:value={option.caption}
						placeholder="Option caption"
						required
					/>
					<span>#{option.presentationOrder}</span>
					<button
						class="btn-remove"
						type="button"
						on:click={() => removeOption(index)}
						disabled={voteOptions.length <= 2}
					>
						X
					</button>
				</div>
			{/each}
			<button type="button" on:click={addVoteOption} class="btn btn-blue">
				+ Add Option
			</button>
		</div>
		<div>
			<button type="submit" class="btn btn-green btn-submit">
				Submit
			</button>
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
	.voteOptions {
		margin-bottom: 20px;
	}
	.voteOption {
		padding: 5px;
	}

	.btn-submit {
		justify-self: center;
	}

	.btn-remove {
		width: 16px;
		height: 16px;
		justify-content: center;
		align-items: center;
		font-size: 12px;
		font-weight: bold;
		border: 2px solid #dc3545;
		border-radius: 50%;
		background-color: white;
		color: #dc3545;
		cursor: pointer;
		transition: all 0.3s ease;
		padding: 0;
		box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	}

	.btn-remove:hover {
		background-color: #dc3545;
		color: white;
		transform: scale(1.1);
		box-shadow: 0 8px 12px rgba(0, 0, 0, 0.1);
	}

	.btn-remove:focus {
		outline: none;
		box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.5);
	}

	.btn-remove:active {
		background-color: #c82333;
		transform: scale(1);
		box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
	}
	.btn-remove:disabled {
		background-color: #f1f1f1;
		color: #ccc;
		border-color: #ddd;
		cursor: not-allowed;
		box-shadow: none;
	}
</style>
