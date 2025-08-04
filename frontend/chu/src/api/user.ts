import apiClient, { type ApiResponse } from "./index";
import type { User } from "../types/model";

// 내 정보를 가져오는 api
export const fetchUserAPI = async (): Promise<User> => {
  try {
    const response = await apiClient.get<ApiResponse<User>>("/user/me");
    if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.error?.message || "사용자 정보를 불러오는데 실패했습니다.");
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.";
    throw new Error(message);
  }
};

// 로그아웃 api
export const logoutAPI = async (): Promise<void> => {
  try {
    const response = await apiClient.post<ApiResponse<Record<string, never>>>("/user/logout");
    if (!response.data.success) {
      throw new Error(response.data.error?.message || "로그아웃에 실패했습니다.");
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.";
    throw new Error(message);
  }
};
