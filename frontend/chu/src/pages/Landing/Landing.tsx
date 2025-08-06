import useUserStore from "../../store/userStore";
import { Link } from "react-router-dom";
import Button from "../../components/common/Button";
import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";
import heroVisual from "../../assets/images/heroImg.svg";

const Landing = () => {
  const { user } = useUserStore();
  const { t } = useTranslation();

  const handleGitHubLogin = () => {
    window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";
  };

  return (
    <div className={styles.landing}>
      {/* 좌측영역 */}
      <div className={styles.heroContent}>
        <h1 className={styles.title}>{t("landing.title")}</h1>
        <p className={styles.subtitle}>{t("landing.subtitle")}</p>
        {user ? (
          <Link to="/dashboard">
            <Button>{t("landing.goToDashboard")}</Button>
          </Link>
        ) : (
          <Button onClick={handleGitHubLogin}>{t("landing.login")}</Button>
        )}
      </div>

      {/* 우측영역 */}
      <img src={heroVisual} alt="Hero Image" className={styles.heroVisual} />
    </div>
  );
};

export default Landing;
