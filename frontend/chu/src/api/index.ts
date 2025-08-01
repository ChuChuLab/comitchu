// 파일명 바꿀까나 axios로~

import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api", // 백엔드 API 프록시 경로
  withCredentials: true, // HTTP-Only 쿠키를 주고받기 위해 필수
});

export default apiClient;
