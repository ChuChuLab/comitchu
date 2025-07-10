"use client"

import type React from "react"
import { useState } from "react"
import Button from "../Button/Button"
import styles from "./Demo.module.css"

const Demo: React.FC = () => {
  const [copied, setCopied] = useState(false)

  const markdownCode = `![CommitChu](https://api.commitchu.com/badge/your-username)`

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(markdownCode)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch (err) {
      console.error("Failed to copy:", err)
    }
  }

  return (
    <section id="demo" className={styles.demo}>
      <div className={styles.container}>
        <h2 className={styles.title}>Live Demo</h2>
        <p className={styles.description}>
          See how your CommitChu badge will look and get the code to embed it anywhere.
        </p>

        <div className={styles.preview}>
          <div className={styles.badgeContainer}>
            <img
              src="/placeholder.svg?height=80&width=200"
              alt="Live CommitChu badge preview"
              className={styles.badge}
            />
          </div>

          <div className={styles.codeSection}>
            <h3 className={styles.codeTitle}>Markdown</h3>
            <div className={styles.codeBlock}>
              <code className={styles.code}>{markdownCode}</code>
              <Button onClick={handleCopy} variant="secondary" size="small" className={styles.copyButton}>
                {copied ? "✓ Copied!" : "Copy"}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

export default Demo
