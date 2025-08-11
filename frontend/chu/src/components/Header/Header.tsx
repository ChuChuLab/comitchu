import { Link, useLocation } from "react-router-dom";
import useUserStore from "../../store/userStore";
import LanguageToggle from "../LanguageToggle/LanguageToggle";
import styles from "./Header.module.css";
import { LogoutIcon } from "./LogoutIcon";

const Header = () => {
  const { user, logout } = useUserStore();
  const location = useLocation();
  const handleLogout = () => {
    logout();
  };

  return (
    <header className={styles.header}>
      <div className={styles.container}>
        <Link to="/" className={styles.logo}>
          ComitChu
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
                to="/custom"
                className={`${styles.navLink} ${location.pathname === "/custom" ? styles.active : ""}`}
              >
                Custom
              </Link>
            </>
          )}
          <LanguageToggle />
          {user && (
            <>
              <div className={styles.userInfo}>
                <svg viewBox="0 0 22 22" height="32px" width="32px">
                  <path
                    fill="currentColor"
                    d="M4 2H18V3H19V4H20V18H19V19H18V20H4V19H3V18H2V4H3V3H4V2M4 16H5V15H7V14H15V15H17V16H18V5H17V4H5V5H4V16M16 18V17H14V16H8V17H6V18H16M9 5H13V6H14V7H15V11H14V12H13V13H9V12H8V11H7V7H8V6H9V5M12 8V7H10V8H9V10H10V11H12V10H13V8H12Z"
                  ></path>
                </svg>
                <span className={styles.userName}>{user.userName}</span>
                <button onClick={handleLogout} className={styles.logoutButton}>
                  <LogoutIcon />
                </button>
              </div>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
