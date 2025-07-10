import type React from "react"
import styles from "./Footer.module.css"

const Footer: React.FC = () => {
  return (
    <footer className={styles.footer}>
      <div className={styles.container}>
        <div className={styles.content}>
          <div className={styles.brand}>
            <span className={styles.brandName}>CommitChu</span>
            <p className={styles.brandDescription}>Making GitHub commits more fun, one Chu at a time.</p>
          </div>

          <div className={styles.links}>
            <div className={styles.linkGroup}>
              <h4 className={styles.linkTitle}>Product</h4>
              <a href="#demo" className={styles.link}>
                Demo
              </a>
              <a href="#how-it-works" className={styles.link}>
                How it works
              </a>
              <a href="#" className={styles.link}>
                Pricing
              </a>
            </div>

            <div className={styles.linkGroup}>
              <h4 className={styles.linkTitle}>Resources</h4>
              <a href="#" className={styles.link}>
                Documentation
              </a>
              <a href="#" className={styles.link}>
                API
              </a>
              <a href="#" className={styles.link}>
                Support
              </a>
            </div>

            <div className={styles.linkGroup}>
              <h4 className={styles.linkTitle}>Connect</h4>
              <a href="https://github.com" className={styles.link}>
                GitHub
              </a>
              <a href="#" className={styles.link}>
                Twitter
              </a>
              <a href="#" className={styles.link}>
                Discord
              </a>
            </div>
          </div>
        </div>

        <div className={styles.bottom}>
          <p className={styles.copyright}>© 2024 CommitChu. Made with ❤️ for developers.</p>
          <div className={styles.bottomLinks}>
            <a href="#" className={styles.bottomLink}>
              Privacy
            </a>
            <a href="#" className={styles.bottomLink}>
              Terms
            </a>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer
