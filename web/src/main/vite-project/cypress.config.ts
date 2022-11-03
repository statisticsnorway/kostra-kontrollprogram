import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    setupNodeEvents(on, config) {},
    baseUrl: "http://localhost:5174",
  },

  component: {
    devServer: {
      framework: "react",
      bundler: "vite",
    },
  },
});
