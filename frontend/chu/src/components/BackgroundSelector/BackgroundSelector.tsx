import { useState, useRef, Fragment, useEffect } from "react";
import useChuStore from "../../store/chuStore";
import { updateChuBackgroundAPI } from "../../api/chu";
import styles from "./BackgroundSelector.module.css";
import { useTranslation } from "react-i18next";
import Modal from "../../components/common/Modal";

const BACKGROUND_IMAGES = [
  "abandonedChurch.png",
  "abandonedRuin.png",
  "cave.png",
  "caveEntrance.png",
  "coffee.png",
  "dragonNest.png",
  "forest.png",
  "glassCity.png",
  "GoldenRoom.png",
  "jail.png",
  "kingdomEntrance.png",
  "library.png",
  "magicCircle.png",
  "magicStore.png",
  "moonLake.png",
  "newbie.png",
  "office.png",
  "ruin.png",
  "sakuraRoad.png",
  "starLibrary.png",
  "terminal.png",
  "timeSquare.png",
  "tower.png",
  "twilight.png",
  "village.png",
  "windmillVillage.png",
  "witchHouse.png",
  "witchSchool.png",
];

const BackgroundSelector = () => {
  const { t } = useTranslation();
  const { mainChu, fetchMainChu } = useChuStore();

  // Pagination states
  const [currentPage, setCurrentPage] = useState(1);
  const backgroundsPerPage = 6;

  const indexOfLastBackground = currentPage * backgroundsPerPage;
  const indexOfFirstBackground = indexOfLastBackground - backgroundsPerPage;
  const currentBackgrounds = BACKGROUND_IMAGES.slice(indexOfFirstBackground, indexOfLastBackground);

  const totalPages = Math.ceil(BACKGROUND_IMAGES.length / backgroundsPerPage);

  const handleNextPage = () => setCurrentPage((prev) => Math.min(prev + 1, totalPages));
  const handlePrevPage = () => setCurrentPage((prev) => Math.max(prev - 1, 1));

  // Result modal states (Dashboard와 동일한 패턴)
  const [showResultModal, setShowResultModal] = useState(false);
  const [isResultModalExiting, setIsResultModalExiting] = useState(false);
  const [resultMessage, setResultMessage] = useState<string>("");
  const resultModalTimeoutRef = useRef<number | null>(null);

  useEffect(() => {
    return () => {
      if (resultModalTimeoutRef.current) window.clearTimeout(resultModalTimeoutRef.current);
    };
  }, []);

  const openResultModal = (message: string) => {
    if (resultModalTimeoutRef.current) {
      window.clearTimeout(resultModalTimeoutRef.current);
    }

    setResultMessage(message);
    setShowResultModal(true);
    setIsResultModalExiting(false);

    resultModalTimeoutRef.current = window.setTimeout(() => {
      setIsResultModalExiting(true);
      resultModalTimeoutRef.current = window.setTimeout(() => {
        setShowResultModal(false);
      }, 800);
    }, 2200);
  };

  const handleSelectBackground = async (backgroundFile: string) => {
    const nameOnly = backgroundFile.replace(".png", "");
    try {
      const message = await updateChuBackgroundAPI(nameOnly);
      openResultModal(message); // alert → Modal
      await fetchMainChu();
    } catch (err) {
      openResultModal(err instanceof Error ? err.message : t("backgroundSelector.updateError"));
    }
  };

  return (
    <Fragment>
      <div className={styles.selectorContainer}>
        <h2>{t("backgroundSelector.title")}</h2>
        <div className={styles.gridContainer}>
          {currentBackgrounds.map((imageFile) => {
            const imageUrl = new URL(`../../assets/images/backgrounds/${imageFile}`, import.meta.url).href;
            const backgroundName = imageFile.replace(".png", "");
            const isActive = mainChu?.background === backgroundName;

            return (
              <div
                key={imageFile}
                className={`${styles.backgroundItem} ${isActive ? styles.activeBackground : ""}`}
                onClick={() => handleSelectBackground(imageFile)}
                role="button"
                tabIndex={0}
                onKeyDown={(e) => {
                  if (e.key === "Enter" || e.key === " ") {
                    e.preventDefault();
                    handleSelectBackground(imageFile);
                  }
                }}
                aria-pressed={isActive}
              >
                <img src={imageUrl} alt={backgroundName} className={styles.backgroundImage} />
                {isActive && <div className={styles.activeTag}>{t("backgroundSelector.activeTag")}</div>}
              </div>
            );
          })}
        </div>

        {/* Pagination Controls */}
        <div className={styles.paginationControls}>
          <button onClick={handlePrevPage} disabled={currentPage === 1} aria-label={t("common.prev")}>
            &lt;
          </button>
          <button onClick={handleNextPage} disabled={currentPage === totalPages} aria-label={t("common.next")}>
            &gt;
          </button>
        </div>
      </div>

      <Modal isOpen={showResultModal} isExiting={isResultModalExiting} message={resultMessage} />
    </Fragment>
  );
};

export default BackgroundSelector;
