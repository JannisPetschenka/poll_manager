<script>
	import Users from "./lib/Users.svelte";
	import Poll from "./lib/Poll.svelte";
	import Polls from "./lib/Polls.svelte";
</script>

<Users />

<div>
	<div class="float-left">
		<Poll />
	</div>
	<div class="float-left">
		<Polls />
	</div>
</div>

<style>
	.float-left {
		width: 50%;
		float: left;
	}
</style>
