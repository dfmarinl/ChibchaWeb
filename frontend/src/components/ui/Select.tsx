import { SelectHTMLAttributes, forwardRef, ReactNode } from 'react';

export interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  helperText?: string;
  leftIcon?: ReactNode;
}

const Select = forwardRef<HTMLSelectElement, SelectProps>(
  (
    {
      label,
      error,
      helperText,
      leftIcon,
      className = '',
      id,
      children,
      ...props
    },
    ref
  ) => {
    const selectId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className='w-full'>
        {label && (
          <label
            htmlFor={selectId}
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
          <select
            ref={ref}
            id={selectId}
            className={`
              w-full rounded-lg border transition-all duration-200
              ${leftIcon ? 'pl-10' : 'pl-4'}
              pr-10 py-2.5 text-sm appearance-none
              ${error
                ? 'border-error-500 focus:ring-error-500 focus:border-error-500'
                : 'border-secondary-300 focus:ring-primary-500 focus:border-primary-500'
              }
              focus:outline-none focus:ring-2
              disabled:bg-secondary-50 disabled:text-secondary-500 disabled:cursor-not-allowed
              bg-white
              ${className}
            `}
            {...props}
          >
            {children}
          </select>
          <div className='absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none'>
            <svg
              className='h-5 w-5 text-secondary-500'
              xmlns='http://www.w3.org/2000/svg'
              viewBox='0 0 20 20'
              fill='currentColor'
              aria-hidden='true'
            >
              <path
                fillRule='evenodd'
                d='M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z'
                clipRule='evenodd'
              />
            </svg>
          </div>
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

Select.displayName = 'Select';

export default Select;
