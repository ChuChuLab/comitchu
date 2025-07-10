import type React from "react"
import Header from "./components/Header/Header"
import Hero from "./components/Hero/Hero"
import Demo from "./components/Demo/Demo"
import HowItWorks from "./components/HowItWorks/HowItWorks"
import Footer from "./components/Footer/Footer"
import styles from "./App.module.css"

const App: React.FC = () => {
  return (
    <div className={styles.app}>
      <Header />
      <main className={styles.main}>
        <Hero />
        <Demo />
        <HowItWorks />
      </main>
      <Footer />
    </div>
  )
}

export default App
