import { useEffect } from "react";
import useChuStore from "../../store/chuStore";
import styles from "./Dashboard.module.css";

const Dashboard = () => {
  const { mainChu, chuSkins, isLoading, error, fetchMainChu, fetchAllChuSkins } = useChuStore();

  useEffect(() => {
    fetchMainChu();
    fetchAllChuSkins();
  }, [fetchMainChu, fetchAllChuSkins]);

  if (isLoading) {
    return <p>Loading your Chus...</p>;
  }

  if (error) {
    return <p>Error: {error}</p>;
  }

  return (
    <div className={styles.dashboard}>
      <h1 className={styles.title}>My Dashboard</h1>

      <section className={styles.mainChuSection}>
        <h2>My Main Chu</h2>
        {mainChu ? (
          <div>
            <p>Nickname: {mainChu.nickname}</p>
            <p>Level: {mainChu.level}</p>
            <p>Language: {mainChu.lang}</p>
          </div>
        ) : (
          <p>No main chu selected.</p>
        )}
      </section>

      <section className={styles.chuSkinsSection}>
        <h2>My Language Skins</h2>
        {chuSkins.length > 0 ? (
          <ul className={styles.skinList}>
            {chuSkins.map((skin) => (
              <li key={skin.langId} className={skin.isUnlocked ? styles.unlocked : styles.locked}>
                Language ID: {skin.langId}
                {skin.isMain && <span> (Main)</span>}
                {skin.isUnlocked ? " (Unlocked)" : " (Locked)"}
              </li>
            ))}
          </ul>
        ) : (
          <p>No language skins found.</p>
        )}
      </section>
    </div>
  );
};

export default Dashboard;
