import axios from "axios";

// 제네릭을 사용한 공통 API 응답 인터페이스
export interface ApiResponse<T> {
  success: boolean;
  data: T;
  // 선택적 프로퍼티 필드 (에러 코드와 메시지)
  error?: {
    code: string;
    message: string;
  };
  timestamp: string;
}

const apiClient = axios.create({
  baseURL: "/api", // 백엔드 API 프록시 경로
  withCredentials: true, // HTTP-Only 쿠키를 주고받기 위해 필수
  headers: {
    "Content-Type": "application/json",
  },
});

// 개발 환경에서만 X-DEV-USER 헤더 추가
if (import.meta.env.DEV) {
  apiClient.defaults.headers.common["X-DEV-USER"] = "1";
  console.log("짜증");
}

export default apiClient;
