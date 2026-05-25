export const APP_NAME = import.meta.env.VITE_APP_NAME || 'ChibchaWEB';

export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const ROUTES = {
  PUBLIC: {
    LOGIN: '/login',
    ROLE_SELECTOR: '/select-role',
  },
  ADMIN: {
    DASHBOARD: '/admin/dashboard',
    CLIENTS: '/admin/clients',
    EMPLOYEES: '/admin/employees',
    PLANS: '/admin/plans',
    DOMAINS: '/admin/domains',
    PAYMENTS: '/admin/payments',
  },
  CLIENT: {
    DASHBOARD: '/client/dashboard',
    PLANS: '/client/plans',
    DOMAINS: '/client/domains',
    PAYMENTS: '/client/payments',
    SUPPORT: '/client/support',
  },
  EMPLOYEE: {
    DASHBOARD: '/employee/dashboard',
    TICKETS: '/employee/tickets',
  },
} as const;

export const USER_ROLES = {
  ADMIN: 'admin',
  CLIENT: 'client',
  EMPLOYEE: 'employee',
} as const;

export type UserRole = typeof USER_ROLES[keyof typeof USER_ROLES];

export const TICKET_STATUS = {
  OPEN: 'open',
  IN_PROGRESS: 'in_progress',
  PENDING: 'pending',
  RESOLVED: 'resolved',
  CLOSED: 'closed',
} as const;

export const TICKET_PRIORITY = {
  LOW: 'low',
  MEDIUM: 'medium',
  HIGH: 'high',
  URGENT: 'urgent',
} as const;

export const PLAN_STATUS = {
  ACTIVE: 'active',
  SUSPENDED: 'suspended',
  CANCELLED: 'cancelled',
} as const;

export const DOMAIN_STATUS = {
  ACTIVE: 'active',
  EXPIRED: 'expired',
  PENDING: 'pending',
} as const;

export const PAYMENT_STATUS = {
  COMPLETED: 'completed',
  PENDING: 'pending',
  FAILED: 'failed',
  REFUNDED: 'refunded',
} as const;
