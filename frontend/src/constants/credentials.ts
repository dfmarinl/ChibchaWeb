export interface Credential {
  rol: string;
  email: string;
  password: string;
}

export const DEMO_CREDENTIALS: Credential[] = [
  { rol: 'Admin', email: 'admin@chibchaweb.com', password: 'admin123' },
  { rol: 'Cliente', email: 'carlos.martinez@email.com', password: '123456' },
  { rol: 'Empleado', email: 'laura.diaz@chibchaweb.com', password: '123456' },
];
