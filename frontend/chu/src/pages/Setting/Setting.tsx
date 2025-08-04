import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import styles from "./Setting.module.css";

const Setting = () => {
  const { user } = useUserStore();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.setting}>
      <h1 className={styles.title}>Pet Setting</h1>
    </div>
  );
};

export default Setting;
