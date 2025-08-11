import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import styles from "./Custom.module.css";
import ChuViewer from "../../components/ChuViewer/ChuViewer";
import ChuSkinSelector from "../../components/ChuSkinSelector/ChuSkinSelector";
import BackgroundSelector from "../../components/BackgroundSelector/BackgroundSelector";

const Custom = () => {
  const { user } = useUserStore();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.custom}>
      <ChuViewer />
      <ChuSkinSelector />
      <BackgroundSelector />
    </div>
  );
};

export default Custom;
