import { Outlet, Link, useLocation, useNavigate } from 'react-router-dom';
import { Server, Globe, CreditCard, HelpCircle, LayoutDashboard, LogOut, User } from 'lucide-react';
import { ROUTES, APP_NAME, USER_ROLES } from '../../constants';
import { useState, useEffect } from 'react';

const ClientLayout: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const navItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: ROUTES.CLIENT.DASHBOARD },
    { icon: Server, label: 'Planes', path: ROUTES.CLIENT.PLANS },
    { icon: Globe, label: 'Dominios', path: ROUTES.CLIENT.DOMAINS },
    { icon: CreditCard, label: 'Pagos', path: ROUTES.CLIENT.PAYMENTS },
    { icon: HelpCircle, label: 'Soporte', path: ROUTES.CLIENT.SUPPORT },
    { icon: User, label: 'Mi Perfil', path: ROUTES.CLIENT.PROFILE },
  ];

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <div className='min-h-screen bg-secondary-50 flex flex-col'>
      {/* Top Navbar */}
      <header className='h-16 bg-white border-b border-secondary-200 sticky top-0 z-30'>
        <div className='h-full max-w-7xl mx-auto px-4 lg:px-8 flex items-center justify-between'>
          {/* Logo */}
          <Link to={ROUTES.CLIENT.DASHBOARD} className='flex items-center gap-3'>
            <div className='w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-700 rounded-lg flex items-center justify-center'>
              <Server className='w-5 h-5 text-white' />
            </div>
            <span className='text-lg font-bold text-secondary-900'>{APP_NAME}</span>
          </Link>

          {/* Navigation */}
          <nav className='hidden md:flex items-center gap-1'>
            {navItems.map((item) => {
              const isActive = location.pathname === item.path;
              return (
                <Link
                  key={item.path}
                  to={item.path}
                  className={`
                    flex items-center gap-2 px-4 py-2 rounded-lg transition-all duration-200 text-sm font-medium
                    ${isActive
                      ? 'bg-primary-50 text-primary-700'
                      : 'text-secondary-600 hover:bg-secondary-50 hover:text-secondary-900'
                    }
                  `}
                >
                  <item.icon className='w-4 h-4' />
                  {item.label}
                </Link>
              );
            })}
          </nav>

          {/* Right Side */}
          <div className='flex items-center gap-4'>
            <div className='flex items-center gap-3 pl-4 border-l border-secondary-200'>
              <div className='text-right hidden sm:block'>
                <p className='text-sm font-medium text-secondary-900'>{user?.name || 'Cliente'}</p>
                <p className='text-xs text-secondary-500'>{user?.company || 'Empresa'}</p>
              </div>
              <div className='w-10 h-10 bg-success-600 rounded-full flex items-center justify-center'>
                <User className='w-5 h-5 text-white' />
              </div>
            </div>

            <button
              onClick={handleLogout}
              className='p-2 rounded-lg hover:bg-error-50 text-error-600 transition-colors'
              title='Cerrar Sesión'
            >
              <LogOut className='w-5 h-5' />
            </button>
          </div>
        </div>
      </header>

      {/* Mobile Navigation */}
      <nav className='md:hidden bg-white border-b border-secondary-200 px-4 py-2'>
        <div className='flex items-center gap-1 overflow-x-auto'>
          {navItems.map((item) => {
            const isActive = location.pathname === item.path;
            return (
              <Link
                key={item.path}
                to={item.path}
                className={`
                  flex items-center gap-2 px-3 py-2 rounded-lg whitespace-nowrap text-sm font-medium
                  ${isActive
                    ? 'bg-primary-50 text-primary-700'
                    : 'text-secondary-600'
                  }
                `}
              >
                <item.icon className='w-4 h-4' />
                <span className='hidden sm:inline'>{item.label}</span>
              </Link>
            );
          })}
        </div>
      </nav>

      {/* Main Content */}
      <main className='flex-1'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8 py-8'>
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default ClientLayout;
