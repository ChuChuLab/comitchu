import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";
import FeatureSection from "../../components/FeatureSection/FeatureSection";
import heroVisual from "../../assets/images/heroImg.svg";
import githubLogin from "../../assets/images/githubLogin.png";
import levelUp from "../../assets/images/LevelUp.png";

const Landing = () => {
  const { t } = useTranslation();

  const sectionsData = [
    {
      title: t("landing.title"),
      subtitle: t("landing.subtitle"),
      imageSrc: heroVisual,
      imagePosition: "right" as const,
      showButton: true,
    },
    {
      title: "GitHub 연동 로그인",
      subtitle: "GitHub 계정으로 간편하게 시작할 수 있어요.",
      imageSrc: githubLogin,
      imagePosition: "left" as const,
      showButton: false,
    },
    {
      title: "커밋 기반 성장 시스템",
      subtitle: "커밋할수록 캐릭터가 성장하고 레벨업합니다.",
      imageSrc: levelUp,
      imagePosition: "right" as const,
      showButton: false,
    },
    {
      title: "GitHub 연동 로그인",
      subtitle: "GitHub 계정으로 간편하게 시작할 수 있어요.",
      imageSrc: githubLogin,
      imagePosition: "left" as const,
      showButton: false,
    },
  ];

  return (
    <div className={styles.landingContainer}>
      {sectionsData.map((section, index) => (
        <FeatureSection key={index} {...section} />
      ))}
    </div>
  );
};

export default Landing;
