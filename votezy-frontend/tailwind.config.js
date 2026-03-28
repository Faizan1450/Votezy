/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./dashboard.html",
    "./admin.html",
    "./js/**/*.js",
  ],
  theme: {
    extend: {
      colors: {
        primary: "#6366f1",
        secondary: "#a855f7",
        dark: "#1e293b",
      }
    },
  },
  plugins: [],
}
