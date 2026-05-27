import apiClient from '../axios';

export interface Card {
  id: number;
  titular: string;
  numeroEnmascarado: string;
  fechaVencimiento: string;
  tipoTarjeta: string;
}

export interface CreateCardData {
  numero: string;
  titular: string;
  fechaVencimiento: string;
  cvv: string;
}

export interface CreateCardResponse {
  exitoso: boolean;
  mensaje: string;
  tarjeta?: Card;
  intentosRestantes?: number;
  limiteExcedido?: boolean;
}

export const cardsApi = {
  getAll: async (): Promise<Card[]> => {
    const response = await apiClient.get<Card[]>('/tarjetas');
    return response.data;
  },

  create: async (data: CreateCardData): Promise<CreateCardResponse> => {
    const response = await apiClient.post<CreateCardResponse>('/tarjetas', data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await apiClient.delete(`/tarjetas/${id}`);
  },
};
