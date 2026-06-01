import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Server, Globe, CreditCard, Ticket, AlertCircle, CheckCircle } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent, Badge, Button } from '../../components/ui';
import { StatsCard, PageHeader } from '../../components/common';
import { suscripcionesApi, Suscripcion } from '../../api/suscripciones';
import { paymentsApi, PagoResponse } from '../../api/payments';
import { SUSCRIPCION_STATUS } from '../../constants';

const formatDate = (dateStr: string) =>
  new Date(dateStr).toLocaleDateString('es-CO', { year: 'numeric', month: 'long', day: 'numeric' });

const getDaysUntilExpiry = (dateStr: string) =>
  Math.ceil((new Date(dateStr).getTime() - Date.now()) / (1000 * 60 * 60 * 24));

const periodLabels: Record<string, string> = {
  MENSUAL: 'Mensual',
  TRIMESTRAL: 'Trimestral',
  SEMESTRAL: 'Semestral',
  ANUAL: 'Anual',
};

const ClientDashboard: React.FC = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState<any>(null);
  const [suscripcion, setSuscripcion] = useState<Suscripcion | null>(null);
  const [payments, setPayments] = useState<PagoResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  useEffect(() => {
    const loadData = async () => {
      try {
        const [subRes, payRes] = await Promise.all([
          suscripcionesApi.getMisSuscripciones(),
          paymentsApi.getMisPagos(),
        ]);
        if (subRes.exitoso && subRes.suscripciones.length > 0) {
          setSuscripcion(subRes.suscripciones[0]);
        }
        if (payRes.exitoso) {
          setPayments(payRes.pagos);
        }
      } catch {
        // ignore
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, []);

  const formatMonto = (monto: number) =>
    `$${monto.toLocaleString('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

  const totalSpent = payments.reduce((sum, p) => sum + p.monto, 0);

  if (loading) {
    return (
      <div className='flex items-center justify-center py-24'>
        <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
      </div>
    );
  }

  const daysUntilExpiry = suscripcion ? getDaysUntilExpiry(suscripcion.fechaFin) : 0;
  const isExpiringSoon = suscripcion && daysUntilExpiry <= 30 && daysUntilExpiry > 0;
  const isActive = suscripcion?.estado === SUSCRIPCION_STATUS.ACTIVA;

  return (
    <div>
      <PageHeader
        title={`Bienvenido, ${user?.name || 'Cliente'}`}
        description='Resumen de tu cuenta y servicios activos'
      />

      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Dominios Activos'
          value='-'
          icon={Globe}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Plan Actual'
          value={suscripcion ? suscripcion.planNombre : 'Sin plan'}
          icon={Server}
          iconColor='text-success-600'
        />
        <StatsCard
          title='Total Gastado'
          value={formatMonto(totalSpent)}
          icon={CreditCard}
          iconColor='text-warning-600'
        />
        <StatsCard
          title='Tickets Abiertos'
          value='-'
          icon={Ticket}
          iconColor='text-blue-600'
        />
      </div>

      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8'>
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Server className='w-5 h-5 text-primary-600' />
              Tu Plan de Hosting
            </CardTitle>
          </CardHeader>
          <CardContent>
            {suscripcion ? (
              <div className='space-y-4'>
                <div className={`p-4 rounded-lg border ${isExpiringSoon ? 'bg-warning-50 border-warning-200' : 'bg-primary-50 border-primary-200'}`}>
                  <h4 className='font-semibold text-primary-900 mb-2'>
                    {suscripcion.planNombre}
                  </h4>
                  <div className='grid grid-cols-2 gap-3 text-sm'>
                    <div>
                      <p className='text-secondary-600'>Plan de pago</p>
                      <p className='font-medium text-secondary-900'>{periodLabels[suscripcion.periodicidad] || suscripcion.periodicidad}</p>
                    </div>
                    <div>
                      <p className='text-secondary-600'>Estado</p>
                      <p className={`font-medium ${isActive ? 'text-success-600' : 'text-error-600'}`}>
                        {isActive ? 'Activa' : suscripcion.estado === SUSCRIPCION_STATUS.EXPIRADA ? 'Expirada' : 'Cancelada'}
                      </p>
                    </div>
                    <div>
                      <p className='text-secondary-600'>Fecha inicio</p>
                      <p className='font-medium text-secondary-900'>{formatDate(suscripcion.fechaInicio)}</p>
                    </div>
                    <div>
                      <p className='text-secondary-600'>Fecha fin</p>
                      <p className='font-medium text-secondary-900'>{formatDate(suscripcion.fechaFin)}</p>
                    </div>
                  </div>
                  {isExpiringSoon && (
                    <div className='mt-3 flex items-center gap-2 text-sm text-warning-700'>
                      <AlertCircle className='w-4 h-4' />
                      <span>Vence en {daysUntilExpiry} días</span>
                    </div>
                  )}
                </div>
                <div className='flex items-center gap-2 text-sm text-secondary-600'>
                  {isActive ? (
                    <CheckCircle className='w-4 h-4 text-success-600' />
                  ) : (
                    <AlertCircle className='w-4 h-4 text-error-600' />
                  )}
                  <span>Estado: {isActive ? 'Activo' : suscripcion.estado === SUSCRIPCION_STATUS.EXPIRADA ? 'Expirado' : 'Cancelado'}</span>
                </div>
                {isActive && (
                  <Button variant='primary' size='sm' onClick={() => navigate('/client/plans')}>
                    Renovar Suscripción
                  </Button>
                )}
              </div>
            ) : (
              <div className='text-center py-6'>
                <Server className='w-12 h-12 text-secondary-400 mx-auto mb-3' />
                <p className='text-secondary-600'>No tienes un plan activo</p>
              </div>
            )}
          </CardContent>
        </Card>

        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <AlertCircle className='w-5 h-5 text-warning-600' />
              Próximas Renovaciones
            </CardTitle>
          </CardHeader>
          <CardContent>
            {suscripcion && isActive ? (
              <div className={`p-3 rounded-lg border ${isExpiringSoon ? 'bg-warning-50 border-warning-200' : 'bg-secondary-50 border-secondary-200'}`}>
                <div className='flex items-center justify-between'>
                  <div className='flex items-center gap-2'>
                    <Server className='w-4 h-4 text-secondary-600' />
                    <span className='font-medium text-sm'>{suscripcion.planNombre}</span>
                  </div>
                  <span className={`text-xs ${isExpiringSoon ? 'text-warning-700' : 'text-secondary-600'}`}>
                    {daysUntilExpiry > 0 ? `${daysUntilExpiry} días` : 'Vencido'}
                  </span>
                </div>
              </div>
            ) : (
              <p className='text-secondary-600 text-center py-4'>No hay suscripciones activas</p>
            )}
          </CardContent>
        </Card>
      </div>

      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6'>
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle>Pagos Recientes</CardTitle>
          </CardHeader>
          <CardContent>
            {payments.length > 0 ? (
              <div className='space-y-3'>
                {payments.slice(0, 4).map((payment) => (
                  <div key={payment.id} className='flex items-center justify-between py-2 border-b border-secondary-100 last:border-0'>
                    <div>
                      <p className='text-sm font-medium'>{payment.referencia || `Pago #${payment.id}`}</p>
                      <p className='text-xs text-secondary-500'>{payment.tipoTarjeta}</p>
                    </div>
                    <div className='text-right'>
                      <p className='text-sm font-semibold'>{formatMonto(payment.monto)}</p>
                      <Badge variant={payment.estado === 'APROBADO' ? 'success' : payment.estado === 'RECHAZADO' ? 'error' : 'warning'} size='sm'>
                        {payment.estado}
                      </Badge>
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className='text-secondary-600 text-center py-4'>No hay pagos registrados</p>
            )}
          </CardContent>
        </Card>

        <Card variant='bordered'>
          <CardHeader>
            <CardTitle>Tickets de Soporte</CardTitle>
          </CardHeader>
          <CardContent>
            <p className='text-secondary-600 text-center py-4'>No hay tickets registrados</p>
          </CardContent>
        </Card>
      </div>


    </div>
  );
};

export default ClientDashboard;
