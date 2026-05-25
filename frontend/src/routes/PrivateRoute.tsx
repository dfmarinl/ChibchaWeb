import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../hooks';
import { ROUTES, USER_ROLES } from '../constants';

interface PrivateRouteProps {
  allowedRoles?: string[];
}

export const PrivateRoute: React.FC<PrivateRouteProps> = ({ allowedRoles }) => {
  const { isAuthenticated, user, isLoading } = useAuth();

  if (isLoading) {
    return (
      <div className='min-h-screen flex items-center justify-center bg-secondary-50'>
        <div className='animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600'></div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.PUBLIC.LOGIN} replace />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    const redirectPath = getRedirectPath(user.role);
    return <Navigate to={redirectPath} replace />;
  }

  return <Outlet />;
};

const getRedirectPath = (role: string): string => {
  switch (role) {
    case USER_ROLES.ADMIN:
      return ROUTES.ADMIN.DASHBOARD;
    case USER_ROLES.CLIENT:
      return ROUTES.CLIENT.DASHBOARD;
    case USER_ROLES.EMPLOYEE:
      return ROUTES.EMPLOYEE.DASHBOARD;
    default:
      return ROUTES.PUBLIC.LOGIN;
  }
};

export default PrivateRoute;
