import { useState } from 'react';
import { Check } from 'lucide-react';
import { Button, Badge, Card, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { mockPlans } from '../../mock';

const ClientPlansPage: React.FC = () => {
  const [selectedPlan, setSelectedPlan] = useState<string | null>(null);

  const handleSelectPlan = (planId: string) => {
    setSelectedPlan(planId);
  };

  return (
    <div>
      <PageHeader
        title='Planes de Hosting'
        description='Elige el plan perfecto para tu sitio web'
      />

      {/* Plans Grid */}
      <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6'>
        {mockPlans.map((plan, index) => {
          const isPopular = index === 1;
          const isSelected = selectedPlan === plan.id;

          return (
            <Card
              key={plan.id}
              variant='bordered'
              className={`flex flex-col relative ${
                isPopular ? 'ring-2 ring-primary-600' : ''
              } ${isSelected ? 'ring-2 ring-success-600' : ''}`}
            >
              {isPopular && (
                <div className='absolute -top-3 left-1/2 -translate-x-1/2'>
                  <Badge variant='primary' size='lg'>
                    Más Popular
                  </Badge>
                </div>
              )}

              <CardContent className='flex flex-col flex-1 pt-8'>
                <div className='mb-4'>
                  <h3 className='text-xl font-bold text-secondary-900'>{plan.name}</h3>
                  <p className='text-sm text-secondary-600 mt-1'>{plan.description}</p>
                </div>

                <div className='mb-6'>
                  <div className='flex items-baseline gap-1'>
                    <span className='text-4xl font-bold text-primary-600'>
                      ${(plan.price / 100).toLocaleString('es-CO')}
                    </span>
                    <span className='text-secondary-500'>/mes</span>
                  </div>
                </div>

                <div className='space-y-3 flex-1'>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>{plan.diskSpace} GB</span>
                    <span className='text-secondary-600'>SSD Storage</span>
                  </div>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>{plan.bandwidth}</span>
                    <span className='text-secondary-600'>Bandwidth</span>
                  </div>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>
                      {plan.emailAccounts === -1 ? 'Unlimited' : plan.emailAccounts}
                    </span>
                    <span className='text-secondary-600'>Email Accounts</span>
                  </div>
                  <div className='flex items-center gap-2 text-sm'>
                    <span className='font-semibold text-secondary-900'>
                      {plan.databases === -1 ? 'Unlimited' : plan.databases}
                    </span>
                    <span className='text-secondary-600'>Databases</span>
                  </div>

                  <div className='pt-3 border-t border-secondary-200'>
                    <ul className='space-y-2'>
                      {plan.features.map((feature, idx) => (
                        <li key={idx} className='flex items-start gap-2 text-sm'>
                          <Check className='w-4 h-4 text-success-600 mt-0.5 flex-shrink-0' />
                          <span className='text-secondary-700'>{feature}</span>
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
              </CardContent>
            </Card>
          );
        })}
      </div>
    </div>
  );
};

export default ClientPlansPage;
