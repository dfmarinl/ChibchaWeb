import { useState } from 'react';
import { Plus, Search, Edit, Check } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { mockPlans } from '../../mock';
import { PLAN_STATUS } from '../../constants';

const PlansPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const plans = mockPlans.filter(
    (plan) =>
      plan.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      plan.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    return status === PLAN_STATUS.ACTIVE ? (
      <Badge variant='success' dot>Activo</Badge>
    ) : (
      <Badge variant='error' dot>Inactivo</Badge>
    );
  };

  return (
    <div>
      <PageHeader
        title='Gestión de Planes de Hosting'
        description='Administra los planes de hosting disponibles'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
            Nuevo Plan
          </Button>
        }
      />

      {/* Search */}
      <div className='mb-6'>
        <div className='relative max-w-md'>
          <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
          <input
            type='text'
            placeholder='Buscar planes...'
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
          />
        </div>
      </div>

      {/* Plans Grid */}
      <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6'>
        {plans.map((plan) => (
          <Card key={plan.id} variant='bordered' className='flex flex-col'>
            <CardContent>
              <div className='mb-4'>
                <div className='flex items-start justify-between mb-2'>
                  <h3 className='text-xl font-bold text-secondary-900'>{plan.name}</h3>
                  {getStatusBadge(plan.status)}
                </div>
                <p className='text-sm text-secondary-600'>{plan.description}</p>
              </div>

              <div className='mb-6'>
                <div className='flex items-baseline gap-1'>
                  <span className='text-3xl font-bold text-primary-600'>
                    ${(plan.price / 100).toLocaleString('es-CO')}
                  </span>
                  <span className='text-sm text-secondary-500'>/mes</span>
                </div>
              </div>

              <div className='space-y-3 flex-1'>
                <div className='flex items-center gap-2 text-sm'>
                  <span className='font-medium text-secondary-700'>{plan.diskSpace} GB</span>
                  <span className='text-secondary-500'>Almacenamiento SSD</span>
                </div>
                <div className='flex items-center gap-2 text-sm'>
                  <span className='font-medium text-secondary-700'>{plan.bandwidth}</span>
                  <span className='text-secondary-500'>Ancho de banda</span>
                </div>
                <div className='flex items-center gap-2 text-sm'>
                  <span className='font-medium text-secondary-700'>
                    {plan.emailAccounts === -1 ? 'Ilimitados' : plan.emailAccounts}
                  </span>
                  <span className='text-secondary-500'>Cuentas de email</span>
                </div>
                <div className='flex items-center gap-2 text-sm'>
                  <span className='font-medium text-secondary-700'>
                    {plan.databases === -1 ? 'Ilimitadas' : plan.databases}
                  </span>
                  <span className='text-secondary-500'>Bases de datos</span>
                </div>

                <div className='pt-3 border-t border-secondary-200 mt-4'>
                  <p className='text-xs font-medium text-secondary-600 mb-2'>Características:</p>
                  <ul className='space-y-1'>
                    {plan.features.slice(0, 5).map((feature, idx) => (
                      <li key={idx} className='flex items-start gap-2 text-xs text-secondary-600'>
                        <Check className='w-3 h-3 text-success-600 mt-0.5 flex-shrink-0' />
                        <span>{feature}</span>
                      </li>
                    ))}
                    {plan.features.length > 5 && (
                      <li className='text-xs text-secondary-500 ml-5'>
                        +{plan.features.length - 5} más...
                      </li>
                    )}
                  </ul>
                </div>
              </div>

              <div className='mt-6 pt-4 border-t border-secondary-200'>
                <Button variant='outline' size='lg' className='w-full'>
                  <Edit className='w-4 h-4 mr-2' />
                  Editar Plan
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default PlansPage;
