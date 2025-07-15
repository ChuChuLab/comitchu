import { Routes, Route } from "react-router-dom"
import Header from "./components/Header/Header"
import Landing from "./pages/Landing/Landing"
import Dashboard from "./pages/Dashboard/Dashboard"
import Setting from "./pages/Setting/Setting"
import styles from "./App.module.css"

function App() {
  return (
    <div className={styles.app}>
      <Header />
      <main className={styles.main}>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/setting" element={<Setting />} />
        </Routes>
      </main>
    </div>
  )
}

export default App
