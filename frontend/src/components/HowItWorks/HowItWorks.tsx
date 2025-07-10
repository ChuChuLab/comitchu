import type React from "react"
import styles from "./HowItWorks.module.css"

interface StepProps {
  number: string
  title: string
  description: string
  icon: string
}

const Step: React.FC<StepProps> = ({ number, title, description, icon }) => (
  <div className={styles.step}>
    <div className={styles.stepIcon}>
      <span className={styles.icon}>{icon}</span>
      <span className={styles.stepNumber}>{number}</span>
    </div>
    <h3 className={styles.stepTitle}>{title}</h3>
    <p className={styles.stepDescription}>{description}</p>
  </div>
)

const HowItWorks: React.FC = () => {
  return (
    <section id="how-it-works" className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.title}>How It Works</h2>
        <p className={styles.subtitle}>Get your CommitChu up and running in just three simple steps</p>

        <div className={styles.steps}>
          <Step
            number="1"
            icon="🔗"
            title="Login with GitHub"
            description="Connect your GitHub account securely to start tracking your commit activity and contributions."
          />
          <Step
            number="2"
            icon="⚙️"
            title="Set up webhook"
            description="We'll automatically configure a webhook to monitor your repositories and track your coding activity."
          />
          <Step
            number="3"
            icon="🐱"
            title="Watch your Chu evolve"
            description="Your CommitChu will grow, change colors, and unlock new features based on your commit patterns!"
          />
        </div>
      </div>
    </section>
  )
}

export default HowItWorks
