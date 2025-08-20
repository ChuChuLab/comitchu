import styles from './Modal.module.css';

interface ModalProps {
  isOpen: boolean;
  isExiting: boolean;
  message: string;
}

const Modal = ({ isOpen, isExiting, message }: ModalProps) => {
  if (!isOpen) {
    return null;
  }

  return (
    <div className={`${styles.modal} ${isExiting ? 'animate-slide-down' : 'animate-slide-up'}`}>
      {message}
    </div>
  );
};

export default Modal;
