import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  LayoutDashboard,
  Users,
  UserCircle,
  Package,
  Globe,
  CreditCard,
  Settings,
  LogOut,
  ChevronLeft,
  ChevronRight,
  Menu,
  X,
  Server,
} from 'lucide-react';
import { ROUTES, APP_NAME, USER_ROLES } from '../../constants';

interface SidebarProps {
  role: typeof USER_ROLES[keyof typeof USER_ROLES];
}

const Sidebar: React.FC<SidebarProps> = ({ role }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();

  const getNavItems = () => {
    if (role === USER_ROLES.ADMIN) {
      return [
        { icon: LayoutDashboard, label: 'Dashboard', path: ROUTES.ADMIN.DASHBOARD },
        { icon: Users, label: 'Clientes', path: ROUTES.ADMIN.CLIENTS },
        { icon: UserCircle, label: 'Empleados', path: ROUTES.ADMIN.EMPLOYEES },
        { icon: Package, label: 'Planes', path: ROUTES.ADMIN.PLANS },
        { icon: Globe, label: 'Dominios', path: ROUTES.ADMIN.DOMAINS },
        { icon: CreditCard, label: 'Pagos', path: ROUTES.ADMIN.PAYMENTS },
      ];
    } else if (role === USER_ROLES.EMPLOYEE) {
      return [
        { icon: LayoutDashboard, label: 'Dashboard', path: ROUTES.EMPLOYEE.DASHBOARD },
        { icon: Users, label: 'Tickets', path: ROUTES.EMPLOYEE.TICKETS },
      ];
    }
    return [];
  };

  const navItems = getNavItems();

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    navigate('/login');
  };

  return (
    <>
      {/* Mobile Menu Button */}
      <button
        onClick={() => setIsMobileOpen(!isMobileOpen)}
        className='lg:hidden fixed top-4 left-4 z-50 p-2 rounded-lg bg-primary-600 text-white shadow-lg'
      >
        {isMobileOpen ? <X className='w-5 h-5' /> : <Menu className='w-5 h-5' />}
      </button>

      {/* Mobile Overlay */}
      {isMobileOpen && (
        <div
          className='lg:hidden fixed inset-0 bg-black bg-opacity-50 z-40'
          onClick={() => setIsMobileOpen(false)}
        />
      )}

      {/* Sidebar */}
      <aside
        className={`
          fixed top-0 left-0 h-full bg-white border-r border-secondary-200 z-40
          transition-all duration-300 ease-in-out
          ${isCollapsed ? 'w-20' : 'w-64'}
          ${isMobileOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
        `}
      >
        {/* Logo */}
        <div className='h-16 flex items-center justify-between px-4 border-b border-secondary-200'>
          <Link to='/' className='flex items-center gap-3'>
            <div className='w-10 h-10 bg-gradient-to-br from-primary-600 to-primary-700 rounded-lg flex items-center justify-center'>
              <Server className='w-5 h-5 text-white' />
            </div>
            {!isCollapsed && (
              <span className='text-lg font-bold text-secondary-900'>{APP_NAME}</span>
            )}
          </Link>
          <button
            onClick={() => setIsCollapsed(!isCollapsed)}
            className='hidden lg:flex p-1.5 rounded-md hover:bg-secondary-100 text-secondary-600'
          >
            {isCollapsed ? (
              <ChevronRight className='w-4 h-4' />
            ) : (
              <ChevronLeft className='w-4 h-4' />
            )}
          </button>
        </div>

        {/* Navigation */}
        <nav className='flex-1 py-6 overflow-y-auto'>
          <ul className='space-y-1 px-3'>
            {navItems.map((item) => {
              const isActive = location.pathname === item.path;
              return (
                <li key={item.path}>
                  <Link
                    to={item.path}
                    onClick={() => setIsMobileOpen(false)}
                    className={`
                      flex items-center gap-3 px-3 py-2.5 rounded-lg transition-all duration-200
                      ${isActive
                        ? 'bg-primary-50 text-primary-700 font-medium'
                        : 'text-secondary-600 hover:bg-secondary-50 hover:text-secondary-900'
                      }
                    `}
                  >
                    <item.icon className='w-5 h-5 flex-shrink-0' />
                    {!isCollapsed && <span>{item.label}</span>}
                  </Link>
                </li>
              );
            })}
          </ul>
        </nav>

        {/* Footer */}
        <div className='border-t border-secondary-200 p-4'>
          <div className='space-y-1'>
            <Link
              to='#'
              className='flex items-center gap-3 px-3 py-2 rounded-lg text-secondary-600 hover:bg-secondary-50 hover:text-secondary-900 transition-all'
            >
              <Settings className='w-5 h-5 flex-shrink-0' />
              {!isCollapsed && <span className='text-sm'>Configuración</span>}
            </Link>
            <button
              onClick={handleLogout}
              className='w-full flex items-center gap-3 px-3 py-2 rounded-lg text-error-600 hover:bg-error-50 transition-all'
            >
              <LogOut className='w-5 h-5 flex-shrink-0' />
              {!isCollapsed && <span className='text-sm'>Cerrar Sesión</span>}
            </button>
          </div>
        </div>
      </aside>
    </>
  );
};

export default Sidebar;
