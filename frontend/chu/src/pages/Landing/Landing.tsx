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
      title: t("landing.githubLogin.title"),
      subtitle: t("landing.githubLogin.subtitle"),
      imageSrc: githubLogin,
      imagePosition: "left" as const,
      showButton: false,
    },
    {
      title: t("landing.commitSystem.title"),
      subtitle: t("landing.commitSystem.subtitle"),
      imageSrc: levelUp,
      imagePosition: "right" as const,
      showButton: false,
    },
    {
      title: t("landing.customization.title"),
      subtitle: t("landing.customization.subtitle"),
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
