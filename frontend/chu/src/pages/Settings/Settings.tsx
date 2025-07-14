"use client"

import { useState } from "react"
import { useUser } from "../../contexts/UserContext"
import { Navigate } from "react-router-dom"
import styles from "./Settings.module.css"

const Settings = () => {
  const { user, updatePet } = useUser()
  const [petName, setPetName] = useState(user?.pet?.name || "")
  const [selectedCharacter, setSelectedCharacter] = useState(user?.pet?.character || "🐱")

  if (!user) {
    return <Navigate to="/" replace />
  }

  if (!user.pet) {
    return <Navigate to="/dashboard" replace />
  }

  const characters = ["🐱", "🐶", "🐰", "🦊", "🐼", "🐨", "🐸", "🐧", "🦄"]

  const handleSave = () => {
    updatePet({
      name: petName,
      character: selectedCharacter,
    })
    alert("Settings saved successfully!")
  }

  return (
    <div className={styles.settings}>
      <h1 className={styles.title}>Pet Settings</h1>

      <div className={styles.settingsGrid}>
        <div className={styles.preview}>
          <h2 className={styles.sectionTitle}>Preview</h2>
          <div className={styles.petPreview}>
            <div className={styles.petCharacter}>{selectedCharacter}</div>
            <div className={styles.petName}>{petName || "Unnamed Pet"}</div>
            <div className={styles.petLevel}>Level {user.pet.level}</div>
          </div>
        </div>

        <div className={styles.controls}>
          <div className={styles.formGroup}>
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

          <div className={styles.formGroup}>
            <label className={styles.label}>Character</label>
            <div className={styles.characterGrid}>
              {characters.map((character) => (
                <button
                  key={character}
                  onClick={() => setSelectedCharacter(character)}
                  className={`${styles.characterButton} ${selectedCharacter === character ? styles.selected : ""}`}
                >
                  {character}
                </button>
              ))}
            </div>
          </div>

          <button onClick={handleSave} className={styles.saveButton}>
            Save Changes
          </button>
        </div>
      </div>
    </div>
  )
}

export default Settings
