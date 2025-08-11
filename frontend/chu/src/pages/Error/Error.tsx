import { Link } from "react-router-dom";
import styles from "./Error.module.css";
import FuzzyText from "../../components/ReactBits/FuzzyText";

const Error = () => {
  return (
    <div className={styles.container}>
      <FuzzyText>404</FuzzyText>
      <FuzzyText>error</FuzzyText>
      <Link to="/" className={styles.link}>
        홈으로 돌아가기
      </Link>
    </div>
  );
};

export default Error;
