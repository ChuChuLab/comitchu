import React, { useState } from "react";
import styles from "./FeatureSection.module.css";
import Button from "../common/Button";
import { useTranslation } from "react-i18next";

type FeatureSectionProps = {
  title: string;
  subtitle: string;
  imageSrc: string;
  imagePosition?: "left" | "right";
  showButton?: boolean;
};

const FeatureSection: React.FC<FeatureSectionProps> = ({
  title,
  subtitle,
  imageSrc,
  imagePosition = "right",
  showButton = false,
}) => {
  const { t } = useTranslation();
  const [imageError, setImageError] = useState(false);

  const handleGitHubLogin = () => {
    window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";
  };

  const handleImageError = () => {
    setImageError(true);
  };

  const content = (
    <div className={styles.featureContent}>
      <h1 className={styles.title}>{title}</h1>
      <p className={styles.subtitle}>{subtitle}</p>
      {showButton && <Button onClick={handleGitHubLogin}>{t("landing.login")}</Button>}
    </div>
  );

  const image = !imageError ? (
    <div className={styles.imageContainer}>
      <img src={imageSrc} alt="" className={styles.featureImageShadow} onError={handleImageError} />
      <img src={imageSrc} alt={title} className={styles.featureImage} onError={handleImageError} />
    </div>
  ) : null;

  return (
    <section className={`${styles.featureSection} animate-slide-up`}>
      {imagePosition === "left" ? (
        <>
          {image}
          {content}
        </>
      ) : (
        <>
          {content}
          {image}
        </>
      )}
    </section>
  );
};

export default FeatureSection;
