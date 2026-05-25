import { Users, UserCircle, Package, Globe, CreditCard, TrendingUp, AlertCircle, CheckCircle } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { StatsCard, PageHeader } from '../../components/common';
import { mockUsers, mockPlans, mockPlans as plans, mockDomains, mockPayments, mockTickets, mockTicketStats } from '../../mock';
import { USER_ROLES } from '../../constants';

const AdminDashboard: React.FC = () => {
  const totalClients = mockUsers.filter((u) => u.role === USER_ROLES.CLIENT).length;
  const totalEmployees = mockUsers.filter((u) => u.role === USER_ROLES.EMPLOYEE).length;
  const totalDomains = mockDomains.length;
  const totalPayments = mockPayments.reduce((sum, p) => sum + (p.status === 'completed' ? p.amount : 0), 0);

  const recentPayments = mockPayments.slice(0, 5);
  const recentTickets = mockTickets.slice(0, 5);

  const getStatusBadge = (status: string) => {
    const styles: Record<string, string> = {
      completed: 'bg-success-100 text-success-700',
      pending: 'bg-warning-100 text-warning-700',
      failed: 'bg-error-100 text-error-700',
      open: 'bg-blue-100 text-blue-700',
      in_progress: 'bg-primary-100 text-primary-700',
      resolved: 'bg-success-100 text-success-700',
    };
    return styles[status] || 'bg-secondary-100 text-secondary-700';
  };

  const getPriorityBadge = (priority: string) => {
    const styles: Record<string, string> = {
      low: 'bg-secondary-100 text-secondary-700',
      medium: 'bg-warning-100 text-warning-700',
      high: 'bg-orange-100 text-orange-700',
      urgent: 'bg-error-100 text-error-700',
    };
    return styles[priority] || 'bg-secondary-100 text-secondary-700';
  };

  return (
    <div>
      <PageHeader
        title='Dashboard Administrativo'
        description='Resumen general del sistema ChibchaWEB'
      />

      {/* Stats Grid */}
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Clientes Activos'
          value={totalClients}
          icon={Users}
          change='+12% este mes'
          changeType='increase'
          iconColor='text-success-600'
        />
        <StatsCard
          title='Empleados'
          value={totalEmployees}
          icon={UserCircle}
          change='2 nuevos'
          changeType='neutral'
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Dominios'
          value={totalDomains}
          icon={Globe}
          change='+8% este mes'
          changeType='increase'
          iconColor='text-blue-600'
        />
        <StatsCard
          title='Ingresos Mensuales'
          value={`$${(totalPayments / 100).toLocaleString('es-CO')}`}
          icon={CreditCard}
          change='+15% vs mes anterior'
          changeType='increase'
          iconColor='text-warning-600'
        />
      </div>

      {/* Charts and Tables */}
      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8'>
        {/* Recent Payments */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <CreditCard className='w-5 h-5 text-primary-600' />
              Pagos Recientes
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-4'>
              {recentPayments.map((payment) => (
                <div key={payment.id} className='flex items-center justify-between py-3 border-b border-secondary-100 last:border-0'>
                  <div>
                    <p className='text-sm font-medium text-secondary-900'>{payment.invoiceNumber}</p>
                    <p className='text-xs text-secondary-500'>{payment.paymentMethod}</p>
                  </div>
                  <div className='text-right'>
                    <p className='text-sm font-semibold text-secondary-900'>
                      ${(payment.amount / 100).toLocaleString('es-CO')}
                    </p>
                    <span className={`text-xs px-2 py-1 rounded-full ${getStatusBadge(payment.status)}`}>
                      {payment.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Ticket Stats */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <AlertCircle className='w-5 h-5 text-warning-600' />
              Estado de Tickets
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='grid grid-cols-2 gap-4'>
              <div className='p-4 bg-secondary-50 rounded-lg'>
                <div className='flex items-center gap-2 mb-2'>
                  <AlertCircle className='w-4 h-4 text-blue-600' />
                  <span className='text-sm text-secondary-600'>Abiertos</span>
                </div>
                <p className='text-2xl font-bold text-secondary-900'>{mockTicketStats.open}</p>
              </div>
              <div className='p-4 bg-secondary-50 rounded-lg'>
                <div className='flex items-center gap-2 mb-2'>
                  <TrendingUp className='w-4 h-4 text-primary-600' />
                  <span className='text-sm text-secondary-600'>En Progreso</span>
                </div>
                <p className='text-2xl font-bold text-secondary-900'>{mockTicketStats.inProgress}</p>
              </div>
              <div className='p-4 bg-secondary-50 rounded-lg'>
                <div className='flex items-center gap-2 mb-2'>
                  <CheckCircle className='w-4 h-4 text-success-600' />
                  <span className='text-sm text-secondary-600'>Resueltos</span>
                </div>
                <p className='text-2xl font-bold text-secondary-900'>{mockTicketStats.resolved}</p>
              </div>
              <div className='p-4 bg-error-50 rounded-lg'>
                <div className='flex items-center gap-2 mb-2'>
                  <AlertCircle className='w-4 h-4 text-error-600' />
                  <span className='text-sm text-secondary-600'>Urgentes</span>
                </div>
                <p className='text-2xl font-bold text-error-900'>{mockTicketStats.urgent}</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Recent Tickets Table */}
      <Card variant='bordered'>
        <CardHeader>
          <CardTitle>Tickets Recientes</CardTitle>
        </CardHeader>
        <CardContent>
          <div className='overflow-x-auto'>
            <table className='min-w-full'>
              <thead>
                <tr className='border-b border-secondary-200'>
                  <th className='text-left py-3 px-4 text-sm font-semibold text-secondary-600'>ID</th>
                  <th className='text-left py-3 px-4 text-sm font-semibold text-secondary-600'>Cliente</th>
                  <th className='text-left py-3 px-4 text-sm font-semibold text-secondary-600'>Asunto</th>
                  <th className='text-left py-3 px-4 text-sm font-semibold text-secondary-600'>Prioridad</th>
                  <th className='text-left py-3 px-4 text-sm font-semibold text-secondary-600'>Estado</th>
                </tr>
              </thead>
              <tbody>
                {recentTickets.map((ticket) => (
                  <tr key={ticket.id} className='border-b border-secondary-100 hover:bg-secondary-50'>
                    <td className='py-3 px-4 text-sm font-mono text-secondary-600'>{ticket.id}</td>
                    <td className='py-3 px-4 text-sm text-secondary-900'>
                      {mockUsers.find(u => u.id === ticket.clientId)?.name || 'Cliente'}
                    </td>
                    <td className='py-3 px-4 text-sm text-secondary-900'>{ticket.subject}</td>
                    <td className='py-3 px-4'>
                      <span className={`text-xs px-2 py-1 rounded-full ${getPriorityBadge(ticket.priority)}`}>
                        {ticket.priority}
                      </span>
                    </td>
                    <td className='py-3 px-4'>
                      <span className={`text-xs px-2 py-1 rounded-full ${getStatusBadge(ticket.status)}`}>
                        {ticket.status}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default AdminDashboard;
