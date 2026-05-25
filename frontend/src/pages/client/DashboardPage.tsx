import { useState, useEffect } from 'react';
import { Server, Globe, CreditCard, Ticket, TrendingUp, Calendar, AlertCircle, CheckCircle } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent, Badge } from '../../components/ui';
import { StatsCard, PageHeader } from '../../components/common';
import { mockDomains, mockPayments, mockPlans, mockPlanSubscriptions, mockTickets, mockUsers } from '../../mock';
import { DOMAIN_STATUS, TICKET_STATUS, PAYMENT_STATUS } from '../../constants';

const ClientDashboard: React.FC = () => {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const myDomains = mockDomains.filter(d => d.clientId === user?.id);
  const myPayments = mockPayments.filter(p => p.clientId === user?.id);
  const myTickets = mockTickets.filter(t => t.clientId === user?.id);
  const mySubscription = mockPlanSubscriptions.find(s => s.clientId === user?.id);

  const activeDomains = myDomains.filter(d => d.status === DOMAIN_STATUS.ACTIVE).length;
  const totalSpent = myPayments
    .filter(p => p.status === PAYMENT_STATUS.COMPLETED)
    .reduce((sum, p) => sum + p.amount, 0);
  const openTickets = myTickets.filter(t => t.status === TICKET_STATUS.OPEN || t.status === TICKET_STATUS.IN_PROGRESS).length;

  const getStatusBadge = (status: string) => {
    const styles: Record<string, string> = {
      completed: 'success',
      pending: 'warning',
      failed: 'error',
      open: 'info',
      in_progress: 'primary',
      resolved: 'success',
    };
    return <Badge variant={styles[status] || 'default'}>{status}</Badge>;
  };

  return (
    <div>
      <PageHeader
        title={`Bienvenido, ${user?.name || 'Cliente'}`}
        description='Resumen de tu cuenta y servicios activos'
      />

      {/* Stats Grid */}
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Dominios Activos'
          value={activeDomains}
          icon={Globe}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Plan Actual'
          value={mySubscription ? mockPlans.find(p => p.id === mySubscription.planId)?.name || '-' : 'Sin plan'}
          icon={Server}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Total Gastado'
          value={`$${(totalSpent / 100).toLocaleString('es-CO')}`}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
        <StatsCard
          title='Tickets Abiertos'
          value={openTickets}
          icon={Ticket}
          iconColor='text-blue-600'
        />
      </div>

      {/* Quick Actions */}
      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8'>
        {/* Active Subscription */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Server className='w-5 h-5 text-primary-600' />
              Tu Plan de Hosting
            </CardTitle>
          </CardHeader>
          <CardContent>
            {mySubscription ? (
              <div className='space-y-4'>
                <div className='p-4 bg-primary-50 rounded-lg border border-primary-200'>
                  <h4 className='font-semibold text-primary-900 mb-2'>
                    Plan {mockPlans.find(p => p.id === mySubscription.planId)?.name}
                  </h4>
                  <div className='grid grid-cols-2 gap-3 text-sm'>
                    <div>
                      <p className='text-secondary-600'>Fecha inicio</p>
                      <p className='font-medium text-secondary-900'>
                        {new Date(mySubscription.startDate).toLocaleDateString('es-CO')}
                      </p>
                    </div>
                    <div>
                      <p className='text-secondary-600'>Fecha fin</p>
                      <p className='font-medium text-secondary-900'>
                        {new Date(mySubscription.endDate).toLocaleDateString('es-CO')}
                      </p>
                    </div>
                  </div>
                </div>
                <div className='flex items-center gap-2 text-sm text-secondary-600'>
                  <CheckCircle className='w-4 h-4 text-success-600' />
                  <span>Estado: Activo</span>
                </div>
              </div>
            ) : (
              <div className='text-center py-6'>
                <Server className='w-12 h-12 text-secondary-400 mx-auto mb-3' />
                <p className='text-secondary-600'>No tienes un plan activo</p>
              </div>
            )}
          </CardContent>
        </Card>

        {/* Renewal Alerts */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <AlertCircle className='w-5 h-5 text-warning-600' />
              Próximas Renovaciones
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-3'>
              {myDomains.slice(0, 3).map((domain) => {
                const daysUntilExpiry = Math.ceil(
                  (new Date(domain.expiryDate).getTime() - Date.now()) / (1000 * 60 * 60 * 24)
                );
                const isExpiringSoon = daysUntilExpiry <= 30 && daysUntilExpiry > 0;

                return (
                  <div
                    key={domain.id}
                    className={`p-3 rounded-lg border ${
                      isExpiringSoon ? 'bg-warning-50 border-warning-200' : 'bg-secondary-50 border-secondary-200'
                    }`}
                  >
                    <div className='flex items-center justify-between'>
                      <div className='flex items-center gap-2'>
                        <Globe className='w-4 h-4 text-secondary-600' />
                        <span className='font-medium text-sm'>{domain.name}</span>
                      </div>
                      <span className={`text-xs ${isExpiringSoon ? 'text-warning-700' : 'text-secondary-600'}`}>
                        {daysUntilExpiry > 0 ? `${daysUntilExpiry} días` : 'Vencido'}
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Recent Activity */}
      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6'>
        {/* Recent Payments */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle>Pagos Recientes</CardTitle>
          </CardHeader>
          <CardContent>
            {myPayments.length > 0 ? (
              <div className='space-y-3'>
                {myPayments.slice(0, 4).map((payment) => (
                  <div key={payment.id} className='flex items-center justify-between py-2 border-b border-secondary-100 last:border-0'>
                    <div>
                      <p className='text-sm font-medium'>{payment.invoiceNumber}</p>
                      <p className='text-xs text-secondary-500'>{payment.paymentMethod}</p>
                    </div>
                    <div className='text-right'>
                      <p className='text-sm font-semibold'>${(payment.amount / 100).toLocaleString('es-CO')}</p>
                      {getStatusBadge(payment.status)}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className='text-secondary-600 text-center py-4'>No hay pagos registrados</p>
            )}
          </CardContent>
        </Card>

        {/* Recent Tickets */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle>Tickets de Soporte</CardTitle>
          </CardHeader>
          <CardContent>
            {myTickets.length > 0 ? (
              <div className='space-y-3'>
                {myTickets.slice(0, 4).map((ticket) => (
                  <div key={ticket.id} className='py-2 border-b border-secondary-100 last:border-0'>
                    <div className='flex items-start justify-between'>
                      <div className='flex-1'>
                        <p className='text-sm font-medium'>{ticket.subject}</p>
                        <p className='text-xs text-secondary-500 mt-1'>
                          {new Date(ticket.createdAt).toLocaleDateString('es-CO')}
                        </p>
                      </div>
                      {getStatusBadge(ticket.status)}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className='text-secondary-600 text-center py-4'>No hay tickets abiertos</p>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default ClientDashboard;
