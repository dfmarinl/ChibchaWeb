import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User } from '../api/users';
import { ROUTES, USER_ROLES, UserRole } from '../constants';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string, role: UserRole) => Promise<string>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('authToken');
    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string, role: UserRole): Promise<string> => {
    setIsLoading(true);

    await new Promise((resolve) => setTimeout(resolve, 800));

    const foundUser = await import('../mock/users').then(({ mockUsers }) =>
      mockUsers.find(
        (u) => u.email.toLowerCase() === email.toLowerCase() && u.role === role
      )
    );

    if (foundUser) {
      const authData = {
        user: foundUser,
        token: `token-${foundUser.id}-${Date.now()}`,
      };

      localStorage.setItem('user', JSON.stringify(authData.user));
      localStorage.setItem('authToken', authData.token);
      setUser(authData.user);

      const redirectPath = getRedirectPath(authData.user.role);
      setIsLoading(false);
      return redirectPath;
    }

    setIsLoading(false);
    throw new Error('Credenciales inválidas o rol incorrecto');
  };

  const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('authToken');
    setUser(null);
  };

  const getRedirectPath = (role: UserRole): string => {
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

  const value = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
