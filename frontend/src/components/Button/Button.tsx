"use client"

import type React from "react"
import styles from "./Button.module.css"

interface ButtonProps {
  children: React.ReactNode
  onClick?: () => void
  variant?: "primary" | "secondary"
  size?: "small" | "medium" | "large"
  className?: string
  disabled?: boolean
}

const Button: React.FC<ButtonProps> = ({
  children,
  onClick,
  variant = "primary",
  size = "medium",
  className = "",
  disabled = false,
}) => {
  const buttonClass = [styles.button, styles[variant], styles[size], className].filter(Boolean).join(" ")

  return (
    <button className={buttonClass} onClick={onClick} disabled={disabled}>
      {children}
    </button>
  )
}

export default Button
