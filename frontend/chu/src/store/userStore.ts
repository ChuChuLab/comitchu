import { create } from "zustand";
import type { User } from "../types/model";
import { fetchUserAPI, logoutAPI } from "../api/user";

// 각 api를 호출하면서 만들 메서드 이름을 여기에 정리하기
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
      const userData = await fetchUserAPI();
      set({ user: userData, isLoggedIn: true, isLoading: false });
    } catch (error) {
      set({
        user: null,
        isLoggedIn: false,
        isLoading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.",
      });
    }
  },
  logout: async () => {
    set({ isLoading: true, error: null });
    try {
      await logoutAPI();
      set({ user: null, isLoggedIn: false, isLoading: false });
    } catch (error) {
      // API 에러가 발생하더라도 로그아웃은 처리되어야 합니다.
      // 사용자 경험을 위해 에러 메시지는 상태에 저장합니다.
      set({
        user: null,
        isLoggedIn: false,
        isLoading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.",
      });
    }
  },
  // setUser는 api를 호출하지 않고 사용자 정보를 불러올 때 쓴다
  // ex) 다른 api를 통해 user값이 바뀌었을 때, 사용자 정보 업데이트 할 때
  setUser: (user) => set({ user, isLoggedIn: !!user }),
}));

export default useUserStore;
