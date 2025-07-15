import { useUser } from "../../contexts/UserContext";
import { Navigate } from "react-router-dom";
import PetCard from "../../components/PetCard/PetCard";
import PetCreator from "../../components/PetCreator/PetCreator";
import styles from "./Dashboard.module.css";

const Dashboard = () => {
  const { user } = useUser();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.dashboard}>
      <h1 className={styles.title}>Dashboard</h1>

      {user.pet ? (
        <div className={styles.petSection}>
          <PetCard pet={user.pet} />
        </div>
      ) : (
        <div className={styles.creatorSection}>
          <h2 className={styles.subtitle}>Create Your CommitChu</h2>
          <p className={styles.description}>Choose your perfect coding companion from the characters below!</p>
          <PetCreator />
        </div>
      )}
    </div>
  );
};

export default Dashboard;
