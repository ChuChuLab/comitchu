import type React from "react"
import styles from "./Header.module.css"

const Header: React.FC = () => {
  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <div className={styles.logo}>
          <span className={styles.logoText}>CommitChu</span>
        </div>
        <nav className={styles.nav}>
          <a href="#demo" className={styles.navLink}>
            Demo
          </a>
          <a href="#how-it-works" className={styles.navLink}>
            How it works
          </a>
          <a href="https://github.com" className={styles.navLink}>
            GitHub
          </a>
        </nav>
      </div>
    </header>
  )
}

export default Header
