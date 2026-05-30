import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { User } from '../api/users';
import { ROUTES, USER_ROLES, UserRole } from '../constants';
import { authApi, RegisterClienteData } from '../api/auth';

const ROLE_MAP: Record<string, UserRole> = {
  CLIENTE: USER_ROLES.CLIENT,
  ADMINISTRADOR: USER_ROLES.ADMIN,
  EMPLEADO: USER_ROLES.EMPLOYEE,
};

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<string>;
  register: (data: RegisterClienteData) => Promise<string>;
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

  const login = async (email: string, password: string): Promise<string> => {
    setIsLoading(true);

    try {
      const response = await authApi.login({ email, contrasena: password });

      const mappedRole = ROLE_MAP[response.rol];
      if (!mappedRole) {
        throw new Error(`Rol desconocido: ${response.rol}`);
      }

      const userData: User = {
        id: email,
        email: response.email,
        name: email.split('@')[0],
        role: mappedRole,
        createdAt: new Date().toISOString(),
        status: 'active',
      };

      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('authToken', response.token);
      setUser(userData);

      const redirectPath = getRedirectPath(mappedRole);
      setIsLoading(false);
      return redirectPath;
    } catch (error) {
      setIsLoading(false);
      throw new Error('Credenciales inválidas');
    }
  };

  const register = async (data: RegisterClienteData): Promise<string> => {
    setIsLoading(true);

    try {
      const response = await authApi.registerCliente(data);

      const mappedRole = ROLE_MAP[response.rol];
      if (!mappedRole) {
        throw new Error(`Rol desconocido: ${response.rol}`);
      }

      const userData: User = {
        id: data.email,
        email: response.email,
        name: data.nombre,
        role: mappedRole,
        createdAt: new Date().toISOString(),
        status: 'active',
      };

      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('authToken', response.token);
      setUser(userData);

      const redirectPath = getRedirectPath(mappedRole);
      setIsLoading(false);
      return redirectPath;
    } catch (error) {
      setIsLoading(false);
      if (error instanceof Error) throw error;
      throw new Error('Error al registrar usuario');
    }
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
    register,
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
