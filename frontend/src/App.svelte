<script>
	import Users from "./lib/Users.svelte";
	import Poll from "./lib/Poll.svelte";
	import Polls from "./lib/Polls.svelte";
</script>

<nav>
	<div class="nav-left">
		<Users />
	</div>

	<div class="nav-center">
		<div class="logo">Poll Manager</div>
	</div>
</nav>

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

	nav {
		background-color: #333;
		padding: 1rem;
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: white;
	}
	.nav-left,
	.nav-center {
		display: flex;
		align-items: center;
	}
	.nav-left {
		margin-right: auto;
	}

	.nav-center {
		justify-content: center;
		flex-grow: 1;
	}
	.logo {
		font-weight: bold;
		font-size: 1.5rem;
	}
</style>
