// src/i18n.js
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

// 번역 파일 import
import translationEN from "./locales/en/translation.json";
import translationKO from "./locales/ko/translation.json";

const resources = {
  en: {
    translation: translationEN,
  },
  ko: {
    translation: translationKO,
  },
};

i18n
  .use(LanguageDetector) // 브라우저 언어 감지
  .use(initReactI18next) // React와 연결
  .init({
    resources,
    fallbackLng: "ko", // 기본 언어
    interpolation: {
      escapeValue: false, // React에선 XSS 걱정 없으므로 false
    },
  });

export default i18n;
