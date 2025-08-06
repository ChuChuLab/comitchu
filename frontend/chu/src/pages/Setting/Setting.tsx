import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import styles from "./Setting.module.css";
import useChuStore from "../../store/chuStore";
import { useEffect } from "react";

const Setting = () => {
  const { user } = useUserStore();
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();

  useEffect(() => {
    fetchMainChu();
  }, [fetchMainChu]);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!mainChu) {
    return <div>No Chu data available.</div>;
  }

  const chuImagePath = `/src/assets/images/chu/happy/${mainChu.lang}.png`;
  const backgroundImagePath = `/src/assets/images/backgrounds/${mainChu.background}.png`;

  return (
    <div className={styles.setting}>
      <img src={backgroundImagePath} alt="Background" />
      <img src={chuImagePath} alt="Chu" />
    </div>
  );
};

export default Setting;
