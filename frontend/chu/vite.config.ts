import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig(({ mode }) => {
  // 현재 모드(development, production 등)에 해당하는 .env 파일 로드
  // 세 번째 인자로 ''를 전달하면 모든 환경 변수를 로드합니다.
  const env = loadEnv(mode, process.cwd(), "");

  return {
    plugins: [react()],
    server: {
      proxy: {
        "/api": {
          target: "https://www.comitchu.shop",
          changeOrigin: true,
          headers: {
            // 로드된 환경 변수 사용
            "X-DEV-USER": env.VITE_DEV_USER_ID,
          },
        },
      },
    },
  };
});
