import { useState, useEffect } from 'react';
import { Users, UserCircle, Package, CreditCard, Loader2 } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { StatsCard, PageHeader } from '../../components/common';
import apiClient from '../../api/axios';
import { paymentsApi, PagoResponse } from '../../api/payments';
import { plansApi } from '../../api/plans';
import type { PlanHosting } from '../../types/plan';

interface UsuarioDto {
  id: number;
  nombre: string;
  email: string;
  telefono: string | null;
  fechaRegistro: string;
  tipoUsuario: string;
}

const formatMonto = (monto: number) =>
  `$${monto.toLocaleString('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

const getStatusBadgeClass = (status: string) => {
  const styles: Record<string, string> = {
    APROBADO: 'bg-success-100 text-success-700',
    PENDIENTE: 'bg-warning-100 text-warning-700',
    RECHAZADO: 'bg-error-100 text-error-700',
    ANULADO: 'bg-secondary-100 text-secondary-600',
  };
  return styles[status] || 'bg-secondary-100 text-secondary-700';
};

const getStatusLabel = (status: string) => {
  const labels: Record<string, string> = {
    APROBADO: 'Aprobado',
    PENDIENTE: 'Pendiente',
    RECHAZADO: 'Rechazado',
    ANULADO: 'Anulado',
  };
  return labels[status] || status;
};

const AdminDashboard: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [users, setUsers] = useState<UsuarioDto[]>([]);
  const [payments, setPayments] = useState<PagoResponse[]>([]);
  const [plans, setPlans] = useState<PlanHosting[]>([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [usersRes, paymentsData, plansData] = await Promise.all([
          apiClient.get<UsuarioDto[]>('/usuarios'),
          paymentsApi.getAll(),
          plansApi.getAll(),
        ]);
        setUsers(usersRes.data);
        setPayments(paymentsData);
        setPlans(plansData);
      } catch (err: any) {
        setError(err?.response?.data?.error || 'Error al cargar datos del dashboard');
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const totalClients = users.filter((u) => u.tipoUsuario === 'CLIENTE').length;
  const totalEmployees = users.filter((u) => u.tipoUsuario === 'EMPLEADO').length;
  const totalPlans = plans.length;
  const totalRevenue = payments
    .filter((p) => p.estado === 'APROBADO')
    .reduce((sum, p) => sum + p.monto, 0);

  const sortedPayments = [...payments].sort(
    (a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime()
  );
  const recentPayments = sortedPayments.slice(0, 5);

  const sortedClients = [...users]
    .filter((u) => u.tipoUsuario === 'CLIENTE')
    .sort((a, b) => new Date(b.fechaRegistro).getTime() - new Date(a.fechaRegistro).getTime());
  const recentClients = sortedClients.slice(0, 4);

  if (loading) {
    return (
      <div className='flex items-center justify-center h-64'>
        <Loader2 className='w-8 h-8 animate-spin text-primary-600' />
      </div>
    );
  }

  return (
    <div>
      <PageHeader
        title='Dashboard Administrativo'
        description='Resumen general del sistema ChibchaWEB'
      />

      {error && (
        <div className='mb-4 p-3 rounded-lg bg-error-50 border border-error-200'>
          <p className='text-sm text-error-700'>{error}</p>
        </div>
      )}

      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Clientes Activos'
          value={totalClients}
          icon={Users}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Empleados'
          value={totalEmployees}
          icon={UserCircle}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Planes Activos'
          value={totalPlans}
          icon={Package}
          iconColor='text-blue-600'
        />
        <StatsCard
          title='Ingresos Mensuales'
          value={formatMonto(totalRevenue)}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
      </div>

      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8'>
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <CreditCard className='w-5 h-5 text-primary-600' />
              Pagos Recientes
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-4'>
              {recentPayments.length === 0 ? (
                <p className='text-sm text-secondary-500 text-center py-4'>No hay pagos registrados</p>
              ) : (
                recentPayments.map((payment) => (
                  <div key={payment.id} className='flex items-center justify-between py-3 border-b border-secondary-100 last:border-0'>
                    <div>
                      <p className='text-sm font-medium text-secondary-900'>{payment.referencia || '-'}</p>
                      <p className='text-xs text-secondary-500'>{payment.tipoTarjeta || payment.tarjetaEnmascarada || '-'}</p>
                    </div>
                    <div className='text-right'>
                      <p className='text-sm font-semibold text-secondary-900'>
                        {formatMonto(payment.monto)}
                      </p>
                      <span className={`text-xs px-2 py-1 rounded-full ${getStatusBadgeClass(payment.estado)}`}>
                        {getStatusLabel(payment.estado)}
                      </span>
                    </div>
                  </div>
                ))
              )}
            </div>
          </CardContent>
        </Card>

        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Users className='w-5 h-5 text-primary-600' />
              Últimos Clientes Registrados
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-4'>
              {recentClients.length === 0 ? (
                <p className='text-sm text-secondary-500 text-center py-4'>No hay clientes registrados</p>
              ) : (
                recentClients.map((client) => (
                  <div key={client.id} className='flex items-center gap-3 py-3 border-b border-secondary-100 last:border-0'>
                    <div className='w-10 h-10 bg-success-100 rounded-full flex items-center justify-center'>
                      <span className='text-sm font-medium text-success-700'>
                        {client.nombre.charAt(0)}
                      </span>
                    </div>
                    <div className='flex-1'>
                      <p className='text-sm font-medium text-secondary-900'>{client.nombre}</p>
                      <p className='text-xs text-secondary-500'>{client.email}</p>
                    </div>
                    <span className='text-xs text-secondary-400'>
                      {new Date(client.fechaRegistro).toLocaleDateString('es-CO')}
                    </span>
                  </div>
                ))
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default AdminDashboard;
