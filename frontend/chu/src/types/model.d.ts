// 서비스 내의 핵심 데이터 모델과 관련된 타입들 정의

// 사용자 데이터 모델
export interface User {
  userName: string;
  avatarUrl: string;
  chu?: Chu; // 대표 츄 정보
}

// 츄 데이터 모델
export interface Chu {
  chuId: number;
  nikname: string;
  level: number;
  exp: number;
  status: string;
  isMain: boolean;
  lastStateUpdatedAt: string;
}
