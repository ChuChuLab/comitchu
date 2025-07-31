import { useEffect } from "react";
import { Navigate } from "react-router-dom";
import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore";
import PetCard from "../../components/PetCard/PetCard";
import PetCreator from "../../components/PetCreator/PetCreator";
import styles from "./Dashboard.module.css";

const Dashboard = () => {
  // 후에 i18n 적용해야함~
  const { user } = useUserStore();
  const { chus, isLoading, error, fetchChus } = useChuStore();

  useEffect(() => {
    fetchChus();
  }, [fetchChus]);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  const renderContent = () => {
    if (isLoading) {
      return <p>Loading your Chus...</p>;
    }

    if (error) {
      return <p>Error: {error}</p>;
    }

    if (chus.length > 0) {
      // 메인 츄를 찾거나, 없으면 첫 번째 츄를 보여줍니다.
      const mainChu = chus.find((chu) => chu.isMain) || chus[0];
      return (
        <div className={styles.petSection}>
          {/* PetCard가 Chu 타입을 받을 수 있도록 수정이 필요할 수 있습니다. */}
          {/* 우선 chu 객체를 pet prop으로 넘겨봅니다. */}
          <PetCard pet={mainChu} />
        </div>
      );
    }

    return (
      <div className={styles.creatorSection}>
        <h2 className={styles.subtitle}>Create Your ComitChu</h2>
        <p className={styles.description}>Choose your perfect coding companion from the characters below!</p>
        <PetCreator />
      </div>
    );
  };

  return (
    <div className={styles.dashboard}>
      <h1 className={styles.title}>Dashboard</h1>
      {renderContent()}
    </div>
  );
};

export default Dashboard;
