import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";
import FeatureSection from "../../components/FeatureSection/FeatureSection";
import heroVisual from "../../assets/images/heroImg.svg";
// import githubLogin from "../../assets/images/githubLogin.png";
import levelUp from "../../assets/images/LevelUp.png";
import custom from "../../assets/images/custom.png";
import howToCopy from "../../assets/images/howToCopy.png";
import howToApply from "../../assets/images/howToApply.png";
import howToUnlock from "../../assets/images/howToUnlock.png";

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
      title: t("landing.howToCopy.title"),
      subtitle: t("landing.howToCopy.subtitle"),
      imageSrc: howToCopy,
      imagePosition: "left" as const,
      showButton: false,
    },
    {
      title: t("landing.howToApply.title"),
      subtitle: t("landing.howToApply.subtitle"),
      imageSrc: howToApply,
      imagePosition: "right" as const,
      showButton: false,
    },
    // {
    //   title: t("landing.githubLogin.title"),
    //   subtitle: t("landing.githubLogin.subtitle"),
    //   imageSrc: githubLogin,
    //   imagePosition: "left" as const,
    //   showButton: false,
    // },
    {
      title: t("landing.howToUnlock.title"),
      subtitle: t("landing.howToUnlock.subtitle"),
      imageSrc: howToUnlock,
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
      imageSrc: custom,
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
