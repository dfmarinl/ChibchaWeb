import apiClient from '../axios';

export interface Client {
  id: number;
  nombre: string;
  email: string;
  telefono: string | null;
  fechaRegistro: string;
  direccion: string | null;
  documentoIdentidad: string;
  region: string | null;
  limitesSitios: number;
}

export interface CreateClientData {
  nombre: string;
  email: string;
  contrasena: string;
  telefono?: string;
  direccion?: string;
  documentoIdentidad: string;
  region?: string;
}

export interface UpdateClientData {
  nombre?: string;
  email?: string;
  telefono?: string;
  direccion?: string;
  documentoIdentidad?: string;
  region?: string;
}

export const clientsApi = {
  getAll: async (): Promise<Client[]> => {
    const response = await apiClient.get<Client[]>('/clientes');
    return response.data;
  },

  getMe: async (): Promise<Client> => {
    const response = await apiClient.get<Client>('/clientes/me');
    return response.data;
  },

  getById: async (id: number): Promise<Client> => {
    const response = await apiClient.get<Client>(`/clientes/${id}`);
    return response.data;
  },

  create: async (data: CreateClientData): Promise<Client> => {
    const response = await apiClient.post<Client>('/clientes', data);
    return response.data;
  },

  update: async (id: number, data: UpdateClientData): Promise<Client> => {
    const response = await apiClient.put<Client>(`/clientes/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/clientes/${id}`);
  },
};
