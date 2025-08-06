import apiClient, { type ApiResponse } from "./index";
import type { Chu, ChuSkin } from "../types/model";

// 사용자의 메인 츄 정보를 가져오는 api
export const fetchMainChuAPI = async (): Promise<Chu> => {
  try {
    const response = await apiClient.get<ApiResponse<Chu>>("/chu/main");
    if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.error?.message || "츄 정보를 가져오는 중 알 수 없는 오류가 발생했습니다.");
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.";
    throw new Error(message);
  }
};

// 사용자가 보유한 츄 스킨(언어)의 상태 목록을 가져오는 api
export const fetchAllChuSkinsAPI = async (): Promise<ChuSkin[]> => {
  try {
    const response = await apiClient.get<ApiResponse<any[]>>("/chu/list");
    if (response.data.success) {
      return response.data.data;
    } else {
      throw new Error(response.data.error?.message || "전체 츄 스킨 목록을 불러오는데 실패했습니다.");
    }
  } catch (error) {
    const message = error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.";
    throw new Error(message);
  }
};

// githubUsername을 기반으로 츄 svg를 가져오는 api
export const fetchChuSvgAPI = async (githubUsername: string): Promise<string> => {
  try {
    const response = await apiClient.get<string>(`/chu/${githubUsername}`);
    return response.data;
  } catch (error) {
    const message = error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.";
    throw new Error(message);
  }
};
