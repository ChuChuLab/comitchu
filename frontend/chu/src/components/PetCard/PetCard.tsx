import type React from "react";
import type { Pet } from "../../store/userStore";
import styles from "./PetCard.module.css";

interface PetCardProps {
  pet: Pet;
}

const PetCard: React.FC<PetCardProps> = ({ pet }) => {
  const xpToNextLevel = pet.level * 100 - pet.xp;
  const xpProgress = ((pet.xp % 100) / 100) * 100;

  const getMoodColor = (mood: Pet["mood"]) => {
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

  const getMoodEmoji = (mood: Pet["mood"]) => {
    switch (mood) {
      case "happy":
        return "😊";
      case "neutral":
        return "😐";
      case "sad":
        return "😢";
      default:
        return "😐";
    }
  };

  return (
    <div className={styles.petCard}>
      <div className={styles.petHeader}>
        <h2 className={styles.petName}>{pet.name}</h2>
        <div className={styles.petMood} style={{ color: getMoodColor(pet.mood) }}>
          {getMoodEmoji(pet.mood)} {pet.mood}
        </div>
      </div>

      <div className={styles.petCharacter}>{pet.character}</div>

      <div className={styles.petStats}>
        <div className={styles.statRow}>
          <span className={styles.statLabel}>Level</span>
          <span className={styles.statValue}>{pet.level}</span>
        </div>

        <div className={styles.statRow}>
          <span className={styles.statLabel}>XP</span>
          <span className={styles.statValue}>{pet.xp}</span>
        </div>

        <div className={styles.xpBar}>
          <div className={styles.xpFill} style={{ width: `${xpProgress}%` }}></div>
        </div>

        <div className={styles.xpText}>{xpToNextLevel} XP to next level</div>
      </div>

      <div className={styles.petAge}>Created {new Date(pet.createdAt).toLocaleDateString()}</div>
    </div>
  );
};

export default PetCard;
