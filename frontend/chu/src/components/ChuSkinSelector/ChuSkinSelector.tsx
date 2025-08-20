import { useEffect, useState, useRef, Fragment } from "react";
import styles from "./ChuSkinSelector.module.css";
import { fetchAllChuSkinsAPI, updateMainChuAPI } from "../../api/chu";
import type { ChuSkin } from "../../types/model";
import useChuStore from "../../store/chuStore";
import { useTranslation } from "react-i18next";
import Modal from "../../components/common/Modal";

const LANG_ID_TO_FILENAME: { [key: number]: string } = {
  1: "comit",
  2: "Java",
  3: "Python",
  4: "Js",
  5: "TypeScript",
  6: "C",
  7: "C++",
  8: "CSharp",
  9: "Swift",
  10: "Go",
  11: "Php",
  12: "Docker",
};

const ChuSkinSelector = () => {
  const { t } = useTranslation();
  const [skins, setSkins] = useState<ChuSkin[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { fetchMainChu } = useChuStore();

  // Pagination states
  const [currentPage, setCurrentPage] = useState(1);
  const skinsPerPage = 4;
  const indexOfLastSkin = currentPage * skinsPerPage;
  const indexOfFirstSkin = indexOfLastSkin - skinsPerPage;
  const currentSkins = skins.slice(indexOfFirstSkin, indexOfLastSkin);
  const totalPages = Math.ceil(skins.length / skinsPerPage);

  const handleNextPage = () => setCurrentPage((prev) => Math.min(prev + 1, totalPages));
  const handlePrevPage = () => setCurrentPage((prev) => Math.max(prev - 1, 1));

  // Result modal states (Dashboard와 동일한 패턴)
  const [showResultModal, setShowResultModal] = useState(false);
  const [isResultModalExiting, setIsResultModalExiting] = useState(false);
  const [resultMessage, setResultMessage] = useState<string>("");
  const resultModalTimeoutRef = useRef<number | null>(null);

  useEffect(() => {
    const getSkins = async () => {
      try {
        setLoading(true);
        const fetchedSkins = await fetchAllChuSkinsAPI();
        setSkins(fetchedSkins);
      } catch (err) {
        setError(err instanceof Error ? err.message : t("chuSkinSelector.fetchError"));
      } finally {
        setLoading(false);
      }
    };
    getSkins();

    // 언마운트 시 타이머 정리
    return () => {
      if (resultModalTimeoutRef.current) {
        window.clearTimeout(resultModalTimeoutRef.current);
      }
    };
  }, [t]);

  const openResultModal = (message: string) => {
    // 기존 타이머 정리
    if (resultModalTimeoutRef.current) {
      window.clearTimeout(resultModalTimeoutRef.current);
    }

    setResultMessage(message);
    setShowResultModal(true);
    setIsResultModalExiting(false);

    // 2.2초 표시 후 닫힘 애니메이션 0.8초
    resultModalTimeoutRef.current = window.setTimeout(() => {
      setIsResultModalExiting(true);
      resultModalTimeoutRef.current = window.setTimeout(() => {
        setShowResultModal(false);
      }, 800);
    }, 2200);
  };

  const handleSelectSkin = async (langId: number) => {
    try {
      const message = await updateMainChuAPI(langId);
      openResultModal(message); // alert 대신 Modal
      await fetchMainChu();

      // 선택 후 스킨 목록 갱신
      const fetchedSkins = await fetchAllChuSkinsAPI();
      setSkins(fetchedSkins);
    } catch (err) {
      openResultModal(err instanceof Error ? err.message : t("chuSkinSelector.updateError"));
    }
  };

  if (loading) return <div>{t("chuSkinSelector.loading")}</div>;
  if (error) return <div>{t("chuSkinSelector.error", { error })}</div>;

  return (
    <Fragment>
      <div className={styles.selectorContainer}>
        <h2>{t("chuSkinSelector.title")}</h2>
        <div className={styles.gridContainer}>
          {currentSkins.map((skin) => {
            const fileName = LANG_ID_TO_FILENAME[skin.langId];
            if (!fileName) return null;

            const imageUrl = new URL(`../../assets/images/chu/normal/${fileName}.png`, import.meta.url).href;

            return (
              <div
                key={skin.langId}
                className={`${styles.skinItem} ${skin.isMain ? styles.mainSkin : ""} ${
                  !skin.isUnlocked ? styles.lockedSkin : ""
                }`}
                onClick={() => skin.isUnlocked && handleSelectSkin(skin.langId)}
                role={skin.isUnlocked ? "button" : "img"}
                aria-disabled={!skin.isUnlocked}
                tabIndex={skin.isUnlocked ? 0 : -1}
                onKeyDown={(e) => {
                  if (skin.isUnlocked && (e.key === "Enter" || e.key === " ")) {
                    e.preventDefault();
                    handleSelectSkin(skin.langId);
                  }
                }}
              >
                <img src={imageUrl} alt={`Skin ${skin.langId}`} className={styles.skinImage} />
                {skin.isMain && <div className={styles.mainTag}>{t("chuSkinSelector.mainTag")}</div>}
                {!skin.isUnlocked && (
                  <div className={styles.lockedOverlay} aria-label={t("chuSkinSelector.locked")}>
                    <svg viewBox="0 0 22 22" width="60px" height="60px" fill="currentColor">
                      <path d="M10 12H12V13H13V15H12V16H10V15H9V13H10V12M8 2H14V3H15V4H16V8H17V9H18V19H17V20H5V19H4V9H5V8H6V4H7V3H8V2M9 4V5H8V8H14V5H13V4H9M16 10H6V18H16V10Z"></path>
                    </svg>
                  </div>
                )}
                <span className={styles.skinNameOverlay}>{fileName}</span>
              </div>
            );
          })}
        </div>

        <div className={styles.paginationControls}>
          <button onClick={handlePrevPage} disabled={currentPage === 1} aria-label={t("common.prev")}>
            &lt;
          </button>
          <button onClick={handleNextPage} disabled={currentPage === totalPages} aria-label={t("common.next")}>
            &gt;
          </button>
        </div>
      </div>

      {/* Dashboard와 동일한 사용 방식 */}
      <Modal isOpen={showResultModal} isExiting={isResultModalExiting} message={resultMessage} />
    </Fragment>
  );
};

export default ChuSkinSelector;
