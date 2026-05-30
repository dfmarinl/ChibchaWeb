export interface PlanHosting {
  id: number;
  nombre: string;
  precioMensual: number;
  espacioDisco: number;
  anchoBanda: number;
  cuentasEmail: number;
  tipoPlan: string;
  plataforma: string;
  mysqlIncluido: boolean | null;
  phpVersion: string | null;
  sqlServerIncluido: boolean | null;
  iisVersion: string | null;
  pythonIncluido: boolean | null;
  aspNetVersion: string | null;
  caracteristicas: Record<string, string>;
}

export interface CrearPlanData {
  nombre: string;
  precioMensual: number;
  espacioDisco: number;
  anchoBanda: number;
  cuentasEmail: number;
  tipoPlan: string;
  plataforma: string;
  mysqlIncluido?: boolean;
  phpVersion?: string;
  sqlServerIncluido?: boolean;
  iisVersion?: string;
  pythonIncluido?: boolean;
  aspNetVersion?: string;
}

export interface ModificarPlanData {
  nombre: string;
  precioMensual: number;
  espacioDisco: number;
  anchoBanda: number;
  cuentasEmail: number;
  mysqlIncluido?: boolean;
  phpVersion?: string;
  sqlServerIncluido?: boolean;
  iisVersion?: string;
  pythonIncluido?: boolean;
  aspNetVersion?: string;
}

export interface AsociarPlataformaData {
  plataforma: string;
  mysqlIncluido?: boolean;
  phpVersion?: string;
  sqlServerIncluido?: boolean;
  iisVersion?: string;
  pythonIncluido?: boolean;
  aspNetVersion?: string;
}

export interface CrearPlanResponse {
  exitoso: boolean;
  mensaje: string;
  plan?: PlanHosting;
  limiteExcedido?: boolean;
}

export interface AsociarPlataformaResponse {
  exitoso: boolean;
  mensaje: string;
  plan?: PlanHosting;
  limiteExcedido?: boolean;
}
