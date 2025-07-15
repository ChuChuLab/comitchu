import { useUser } from "../../contexts/UserContext";
import { Navigate } from "react-router-dom";
import BadgePreview from "../../components/BadgePreview/BadgePreview";
import styles from "./Landing.module.css";

const Landing = () => {
  const { user, login } = useUser();

  if (user) {
    return <Navigate to="/dashboard" replace />;
  }

  const handleGitHubLogin = () => {
    // Simulate GitHub login
    const mockUser = {
      id: "user123",
      username: "developer",
      avatarUrl: "https://github.com/github.png",
    };
    login(mockUser);
  };

  return (
    <div className={styles.landing}>
      <section className={styles.hero}>
        <div className={styles.heroContent}>
          <h1 className={styles.title}>
            Meet Your Git Companion
            <span className={styles.titleAccent}>CommitChu</span>
          </h1>
          <p className={styles.subtitle}>
            A virtual pet that grows with your coding journey. The more you commit, the happier and stronger your
            CommitChu becomes!
          </p>
          <button onClick={handleGitHubLogin} className={styles.loginBtn}>
            <span className={styles.githubIcon}>🐙</span>
            Login with GitHub
          </button>
        </div>
        <div className={styles.heroVisual}>
          <div className={styles.petPreview}>
            <div className={styles.petCharacter}>🐱</div>
            <div className={styles.petStats}>
              <div className={styles.statBar}>
                <span>Level 5</span>
                <div className={styles.xpBar}>
                  <div className={styles.xpFill} style={{ width: "70%" }}></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section className={styles.features}>
        <h2 className={styles.sectionTitle}>How It Works</h2>
        <div className={styles.featureGrid}>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🎨</div>
            <h3>Choose Your Pet</h3>
            <p>Select from various adorable characters to be your coding companion</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>📈</div>
            <h3>Grow Together</h3>
            <p>Your pet gains XP and levels up as you make commits to your repositories</p>
          </div>
          <div className={styles.featureCard}>
            <div className={styles.featureIcon}>🏆</div>
            <h3>Show Off</h3>
            <p>Display your pet's progress with beautiful SVG badges in your README</p>
          </div>
        </div>
      </section>

      <section className={styles.preview}>
        <h2 className={styles.sectionTitle}>Live Badge Preview</h2>
        <p className={styles.previewDescription}>Here's what your CommitChu badge will look like in your repository:</p>
        <BadgePreview petName="Whiskers" level={5} character="🐱" mood="happy" />
      </section>
    </div>
  );
};

export default Landing;
