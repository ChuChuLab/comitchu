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
import FullpageScroll from "../../components/FullpageScroll/FullpageScroll";

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
    <FullpageScroll>
      {sectionsData.map((section, idx) => (
        <FullpageScroll.Section key={idx}>
          <div className={styles.landingContainer}>
            <FeatureSection {...section} />
            {idx < sectionsData.length - 1 && (
              <svg className={styles.blinkingSvg} viewBox="0 0 22 22" style={{ height: "60px", width: "60px" }}>
                <path
                  fill="currentColor"
                  d="M4 8H18V10H17V11H16V12H15V13H14V14H13V15H12V16H10V15H9V14H8V13H7V12H6V11H5V10H4V8M8 10V11H9V12H10V13H12V12H13V11H14V10H8Z"
                ></path>
              </svg>
            )}
          </div>
        </FullpageScroll.Section>
      ))}
    </FullpageScroll>
  );
};

export default Landing;
