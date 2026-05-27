import { ReactNode, useEffect } from 'react';
import { X } from 'lucide-react';

interface ModalProps {
  open: boolean;
  onClose: () => void;
  title: string;
  children: ReactNode;
  footer?: ReactNode;
}

const Modal: React.FC<ModalProps> = ({ open, onClose, title, children, footer }) => {
  useEffect(() => {
    if (open) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
    return () => {
      document.body.style.overflow = '';
    };
  }, [open]);

  if (!open) return null;

  return (
    <div className='fixed inset-0 z-50 flex items-center justify-center'>
      <div
        className='fixed inset-0 bg-black/50 transition-opacity'
        onClick={onClose}
      />
      <div className='relative bg-white rounded-xl shadow-2xl w-full max-w-lg mx-4 max-h-[90vh] overflow-y-auto'>
        <div className='flex items-center justify-between px-6 py-4 border-b border-secondary-200'>
          <h2 className='text-lg font-semibold text-secondary-900'>{title}</h2>
          <button
            onClick={onClose}
            className='p-1 rounded-lg text-secondary-400 hover:text-secondary-600 hover:bg-secondary-100 transition-colors'
          >
            <X className='w-5 h-5' />
          </button>
        </div>
        <div className='px-6 py-4'>{children}</div>
        {footer && (
          <div className='flex items-center justify-end gap-3 px-6 py-4 border-t border-secondary-200 bg-secondary-50 rounded-b-xl'>
            {footer}
          </div>
        )}
      </div>
    </div>
  );
};

export default Modal;
