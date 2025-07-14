"use client"

import { useState } from "react"
import { useUser } from "../../contexts/UserContext"
import CharacterGridItem from "../CharacterGridItem/CharacterGridItem"
import styles from "./PetCreator.module.css"

const PetCreator = () => {
  const { createPet } = useUser()
  const [selectedCharacter, setSelectedCharacter] = useState("🐱")
  const [petName, setPetName] = useState("")

  const characters = ["🐱", "🐶", "🐰", "🦊", "🐼", "🐨", "🐸", "🐧", "🦄", "🐹", "🐯", "🦁"]

  const handleCreatePet = () => {
    if (!petName.trim()) {
      alert("Please enter a name for your pet!")
      return
    }

    createPet({
      name: petName.trim(),
      character: selectedCharacter,
      level: 1,
      xp: 0,
      mood: "happy",
    })
  }

  return (
    <div className={styles.creator}>
      <div className={styles.creatorGrid}>
        <div className={styles.preview}>
          <h3 className={styles.previewTitle}>Preview</h3>
          <div className={styles.petPreview}>
            <div className={styles.petCharacter}>{selectedCharacter}</div>
            <div className={styles.petName}>{petName || "Unnamed Pet"}</div>
            <div className={styles.petLevel}>Level 1</div>
          </div>

          <div className={styles.nameInput}>
            <label htmlFor="petName" className={styles.label}>
              Pet Name
            </label>
            <input
              id="petName"
              type="text"
              value={petName}
              onChange={(e) => setPetName(e.target.value)}
              placeholder="Enter pet name"
              className={styles.input}
              maxLength={20}
            />
          </div>

          <button onClick={handleCreatePet} className={styles.createButton} disabled={!petName.trim()}>
            Create My CommitChu
          </button>
        </div>

        <div className={styles.selection}>
          <h3 className={styles.selectionTitle}>Choose Your Character</h3>
          <div className={styles.characterGrid}>
            {characters.map((character) => (
              <CharacterGridItem
                key={character}
                character={character}
                isSelected={selectedCharacter === character}
                onSelect={() => setSelectedCharacter(character)}
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

export default PetCreator
