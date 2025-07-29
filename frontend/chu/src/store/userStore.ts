import { create } from "zustand";
import apiClient from "../api";

// API 응답에 따른 타입 정의
interface UserData {
  userName: string;
  avatarUrl: string;
}

interface ApiResponse {
  success: boolean;
  data: UserData | Record<string, never>; // 성공 시 UserData, 실패 시 빈 객체
  error?: {
    code: string;
    message: string;
  };
  timestamp: string;
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
  fetchUser: () => Promise<void>;
  logout: () => Promise<void>;
  setUser: (user: User | null) => void;
  createPet: (petData: Omit<Pet, "id" | "createdAt">) => void;
  updatePet: (petData: Partial<Pet>) => void;
}

const useUserStore = create<UserState>((set) => ({
  user: null,
  fetchUser: async () => {
    try {
      const response = await apiClient.get<ApiResponse>("/user/me");
      if (response.data.success && "userName" in response.data.data) {
        const { userName, avatarUrl } = response.data.data;
        const userData: User = {
          id: userName, // id 필드를 userName으로 설정
          username: userName,
          avatarUrl,
        };
        set({ user: userData });
        console.log("Fetched user data:", userData);
      } else {
        set({ user: null });
        console.log("Failed to fetch user data or user data is empty.");
      }
    } catch (error) {
      console.error("Failed to fetch user:", error);
      set({ user: null });
    }
  },
  logout: async () => {
    try {
      // 로그아웃 API 명세가 따로 없다면 이 부분은 백엔드에 확인이 필요합니다.
      // 우선 일반적인 logout 경로로 요청합니다.
      await apiClient.post("/user/logout");
      set({ user: null });
    } catch (error) {
      console.error("Logout failed:", error);
      // 로그아웃 실패 시에도 클라이언트 상태는 초기화
      set({ user: null });
    }
  },
  setUser: (user) => set({ user }),
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
