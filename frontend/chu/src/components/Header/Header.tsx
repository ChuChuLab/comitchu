import { Link, useLocation } from "react-router-dom";
import { useUser } from "../../contexts/UserContext";
import { useTranslation } from "react-i18next";
import styles from "./Header.module.css";

const Header = () => {
  const { user, logout } = useUser();
  const location = useLocation();
  const { i18n } = useTranslation();

  const handleLogout = () => {
    logout();
  };

  const changeLanguage = (lang: "en" | "ko") => {
    i18n.changeLanguage(lang);
  };

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <Link to="/" className={styles.logo}>
          <span className={styles.logoIcon}>🐾</span>
          CommitChu
        </Link>

        <nav className={styles.nav}>
          {user ? (
            <>
              <Link
                to="/dashboard"
                className={`${styles.navLink} ${location.pathname === "/dashboard" ? styles.active : ""}`}
              >
                Dashboard
              </Link>
              <Link
                to="/settings"
                className={`${styles.navLink} ${location.pathname === "/settings" ? styles.active : ""}`}
              >
                Settings
              </Link>
              <div className={styles.userInfo}>
                <img src={user.avatarUrl || "/placeholder.svg"} alt={user.username} className={styles.avatar} />
                <span className={styles.username}>{user.username}</span>
                <button onClick={handleLogout} className={styles.logoutBtn}>
                  Logout
                </button>
              </div>
            </>
          ) : (
            <Link to="/" className={styles.navLink}>
              Home
            </Link>
          )}
          <div className={styles.langToggle}>
            <button
              className={`${styles.langBtn} ${i18n.language === "en" ? styles.activeLang : ""}`}
              onClick={() => changeLanguage("en")}
            >
              EN
            </button>
            <button
              className={`${styles.langBtn} ${i18n.language === "ko" ? styles.activeLang : ""}`}
              onClick={() => changeLanguage("ko")}
            >
              KO
            </button>
          </div>
        </nav>
      </div>
    </header>
  );
};

export default Header;
