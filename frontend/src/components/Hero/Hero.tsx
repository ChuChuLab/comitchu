"use client"

import type React from "react"
import Button from "../Button/Button"
import styles from "./Hero.module.css"

const Hero: React.FC = () => {
  const handleGetStarted = () => {
    document.getElementById("demo")?.scrollIntoView({ behavior: "smooth" })
  }

  return (
    <section className={styles.hero}>
      <div className={styles.content}>
        <h1 className={styles.title}>CommitChu</h1>
        <p className={styles.subtitle}>Your GitHub Commit Pet that evolves with every commit!</p>
        <p className={styles.description}>
          Dynamic SVG badges that grow and change based on your GitHub activity. Watch your CommitChu evolve as you code
          more and build amazing projects.
        </p>
        <Button onClick={handleGetStarted} variant="primary" size="large">
          Get Your Chu! 🐱
        </Button>
      </div>
      <div className={styles.visual}>
        <div className={styles.badge}>
          <img
            src="/placeholder.svg?height=120&width=240"
            alt="Sample CommitChu badge showing an evolving pet"
            className={styles.badgeImage}
          />
        </div>
      </div>
    </section>
  )
}

export default Hero
