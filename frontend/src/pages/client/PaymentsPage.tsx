import { useState, useEffect, useCallback } from 'react';
import { CreditCard, Download } from 'lucide-react';
import { Button, Badge, Select, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader, StatsCard } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { paymentsApi, PagoResponse } from '../../api/payments';

const getStatusBadge = (estado: string) => {
  switch (estado) {
    case 'APROBADO':
      return <Badge variant='success' dot>Aprobado</Badge>;
    case 'PENDIENTE':
      return <Badge variant='warning' dot>Pendiente</Badge>;
    case 'RECHAZADO':
      return <Badge variant='error' dot>Rechazado</Badge>;
    case 'ANULADO':
      return <Badge variant='default'>Anulado</Badge>;
    default:
      return <Badge variant='default'>-</Badge>;
  }
};

const formatMonto = (monto: number) =>
  `$${monto.toLocaleString('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

const ClientPaymentsPage: React.FC = () => {
  const [payments, setPayments] = useState<PagoResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [filterStatus, setFilterStatus] = useState('all');

  const loadPayments = useCallback(async () => {
    try {
      const res = await paymentsApi.getMisPagos();
      setPayments(res.pagos);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadPayments();
  }, [loadPayments]);

  const filtered = payments.filter(
    (p) => filterStatus === 'all' || p.estado === filterStatus
  );

  const totalPagado = payments
    .filter((p) => p.estado === 'APROBADO')
    .reduce((sum, p) => sum + p.monto, 0);

  const montoPendiente = payments
    .filter((p) => p.estado === 'PENDIENTE')
    .reduce((sum, p) => sum + p.monto, 0);

  if (loading) {
    return (
      <div className='flex items-center justify-center py-24'>
        <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
      </div>
    );
  }

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

      <div className='grid grid-cols-1 md:grid-cols-3 gap-6 mb-8'>
        <StatsCard
          title='Total Pagado'
          value={formatMonto(totalPagado)}
          icon={CreditCard}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Pagos Aprobados'
          value={payments.filter((p) => p.estado === 'APROBADO').length}
          icon={CreditCard}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Monto Pendiente'
          value={formatMonto(montoPendiente)}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
      </div>

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
                <option value='APROBADO'>Aprobados</option>
                <option value='PENDIENTE'>Pendientes</option>
                <option value='RECHAZADO'>Rechazados</option>
              </Select>
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {filtered.length === 0 ? (
            <EmptyState
              message='No hay pagos registrados'
              description='No se encontraron pagos con los filtros seleccionados.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Referencia</Th>
                  <Th>Tarjeta</Th>
                  <Th>Tipo</Th>
                  <Th>Monto</Th>
                  <Th>Estado</Th>
                  <Th>Fecha</Th>
                </Tr>
              </Thead>
              <Tbody>
                {filtered.map((payment) => (
                  <Tr key={payment.id}>
                    <Td>
                      <span className='font-mono text-sm'>{payment.referencia || '-'}</span>
                    </Td>
                    <Td>{payment.tarjetaEnmascarada || '-'}</Td>
                    <Td>{payment.tipoTarjeta || '-'}</Td>
                    <Td>
                      <span className='font-semibold text-secondary-900'>{formatMonto(payment.monto)}</span>
                    </Td>
                    <Td>{getStatusBadge(payment.estado)}</Td>
                    <Td>{new Date(payment.fecha).toLocaleDateString('es-CO')}</Td>
                  </Tr>
                ))}
              </Tbody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default ClientPaymentsPage;
