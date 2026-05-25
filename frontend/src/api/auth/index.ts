import apiClient from '../axios';
import { UserRole } from '../../constants';

export interface LoginCredentials {
  email: string;
  password: string;
  role: UserRole;
}

export interface AuthResponse {
  user: {
    id: string;
    email: string;
    name: string;
    role: UserRole;
  };
  token: string;
}

export const authApi = {
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/auth/logout');
  },

  getCurrentUser: async (): Promise<AuthResponse['user']> => {
    const response = await apiClient.get<AuthResponse['user']>('/auth/me');
    return response.data;
  },
};
