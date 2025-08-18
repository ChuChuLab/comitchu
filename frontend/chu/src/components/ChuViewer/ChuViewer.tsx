import useChuStore from "../../store/chuStore";
import { useEffect } from "react";
import styles from "./ChuViewer.module.css";

const ChuViewer = () => {
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();

  useEffect(() => {
    fetchMainChu();
  }, [fetchMainChu]);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!mainChu) {
    return <div>No Chu data available.</div>;
  }

  const chuImagePath = new URL(`../../assets/images/chu/normal/${mainChu.lang}.png`, import.meta.url).href;
  const backgroundImagePath = new URL(`../../assets/images/backgrounds/${mainChu.background}.png`, import.meta.url)
    .href;

  return (
    <div className={styles.chuViewer}>
      <img src={backgroundImagePath} alt="Background" className={styles.backgroundImage} />
      <img src={chuImagePath} alt="Chu" className={styles.chuImage} />
    </div>
  );
};

export default ChuViewer;
