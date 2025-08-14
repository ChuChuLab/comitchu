import { useEffect, useState } from "react";
import styles from "./ChuSkinSelector.module.css";
import { fetchAllChuSkinsAPI, updateMainChuAPI } from "../../api/chu";
import type { ChuSkin } from "../../types/model";

// NOTE: 서버로부터 받은 langId와 실제 이미지 파일명을 매핑합니다.
// 일부 파일명은 추측된 값으로, 실제 파일명과 다를 수 있습니다.
const LANG_ID_TO_FILENAME: { [key: number]: string } = {
  1: "comit.png",
  2: "Java.png",
  3: "Python.png",
  4: "Js.png", // 아직 이미지 없음
  5: "TypeScript.png", // 아직 이미지 없음
  6: "C.png",
  7: "C++.png", // 아직 이미지 없음
  8: "Csharp.png", // 아직 이미지 없음
  9: "Swift.png", // 아직 이미지 없음
  10: "Go.png", // 아직 이미지 없음
};

const ChuSkinSelector = () => {
  const [skins, setSkins] = useState<ChuSkin[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const getSkins = async () => {
      try {
        setLoading(true);
        const fetchedSkins = await fetchAllChuSkinsAPI();
        setSkins(fetchedSkins);
      } catch (err) {
        setError(err instanceof Error ? err.message : "스킨 목록을 불러오는데 실패했습니다.");
      } finally {
        setLoading(false);
      }
    };
    getSkins();
  }, []);

  const handleSelectSkin = async (langId: number) => {
    try {
      const message = await updateMainChuAPI(langId);
      alert(message); // 성공 메시지 표시
      // 선택 후 스킨 목록을 다시 불러와서 UI를 업데이트합니다.
      const fetchedSkins = await fetchAllChuSkinsAPI();
      setSkins(fetchedSkins);
    } catch (err) {
      alert(err instanceof Error ? err.message : "스킨 변경에 실패했습니다.");
    }
  };

  if (loading) return <div>로딩 중...</div>;
  if (error) return <div>에러: {error}</div>;

  return (
    <div className={styles.selectorContainer}>
      <h2>대표 츄 스킨 선택</h2>
      <div className={styles.gridContainer}>
        {skins.map((skin) => {
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
              {skin.isMain && <div className={styles.mainTag}>대표</div>}
              {!skin.isUnlocked && <div className={styles.lockedOverlay}>🔒</div>}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ChuSkinSelector;
