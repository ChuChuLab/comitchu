import { useState, useEffect } from "react";
import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore";
import { fetchChuSvgAPI } from "../../api/chu";
import styles from "./Dashboard.module.css";
import { useTranslation } from "react-i18next";

const Dashboard = () => {
  const { t } = useTranslation();
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();

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

  if (isLoading || svgLoading) {
    return <p>{t("dashboard.loading")}</p>;
  }

  if (error || svgError) {
    return <p>{t("dashboard.error", { error: error || svgError })}</p>;
  }

  return (
    <div className={`${styles.dashboard} animate-slide-up`}>
      <div className={styles.contentWrapper}>
        {svgContent && (
          <div className={styles.svgImageContainer}>
            <div className={styles.svgImageBackground} dangerouslySetInnerHTML={{ __html: svgContent }} />
            <div className={styles.svgImage} dangerouslySetInnerHTML={{ __html: svgContent }} />
          </div>
        )}
        <section className={styles.mainChuSection}>
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
              {/* <p>{t("dashboard.background")}: {mainChu.background}</p> */}
            </div>
          ) : (
            <p>{t("dashboard.noChu")}</p>
          )}
        </section>
      </div>
    </div>
  );
};

export default Dashboard;
