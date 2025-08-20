import React, { useState, useEffect, useRef, PropsWithChildren } from "react";
import styles from "./FullpageScroll.module.css";

function Section({ children, className = "" }: PropsWithChildren<{ className?: string }>) {
  return <div className={`${styles.section} ${className}`}>{children}</div>;
}

export default function FullpageScroll({ children }: PropsWithChildren) {
  const [current, setCurrent] = useState(0);
  const containerRef = useRef<HTMLDivElement | null>(null);
  const isScrolling = useRef(false);
  const timeoutRef = useRef<number | null>(null);
  const touchStartY = useRef(0);
  const touchEndY = useRef(0);

  const slides = React.Children.toArray(children).filter(React.isValidElement);
  const count = slides.length;

  const go = (dir: "up" | "down") => {
    if (isScrolling.current) return;
    isScrolling.current = true;
    setCurrent((prev) => {
      if (dir === "down") return Math.min(prev + 1, count - 1);
      return Math.max(prev - 1, 0);
    });
    if (timeoutRef.current) window.clearTimeout(timeoutRef.current);
    timeoutRef.current = window.setTimeout(() => (isScrolling.current = false), 800);
  };

  const onWheel = (e: WheelEvent) => {
    e.preventDefault();
    go(e.deltaY > 0 ? "down" : "up");
  };

  const onTouchStart = (e: TouchEvent) => (touchStartY.current = e.touches[0].clientY);
  const onTouchMove = (e: TouchEvent) => {
    e.preventDefault();
    touchEndY.current = e.touches[0].clientY;
  };
  const onTouchEnd = () => {
    const dy = touchStartY.current - touchEndY.current;
    if (Math.abs(dy) > 50) go(dy > 0 ? "down" : "up");
  };

  useEffect(() => {
    const el = containerRef.current;
    if (!el) return;
    el.addEventListener("wheel", onWheel, { passive: false });
    el.addEventListener("touchstart", onTouchStart, { passive: true });
    el.addEventListener("touchmove", onTouchMove, { passive: false });
    el.addEventListener("touchend", onTouchEnd, { passive: true });
    return () => {
      el.removeEventListener("wheel", onWheel);
      el.removeEventListener("touchstart", onTouchStart);
      el.removeEventListener("touchmove", onTouchMove);
      el.removeEventListener("touchend", onTouchEnd);
      if (timeoutRef.current) window.clearTimeout(timeoutRef.current);
    };
  }, [count]);

  return (
    <div ref={containerRef} className={styles.container}>
      <div className={styles.track} style={{ transform: `translateY(-${current * 100}%)` }}>
        {slides}
      </div>

      {/* 인디케이터 */}
      {count > 1 && (
        <div className={styles.dots}>
          {Array.from({ length: count }).map((_, i) => (
            <button
              key={i}
              className={`${styles.dot} ${current === i ? styles.active : ""}`}
              onClick={() => setCurrent(i)}
              aria-label={`Go to section ${i + 1}`}
            />
          ))}
        </div>
      )}
    </div>
  );
}
FullpageScroll.Section = Section;
