import useChuStore from "../../store/chuStore";
import { useEffect } from "react";
import styles from "./ChuViewer.module.css";
import { useTranslation } from "react-i18next";

interface ChuViewerProps {
  backgroundImagePath: string;
  chuImagePath: string;
  className?: string;
}

const ChuViewer = ({ backgroundImagePath, chuImagePath, className }: ChuViewerProps) => {
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

  return (
    <div className={className}>
      {" "}
      {/* Apply className to the outermost div */}
      <div className={styles.svgImageBackground}>
        <img src={backgroundImagePath} alt={t("chuViewer.backgroundAlt")} className={styles.backgroundImage} />
      </div>
      <div className={styles.svgImage}>
        <img src={chuImagePath} alt={t("chuViewer.chuAlt")} className={styles.chuImage} />
      </div>
    </div>
  );
};

export default ChuViewer;
