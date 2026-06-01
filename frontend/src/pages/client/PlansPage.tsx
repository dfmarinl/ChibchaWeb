import { useState, useEffect, useCallback } from 'react';
import { Check, CreditCard, AlertCircle, Loader2 } from 'lucide-react';
import { Button, Badge, Modal } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { EmptyState } from '../../components/tables/Table';
import { plansApi } from '../../api/plans';
import { paymentsApi } from '../../api/payments';
import { cardsApi, Card } from '../../api/cards';
import { PlanHosting } from '../../types/plan';

const tierBadges: Record<string, string> = {
  PLATINO: 'primary',
  ORO: 'warning',
  PLATA: 'default',
};

const formatMonto = (monto: number) =>
  `$${monto.toLocaleString('es-CO', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;

const PERIOD_OPTIONS = [
  { value: 'MENSUAL', label: 'Mensual', meses: 1 },
  { value: 'TRIMESTRAL', label: 'Trimestral', meses: 3 },
  { value: 'SEMESTRAL', label: 'Semestral', meses: 6 },
  { value: 'ANUAL', label: 'Anual', meses: 12 },
];

const calcularTotal = (precioMensual: number, periodValue: string) => {
  const opt = PERIOD_OPTIONS.find(p => p.value === periodValue);
  return opt ? precioMensual * opt.meses : precioMensual;
};

const ClientPlansPage: React.FC = () => {
  const [plans, setPlans] = useState<PlanHosting[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedPlan, setSelectedPlan] = useState<number | null>(null);

  const [purchasePlan, setPurchasePlan] = useState<PlanHosting | null>(null);
  const [cards, setCards] = useState<Card[]>([]);
  const [selectedCardId, setSelectedCardId] = useState<number | null>(null);
  const [selectedPeriod, setSelectedPeriod] = useState<string>('MENSUAL');
  const [purchasing, setPurchasing] = useState(false);
  const [purchaseError, setPurchaseError] = useState<string | null>(null);
  const [purchaseSuccess, setPurchaseSuccess] = useState<string | null>(null);
  const [intentosRestantes, setIntentosRestantes] = useState(4);
  const [limiteExcedido, setLimiteExcedido] = useState(false);

  const loadPlans = useCallback(async () => {
    try {
      const data = await plansApi.getAll();
      setPlans(data);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadPlans();
  }, [loadPlans]);

  const openPurchaseModal = async (plan: PlanHosting) => {
    setPurchasePlan(plan);
    setPurchaseError(null);
    setPurchaseSuccess(null);
    setSelectedCardId(null);
    setSelectedPeriod('MENSUAL');
    setPurchasing(false);
    try {
      const [cardsData, intentosData] = await Promise.all([
        cardsApi.getAll(),
        paymentsApi.consultarIntentos(),
      ]);
      setCards(cardsData);
      setIntentosRestantes(intentosData.intentosRestantes);
      setLimiteExcedido(intentosData.limiteExcedido);
    } catch {
      setCards([]);
      setIntentosRestantes(4);
      setLimiteExcedido(false);
    }
  };

  const closePurchaseModal = () => {
    setPurchasePlan(null);
  };

  const handleConfirmPurchase = async () => {
    if (!purchasePlan || !selectedCardId) return;
    setPurchasing(true);
    setPurchaseError(null);
    try {
      const res = await plansApi.comprar(purchasePlan.id, { tarjetaId: selectedCardId, periodicidad: selectedPeriod });
      if (res.exitoso) {
        const fechaFin = res.fechaFin
          ? new Date(res.fechaFin).toLocaleDateString('es-CO', { year: 'numeric', month: 'long', day: 'numeric' })
          : '';
        setPurchaseSuccess(
          `Pago aprobado — Referencia: ${res.pago?.referencia || '-'}. Suscripción activa hasta el ${fechaFin}.`
        );
      } else {
        setPurchaseError(res.mensaje || 'Error al procesar el pago');
      }
    } catch (err: any) {
      const status = err?.response?.status;
      const backendMsg = err?.response?.data?.mensaje;

      if (!err?.response) {
        setPurchaseError('Error de conexión. Verifica tu internet e intenta de nuevo.');
      } else if (status === 423) {
        setPurchaseError('Has superado el límite de intentos. Espera 30 minutos e intenta de nuevo.');
      } else if (backendMsg) {
        setPurchaseError(backendMsg);
      } else {
        setPurchaseError('Error al realizar la transacción. Intenta de nuevo más tarde.');
      }
    } finally {
      setPurchasing(false);
    }
  };

  const handleSelectPlan = (planId: number) => {
    setSelectedPlan(planId === selectedPlan ? null : planId);
  };

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
        title='Planes de Hosting'
        description='Elige el plan perfecto para tu sitio web'
      />

      {plans.length === 0 ? (
        <EmptyState
          message='No hay planes disponibles'
          description='En este momento no hay planes de hosting registrados. Vuelve más tarde.'
        />
      ) : (
      <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6'>
        {plans.map((plan) => {
          const isSelected = selectedPlan === plan.id;
          const tier = plan.tipoPlan;

          return (
            <div
              key={plan.id}
              className={`bg-white rounded-xl border border-secondary-200 flex flex-col relative transition-all
                ${isSelected ? `ring-2 ring-success-600 shadow-lg scale-[1.02]` : 'hover:shadow-md'}`}
            >
              <div className='absolute -top-3 left-1/2 -translate-x-1/2 z-10'>
                <Badge variant={(tierBadges[tier] as 'primary' | 'warning' | 'default') || 'default'} size='lg'>
                  {plan.tipoPlan}
                </Badge>
              </div>

              <div className='p-6 pt-8 flex flex-col flex-1'>
                <div className='mb-4'>
                  <h3 className='text-xl font-bold text-secondary-900'>{plan.nombre}</h3>
                  <p className='text-xs text-secondary-500 mt-1'>Plataforma: {plan.plataforma}</p>
                </div>

                <div className='mb-6'>
                  <div className='flex items-baseline gap-1'>
                    <span className='text-4xl font-bold text-primary-600'>
                      {formatMonto(plan.precioMensual)}
                    </span>
                    <span className='text-secondary-500'>/mes</span>
                  </div>
                </div>

                <div className='space-y-3 flex-1'>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>{plan.espacioDisco} MB</span>
                    <span className='text-secondary-600'>Disco SSD</span>
                  </div>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>{plan.anchoBanda} MB</span>
                    <span className='text-secondary-600'>Ancho de banda</span>
                  </div>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>{plan.cuentasEmail}</span>
                    <span className='text-secondary-600'>Cuentas de email</span>
                  </div>

                  {plan.mysqlIncluido !== null && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>MySQL</span>
                      <span className='text-secondary-600'>{plan.mysqlIncluido ? 'Incluido' : 'No incluido'}</span>
                    </div>
                  )}
                  {plan.phpVersion && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>PHP</span>
                      <span className='text-secondary-600'>{plan.phpVersion}</span>
                    </div>
                  )}
                  {plan.sqlServerIncluido !== null && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>SQL Server</span>
                      <span className='text-secondary-600'>{plan.sqlServerIncluido ? 'Incluido' : 'No incluido'}</span>
                    </div>
                  )}
                  {plan.iisVersion && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>IIS</span>
                      <span className='text-secondary-600'>{plan.iisVersion}</span>
                    </div>
                  )}
                  {plan.pythonIncluido !== null && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>Python</span>
                      <span className='text-secondary-600'>{plan.pythonIncluido ? 'Incluido' : 'No incluido'}</span>
                    </div>
                  )}
                  {plan.aspNetVersion && (
                    <div className='flex items-center gap-2 text-sm'>
                      <span className='font-semibold text-secondary-900'>ASP.NET</span>
                      <span className='text-secondary-600'>{plan.aspNetVersion}</span>
                    </div>
                  )}

                  <div className='pt-3 border-t border-secondary-200'>
                    <ul className='space-y-2'>
                      {Object.entries(plan.caracteristicas).map(([key, value]) => (
                        <li key={key} className='flex items-start gap-2 text-sm'>
                          <Check className='w-4 h-4 text-success-600 mt-0.5 flex-shrink-0' />
                          <span className='text-secondary-700 capitalize'>{key.replace(/([A-Z])/g, ' $1').trim()}: {value}</span>
                        </li>
                      ))}
                    </ul>
                  </div>
                </div>

                <div className='mt-6 space-y-2'>
                  <Button
                    variant={isSelected ? 'secondary' : 'primary'}
                    size='lg'
                    className='w-full'
                    onClick={() => handleSelectPlan(plan.id)}
                  >
                    {isSelected ? 'Plan Seleccionado' : 'Seleccionar Plan'}
                  </Button>
                  {isSelected && (
                    <Button
                      variant='primary'
                      size='lg'
                      className='w-full'
                      onClick={() => openPurchaseModal(plan)}
                    >
                      Adquirir Ahora
                    </Button>
                  )}
                </div>
              </div>
            </div>
          );
        })}
      </div>
      )}

      <Modal
        open={purchasePlan !== null}
        onClose={closePurchaseModal}
        title='Confirmar Compra'
        footer={
          purchaseSuccess ? (
            <Button variant='primary' onClick={closePurchaseModal}>
              Cerrar
            </Button>
          ) : (
            <div className='flex gap-3 w-full justify-end'>
              <Button variant='outline' onClick={closePurchaseModal} disabled={purchasing}>
                Cancelar
              </Button>
              <Button
                variant='primary'
                onClick={handleConfirmPurchase}
                disabled={!selectedCardId || purchasing || limiteExcedido}
              >
                {purchasing ? (
                  <span className='flex items-center gap-2'>
                    <Loader2 className='w-4 h-4 animate-spin' />
                    Procesando...
                  </span>
                ) : (
                  `Pagar ${purchasePlan ? formatMonto(calcularTotal(purchasePlan.precioMensual, selectedPeriod)) : ''}`
                )}
              </Button>
            </div>
          )
        }
      >
        {purchaseError && (
          <div className='flex items-start gap-3 p-4 mb-4 bg-error-50 border border-error-200 rounded-lg'>
            <AlertCircle className='w-5 h-5 text-error-600 mt-0.5 flex-shrink-0' />
            <div>
              <p className='text-sm font-semibold text-error-800'>Error al realizar la transacción</p>
              <p className='text-sm text-error-700 mt-0.5'>{purchaseError}</p>
            </div>
          </div>
        )}

        {purchaseSuccess ? (
          <div className='py-4 text-center'>
            <div className='w-12 h-12 bg-success-100 rounded-full flex items-center justify-center mx-auto mb-3'>
              <Check className='w-6 h-6 text-success-600' />
            </div>
            <p className='text-success-700 font-medium text-sm'>{purchaseSuccess}</p>
          </div>
        ) : (
          <>
            {purchasePlan && (
              <div className='mb-4 p-3 bg-secondary-50 rounded-lg'>
                <p className='text-sm font-medium text-secondary-900'>{purchasePlan.nombre}</p>
                <p className='text-xs text-secondary-500'>{purchasePlan.tipoPlan} &middot; {purchasePlan.plataforma}</p>
                <p className='text-lg font-bold text-primary-600 mt-1'>
                  {formatMonto(calcularTotal(purchasePlan.precioMensual, selectedPeriod))}
                </p>
                <p className='text-xs text-secondary-500'>
                  {formatMonto(purchasePlan.precioMensual)}/mes &times; {PERIOD_OPTIONS.find(p => p.value === selectedPeriod)?.meses} {PERIOD_OPTIONS.find(p => p.value === selectedPeriod)?.meses === 1 ? 'mes' : 'meses'}
                </p>
              </div>
            )}

            <p className='text-sm font-medium text-secondary-700 mb-2'>Plan de pago</p>
            <div className='grid grid-cols-2 gap-2 mb-4'>
              {PERIOD_OPTIONS.map((opt) => {
                const total = purchasePlan ? purchasePlan.precioMensual * opt.meses : 0;
                return (
                  <label
                    key={opt.value}
                    className={`flex items-center gap-2 p-2.5 border rounded-lg cursor-pointer transition-colors
                      ${selectedPeriod === opt.value
                        ? 'border-primary-500 bg-primary-50'
                        : 'border-secondary-200 hover:border-secondary-300'}`}
                  >
                    <input
                      type='radio'
                      name='period'
                      value={opt.value}
                      checked={selectedPeriod === opt.value}
                      onChange={() => setSelectedPeriod(opt.value)}
                      className='accent-primary-600'
                    />
                    <div>
                      <p className='text-sm font-medium text-secondary-900'>{opt.label}</p>
                      <p className='text-xs text-secondary-500'>{formatMonto(total)}</p>
                    </div>
                  </label>
                );
              })}
            </div>

            <div className='flex items-center gap-2 mb-4 p-2.5 bg-secondary-50 border border-secondary-200 rounded-lg'>
              <span className='text-xs font-medium text-secondary-600'>Intentos restantes:</span>
              <div className='flex gap-1'>
                {[1, 2, 3, 4].map((i) => (
                  <div
                    key={i}
                    className={`w-3 h-3 rounded-full ${
                      i <= intentosRestantes
                        ? intentosRestantes <= 1
                          ? 'bg-error-500'
                          : intentosRestantes <= 2
                          ? 'bg-warning-500'
                          : 'bg-success-500'
                        : 'bg-secondary-200'
                    }`}
                  />
                ))}
              </div>
              <span className='text-xs text-secondary-500'>{intentosRestantes}/4</span>
            </div>

            <p className='text-sm font-medium text-secondary-700 mb-3'>Selecciona una tarjeta</p>

            {cards.length === 0 ? (
              <div className='text-center py-4'>
                <CreditCard className='w-8 h-8 text-secondary-400 mx-auto mb-2' />
                <p className='text-sm text-secondary-500'>No tienes tarjetas registradas</p>
                <p className='text-xs text-secondary-400 mt-1'>
                  Agrega una desde tu{' '}
                  <a href='/client/profile' className='text-primary-600 underline'>perfil</a>
                </p>
              </div>
            ) : (
              <div className='space-y-2'>
                {cards.map((card) => (
                  <label
                    key={card.id}
                    className={`flex items-center gap-3 p-3 border rounded-lg cursor-pointer transition-colors
                      ${selectedCardId === card.id
                        ? 'border-primary-500 bg-primary-50'
                        : 'border-secondary-200 hover:border-secondary-300'}`}
                  >
                    <input
                      type='radio'
                      name='card'
                      value={card.id}
                      checked={selectedCardId === card.id}
                      onChange={() => setSelectedCardId(card.id)}
                      className='accent-primary-600'
                    />
                    <div className='flex-1'>
                      <p className='text-sm font-medium text-secondary-900'>{card.titular}</p>
                      <p className='text-xs text-secondary-500'>
                        {card.numeroEnmascarado} &middot; {card.tipoTarjeta} &middot; Vence {card.fechaVencimiento}
                      </p>
                    </div>
                  </label>
                ))}
              </div>
            )}
          </>
        )}
      </Modal>
    </div>
  );
};

export default ClientPlansPage;
