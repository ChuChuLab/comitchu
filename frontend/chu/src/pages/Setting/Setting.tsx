import { useState } from "react";
import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import Button from "../../components/common/Button";
import Input from "../../components/common/Input";
import styles from "./Setting.module.css";

const Setting = () => {
  const { user, updatePet } = useUserStore();
  const [petName, setPetName] = useState(user?.pet?.name || "");
  const [selectedCharacter, setSelectedCharacter] = useState(user?.pet?.character || "🐱");

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (!user.pet) {
    return <Navigate to="/dashboard" replace />;
  }

  const characters = ["🐱", "🐶", "🐰", "🦊", "🐼", "🐨", "🐸", "🐧", "🦄"];

  const handleSave = () => {
    updatePet({
      name: petName,
      character: selectedCharacter,
    });
    alert("Setting saved successfully!");
  };

  return (
    <div className={styles.setting}>
      <h1 className={styles.title}>Pet Setting</h1>

      <div className={styles.settingGrid}>
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
            <Input
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

          <Button onClick={handleSave}>
            Save Changes
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Setting;
