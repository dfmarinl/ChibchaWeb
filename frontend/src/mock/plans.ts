import { PLAN_STATUS } from '../constants';

export interface HostingPlan {
  id: string;
  name: string;
  description: string;
  price: number;
  diskSpace: number;
  bandwidth: string;
  emailAccounts: number;
  databases: number;
  sslIncluded: boolean;
  domainIncluded: boolean;
  support: string;
  features: string[];
  status: typeof PLAN_STATUS[keyof typeof PLAN_STATUS];
  createdAt: string;
}

export const mockPlans: HostingPlan[] = [
  {
    id: 'plan-1',
    name: 'Starter',
    description: 'Perfect for personal websites and small blogs',
    price: 29900,
    diskSpace: 5,
    bandwidth: '50 GB',
    emailAccounts: 5,
    databases: 1,
    sslIncluded: true,
    domainIncluded: false,
    support: 'Email',
    features: [
      '5 GB SSD Storage',
      '50 GB Bandwidth',
      '5 Email Accounts',
      '1 MySQL Database',
      'Free SSL Certificate',
      'Daily Backups',
      '99.5% Uptime Guarantee',
    ],
    status: PLAN_STATUS.ACTIVE,
    createdAt: '2024-01-01T00:00:00Z',
  },
  {
    id: 'plan-2',
    name: 'Business',
    description: 'Ideal for growing businesses and e-commerce',
    price: 59900,
    diskSpace: 20,
    bandwidth: '200 GB',
    emailAccounts: 25,
    databases: 5,
    sslIncluded: true,
    domainIncluded: true,
    support: 'Email & Chat',
    features: [
      '20 GB SSD Storage',
      '200 GB Bandwidth',
      '25 Email Accounts',
      '5 MySQL Databases',
      'Free SSL Certificate',
      'Free Domain for 1 Year',
      'Daily Backups',
      'Priority Support',
      '99.9% Uptime Guarantee',
    ],
    status: PLAN_STATUS.ACTIVE,
    createdAt: '2024-01-01T00:00:00Z',
  },
  {
    id: 'plan-3',
    name: 'Professional',
    description: 'Powerful solution for high-traffic websites',
    price: 99900,
    diskSpace: 50,
    bandwidth: '500 GB',
    emailAccounts: 100,
    databases: 20,
    sslIncluded: true,
    domainIncluded: true,
    support: 'Email, Chat & Phone',
    features: [
      '50 GB SSD Storage',
      '500 GB Bandwidth',
      '100 Email Accounts',
      '20 MySQL Databases',
      'Free SSL Certificate',
      'Free Domain for 1 Year',
      'Real-time Backups',
      '24/7 Priority Support',
      'Staging Environment',
      '99.99% Uptime Guarantee',
    ],
    status: PLAN_STATUS.ACTIVE,
    createdAt: '2024-01-01T00:00:00Z',
  },
  {
    id: 'plan-4',
    name: 'Enterprise',
    description: 'Ultimate performance for large organizations',
    price: 199900,
    diskSpace: 200,
    bandwidth: 'Unlimited',
    emailAccounts: -1,
    databases: -1,
    sslIncluded: true,
    domainIncluded: true,
    support: 'Dedicated Team',
    features: [
      '200 GB SSD Storage',
      'Unlimited Bandwidth',
      'Unlimited Email Accounts',
      'Unlimited Databases',
      'Free SSL Certificate',
      'Free Domain for 1 Year',
      'Real-time Backups + Disaster Recovery',
      'Dedicated Support Team',
      'Custom Staging & Development',
      'Load Balancing',
      'Enterprise Security Suite',
      '99.999% Uptime SLA',
    ],
    status: PLAN_STATUS.ACTIVE,
    createdAt: '2024-01-01T00:00:00Z',
  },
];

export const mockPlanSubscriptions = [
  {
    id: 'sub-1',
    clientId: '2',
    planId: 'plan-2',
    startDate: '2024-02-20T14:30:00Z',
    endDate: '2025-02-20T14:30:00Z',
    status: PLAN_STATUS.ACTIVE,
  },
  {
    id: 'sub-2',
    clientId: '3',
    planId: 'plan-3',
    startDate: '2024-03-10T09:15:00Z',
    endDate: '2025-03-10T09:15:00Z',
    status: PLAN_STATUS.ACTIVE,
  },
  {
    id: 'sub-3',
    clientId: '6',
    planId: 'plan-1',
    startDate: '2024-03-15T08:00:00Z',
    endDate: '2025-03-15T08:00:00Z',
    status: PLAN_STATUS.ACTIVE,
  },
  {
    id: 'sub-4',
    clientId: '7',
    planId: 'plan-2',
    startDate: '2024-04-01T12:00:00Z',
    endDate: '2025-04-01T12:00:00Z',
    status: PLAN_STATUS.ACTIVE,
  },
];
