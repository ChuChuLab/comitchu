import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore"; // Import useChuStore
import { Navigate } from "react-router-dom";
import styles from "./Custom.module.css";
import ChuViewer from "../../components/ChuViewer/ChuViewer";
import ChuSkinSelector from "../../components/ChuSkinSelector/ChuSkinSelector";
import BackgroundSelector from "../../components/BackgroundSelector/BackgroundSelector";
import dashboardStyles from "../../pages/Dashboard/Dashboard.module.css"; // Import dashboard styles

const Custom = () => {
  const { user } = useUserStore();
  const { mainChu } = useChuStore(); // Get mainChu from useChuStore

  if (!user) {
    return <Navigate to="/" replace />;
  }

  const chuImagePath = mainChu
    ? new URL(`../../assets/images/chu/normal/${mainChu.lang}.png`, import.meta.url).href
    : "";
  const backgroundImagePath = mainChu
    ? new URL(`../../assets/images/backgrounds/${mainChu.background}.png`, import.meta.url).href
    : "";

  return (
    <div className={`${styles.custom} animate-slide-up`}>
      <div className={styles.contentWrapper}>
        <div className={dashboardStyles.svgImageContainer}>
          <ChuViewer
            className={styles.svgImageBackground}
            backgroundImagePath={backgroundImagePath}
            chuImagePath={chuImagePath}
          />
          <ChuViewer
            className={styles.svgImage}
            backgroundImagePath={backgroundImagePath}
            chuImagePath={chuImagePath}
          />
        </div>
        <div className={styles.customSection}>
          <ChuSkinSelector />
          <BackgroundSelector />
        </div>
      </div>
    </div>
  );
};

export default Custom;
