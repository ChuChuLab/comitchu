import { create } from "zustand";
import type { Chu, ChuSkin } from "../types/model";
import { fetchMainChuAPI, fetchAllChuSkinsAPI } from "../api/chu";

// 츄 스토어의 상태와 액션에 대한 인터페이스
interface ChuState {
  mainChu: Chu | null;
  chuSkins: ChuSkin[];
  isLoading: boolean;
  error: string | null;
  fetchMainChu: () => Promise<void>;
  fetchAllChuSkins: () => Promise<void>;
}

const useChuStore = create<ChuState>((set) => ({
  mainChu: null,
  chuSkins: [],
  isLoading: false,
  error: null,
  fetchMainChu: async () => {
    set({ isLoading: true, error: null });
    try {
      const chuData = await fetchMainChuAPI();
      set({ mainChu: chuData, isLoading: false });
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.",
      });
    }
  },
  fetchAllChuSkins: async () => {
    set({ isLoading: true, error: null });
    try {
      const skinsData = await fetchAllChuSkinsAPI();
      set({ chuSkins: skinsData, isLoading: false });
    } catch (error) {
      set({
        isLoading: false,
        error: error instanceof Error ? error.message : "알 수 없는 오류가 발생했습니다.",
      });
    }
  },
}));

export default useChuStore;
