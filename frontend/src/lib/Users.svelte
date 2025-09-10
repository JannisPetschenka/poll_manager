<script>
	import { onMount } from "svelte";
	import { users, user, getUsers } from "./store.js";

	onMount(() => getUsers());
</script>

<div>
	Select User:
	<select bind:value={$user}>
		{#each $users as user}
			<option value={user}>
				{user.username}
			</option>
		{/each}
	</select>
</div>
