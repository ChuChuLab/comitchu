
import React from 'react';
import styles from './Button.module.css';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
  variant?: 'primary' | 'danger'; // 'primary'는 기본값, 'danger'는 경고/삭제용
}

const Button: React.FC<ButtonProps> = ({ children, variant = 'primary', className, ...props }) => {
  const buttonClassName = `${styles.button} ${styles[variant]} ${className || ''}`;
  return (
    <button className={buttonClassName} {...props}>
      {children}
    </button>
  );
};

export default Button;
