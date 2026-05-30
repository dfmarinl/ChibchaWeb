import apiClient from '../axios';

export interface Distribuidor {
  id: number;
  nombre: string;
  email: string;
  region: string | null;
  codigoDistribuidor: string;
  maxDominios: number;
  nivelDistribuidor: 'BASICO' | 'PLATA' | 'ORO' | 'DIAMANTE' | 'PREMIUM';
}

export interface CreateDistribuidorData {
  nombre: string;
  email: string;
  region?: string;
  codigoDistribuidor: string;
  maxDominios: number;
}

export interface UpdateDistribuidorData {
  nombre?: string;
  email?: string;
  region?: string;
  codigoDistribuidor?: string;
  maxDominios?: number;
}

export const distribuidoresApi = {
  getAll: async (): Promise<Distribuidor[]> => {
    const response = await apiClient.get<Distribuidor[]>('/distribuidores');
    return response.data;
  },

  getById: async (id: number): Promise<Distribuidor> => {
    const response = await apiClient.get<Distribuidor>(`/distribuidores/${id}`);
    return response.data;
  },

  create: async (data: CreateDistribuidorData): Promise<Distribuidor> => {
    const response = await apiClient.post<Distribuidor>('/distribuidores', data);
    return response.data;
  },

  update: async (id: number, data: UpdateDistribuidorData): Promise<Distribuidor> => {
    const response = await apiClient.put<Distribuidor>(`/distribuidores/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/distribuidores/${id}`);
  },
};
