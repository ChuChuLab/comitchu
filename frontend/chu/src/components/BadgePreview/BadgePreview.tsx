import type React from "react";

import Button from "../common/Button";
import styles from "./BadgePreview.module.css";

interface BadgePreviewProps {
  petName: string;
  level: number;
  character: string;
  mood: "happy" | "neutral" | "sad";
}

const BadgePreview: React.FC<BadgePreviewProps> = ({ petName, level, character, mood }) => {
  const getMoodColor = (mood: string) => {
    switch (mood) {
      case "happy":
        return "#4CAF50";
      case "neutral":
        return "#FF9800";
      case "sad":
        return "#F44336";
      default:
        return "#9E9E9E";
    }
  };

  const svgContent = `
    <svg width="200" height="80" xmlns="http://www.w3.org/2000/svg">
      <defs>
        <linearGradient id="bg" x1="0%" y1="0%" x2="100%" y2="0%">
          <stop offset="0%" style="stop-color:#667eea;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#764ba2;stop-opacity:1" />
        </linearGradient>
      </defs>
      <rect width="200" height="80" rx="8" fill="url(#bg)"/>
      <text x="50" y="25" fontFamily="Arial, sans-serif" fontSize="24" fill="white">${character}</text>
      <text x="50" y="45" fontFamily="Arial, sans-serif" fontSize="14" fontWeight="bold" fill="white">${petName}</text>
      <text x="50" y="60" fontFamily="Arial, sans-serif" fontSize="12" fill="white">Level ${level}</text>
      <circle cx="170" cy="20" r="6" fill="${getMoodColor(mood)}"/>
    </svg>
  `;

  const encodedSvg = encodeURIComponent(svgContent);
  const dataUri = `data:image/svg+xml,${encodedSvg}`;

  return (
    <div className={styles.badgePreview}>
      <div className={styles.badgeContainer}>
        <img src={dataUri || "/placeholder.svg"} alt={`${petName} CommitChu Badge`} className={styles.badge} />
      </div>

      <div className={styles.codeBlock}>
        <h4 className={styles.codeTitle}>Markdown Code:</h4>
        <code className={styles.code}>{`![CommitChu](${dataUri})`}</code>
        <Button onClick={() => navigator.clipboard.writeText(`![CommitChu](${dataUri})`)}>
          Copy to Clipboard
        </Button>
      </div>
    </div>
  );
};

export default BadgePreview;
