import React from 'react';
import HoloCard from '../ReactBits/HoloCard';
import styles from './FeatureSection.module.css';
import Button from '../common/Button';
import { useTranslation } from 'react-i18next';

type FeatureSectionProps = {
  title: string;
  subtitle: string;
  imageSrc: string;
  imagePosition?: 'left' | 'right';
  showButton?: boolean;
};

const FeatureSection: React.FC<FeatureSectionProps> = ({
  title,
  subtitle,
  imageSrc,
  imagePosition = 'right',
  showButton = false,
}) => {
  const { t } = useTranslation();
  const handleGitHubLogin = () => {
    window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";
  };

  const content = (
    <div className={styles.featureContent}>
      <h1 className={styles.title}>{title}</h1>
      <p className={styles.subtitle}>{subtitle}</p>
      {showButton && <Button onClick={handleGitHubLogin}>{t("landing.login")}</Button>}
    </div>
  );

  const image = <HoloCard imageSrc={imageSrc} width={450} height={300} />;

  return (
    <section className={styles.featureSection}>
      {imagePosition === 'left' ? (
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