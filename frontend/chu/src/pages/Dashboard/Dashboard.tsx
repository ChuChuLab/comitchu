import { useState, useEffect, Fragment, useRef } from "react";
import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore";
import { fetchChuSvgAPI } from "../../api/chu";
import styles from "./Dashboard.module.css";
import { useTranslation } from "react-i18next";

const Dashboard = () => {
  const { t } = useTranslation();
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();
  const [showCopyModal, setShowCopyModal] = useState(false);
  const [isExiting, setIsExiting] = useState(false);
  const modalTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  // 로그인한 사용자 정보
  const user = useUserStore((state) => state.user);
  // svg 관련 상태 정보
  const [svgContent, setSvgContent] = useState<string | null>(null);
  const [svgLoading, setSvgLoading] = useState(true);
  const [svgError, setSvgError] = useState<string | null>(null);

  useEffect(() => {
    fetchMainChu();
  }, [fetchMainChu]);

  useEffect(() => {
    const getChuSvg = async () => {
      if (user && user.userName) {
        try {
          setSvgLoading(true);
          const svgData = await fetchChuSvgAPI(user.userName);
          setSvgContent(svgData);
          setSvgError(null);
        } catch (err) {
          setSvgError(err instanceof Error ? err.message : t("dashboard.fetchError"));
        } finally {
          setSvgLoading(false);
        }
      }
    };

    getChuSvg();
  }, [user, t]);

  useEffect(() => {
    // Cleanup timeout on component unmount
    return () => {
      if (modalTimeoutRef.current) {
        clearTimeout(modalTimeoutRef.current);
      }
    };
  }, []);

  const handleCopyClick = () => {
    if (user && user.userName) {
      if (modalTimeoutRef.current) {
        clearTimeout(modalTimeoutRef.current);
      }

      const textToCopy = `<a href="https://www.comitchu.shop" target="_blank"><img src="https://www.comitchu.shop/api/chu/mini-${user.userName}" alt="커밋츄" width="300" height="200" /></a>`;
      navigator.clipboard.writeText(textToCopy).then(() => {
        setShowCopyModal(true);
        setIsExiting(false);

        modalTimeoutRef.current = setTimeout(() => {
          setIsExiting(true);

          modalTimeoutRef.current = setTimeout(() => {
            setShowCopyModal(false);
          }, 800); // Animation duration
        }, 2200); // Time before exit starts
      });
    }
  };

  if (isLoading || svgLoading) {
    return <p>{t("dashboard.loading")}</p>;
  }

  if (error || svgError) {
    return <p>{t("dashboard.error", { error: error || svgError })}</p>;
  }

  return (
    <Fragment>
      <div className={`${styles.dashboard} animate-slide-up`}>
        <div className={styles.contentWrapper}>
          {svgContent && (
            <div className={styles.svgImageContainer}>
              <div className={styles.svgImageBackground} dangerouslySetInnerHTML={{ __html: svgContent }} />
              <div className={styles.svgImage} dangerouslySetInnerHTML={{ __html: svgContent }} />
            </div>
          )}
          <div className={styles.mainChuSection}>
            {mainChu ? (
              <div>
                <h2>{mainChu.nickname}</h2>
                <p>
                  {t("dashboard.level")}: {mainChu.level}
                </p>
                <p>
                  {t("dashboard.exp")}: {mainChu.exp}
                </p>
                <p>
                  {t("dashboard.status")}: {mainChu.status}
                </p>
                <p>
                  {t("dashboard.language")}: {mainChu.lang}
                </p>
                <button onClick={handleCopyClick} className={styles.copyButton}>
                  <svg viewBox="0 0 22 22" width="32px" height="32px">
                    <path
                      fill="currentColor"
                      d="M4 2H18V3H19V4H20V18H19V19H18V20H4V19H3V18H2V4H3V3H4V2M17 5V4H5V5H4V17H5V18H17V17H18V5H17M6 8H16V10H6V8M6 12H13V14H6V12Z"
                    ></path>
                  </svg>
                </button>
              </div>
            ) : (
              <p>{t("dashboard.noChu")}</p>
            )}
          </div>
        </div>
      </div>
      {showCopyModal && (
        <div className={`${styles.copyModal} ${isExiting ? "animate-slide-down" : "animate-slide-up"}`}>
          {t("dashboard.copiedToClipboard")}
        </div>
      )}
    </Fragment>
  );
};

export default Dashboard;
