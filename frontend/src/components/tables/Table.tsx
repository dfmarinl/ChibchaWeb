import { HTMLAttributes } from 'react';

interface TableProps extends HTMLAttributes<HTMLTableElement> {
  children: React.ReactNode;
}

export const Table: React.FC<TableProps> = ({ children, className = '', ...props }) => {
  return (
    <div className='overflow-x-auto'>
      <table className={`min-w-full divide-y divide-secondary-200 ${className}`} {...props}>
        {children}
      </table>
    </div>
  );
};

interface TheadProps extends HTMLAttributes<HTMLTableSectionElement> {
  children: React.ReactNode;
}

export const Thead: React.FC<TheadProps> = ({ children, className = '', ...props }) => {
  return (
    <thead className={`bg-secondary-50 ${className}`} {...props}>
      {children}
    </thead>
  );
};

interface TbodyProps extends HTMLAttributes<HTMLTableSectionElement> {
  children: React.ReactNode;
}

export const Tbody: React.FC<TbodyProps> = ({ children, className = '', ...props }) => {
  return (
    <tbody className={`bg-white divide-y divide-secondary-200 ${className}`} {...props}>
      {children}
    </tbody>
  );
};

interface ThProps extends HTMLAttributes<HTMLTableCellElement> {
  children?: React.ReactNode;
}

export const Th: React.FC<ThProps> = ({ children, className = '', ...props }) => {
  return (
    <th
      scope='col'
      className={`px-6 py-3 text-left text-xs font-semibold text-secondary-600 uppercase tracking-wider ${className}`}
      {...props}
    >
      {children}
    </th>
  );
};

interface TdProps extends HTMLAttributes<HTMLTableCellElement> {
  children?: React.ReactNode;
}

export const Td: React.FC<TdProps> = ({ children, className = '', ...props }) => {
  return (
    <td className={`px-6 py-4 whitespace-nowrap text-sm text-secondary-900 ${className}`} {...props}>
      {children}
    </td>
  );
};

interface TrProps extends HTMLAttributes<HTMLTableRowElement> {
  children: React.ReactNode;
  onClick?: () => void;
}

export const Tr: React.FC<TrProps> = ({ children, className = '', onClick, ...props }) => {
  return (
    <tr
      className={`${onClick ? 'cursor-pointer hover:bg-secondary-50' : ''} transition-colors ${className}`}
      onClick={onClick}
      {...props}
    >
      {children}
    </tr>
  );
};

interface EmptyStateProps {
  message?: string;
  description?: string;
  action?: React.ReactNode;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  message = 'No data available',
  description = 'There are no items to display.',
  action,
}) => {
  return (
    <div className='text-center py-12'>
      <svg
        className='mx-auto h-12 w-12 text-secondary-400'
        fill='none'
        viewBox='0 0 24 24'
        stroke='currentColor'
        aria-hidden='true'
      >
        <path
          vectorEffect='non-scaling-stroke'
          strokeLinecap='round'
          strokeLinejoin='round'
          strokeWidth={1.5}
          d='M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4'
        />
      </svg>
      <h3 className='mt-2 text-sm font-medium text-secondary-900'>{message}</h3>
      <p className='mt-1 text-sm text-secondary-500'>{description}</p>
      {action && <div className='mt-6'>{action}</div>}
    </div>
  );
};
