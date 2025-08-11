import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import styles from "./Custom.module.css";
import ChuViewer from "../../components/ChuViewer/ChuViewer";

const Custom = () => {
  const { user } = useUserStore();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.custom}>
      <ChuViewer />
    </div>
  );
};

export default Custom;
