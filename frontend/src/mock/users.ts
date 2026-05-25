import { User } from '../api/users';
import { USER_ROLES } from '../constants';

export const mockUsers: User[] = [
  {
    id: '1',
    email: 'admin@chibchaweb.com',
    name: 'Carlos Admin',
    role: USER_ROLES.ADMIN,
    phone: '+57 300 123 4567',
    createdAt: '2024-01-15T10:00:00Z',
    status: 'active',
  },
  {
    id: '2',
    email: 'cliente1@empresa.com',
    name: 'María García López',
    role: USER_ROLES.CLIENT,
    phone: '+57 320 987 6543',
    company: 'Tech Solutions SAS',
    createdAt: '2024-02-20T14:30:00Z',
    status: 'active',
  },
  {
    id: '3',
    email: 'cliente2@startup.co',
    name: 'Andrés Martínez',
    role: USER_ROLES.CLIENT,
    phone: '+57 315 555 1234',
    company: 'Startup Innovation',
    createdAt: '2024-03-10T09:15:00Z',
    status: 'active',
  },
  {
    id: '4',
    email: 'empleado1@chibchaweb.com',
    name: 'Juan Rodríguez',
    role: USER_ROLES.EMPLOYEE,
    phone: '+57 301 222 3333',
    createdAt: '2024-01-25T11:20:00Z',
    status: 'active',
  },
  {
    id: '5',
    email: 'empleado2@chibchaweb.com',
    name: 'Laura Pérez',
    role: USER_ROLES.EMPLOYEE,
    phone: '+57 302 444 5555',
    createdAt: '2024-02-05T15:45:00Z',
    status: 'active',
  },
  {
    id: '6',
    email: 'cliente3@empresa.com',
    name: 'Pedro Sánchez',
    role: USER_ROLES.CLIENT,
    phone: '+57 318 666 7777',
    company: 'Digital Marketing Pro',
    createdAt: '2024-03-15T08:00:00Z',
    status: 'active',
  },
  {
    id: '7',
    email: 'cliente4@negocio.com',
    name: 'Ana María Torres',
    role: USER_ROLES.CLIENT,
    phone: '+57 319 888 9999',
    company: 'E-Commerce Express',
    createdAt: '2024-04-01T12:00:00Z',
    status: 'active',
  },
];

export const mockAuthUser = {
  user: mockUsers[0],
  token: 'mock-jwt-token-12345',
};
