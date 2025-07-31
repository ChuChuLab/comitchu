import { create } from "zustand";
import apiClient from "../api";
import type { User, Chu } from "../types/model";

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

// fetchUser API가 반환하는 data의 타입
interface UserData {
  userName: string;
  avatarUrl: string;
}

// 각 api를 호출하면서 만들 메서드 이름을 여기에 정리하기
// 후에 기능 완료 후 필요없는 기능은 날려야댐
// pet -> chu로 필드명 변경
interface UserState {
  user: User | null;
  isLoggedIn: boolean;
  isLoading: boolean;
  error: string | null;
  fetchUser: () => Promise<void>;
  logout: () => Promise<void>;
  setUser: (user: User | null) => void;
}

const useUserStore = create<UserState>((set) => ({
  user: null,
  isLoggedIn: false,
  isLoading: false,
  error: null,
  fetchUser: async () => {
    set({ isLoading: true, error: null });
    try {
      const response = await apiClient.get<ApiResponse<UserData>>("/user/me");
      if (response.data.success) {
        const { userName, avatarUrl } = response.data.data;
        const userData: User = {
          userName,
          avatarUrl,
        };
        set({ user: userData, isLoggedIn: true, isLoading: false });
      } else {
        set({
          user: null,
          isLoggedIn: false,
          isLoading: false,
          error: response.data.error?.message || "사용자 정보를 불러오는데 실패했습니다.",
        });
      }
    } catch (error) {
      set({
        user: null,
        isLoggedIn: false,
        isLoading: false,
        error: "네트워크 오류가 발생했습니다. 다시 시도해주세요.",
      });
    }
  },
  logout: async () => {
    set({ isLoading: true, error: null });
    try {
      await apiClient.post<ApiResponse<Record<string, never>>>("/user/logout");
      set({ user: null, isLoggedIn: false, isLoading: false });
    } catch (error) {
      // API 에러가 발생하더라도 로그아웃은 처리되어야 합니다.
      // 사용자 경험을 위해 에러 메시지는 상태에 저장합니다.
      const anyError = error as any;
      let errorMessage = "로그아웃 중 오류가 발생했습니다.";
      if (anyError.response && anyError.response.data && anyError.response.data.error) {
        errorMessage = anyError.response.data.error.message;
      }
      set({ user: null, isLoggedIn: false, isLoading: false, error: errorMessage });
    }
  },
  // setUser는 api를 호출하지 않고 사용자 정보를 불러올 때 쓴다
  // ex) 다른 api를 통해 user값이 바뀌었을 때, 사용자 정보 업데이트 할 때
  setUser: (user) => set({ user, isLoggedIn: !!user }),
}));

export default useUserStore;
