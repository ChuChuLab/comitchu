import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";
import heroVisual from "../../assets/images/heroImg.svg";
import FeatureSection from "../../components/FeatureSection/FeatureSection";

const Landing = () => {
  const { t } = useTranslation();

  const sectionsData = [
    {
      title: t("landing.title"),
      subtitle: t("landing.subtitle"),
      imageSrc: heroVisual,
      imagePosition: 'right' as const,
      showButton: true,
    },
    {
      title: "꾸준한 커밋을 위한 동기부여",
      subtitle: "커밋츄와 함께라면 매일의 커밋이 즐거워집니다. 당신의 노력을 시각적으로 확인하고 성취감을 느껴보세요.",
      imageSrc: heroVisual, // Should be a different image
      imagePosition: 'left' as const,
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