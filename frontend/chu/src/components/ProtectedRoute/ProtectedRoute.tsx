import { Navigate, Outlet } from "react-router-dom";
import useUserStore from "../../store/userStore";

const ProtectedRoute = () => {
  const { user } = useUserStore();

  return user ? <Outlet /> : <Navigate to="/" replace />;
};

export default ProtectedRoute;
