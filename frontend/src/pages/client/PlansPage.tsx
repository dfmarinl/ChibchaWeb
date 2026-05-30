import { useState, useEffect, useCallback } from 'react';
import { Check } from 'lucide-react';
import { Button, Badge } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { plansApi } from '../../api/plans';
import { PlanHosting } from '../../types/plan';

const tierColors: Record<string, string> = {
  PLATINO: 'ring-purple-600',
  ORO: 'ring-yellow-600',
  PLATA: 'ring-gray-600',
};

const tierBadges: Record<string, string> = {
  PLATINO: 'primary',
  ORO: 'warning',
  PLATA: 'default',
};

const ClientPlansPage: React.FC = () => {
  const [plans, setPlans] = useState<PlanHosting[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedPlan, setSelectedPlan] = useState<number | null>(null);

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
                      ${plan.precioMensual.toLocaleString('es-CO')}
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

                <div className='mt-6'>
                  <Button
                    variant={isSelected ? 'secondary' : 'primary'}
                    size='lg'
                    className='w-full'
                    onClick={() => handleSelectPlan(plan.id)}
                  >
                    {isSelected ? 'Plan Seleccionado' : 'Seleccionar Plan'}
                  </Button>
                </div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default ClientPlansPage;
