// 로그인 권한이 있어야 입장 가능하게 하는 컴포넌트

import { Navigate, Outlet } from "react-router-dom";
import useUserStore from "../../store/userStore";

const ProtectedRoute = () => {
  const { user } = useUserStore();

  return user ? <Outlet /> : <Navigate to="/" replace />;
};

export default ProtectedRoute;
