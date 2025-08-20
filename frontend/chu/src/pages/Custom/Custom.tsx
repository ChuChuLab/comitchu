import { useState } from "react";
import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore";
import { Navigate } from "react-router-dom";
import styles from "./Custom.module.css";
import ChuViewer from "../../components/ChuViewer/ChuViewer";
import ChuSkinSelector from "../../components/ChuSkinSelector/ChuSkinSelector";
import BackgroundSelector from "../../components/BackgroundSelector/BackgroundSelector";
import dashboardStyles from "../../pages/Dashboard/Dashboard.module.css";

const Custom = () => {
  const { user } = useUserStore();
  const { mainChu } = useChuStore();
  const [showSkinSelector, setShowSkinSelector] = useState(true);
  const [fade, setFade] = useState(false);

  if (!user) {
    return <Navigate to="/" replace />;
  }

  const chuImagePath = mainChu
    ? new URL(`../../assets/images/chu/normal/${encodeURIComponent(mainChu.lang)}.png`, import.meta.url).href
    : "";
  const backgroundImagePath = mainChu
    ? new URL(`../../assets/images/backgrounds/${mainChu.background}.png`, import.meta.url).href
    : "";

  const handleToggle = () => {
    setFade(true);
    setTimeout(() => {
      setShowSkinSelector((prev) => !prev);
      setFade(false);
    }, 250);
  };

  return (
    <div className={styles.custom}>
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
          <div className={styles.toggleContainer}>
            <button className={styles.toggleBtn} onClick={handleToggle}>
              <span className={`${styles.toggleText} ${fade ? styles.fade : ""}`}>
                {showSkinSelector ? (
                  <svg viewBox="0 0 22 22" style={{ height: "30px", width: "30px" }}>
                    <path
                      fill="currentColor"
                      d="M12 20H10V19H9V18H8V17H7V16H6V15H5V14H4V13H3V12H2V10H1V5H2V4H3V3H4V2H9V3H10V4H12V3H13V2H18V3H19V4H20V5H21V10H20V12H19V13H18V14H17V15H16V16H15V17H14V18H13V19H12V20M5 11V12H6V13H7V14H8V15H9V16H10V17H12V16H13V15H14V14H15V13H16V12H17V11H18V9H19V6H18V5H17V4H14V5H13V6H12V7H10V6H9V5H8V4H5V5H4V6H3V9H4V11H5Z"
                    ></path>
                  </svg>
                ) : (
                  <svg viewBox="0 0 22 22" style={{ height: "32px", width: "32px" }}>
                    <path
                      fill="currentColor"
                      d="M1 4H2V3H20V4H21V18H20V19H2V18H1V4M3 14H4V13H5V12H6V11H7V10H8V9H10V10H11V11H12V12H13V13H14V14H15V15H16V16H17V17H19V5H3V14M14 17V16H13V15H12V14H11V13H10V12H8V13H7V14H6V15H5V16H4V17H14M13 8H14V7H16V8H17V10H16V11H14V10H13V8Z"
                    ></path>
                  </svg>
                )}
              </span>
              <span className={styles.toggleDivider}>|</span>
              <span className={`${styles.toggleTextInactive} ${fade ? styles.fade : ""}`}>
                {showSkinSelector ? (
                  <svg viewBox="0 0 22 22" style={{ height: "32px", width: "32px" }}>
                    <path
                      fill="currentColor"
                      d="M1 4H2V3H20V4H21V18H20V19H2V18H1V4M3 14H4V13H5V12H6V11H7V10H8V9H10V10H11V11H12V12H13V13H14V14H15V15H16V16H17V17H19V5H3V14M14 17V16H13V15H12V14H11V13H10V12H8V13H7V14H6V15H5V16H4V17H14M13 8H14V7H16V8H17V10H16V11H14V10H13V8Z"
                    ></path>
                  </svg>
                ) : (
                  <svg viewBox="0 0 22 22" style={{ height: "30px", width: "30px" }}>
                    <path
                      fill="currentColor"
                      d="M12 20H10V19H9V18H8V17H7V16H6V15H5V14H4V13H3V12H2V10H1V5H2V4H3V3H4V2H9V3H10V4H12V3H13V2H18V3H19V4H20V5H21V10H20V12H19V13H18V14H17V15H16V16H15V17H14V18H13V19H12V20M5 11V12H6V13H7V14H8V15H9V16H10V17H12V16H13V15H14V14H15V13H16V12H17V11H18V9H19V6H18V5H17V4H14V5H13V6H12V7H10V6H9V5H8V4H5V5H4V6H3V9H4V11H5Z"
                    ></path>
                  </svg>
                )}
              </span>
            </button>
          </div>

          {showSkinSelector ? <ChuSkinSelector /> : <BackgroundSelector />}
        </div>
      </div>
    </div>
  );
};

export default Custom;
