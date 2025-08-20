import React, { useState } from "react";
import styles from "./FeatureSection.module.css";
import Button from "../common/Button";
import { useTranslation } from "react-i18next";
import useUserStore from "../../store/userStore"; // Import useUserStore
import { useNavigate } from "react-router-dom"; // Import useNavigate

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
  const { user } = useUserStore();
  const navigate = useNavigate();

  const handleGitHubLogin = () => {
    if (user) {
      navigate("/dashboard");
    } else {
      window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";
    }
  };

  const handleImageError = () => {
    setImageError(true);
  };

  const content = (
    <div className={styles.featureContent}>
      <h1 className={styles.title}>{title}</h1>
      <p className={styles.subtitle}>{subtitle}</p>
      {showButton && (
        <Button onClick={handleGitHubLogin}>
          {user ? t("landing.goToDashboard") : t("landing.login")}
        </Button>
      )}
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
