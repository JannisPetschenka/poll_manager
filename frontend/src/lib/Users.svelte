<script>
	import { user } from "./store.js";

	let users = $state(
		fetch("/api/v1/users").then(async (response) => {
			let resp = await response.json();
			user.set(resp[0]);
			return resp;
		}),
	);
</script>

<div>
	Select User:
	<select bind:value={$user}>
		{#await users}
			its is loading...
		{:then ready}
			{#each ready as user}
				<option value={user}>
					{user.username}
				</option>
			{/each}
		{:catch error}
			{error}
		{/await}
	</select>
</div>
