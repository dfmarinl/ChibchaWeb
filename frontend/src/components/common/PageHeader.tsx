interface PageHeaderProps {
  title: string;
  description?: string;
  action?: React.ReactNode;
  breadcrumbs?: { label: string; href?: string }[];
}

const PageHeader: React.FC<PageHeaderProps> = ({
  title,
  description,
  action,
  breadcrumbs,
}) => {
  return (
    <div className='mb-8'>
      {breadcrumbs && breadcrumbs.length > 0 && (
        <nav className='mb-3 flex' aria-label='Breadcrumb'>
          <ol className='flex items-center space-x-2'>
            {breadcrumbs.map((crumb, index) => (
              <li key={index} className='flex items-center'>
                {index > 0 && (
                  <svg
                    className='h-5 w-5 text-secondary-400 mx-2'
                    fill='currentColor'
                    viewBox='0 0 20 20'
                  >
                    <path
                      fillRule='evenodd'
                      d='M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z'
                      clipRule='evenodd'
                    />
                  </svg>
                )}
                {crumb.href ? (
                  <a
                    href={crumb.href}
                    className='text-sm font-medium text-secondary-500 hover:text-secondary-700'
                  >
                    {crumb.label}
                  </a>
                ) : (
                  <span className='text-sm font-medium text-secondary-900'>
                    {crumb.label}
                  </span>
                )}
              </li>
            ))}
          </ol>
        </nav>
      )}
      <div className='flex items-center justify-between'>
        <div>
          <h1 className='text-2xl font-bold text-secondary-900'>{title}</h1>
          {description && (
            <p className='mt-1 text-sm text-secondary-600'>{description}</p>
          )}
        </div>
        {action && <div>{action}</div>}
      </div>
    </div>
  );
};

export default PageHeader;
