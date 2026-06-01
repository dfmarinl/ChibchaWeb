import apiClient from '../axios';

export interface PagoResponse {
  id: number;
  monto: number;
  referencia: string;
  estado: string;
  fecha: string;
  tarjetaEnmascarada: string;
  tipoTarjeta: string;
  clienteNombre: string;
  periodicidad: string;
}

export interface CompraPlanData {
  tarjetaId: number;
  periodicidad: string;
}

export interface CompraPlanResponse {
  exitoso: boolean;
  mensaje: string;
  pago?: PagoResponse;
  limiteExcedido?: boolean;
  suscripcionId?: number;
  fechaFin?: string;
}

export interface MisPagosResponse {
  exitoso: boolean;
  pagos: PagoResponse[];
}

export const paymentsApi = {
  getAll: async (): Promise<PagoResponse[]> => {
    const response = await apiClient.get<PagoResponse[]>('/pagos');
    return response.data;
  },

  getMisPagos: async (): Promise<MisPagosResponse> => {
    const response = await apiClient.get<MisPagosResponse>('/pagos/mis-pagos');
    return response.data;
  },

  consultarIntentos: async (): Promise<{ exitoso: boolean; intentosRestantes: number; limiteExcedido: boolean }> => {
    const response = await apiClient.get('/pagos/intentos');
    return response.data;
  },
};
