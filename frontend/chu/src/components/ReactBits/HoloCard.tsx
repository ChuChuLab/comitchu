import React, { useRef, useCallback } from "react";
import styles from "./HoloCard.module.css";

type HoloCardProps = {
  imageSrc: string; // 예: "/images/pika.webp"
  width?: number; // 기본 220
  height?: number;
  perspective?: number; // 기본 350(px)
  className?: string; // 외부에서 추가 스타일이 필요할 때
  alt?: string; // 접근성용 설명
};

const HoloCard: React.FC<HoloCardProps> = ({
  imageSrc,
  width = 300,
  height = 200,
  perspective = 350,
  className,
  alt = "",
}) => {
  const containerRef = useRef<HTMLDivElement | null>(null);
  const overlayRef = useRef<HTMLDivElement | null>(null);
  const cardRef = useRef<HTMLDivElement | null>(null);

  const handleMouseMove = useCallback(
    (e: React.MouseEvent<HTMLDivElement>) => {
      if (!containerRef.current || !overlayRef.current) return;

      // offsetX/Y는 React 합성 이벤트에 직접 없음 -> 현재 타겟 기준 좌표 계산
      const rect = (e.currentTarget as HTMLDivElement).getBoundingClientRect();
      const x = e.clientX - rect.left; // 0 ~ width
      const y = e.clientY - rect.top; // 0 ~ height

      // 원본 수식 유지
      const rotateY = (-2 / 20) * x + 20;
      const rotateX = (4 / 200) * y - 10;

      overlayRef.current.style.backgroundPosition = `${x / 5 + y / 5}%`;
      overlayRef.current.style.filter = `opacity(${x / 200}) brightness(1.2)`;

      containerRef.current.style.transform = `perspective(${perspective}px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
    },
    [perspective]
  );

  const handleMouseLeave = useCallback(() => {
    if (!containerRef.current || !overlayRef.current) return;

    overlayRef.current.style.filter = "opacity(0)";
    containerRef.current.style.transform = `perspective(${perspective}px) rotateY(0deg) rotateX(0deg)`;
  }, [perspective]);

  return (
    <div
      ref={containerRef}
      className={`${styles.container} ${className ?? ""}`}
      style={{ width, height }}
      onMouseMove={handleMouseMove}
      onMouseLeave={handleMouseLeave}
      aria-label={alt}
      role="img"
    >
      <div ref={overlayRef} className={styles.overlay} />
      <div
        className={styles.cardShadow}
        style={{
          backgroundImage: `url(${imageSrc})`,
        }}
        aria-hidden
      />
      <div
        ref={cardRef}
        className={styles.card}
        style={{
          backgroundImage: `url(${imageSrc})`,
          width,
          height,
        }}
        aria-hidden
      />
    </div>
  );
};

export default HoloCard;
