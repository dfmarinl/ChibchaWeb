import apiClient from '../axios';

export interface LoginCredentials {
  email: string;
  contrasena: string;
}

export interface RegisterClienteData {
  nombre: string;
  email: string;
  contrasena: string;
  telefono?: string;
  direccion?: string;
  documentoIdentidad: string;
  region?: string;
}

export interface AuthResponse {
  token: string;
  sesionId: number;
  email: string;
  rol: string;
}

export const authApi = {
  login: async (credentials: LoginCredentials): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/login', credentials);
    return response.data;
  },

  registerCliente: async (data: RegisterClienteData): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>('/auth/registro/cliente', data);
    return response.data;
  },

  logout: async (): Promise<void> => {
    await apiClient.post('/auth/logout');
  },

  getCurrentUser: async (): Promise<AuthResponse> => {
    const response = await apiClient.get<AuthResponse>('/auth/me');
    return response.data;
  },
};
