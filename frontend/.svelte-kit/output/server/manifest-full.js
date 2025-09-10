export const manifest = (() => {
function __memo(fn) {
	let value;
	return () => value ??= (value = fn());
}

return {
	appDir: "_app",
	appPath: "_app",
	assets: new Set(["robots.txt"]),
	mimeTypes: {".txt":"text/plain"},
	_: {
		client: {start:"_app/immutable/entry/start.DuG0xj0W.js",app:"_app/immutable/entry/app.ByGBP4oW.js",imports:["_app/immutable/entry/start.DuG0xj0W.js","_app/immutable/chunks/BPCq7rvb.js","_app/immutable/chunks/CAmFdfWF.js","_app/immutable/chunks/FtZSCd-Y.js","_app/immutable/chunks/BnLhbYco.js","_app/immutable/chunks/_WFN-eNe.js","_app/immutable/entry/app.ByGBP4oW.js","_app/immutable/chunks/FtZSCd-Y.js","_app/immutable/chunks/CAmFdfWF.js","_app/immutable/chunks/BnLhbYco.js","_app/immutable/chunks/_WFN-eNe.js","_app/immutable/chunks/DsnmJJEf.js"],stylesheets:[],fonts:[],uses_env_dynamic_public:false},
		nodes: [
			__memo(() => import('./nodes/0.js')),
			__memo(() => import('./nodes/1.js')),
			__memo(() => import('./nodes/2.js')),
			__memo(() => import('./nodes/3.js')),
			__memo(() => import('./nodes/4.js'))
		],
		remotes: {
			
		},
		routes: [
			{
				id: "/",
				pattern: /^\/$/,
				params: [],
				page: { layouts: [0,], errors: [1,], leaf: 2 },
				endpoint: null
			},
			{
				id: "/poll",
				pattern: /^\/poll\/?$/,
				params: [],
				page: { layouts: [0,], errors: [1,], leaf: 3 },
				endpoint: null
			},
			{
				id: "/users",
				pattern: /^\/users\/?$/,
				params: [],
				page: { layouts: [0,], errors: [1,], leaf: 4 },
				endpoint: null
			}
		],
		prerendered_routes: new Set([]),
		matchers: async () => {
			
			return {  };
		},
		server_assets: {}
	}
}
})();
