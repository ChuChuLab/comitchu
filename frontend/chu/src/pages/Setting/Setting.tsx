import useUserStore from "../../store/userStore";
import { Navigate } from "react-router-dom";
import styles from "./Setting.module.css";
import ChuViewer from "../../components/ChuViewer/ChuViewer";

const Setting = () => {
  const { user } = useUserStore();

  if (!user) {
    return <Navigate to="/" replace />;
  }

  return (
    <div className={styles.setting}>
      <ChuViewer />
    </div>
  );
};

export default Setting;
