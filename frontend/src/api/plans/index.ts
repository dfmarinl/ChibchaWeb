import apiClient from '../axios';
import { PlanHosting, CrearPlanData, CrearPlanResponse, AsociarPlataformaData, AsociarPlataformaResponse } from '../../types/plan';
import { CompraPlanData, CompraPlanResponse } from '../payments';

export const plansApi = {
  getAll: async (): Promise<PlanHosting[]> => {
    const response = await apiClient.get<PlanHosting[]>('/planes');
    return response.data;
  },

  getById: async (id: number): Promise<PlanHosting> => {
    const response = await apiClient.get<PlanHosting>(`/planes/${id}`);
    return response.data;
  },

  create: async (data: CrearPlanData): Promise<CrearPlanResponse> => {
    const response = await apiClient.post<CrearPlanResponse>('/planes', data);
    return response.data;
  },

  update: async (id: number, data: CrearPlanData): Promise<PlanHosting> => {
    const response = await apiClient.put<PlanHosting>(`/planes/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/planes/${id}`);
  },

  asociarPlataforma: async (id: number, data: AsociarPlataformaData): Promise<AsociarPlataformaResponse> => {
    const response = await apiClient.post<AsociarPlataformaResponse>(`/planes/${id}/asociar-plataforma`, data);
    return response.data;
  },

  comprar: async (id: number, data: CompraPlanData): Promise<CompraPlanResponse> => {
    const response = await apiClient.post<CompraPlanResponse>(`/planes/${id}/comprar`, data);
    return response.data;
  },
};
