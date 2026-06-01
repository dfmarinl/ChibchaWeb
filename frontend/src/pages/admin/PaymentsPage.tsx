import { useState, useEffect, useCallback } from 'react';
import { Search, Download, CreditCard } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
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

const PaymentsPage: React.FC = () => {
  const [payments, setPayments] = useState<PagoResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const loadPayments = useCallback(async () => {
    try {
      const data = await paymentsApi.getAll();
      setPayments(data);
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
    (p) =>
      p.referencia?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.clienteNombre?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalAprobado = payments
    .filter((p) => p.estado === 'APROBADO')
    .reduce((sum, p) => sum + p.monto, 0);

  const aprobados = payments.filter((p) => p.estado === 'APROBADO').length;
  const pendientes = payments.filter((p) => p.estado === 'PENDIENTE').length;
  const rechazados = payments.filter((p) => p.estado === 'RECHAZADO').length;

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
        title='Gestión de Pagos'
        description='Historial y seguimiento de transacciones'
        action={
          <div className='flex gap-2'>
            <Button variant='outline' leftIcon={<Download className='w-4 h-4' />}>
              Exportar
            </Button>
          </div>
        }
      />

      <div className='grid grid-cols-1 md:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Total Recaudado'
          value={formatMonto(totalAprobado)}
          icon={CreditCard}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Pagos Aprobados'
          value={aprobados}
          icon={CreditCard}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Pagos Pendientes'
          value={pendientes}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
        <StatsCard
          title='Pagos Rechazados'
          value={rechazados}
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
          {filtered.length === 0 ? (
            <EmptyState
              message='No hay pagos registrados'
              description='No hay pagos que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Referencia</Th>
                  <Th>Cliente</Th>
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
                    <Td>{payment.clienteNombre || '-'}</Td>
                    <Td>{payment.tarjetaEnmascarada || '-'}</Td>
                    <Td>{payment.tipoTarjeta || '-'}</Td>
                    <Td>
                      <span className='font-semibold'>{formatMonto(payment.monto)}</span>
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

export default PaymentsPage;
