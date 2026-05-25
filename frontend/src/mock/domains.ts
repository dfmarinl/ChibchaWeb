import { Domain } from '../api/domains';
import { DOMAIN_STATUS } from '../constants';

export const mockDomains: Domain[] = [
  {
    id: 'domain-1',
    name: 'techsolutions.com',
    clientId: '2',
    status: DOMAIN_STATUS.ACTIVE,
    expiryDate: '2025-02-20T23:59:59Z',
    registrationDate: '2024-02-20T14:30:00Z',
    autoRenew: true,
  },
  {
    id: 'domain-2',
    name: 'startupinnovation.co',
    clientId: '3',
    status: DOMAIN_STATUS.ACTIVE,
    expiryDate: '2025-03-10T23:59:59Z',
    registrationDate: '2024-03-10T09:15:00Z',
    autoRenew: true,
  },
  {
    id: 'domain-3',
    name: 'digitalmarketingpro.com',
    clientId: '6',
    status: DOMAIN_STATUS.ACTIVE,
    expiryDate: '2025-03-15T23:59:59Z',
    registrationDate: '2024-03-15T08:00:00Z',
    autoRenew: false,
  },
  {
    id: 'domain-4',
    name: 'ecommerceexpress.com.co',
    clientId: '7',
    status: DOMAIN_STATUS.ACTIVE,
    expiryDate: '2025-04-01T23:59:59Z',
    registrationDate: '2024-04-01T12:00:00Z',
    autoRenew: true,
  },
  {
    id: 'domain-5',
    name: 'techsolutions-store.com',
    clientId: '2',
    status: DOMAIN_STATUS.ACTIVE,
    expiryDate: '2025-06-15T23:59:59Z',
    registrationDate: '2024-06-15T10:00:00Z',
    autoRenew: true,
  },
  {
    id: 'domain-6',
    name: 'old-startup.co',
    clientId: '3',
    status: DOMAIN_STATUS.EXPIRED,
    expiryDate: '2024-01-15T23:59:59Z',
    registrationDate: '2023-01-15T10:00:00Z',
    autoRenew: false,
  },
];
