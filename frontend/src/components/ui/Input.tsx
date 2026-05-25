import { InputHTMLAttributes, forwardRef, ReactNode } from 'react';

export interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helperText?: string;
  leftIcon?: ReactNode;
  rightIcon?: ReactNode;
}

const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      label,
      error,
      helperText,
      leftIcon,
      rightIcon,
      className = '',
      id,
      ...props
    },
    ref
  ) => {
    const inputId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className='w-full'>
        {label && (
          <label
            htmlFor={inputId}
            className='block text-sm font-medium text-secondary-700 mb-1.5'
          >
            {label}
          </label>
        )}
        <div className='relative'>
          {leftIcon && (
            <div className='absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-secondary-500'>
              {leftIcon}
            </div>
          )}
          <input
            ref={ref}
            id={inputId}
            className={`
              w-full rounded-lg border transition-all duration-200
              ${leftIcon ? 'pl-10' : 'pl-4'}
              ${rightIcon ? 'pr-10' : 'pr-4'}
              py-2.5 text-sm
              ${error
                ? 'border-error-500 focus:ring-error-500 focus:border-error-500'
                : 'border-secondary-300 focus:ring-primary-500 focus:border-primary-500'
              }
              focus:outline-none focus:ring-2
              disabled:bg-secondary-50 disabled:text-secondary-500 disabled:cursor-not-allowed
              placeholder:text-secondary-400
              ${className}
            `}
            {...props}
          />
          {rightIcon && (
            <div className='absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none text-secondary-500'>
              {rightIcon}
            </div>
          )}
        </div>
        {error && (
          <p className='mt-1.5 text-sm text-error-600'>{error}</p>
        )}
        {helperText && !error && (
          <p className='mt-1.5 text-sm text-secondary-500'>{helperText}</p>
        )}
      </div>
    );
  }
);

Input.displayName = 'Input';

export default Input;
