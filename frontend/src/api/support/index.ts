import apiClient from '../axios';
import { TICKET_STATUS, TICKET_PRIORITY } from '../../constants';

export interface Ticket {
  id: string;
  clientId: string;
  assignedTo?: string;
  subject: string;
  description: string;
  status: typeof TICKET_STATUS[keyof typeof TICKET_STATUS];
  priority: typeof TICKET_PRIORITY[keyof typeof TICKET_PRIORITY];
  createdAt: string;
  updatedAt: string;
}

export interface CreateTicketData {
  clientId: string;
  subject: string;
  description: string;
  priority: typeof TICKET_PRIORITY[keyof typeof TICKET_PRIORITY];
}

export const supportApi = {
  getAll: async (): Promise<Ticket[]> => {
    const response = await apiClient.get<Ticket[]>('/tickets');
    return response.data;
  },

  getByClient: async (clientId: string): Promise<Ticket[]> => {
    const response = await apiClient.get<Ticket[]>(`/tickets/client/${clientId}`);
    return response.data;
  },

  getByEmployee: async (employeeId: string): Promise<Ticket[]> => {
    const response = await apiClient.get<Ticket[]>(`/tickets/assigned/${employeeId}`);
    return response.data;
  },

  getById: async (id: string): Promise<Ticket> => {
    const response = await apiClient.get<Ticket>(`/tickets/${id}`);
    return response.data;
  },

  create: async (data: CreateTicketData): Promise<Ticket> => {
    const response = await apiClient.post<Ticket>('/tickets', data);
    return response.data;
  },

  update: async (id: string, data: Partial<Ticket>): Promise<Ticket> => {
    const response = await apiClient.put<Ticket>(`/tickets/${id}`, data);
    return response.data;
  },

  assign: async (id: string, employeeId: string): Promise<Ticket> => {
    const response = await apiClient.put<Ticket>(`/tickets/${id}/assign`, { employeeId });
    return response.data;
  },
};
