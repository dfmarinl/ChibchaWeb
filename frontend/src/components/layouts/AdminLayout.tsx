import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import { Bell, Search, User } from 'lucide-react';
import { USER_ROLES } from '../../constants';
import { useState, useEffect } from 'react';

const AdminLayout: React.FC = () => {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  return (
    <div className='min-h-screen bg-secondary-50 flex'>
      <Sidebar role={USER_ROLES.ADMIN} />

      <div className='flex-1 lg:ml-64 flex flex-col'>
        {/* Top Navbar */}
        <header className='h-16 bg-white border-b border-secondary-200 flex items-center justify-between px-4 lg:px-8 sticky top-0 z-30'>
          <div className='flex items-center gap-4 flex-1 lg:ml-0 ml-12'>
            <div className='relative flex-1 max-w-md'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar...'
                className='w-full pl-10 pr-4 py-2 rounded-lg border border-secondary-300 focus:ring-2 focus:ring-primary-500 focus:border-primary-500 text-sm'
              />
            </div>
          </div>

          <div className='flex items-center gap-4'>
            <button className='relative p-2 rounded-lg hover:bg-secondary-100 text-secondary-600 transition-colors'>
              <Bell className='w-5 h-5' />
              <span className='absolute top-1 right-1 w-2 h-2 bg-error-500 rounded-full'></span>
            </button>

            <div className='flex items-center gap-3 pl-4 border-l border-secondary-200'>
              <div className='text-right hidden sm:block'>
                <p className='text-sm font-medium text-secondary-900'>{user?.name || 'Admin'}</p>
                <p className='text-xs text-secondary-500'>{user?.email || 'admin@chibchaweb.com'}</p>
              </div>
              <div className='w-10 h-10 bg-primary-600 rounded-full flex items-center justify-center'>
                <User className='w-5 h-5 text-white' />
              </div>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className='flex-1 p-4 lg:p-8 overflow-auto'>
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default AdminLayout;
