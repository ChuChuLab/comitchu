import type React from "react";

import styles from "./CharacterGridItem.module.css";

interface CharacterGridItemProps {
  character: string;
  isSelected: boolean;
  onSelect: () => void;
}

const CharacterGridItem: React.FC<CharacterGridItemProps> = ({ character, isSelected, onSelect }) => {
  return (
    <button onClick={onSelect} className={`${styles.gridItem} ${isSelected ? styles.selected : ""}`}>
      <span className={styles.character}>{character}</span>
    </button>
  );
};

export default CharacterGridItem;
