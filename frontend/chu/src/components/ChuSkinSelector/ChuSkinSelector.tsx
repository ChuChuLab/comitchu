import { useEffect, useState } from "react";
import styles from "./ChuSkinSelector.module.css";
import { fetchAllChuSkinsAPI, updateMainChuAPI } from "../../api/chu";
import type { ChuSkin } from "../../types/model";
import useChuStore from "../../store/chuStore";
import { useTranslation } from "react-i18next";

// NOTE: 서버로부터 받은 langId와 실제 이미지 파일명을 매핑합니다.
// 일부 파일명은 추측된 값으로, 실제 파일명과 다를 수 있습니다.
const LANG_ID_TO_FILENAME: { [key: number]: string } = {
  1: "comit.png",
  2: "Java.png",
  3: "Python.png",
  4: "Js.png",
  5: "TypeScript.png",
  6: "C.png",
  7: "C++.png",
  8: "CSharp.png",
  9: "Swift.png",
  10: "Php.png",
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

  const handleNextPage = () => {
    setCurrentPage((prev) => Math.min(prev + 1, totalPages));
  };

  const handlePrevPage = () => {
    setCurrentPage((prev) => Math.max(prev - 1, 1));
  };

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
  }, [t]);

  const handleSelectSkin = async (langId: number) => {
    try {
      const message = await updateMainChuAPI(langId);
      alert(message); // 성공 메시지 표시
      await fetchMainChu();
      // 선택 후 스킨 목록을 다시 불러와서 UI를 업데이트합니다.
      const fetchedSkins = await fetchAllChuSkinsAPI();
      setSkins(fetchedSkins);
    } catch (err) {
      alert(err instanceof Error ? err.message : t("chuSkinSelector.updateError"));
    }
  };

  if (loading) return <div>{t("chuSkinSelector.loading")}</div>;
  if (error) return <div>{t("chuSkinSelector.error", { error: error })}</div>;

  return (
    <div className={styles.selectorContainer}>
      <h2>{t("chuSkinSelector.title")}</h2>
      <div className={styles.gridContainer}>
        {currentSkins.map((skin) => {
          const fileName = LANG_ID_TO_FILENAME[skin.langId];
          if (!fileName) return null; // 매핑에 없는 경우 렌더링하지 않음

          const imageUrl = new URL(`../../assets/images/chu/normal/${fileName}`, import.meta.url).href;

          return (
            <div
              key={skin.langId}
              className={`${styles.skinItem} ${skin.isMain ? styles.mainSkin : ""} ${
                !skin.isUnlocked ? styles.lockedSkin : ""
              }`}
              onClick={() => skin.isUnlocked && handleSelectSkin(skin.langId)}
            >
              <img src={imageUrl} alt={`Skin ${skin.langId}`} className={styles.skinImage} />
              {skin.isMain && <div className={styles.mainTag}>{t("chuSkinSelector.mainTag")}</div>}
              {!skin.isUnlocked && (
                <div className={styles.lockedOverlay}>
                  <svg viewBox="0 0 22 22" width="60px" height="60px" fill="currentColor">
                    <path d="M10 12H12V13H13V15H12V16H10V15H9V13H10V12M8 2H14V3H15V4H16V8H17V9H18V19H17V20H5V19H4V9H5V8H6V4H7V3H8V2M9 4V5H8V8H14V5H13V4H9M16 10H6V18H16V10Z"></path>
                  </svg>
                </div>
              )}
            </div>
          );
        })}
      </div>
      <div className={styles.paginationControls}>
        <button onClick={handlePrevPage} disabled={currentPage === 1}>
          &lt;
        </button>
        <button onClick={handleNextPage} disabled={currentPage === totalPages}>
          &gt;
        </button>
      </div>
    </div>
  );
};

export default ChuSkinSelector;
