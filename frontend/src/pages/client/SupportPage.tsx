import { Construction } from 'lucide-react';
import { PageHeader } from '../../components/common';
import { Card } from '../../components/ui';

const ClientSupportPage: React.FC = () => {
  return (
    <div>
      <PageHeader
        title='Soporte T&eacute;cnico'
        description='Gestiona tus tickets de soporte'
      />
      <Card>
        <div className='flex flex-col items-center justify-center py-16 text-center'>
          <Construction className='w-16 h-16 text-secondary-300 mb-4' />
          <h2 className='text-xl font-semibold text-secondary-700 mb-2'>
            M&oacute;dulo en Desarrollo
          </h2>
          <p className='text-secondary-500 max-w-md'>
            Esta secci&oacute;n est&aacute; siendo implementada. Pronto podr&aacute;s gestionar tus tickets de soporte desde aqu&iacute;.
          </p>
        </div>
      </Card>
    </div>
  );
};

export default ClientSupportPage;
