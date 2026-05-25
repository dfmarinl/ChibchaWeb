import apiClient from '../axios';
import { DOMAIN_STATUS } from '../../constants';

export interface Domain {
  id: string;
  name: string;
  clientId: string;
  status: typeof DOMAIN_STATUS[keyof typeof DOMAIN_STATUS];
  expiryDate: string;
  registrationDate: string;
  autoRenew: boolean;
}

export interface CreateDomainData {
  name: string;
  clientId: string;
  expiryDate: string;
  autoRenew?: boolean;
}

export const domainsApi = {
  getAll: async (): Promise<Domain[]> => {
    const response = await apiClient.get<Domain[]>('/domains');
    return response.data;
  },

  getByClient: async (clientId: string): Promise<Domain[]> => {
    const response = await apiClient.get<Domain[]>(`/domains/client/${clientId}`);
    return response.data;
  },

  getById: async (id: string): Promise<Domain> => {
    const response = await apiClient.get<Domain>(`/domains/${id}`);
    return response.data;
  },

  create: async (data: CreateDomainData): Promise<Domain> => {
    const response = await apiClient.post<Domain>('/domains', data);
    return response.data;
  },

  update: async (id: string, data: Partial<CreateDomainData>): Promise<Domain> => {
    const response = await apiClient.put<Domain>(`/domains/${id}`, data);
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await apiClient.delete(`/domains/${id}`);
  },
};
