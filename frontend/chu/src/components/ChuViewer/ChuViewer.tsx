import useChuStore from "../../store/chuStore";
import { useEffect } from "react";
import styles from "./ChuViewer.module.css";
import { useTranslation } from "react-i18next";

const ChuViewer = () => {
  const { t } = useTranslation();
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();

  useEffect(() => {
    fetchMainChu();
  }, [fetchMainChu]);

  if (isLoading) {
    return <div>{t("chuViewer.loading")}</div>;
  }

  if (error) {
    return <div>{t("chuViewer.error", { error: error })}</div>;
  }

  if (!mainChu) {
    return <div>{t("chuViewer.noData")}</div>;
  }

  const chuImagePath = new URL(`../../assets/images/chu/normal/${mainChu.lang}.png`, import.meta.url).href;
  const backgroundImagePath = new URL(`../../assets/images/backgrounds/${mainChu.background}.png`, import.meta.url)
    .href;

  return (
    <div className={styles.chuViewer}>
      <img src={backgroundImagePath} alt={t("chuViewer.backgroundAlt")} className={styles.backgroundImage} />
      <img src={chuImagePath} alt={t("chuViewer.chuAlt")} className={styles.chuImage} />
    </div>
  );
};

export default ChuViewer;
