import { create } from "zustand";
import apiClient from "../api";
import type { Chu } from "../types/model";

// 제네릭을 사용한 공통 API 응답 인터페이스
interface ApiResponse<T> {
  success: boolean;
  data: T;
  // 선택적 프로퍼티 필드 (에러 코드와 메시지)
  error?: {
    code: string;
    message: string;
  };
  timestamp: string;
}

// 츄 목록 api가 반환하는 data 타입 정의
type ChuListData = Chu[];

// 츄 스토어의 상태와 액션에 대한 인터페이스
// 각 api를 호출하면서 만들 메서드 이름을 여기에 정리하기
interface ChuState {
  chus: Chu[];
  isLoading: boolean;
  error: string | null;
  fetchChus: () => Promise<void>;
}

const useChuStore = create<ChuState>((set) => ({
  chus: [],
  isLoading: false,
  error: null,
  fetchChus: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await apiClient.get<ApiResponse<ChuListData>>("/chu/list");
      if (response.data.success) {
        set({ chus: response.data.data, isLoading: false });
      } else {
        set({
          chus: [],
          isLoading: false,
          error: response.data.error?.message || "서버 에러가 발생했습니다.",
        });
      }
    } catch (error) {
      set({
        chus: [],
        isLoading: false,
        error: "츄 목록을 불러오는데 실패했습니다. 네트워크 연결을 확인해주세요.",
      });
    }
  },
}));

export default useChuStore;
