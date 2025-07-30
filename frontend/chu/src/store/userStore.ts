import { create } from "zustand";
import apiClient from "../api";

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

// 프론트엔드에서 사용할 User 타입
export interface Pet {
  id: string;
  name: string;
  character: string;
  level: number;
  xp: number;
  mood: "happy" | "neutral" | "sad";
  createdAt: string;
}

export interface User {
  id: string; // userName을 id로 사용
  username: string;
  avatarUrl: string;
  pet?: Pet;
}

interface UserState {
  user: User | null;
  isLoggedIn: boolean;
  fetchUser: () => Promise<void>;
  logout: () => Promise<void>;
  setUser: (user: User | null) => void;
  createPet: (petData: Omit<Pet, "id" | "createdAt">) => void;
  updatePet: (petData: Partial<Pet>) => void;
}

const useUserStore = create<UserState>((set) => ({
  user: null,
  isLoggedIn: false,
  fetchUser: async () => {
    try {
      const response = await apiClient.get<ApiResponse<UserData>>("/user/me");
      if (response.data.success) {
        const { userName, avatarUrl } = response.data.data;
        const userData: User = {
          id: userName, // id 필드를 userName으로 설정
          username: userName,
          avatarUrl,
        };
        set({ user: userData, isLoggedIn: true });
      } else {
        set({ user: null, isLoggedIn: false });
        console.error("API request failed:", response.data.error?.message);
      }
    } catch (error) {
      console.error("Failed to fetch user:", error);
      set({ user: null, isLoggedIn: false });
    }
  },
  logout: async () => {
    try {
      await apiClient.post<ApiResponse<Record<string, never>>>("/user/logout");
      set({ user: null, isLoggedIn: false });
    } catch (error) {
      // API 명세에 따라 에러가 발생해도 로그아웃 처리
      const anyError = error as any;
      if (anyError.response && anyError.response.data && !anyError.response.data.success) {
        const errorCode = anyError.response.data.error?.code;
        if (errorCode === "UNAUTHORIZED" || errorCode === "FORBIDDEN") {
          set({ user: null, isLoggedIn: false });
        } else {
          // 그 외 서버 에러(INTERNAL_ERROR) 등은 콘솔에만 기록합니다.
          console.error(`Logout API Error: ${errorCode}`, anyError.response.data.error?.message);
        }
      } else {
        // 네트워크 에러 등 응답이 없는 경우에도 로그아웃 처리
        console.error("Logout failed due to network or unexpected error:", error);
        set({ user: null, isLoggedIn: false });
      }
    }
  },
  setUser: (user) => set({ user, isLoggedIn: !!user }),
  createPet: (petData) =>
    set((state) => {
      if (!state.user) return state;
      const newPet: Pet = {
        ...petData,
        id: Date.now().toString(),
        createdAt: new Date().toISOString(),
      };
      return { user: { ...state.user, pet: newPet } };
    }),
  updatePet: (petData) =>
    set((state) => {
      if (!state.user || !state.user.pet) return state;
      const updatedPet = { ...state.user.pet, ...petData };
      return { user: { ...state.user, pet: updatedPet } };
    }),
}));

export default useUserStore;
