import { useState, useEffect } from 'react';
import { CreditCard, Download, Filter } from 'lucide-react';
import { Button, Badge, Select, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader, StatsCard } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockPayments, mockPlans } from '../../mock';
import { PAYMENT_STATUS } from '../../constants';

const ClientPaymentsPage: React.FC = () => {
  const [user, setUser] = useState<any>(null);
  const [filterStatus, setFilterStatus] = useState('all');

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const myPayments = mockPayments
    .filter(p => p.clientId === user?.id)
    .filter(p => filterStatus === 'all' || p.status === filterStatus);

  const totalPaid = mockPayments
    .filter(p => p.clientId === user?.id && p.status === PAYMENT_STATUS.COMPLETED)
    .reduce((sum, p) => sum + p.amount, 0);

  const pendingAmount = mockPayments
    .filter(p => p.clientId === user?.id && p.status === PAYMENT_STATUS.PENDING)
    .reduce((sum, p) => sum + p.amount, 0);

  const getStatusBadge = (status: string) => {
    switch (status) {
      case PAYMENT_STATUS.COMPLETED:
        return <Badge variant='success' dot>Completado</Badge>;
      case PAYMENT_STATUS.PENDING:
        return <Badge variant='warning' dot>Pendiente</Badge>;
      case PAYMENT_STATUS.FAILED:
        return <Badge variant='error' dot>Rechazado</Badge>;
      default:
        return <Badge variant='default'>-</Badge>;
    }
  };

  return (
    <div>
      <PageHeader
        title='Mis Pagos'
        description='Historial completo de transacciones'
        action={
          <Button variant='outline' leftIcon={<Download className='w-4 h-4' />}>
            Exportar Historial
          </Button>
        }
      />

      {/* Stats */}
      <div className='grid grid-cols-1 md:grid-cols-3 gap-6 mb-8'>
        <StatsCard
          title='Total Pagado'
          value={`$${(totalPaid / 100).toLocaleString('es-CO')}`}
          icon={CreditCard}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Pagos Completados'
          value={mockPayments.filter(p => p.clientId === user?.id && p.status === PAYMENT_STATUS.COMPLETED).length}
          icon={CreditCard}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Monto Pendiente'
          value={`$${(pendingAmount / 100).toLocaleString('es-CO')}`}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
      </div>

      {/* Payments Table */}
      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Historial de Pagos</CardTitle>
            <div className='flex items-center gap-3'>
              <Select
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
                className='w-40'
              >
                <option value='all'>Todos</option>
                <option value={PAYMENT_STATUS.COMPLETED}>Completados</option>
                <option value={PAYMENT_STATUS.PENDING}>Pendientes</option>
                <option value={PAYMENT_STATUS.FAILED}>Rechazados</option>
              </Select>
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {myPayments.length === 0 ? (
            <EmptyState
              message='No hay pagos registrados'
              description='No se encontraron pagos con los filtros seleccionados.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Factura</Th>
                  <Th>Plan</Th>
                  <Th>Método de Pago</Th>
                  <Th>Monto</Th>
                  <Th>Estado</Th>
                  <Th>Fecha</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {myPayments.map((payment) => {
                  const plan = mockPlans.find(p => p.id === payment.planId);

                  return (
                    <Tr key={payment.id}>
                      <Td>
                        <span className='font-mono text-sm'>{payment.invoiceNumber}</span>
                      </Td>
                      <Td>{plan?.name || '-'}</Td>
                      <Td>{payment.paymentMethod}</Td>
                      <Td>
                        <span className='font-semibold text-secondary-900'>
                          ${(payment.amount / 100).toLocaleString('es-CO')}
                        </span>
                      </Td>
                      <Td>{getStatusBadge(payment.status)}</Td>
                      <Td>{new Date(payment.paymentDate).toLocaleDateString('es-CO')}</Td>
                      <Td>
                        <Button variant='ghost' size='sm'>
                          <Download className='w-4 h-4' />
                        </Button>
                      </Td>
                    </Tr>
                  );
                })}
              </Tbody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default ClientPaymentsPage;
