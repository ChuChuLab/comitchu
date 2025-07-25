import { useUser } from "../../contexts/UserContext";
import { Link } from "react-router-dom";
import Button from "../../components/common/Button";
import styles from "./Landing.module.css";
import { useTranslation } from "react-i18next";

const Landing = () => {
  const { user, login } = useUser();
  const { t } = useTranslation();

  const handleGitHubLogin = () => {
    window.location.href = "https://www.comitchu.shop/oauth2/authorization/github";

    // 개발 환경에서 Mock 로그인 기능
    // api 나오면 axios 만들고 구현하기
    const mockUser = {
      id: "mockUser123",
      username: "mini-suyo",
      avatarUrl: "https://avatars.githubusercontent.com/u/175273485?v=4",
    };
    login(mockUser);
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
      <img src="/src/assets/img/cave.svg" alt="Hero Image" className={styles.heroVisual} />
    </div>
  );
};

export default Landing;
