import { useTranslation } from "react-i18next";
import styles from "./LanguageToggle.module.css";
import { useState } from "react";

const LanguageToggle = () => {
  const { i18n } = useTranslation();
  const isKorean = i18n.language === "ko";
  const [fade, setFade] = useState(false);

  const handleToggle = () => {
    setFade(true);
    setTimeout(() => {
      i18n.changeLanguage(isKorean ? "en" : "ko");
      setFade(false);
    }, 250);
  };

  return (
    <div className={styles.languageToggle}>
      <span className={styles.globeIcon}>
        <svg viewBox="0 0 22 22" height="32px" width="32px">
          <path
            fill="currentColor"
            d="M8 8H6V6H8M12 8H10V6H12M16 8H14V6H16M8 12H6V10H8M12 12H10V10H12M16 12H14V10H16M8 16H6V14H8M12 16H10V14H12M16 16H14V14H16M18 20H4V19H3V18H2V4H3V3H4V2H18V3H19V4H20V18H19V19H18M17 18V17H18V5H17V4H5V5H4V17H5V18Z"
          ></path>
        </svg>
      </span>
      <button className={styles.toggleBtn} onClick={handleToggle}>
        <span className={`${styles.langText} ${fade ? styles.fade : ""}`}>{isKorean ? "ko" : "en"}</span>
        <span className={styles.langDivider}>|</span>
        <span className={`${styles.langTextInactive} ${fade ? styles.fade : ""}`}>{isKorean ? "en" : "ko"}</span>
      </button>
    </div>
  );
};

export default LanguageToggle;
