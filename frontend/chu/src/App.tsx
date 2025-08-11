import { useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import Header from "./components/Header/Header";
import Landing from "./pages/Landing/Landing";
import Dashboard from "./pages/Dashboard/Dashboard";
import Custom from "./pages/Custom/Custom";
import Error from "./pages/Error/Error";
import styles from "./App.module.css";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute";
import useUserStore from "./store/userStore";

function App() {
  const { fetchUser } = useUserStore();

  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  return (
    <div className={styles.app}>
      <Header />
      <main className={styles.main} role="main">
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/custom" element={<Custom />} />
          </Route>
          <Route path="*" element={<Error />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;
