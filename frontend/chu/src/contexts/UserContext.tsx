import type React from "react";
import { createContext, useContext, useState, useEffect, type ReactNode } from "react";

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
  id: string;
  username: string;
  avatarUrl: string;
  pet?: Pet;
}

interface UserContextType {
  user: User | null;
  login: (userData: Omit<User, "pet">) => void;
  logout: () => void;
  createPet: (petData: Omit<Pet, "id" | "createdAt">) => void;
  updatePet: (petData: Partial<Pet>) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const useUser = () => {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};

interface UserProviderProps {
  children: ReactNode;
}

export const UserProvider: React.FC<UserProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("commitchu-user");
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
  }, []);

  const login = (userData: Omit<User, "pet">) => {
    const newUser: User = { ...userData };
    setUser(newUser);
    localStorage.setItem("commitchu-user", JSON.stringify(newUser));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem("commitchu-user");
  };

  const createPet = (petData: Omit<Pet, "id" | "createdAt">) => {
    if (!user) return;

    const newPet: Pet = {
      ...petData,
      id: Date.now().toString(),
      createdAt: new Date().toISOString(),
    };

    const updatedUser = { ...user, pet: newPet };
    setUser(updatedUser);
    localStorage.setItem("commitchu-user", JSON.stringify(updatedUser));
  };

  const updatePet = (petData: Partial<Pet>) => {
    if (!user || !user.pet) return;

    const updatedPet = { ...user.pet, ...petData };
    const updatedUser = { ...user, pet: updatedPet };
    setUser(updatedUser);
    localStorage.setItem("commitchu-user", JSON.stringify(updatedUser));
  };

  return <UserContext.Provider value={{ user, login, logout, createPet, updatePet }}>{children}</UserContext.Provider>;
};
