import apiClient from '../axios';

export interface Suscripcion {
  id: number;
  planId: number;
  planNombre: string;
  periodicidad: string;
  fechaInicio: string;
  fechaFin: string;
  estado: string;
  pagoId: number | null;
}

export interface MisSuscripcionesResponse {
  exitoso: boolean;
  suscripciones: Suscripcion[];
}

export interface RenovarResponse {
  exitoso: boolean;
  mensaje: string;
  suscripcion?: Suscripcion;
}

export const suscripcionesApi = {
  getMisSuscripciones: async (): Promise<MisSuscripcionesResponse> => {
    const response = await apiClient.get<MisSuscripcionesResponse>('/suscripciones/mis-suscripciones');
    return response.data;
  },

  renovar: async (id: number, data: { periodicidad: string; tarjetaId: number }): Promise<RenovarResponse> => {
    const response = await apiClient.post<RenovarResponse>(`/suscripciones/${id}/renovar`, data);
    return response.data;
  },
};
