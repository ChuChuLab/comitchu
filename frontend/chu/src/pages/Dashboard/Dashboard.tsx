import { useState, useEffect, Fragment, useRef } from "react";
import useUserStore from "../../store/userStore";
import useChuStore from "../../store/chuStore";
import { fetchChuSvgAPI, updateChuNicknameAPI } from "../../api/chu";
import styles from "./Dashboard.module.css";
import { useTranslation } from "react-i18next";
import Modal from "../../components/common/Modal";

const Dashboard = () => {
  const { t } = useTranslation();
  const { mainChu, isLoading, error, fetchMainChu } = useChuStore();

  // State for copy modal
  const [showCopyModal, setShowCopyModal] = useState(false);
  const [isCopyModalExiting, setIsCopyModalExiting] = useState(false);
  const copyModalTimeoutRef = useRef<number | null>(null);

  // State for nickname editing
  const [isEditingNickname, setIsEditingNickname] = useState(false);
  const [newNickname, setNewNickname] = useState("");

  // State for nickname result modal
  const [showNicknameModal, setShowNicknameModal] = useState(false);
  const [isNicknameModalExiting, setIsNicknameModalExiting] = useState(false);
  const nicknameModalTimeoutRef = useRef<number | null>(null);
  const [nicknameMessage, setNicknameMessage] = useState<string>("");

  // 로그인한 사용자 정보
  const user = useUserStore((state) => state.user);
  // svg 관련 상태 정보
  const [svgContent, setSvgContent] = useState<string | null>(null);
  const [svgLoading, setSvgLoading] = useState(true);
  const [svgError, setSvgError] = useState<string | null>(null);

  useEffect(() => {
    fetchMainChu();
  }, [fetchMainChu]);

  useEffect(() => {
    if (mainChu) {
      setNewNickname(mainChu.nickname);
    }
  }, [mainChu]);

  useEffect(() => {
    const getChuSvg = async () => {
      if (user && user.userName) {
        try {
          setSvgLoading(true);
          const svgData = await fetchChuSvgAPI(user.userName);
          setSvgContent(svgData);
          setSvgError(null);
        } catch (err) {
          setSvgError(err instanceof Error ? err.message : t("dashboard.fetchError"));
        } finally {
          setSvgLoading(false);
        }
      }
    };

    getChuSvg();
  }, [user, t]);

  useEffect(() => {
    // Cleanup timeouts on component unmount
    return () => {
      if (copyModalTimeoutRef.current) {
        window.clearTimeout(copyModalTimeoutRef.current);
      }
      if (nicknameModalTimeoutRef.current) {
        window.clearTimeout(nicknameModalTimeoutRef.current);
      }
    };
  }, []);

  const handleCopyClick = () => {
    if (user && user.userName) {
      if (copyModalTimeoutRef.current) {
        window.clearTimeout(copyModalTimeoutRef.current);
      }

      const textToCopy = `<a href="https://www.comitchu.shop" target="_blank"><img src="https://www.comitchu.shop/api/chu/${user.userName}" alt="커밋츄" width="300" height="200" /></a>`;
      navigator.clipboard.writeText(textToCopy).then(() => {
        setShowCopyModal(true);
        setIsCopyModalExiting(false);

        copyModalTimeoutRef.current = window.setTimeout(() => {
          setIsCopyModalExiting(true);

          copyModalTimeoutRef.current = window.setTimeout(() => {
            setShowCopyModal(false);
          }, 800);
        }, 2200);
      });
    }
  };

  const handleEditNicknameClick = () => {
    if (isEditingNickname) {
      setIsEditingNickname(false);
      setNewNickname(mainChu?.nickname || "");
    } else {
      setIsEditingNickname(true);
    }
  };

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewNickname(e.target.value);
  };

  const showResultModal = (message: string) => {
    if (nicknameModalTimeoutRef.current) {
      window.clearTimeout(nicknameModalTimeoutRef.current);
    }

    setNicknameMessage(message);
    setShowNicknameModal(true);
    setIsNicknameModalExiting(false);

    nicknameModalTimeoutRef.current = window.setTimeout(() => {
      setIsNicknameModalExiting(true);
      nicknameModalTimeoutRef.current = window.setTimeout(() => {
        setShowNicknameModal(false);
      }, 800);
    }, 2200);
  };

  const handleSaveNickname = async () => {
    if (newNickname.trim() === "" || newNickname.trim() === mainChu?.nickname) {
      setIsEditingNickname(false);
      return;
    }

    try {
      await updateChuNicknameAPI(newNickname);
      showResultModal(t("dashboard.nicknameUpdateSuccess"));
      fetchMainChu();
    } catch (error) {
      const message = error instanceof Error ? error.message : "An unknown error occurred";
      showResultModal(message);
    } finally {
      setIsEditingNickname(false);
    }
  };

  if (isLoading || svgLoading) {
    return <p>{t("dashboard.loading")}</p>;
  }

  if (error || svgError) {
    return <p>{t("dashboard.error", { error: error || svgError })}</p>;
  }

  return (
    <Fragment>
      <div className={styles.dashboard}>
        <div className={styles.contentWrapper}>
          {svgContent && (
            <div className={styles.svgImageContainer}>
              {/* <div className={styles.svgImageBackground} dangerouslySetInnerHTML={{ __html: svgContent }} /> */}
              <div className={styles.svgImage} dangerouslySetInnerHTML={{ __html: svgContent }} />
            </div>
          )}
          <div className={styles.mainChuSection}>
            {mainChu ? (
              <div>
                {isEditingNickname ? (
                  <div className={styles.editNicknameWrapper}>
                    <input
                      type="text"
                      value={newNickname}
                      onChange={handleNicknameChange}
                      onKeyDown={(e) => e.key === "Enter" && handleSaveNickname()}
                      className={styles.nicknameInput}
                      maxLength={6}
                      autoFocus
                    />
                    <button onClick={handleSaveNickname} className={styles.saveButton}>
                      <svg viewBox="0 0 22 22" width="32px" height="32px">
                        <path
                          fill="currentColor"
                          d="M4 11H6V12H7V13H8V14H10V13H11V12H12V11H13V10H14V9H15V8H16V7H17V6H19V8H18V9H17V10H16V11H15V12H14V13H13V14H12V15H11V16H10V17H8V16H7V15H6V14H5V13H4V11Z"
                        ></path>
                      </svg>
                    </button>
                  </div>
                ) : (
                  <div className={styles.nicknameWrapper}>
                    <h2>{mainChu.nickname}</h2>
                  </div>
                )}
                <hr />
                <p>
                  {t("dashboard.level")}: {mainChu.level}
                </p>
                <p>
                  {t("dashboard.exp")}: {mainChu.exp}
                </p>
                <p>
                  {t("dashboard.status")}: {mainChu.status}
                </p>
                <p>
                  {t("dashboard.language")}: {mainChu.lang}
                </p>
                <div className={styles.buttonContainer}>
                  <button onClick={handleEditNicknameClick} className={styles.editButton}>
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 24 24"
                      fill="currentColor"
                      width="32px"
                      height="32px"
                    >
                      <path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z" />
                    </svg>
                  </button>
                  <button onClick={handleCopyClick} className={styles.copyButton}>
                    <svg viewBox="0 0 22 22" width="32px" height="32px">
                      <path
                        fill="currentColor"
                        d="M4 2H18V3H19V4H20V18H19V19H18V20H4V19H3V18H2V4H3V3H4V2M17 5V4H5V5H4V17H5V18H17V17H18V5H17M6 8H16V10H6V8M6 12H13V14H6V12Z"
                      ></path>
                    </svg>
                  </button>
                </div>
              </div>
            ) : (
              <p>{t("dashboard.noChu")}</p>
            )}
          </div>
        </div>
      </div>
      <Modal isOpen={showCopyModal} isExiting={isCopyModalExiting} message={t("dashboard.copiedToClipboard")} />
      <Modal isOpen={showNicknameModal} isExiting={isNicknameModalExiting} message={nicknameMessage} />
    </Fragment>
  );
};

export default Dashboard;
