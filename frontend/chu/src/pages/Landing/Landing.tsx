import { useUser } from "../../contexts/UserContext";
import { Navigate } from "react-router-dom";
import BadgePreview from "../../components/BadgePreview/BadgePreview";
import Button from "../../components/common/Button";
import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";

const Landing = () => {
  const { user, login } = useUser();
  const { t } = useTranslation();

  if (user) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleGitHubLogin = () => {
    window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";

    // 개발 환경에서 Mock 로그인 기능
    // api 나오면 axios 만들고 구현하기
    const mockUser = {
      id: "test-user",
      username: "mini-suyo",
      avatarUrl: "https://avatars.githubusercontent.com/u/175273485?v=4",
    };
    login(mockUser);
  };

  return (
    <div className={styles.landing}>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1 className={styles.title}>
            {t("landing.title")}
            <span className={styles.titleAccent}>{t("landing.titleAccent")}</span>
          </h1>
          <p className={styles.subtitle}>{t("landing.subtitle")}</p>
          <Button onClick={handleGitHubLogin}>{t("landing.login")}</Button>
        </div>
        <div className={styles.heroVisual}>
          <div className={styles.petPreview}>
            <div className={styles.petCharacter}>🐱</div>
            <div className={styles.petStats}>
              <div className={styles.statBar}>
                <span>{t("landing.level", { level: 5 })}</span>
                <div className={styles.xpBar}>
                  <div className={styles.xpFill} style={{ width: "70%" }}></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className={styles.features}>
        <h2 className={styles.sectionTitle}>{t("landing.howItWorks")}</h2>
        <div className={styles.featureGrid}>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🎨</div>
            <h3>{t("landing.feature.choose.title")}</h3>
            <p>{t("landing.feature.choose.desc")}</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>📈</div>
            <h3>{t("landing.feature.grow.title")}</h3>
            <p>{t("landing.feature.grow.desc")}</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🏆</div>
            <h3>{t("landing.feature.show.title")}</h3>
            <p>{t("landing.feature.show.desc")}</p>
          </div>
        </div>
      </section>

      <section className={styles.preview}>
        <h2 className={styles.sectionTitle}>{t("landing.preview.title")}</h2>
        <p className={styles.previewDescription}>{t("landing.preview.description")}</p>
        <BadgePreview petName="Whiskers" level={5} character="🐱" mood="happy" />
      </section>
    </div>
  );
};

export default Landing;
