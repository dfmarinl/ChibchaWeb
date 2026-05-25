import apiClient from '../axios';
import { UserRole } from '../../constants';

export interface User {
  id: string;
  email: string;
  name: string;
  role: UserRole;
  phone?: string;
  company?: string;
  createdAt: string;
  status: 'active' | 'inactive';
}

export interface CreateUserData {
  email: string;
  name: string;
  role: UserRole;
  phone?: string;
  company?: string;
}

export const usersApi = {
  getAll: async (): Promise<User[]> => {
    const response = await apiClient.get<User[]>('/users');
    return response.data;
  },

  getById: async (id: string): Promise<User> => {
    const response = await apiClient.get<User>(`/users/${id}`);
    return response.data;
  },

  create: async (data: CreateUserData): Promise<User> => {
    const response = await apiClient.post<User>('/users', data);
    return response.data;
  },

  update: async (id: string, data: Partial<CreateUserData>): Promise<User> => {
    const response = await apiClient.put<User>(`/users/${id}`, data);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/users/${id}`);
  },
};
