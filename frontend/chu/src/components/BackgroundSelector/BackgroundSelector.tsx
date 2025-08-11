import useChuStore from "../../store/chuStore";
import { updateChuBackgroundAPI } from "../../api/chu";
import styles from "./BackgroundSelector.module.css";

const BACKGROUND_IMAGES = [
  "abandonedChurch.png",
  "abandonedRuin.png",
  "cafe.png",
  "cave.png",
  "caveEntrance.png",
  "coffee.png",
  "dragonNest.png",
  "flower.png",
  "forest.png",
  "glassCity.png",
  "GoldenRoom.png",
  "grass.png",
  "jail.png",
  "kingdomEntrance.png",
  "lab.png",
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
  "turminal.png",
  "twilight.png",
  "village.png",
  "windmillVillage.png",
  "witchHouse.png",
  "witchSchool.png",
];

const BackgroundSelector = () => {
  const { mainChu, fetchMainChu } = useChuStore();

  const handleSelectBackground = async (backgroundName: string) => {
    // API 요청 시에는 파일 확장자를 제거하고 보냅니다.
    const nameOnly = backgroundName.replace(".png", "");
    try {
      const message = await updateChuBackgroundAPI(nameOnly);
      alert(message);
      await fetchMainChu(); // 상태를 최신화하여 UI에 반영
    } catch (err) {
      alert(err instanceof Error ? err.message : "배경 변경에 실패했습니다.");
    }
  };

  return (
    <div className={styles.selectorContainer}>
      <h2>배경화면 선택</h2>
      <div className={styles.gridContainer}>
        {BACKGROUND_IMAGES.map((imageFile) => {
          const imageUrl = new URL(`../../assets/images/backgrounds/${imageFile}`, import.meta.url).href;
          const backgroundName = imageFile.replace(".png", "");
          const isActive = mainChu?.background === backgroundName;

          return (
            <div
              key={imageFile}
              className={`${styles.backgroundItem} ${isActive ? styles.activeBackground : ""}`}
              onClick={() => handleSelectBackground(imageFile)}
            >
              <img src={imageUrl} alt={backgroundName} className={styles.backgroundImage} />
              {isActive && <div className={styles.activeTag}>적용중</div>}
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default BackgroundSelector;
