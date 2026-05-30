import { createBrowserRouter, Navigate } from 'react-router-dom';
import { ROUTES, USER_ROLES } from '../constants';
import PrivateRoute from './PrivateRoute';

// Layouts
import { AdminLayout, ClientLayout, EmployeeLayout } from '../components/layouts';

// Public Pages
import { LandingPage } from '../pages/public';

// Auth Pages
import { LoginPage, RegisterPage } from '../pages/auth';

// Admin Pages
import {
  AdminDashboard,
  ClientsPage,
  EmployeesPage,
  PlansPage,
  DomainsPage,
  PaymentsPage,
  DistribuidoresPage,
} from '../pages/admin';

// Client Pages
import {
  ClientDashboard,
  ClientPlans,
  ClientDomains,
  ClientPayments,
  ClientSupport,
  ClientProfile,
} from '../pages/client';

// Employee Pages
import { EmployeeDashboard, EmployeeTickets } from '../pages/employee';

export const router = createBrowserRouter([
  // Public Routes
  {
    path: ROUTES.PUBLIC.LANDING,
    element: <LandingPage />,
  },
  {
    path: ROUTES.PUBLIC.LOGIN,
    element: <LoginPage />,
  },
  {
    path: ROUTES.PUBLIC.REGISTER,
    element: <RegisterPage />,
  },

  // Admin Routes
  {
    element: <PrivateRoute allowedRoles={[USER_ROLES.ADMIN]} />,
    children: [
      {
        element: <AdminLayout />,
        children: [
          {
            path: ROUTES.ADMIN.DASHBOARD,
            element: <AdminDashboard />,
          },
          {
            path: ROUTES.ADMIN.CLIENTS,
            element: <ClientsPage />,
          },
          {
            path: ROUTES.ADMIN.EMPLOYEES,
            element: <EmployeesPage />,
          },
          {
            path: ROUTES.ADMIN.PLANS,
            element: <PlansPage />,
          },
          {
            path: ROUTES.ADMIN.DOMAINS,
            element: <DomainsPage />,
          },
          {
            path: ROUTES.ADMIN.PAYMENTS,
            element: <PaymentsPage />,
          },
          {
            path: ROUTES.ADMIN.DISTRIBUIDORES,
            element: <DistribuidoresPage />,
          },
        ],
      },
    ],
  },

  // Client Routes
  {
    element: <PrivateRoute allowedRoles={[USER_ROLES.CLIENT]} />,
    children: [
      {
        element: <ClientLayout />,
        children: [
          {
            path: ROUTES.CLIENT.DASHBOARD,
            element: <ClientDashboard />,
          },
          {
            path: ROUTES.CLIENT.PLANS,
            element: <ClientPlans />,
          },
          {
            path: ROUTES.CLIENT.DOMAINS,
            element: <ClientDomains />,
          },
          {
            path: ROUTES.CLIENT.PAYMENTS,
            element: <ClientPayments />,
          },
          {
            path: ROUTES.CLIENT.SUPPORT,
            element: <ClientSupport />,
          },
          {
            path: ROUTES.CLIENT.PROFILE,
            element: <ClientProfile />,
          },
        ],
      },
    ],
  },

  // Employee Routes
  {
    element: <PrivateRoute allowedRoles={[USER_ROLES.EMPLOYEE]} />,
    children: [
      {
        element: <EmployeeLayout />,
        children: [
          {
            path: ROUTES.EMPLOYEE.DASHBOARD,
            element: <EmployeeDashboard />,
          },
          {
            path: ROUTES.EMPLOYEE.TICKETS,
            element: <EmployeeTickets />,
          },
        ],
      },
    ],
  },

  // Catch-all route
  {
    path: '*',
    element: <Navigate to={ROUTES.PUBLIC.LOGIN} replace />,
  },
]);
