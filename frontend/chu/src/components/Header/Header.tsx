import { Link, useLocation } from "react-router-dom";
import { useUser } from "../../contexts/UserContext";
import LanguageToggle from "../LanguageToggle/LanguageToggle";
import styles from "./Header.module.css";
import Button from "../common/Button";

const Header = () => {
  const { user, logout } = useUser();
  const location = useLocation();
  const handleLogout = () => {
    logout();
  };

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <Link to="/" className={styles.logo}>
          <span className={styles.logoIcon}>🐾</span>
          CommitChu
        </Link>

        <nav className={styles.nav}>
          {user && (
            <>
              <Link
                to="/dashboard"
                className={`${styles.navLink} ${location.pathname === "/dashboard" ? styles.active : ""}`}
              >
                Dashboard
              </Link>
              <Link
                to="/setting"
                className={`${styles.navLink} ${location.pathname === "/setting" ? styles.active : ""}`}
              >
                Settings
              </Link>
              <div className={styles.userInfo}>
                <img src={user.avatarUrl || "/placeholder.svg"} alt={user.username} className={styles.avatar} />
                <span className={styles.username}>{user.username}</span>
                <Button onClick={handleLogout} variant="danger">
                  Logout
                </Button>
              </div>
            </>
          )}
          <LanguageToggle />
        </nav>
      </div>
    </header>
  );
};

export default Header;
