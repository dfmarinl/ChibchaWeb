import apiClient from '../axios';

export interface Site {
  id: number;
  urlSitio: string;
  estadoActivo: boolean;
  fechaCreacion: string;
}

export interface CreateSiteData {
  urlSitio: string;
}

export const sitesApi = {
  getAll: async (): Promise<Site[]> => {
    const response = await apiClient.get<Site[]>('/sitios-web');
    return response.data;
  },

  create: async (data: CreateSiteData): Promise<Site> => {
    const response = await apiClient.post<Site>('/sitios-web', data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/sitios-web/${id}`);
  },
};
