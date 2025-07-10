import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App.tsx"
import "./index.css"

// Find an existing mount point or create one if it doesn't exist.
const container =
  document.getElementById("root") ??
  document.getElementById("__next") ?? // v0 / Next-lite root
  (() => {
    const el = document.createElement("div")
    el.id = "root"
    document.body.appendChild(el)
    return el
  })()

ReactDOM.createRoot(container).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
