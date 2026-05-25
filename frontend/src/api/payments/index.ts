import apiClient from '../axios';
import { PAYMENT_STATUS } from '../../constants';

export interface Payment {
  id: string;
  clientId: string;
  planId: string;
  amount: number;
  status: typeof PAYMENT_STATUS[keyof typeof PAYMENT_STATUS];
  paymentDate: string;
  paymentMethod: string;
  invoiceNumber: string;
}

export interface CreatePaymentData {
  clientId: string;
  planId: string;
  amount: number;
  paymentMethod: string;
}

export const paymentsApi = {
  getAll: async (): Promise<Payment[]> => {
    const response = await apiClient.get<Payment[]>('/payments');
    return response.data;
  },

  getByClient: async (clientId: string): Promise<Payment[]> => {
    const response = await apiClient.get<Payment[]>(`/payments/client/${clientId}`);
    return response.data;
  },

  getById: async (id: string): Promise<Payment> => {
    const response = await apiClient.get<Payment>(`/payments/${id}`);
    return response.data;
  },

  create: async (data: CreatePaymentData): Promise<Payment> => {
    const response = await apiClient.post<Payment>('/payments', data);
    return response.data;
  },
};
