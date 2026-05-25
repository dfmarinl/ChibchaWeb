import { useState } from 'react';
import { Plus, Search, Download, CreditCard } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader, StatsCard } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockPayments, mockUsers, mockPlans } from '../../mock';
import { PAYMENT_STATUS } from '../../constants';

const PaymentsPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const payments = mockPayments.filter(
    (payment) =>
      payment.invoiceNumber.toLowerCase().includes(searchTerm.toLowerCase()) ||
      mockUsers.find(u => u.id === payment.clientId)?.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    switch (status) {
      case PAYMENT_STATUS.COMPLETED:
        return <Badge variant='success' dot>Completado</Badge>;
      case PAYMENT_STATUS.PENDING:
        return <Badge variant='warning' dot>Pendiente</Badge>;
      case PAYMENT_STATUS.FAILED:
        return <Badge variant='error' dot>Rechazado</Badge>;
      case PAYMENT_STATUS.REFUNDED:
        return <Badge variant='default'>Reembolsado</Badge>;
      default:
        return <Badge variant='default'>-</Badge>;
    }
  };

  const totalCompleted = mockPayments
    .filter((p) => p.status === PAYMENT_STATUS.COMPLETED)
    .reduce((sum, p) => sum + p.amount, 0);

  const pendingPayments = mockPayments.filter((p) => p.status === PAYMENT_STATUS.PENDING).length;
  const failedPayments = mockPayments.filter((p) => p.status === PAYMENT_STATUS.FAILED).length;

  return (
    <div>
      <PageHeader
        title='Gestión de Pagos'
        description='Historial y seguimiento de transacciones'
        action={
          <div className='flex gap-2'>
            <Button variant='outline' leftIcon={<Download className='w-4 h-4' />}>
              Exportar
            </Button>
            <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
              Nuevo Pago
            </Button>
          </div>
        }
      />

      {/* Stats */}
      <div className='grid grid-cols-1 md:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Total Recaudado'
          value={`$${(totalCompleted / 100).toLocaleString('es-CO')}`}
          icon={CreditCard}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Pagos Completados'
          value={mockPayments.filter((p) => p.status === PAYMENT_STATUS.COMPLETED).length}
          icon={CreditCard}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Pagos Pendientes'
          value={pendingPayments}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
        <StatsCard
          title='Pagos Rechazados'
          value={failedPayments}
          icon={CreditCard}
          iconColor='text-error-600'
        />
      </div>

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Historial de Pagos</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar pago...'
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {payments.length === 0 ? (
            <EmptyState
              message='No se encontraron pagos'
              description='No hay pagos que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Factura</Th>
                  <Th>Cliente</Th>
                  <Th>Plan</Th>
                  <Th>Método</Th>
                  <Th>Monto</Th>
                  <Th>Estado</Th>
                  <Th>Fecha</Th>
                </Tr>
              </Thead>
              <Tbody>
                {payments.map((payment) => {
                  const client = mockUsers.find((u) => u.id === payment.clientId);
                  const plan = mockPlans.find((p) => p.id === payment.planId);

                  return (
                    <Tr key={payment.id}>
                      <Td>
                        <span className='font-mono text-sm'>{payment.invoiceNumber}</span>
                      </Td>
                      <Td>{client?.name || '-'}</Td>
                      <Td>{plan?.name || '-'}</Td>
                      <Td>{payment.paymentMethod}</Td>
                      <Td>
                        <span className='font-semibold'>
                          ${(payment.amount / 100).toLocaleString('es-CO')}
                        </span>
                      </Td>
                      <Td>{getStatusBadge(payment.status)}</Td>
                      <Td>{new Date(payment.paymentDate).toLocaleDateString('es-CO')}</Td>
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

export default PaymentsPage;
