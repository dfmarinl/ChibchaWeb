import { useState } from 'react';
import { Plus, Search, Edit, Trash2, Globe } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockDomains, mockUsers } from '../../mock';
import { DOMAIN_STATUS } from '../../constants';

const DomainsPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const domains = mockDomains.filter(
    (domain) =>
      domain.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const getStatusBadge = (status: string) => {
    switch (status) {
      case DOMAIN_STATUS.ACTIVE:
        return <Badge variant='success' dot>Activo</Badge>;
      case DOMAIN_STATUS.EXPIRED:
        return <Badge variant='error' dot>Vencido</Badge>;
      case DOMAIN_STATUS.PENDING:
        return <Badge variant='warning' dot>Pendiente</Badge>;
      default:
        return <Badge variant='default'>-</Badge>;
    }
  };

  const isExpiringSoon = (expiryDate: string) => {
    const now = new Date();
    const expiry = new Date(expiryDate);
    const daysUntilExpiry = Math.ceil((expiry.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
    return daysUntilExpiry <= 30 && daysUntilExpiry > 0;
  };

  return (
    <div>
      <PageHeader
        title='Gestión de Dominios'
        description='Administra todos los dominios registrados'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
            Nuevo Dominio
          </Button>
        }
      />

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Dominios Registrados</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar dominio...'
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {domains.length === 0 ? (
            <EmptyState
              message='No hay dominios registrados'
              description='No hay dominios que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Dominio</Th>
                  <Th>Cliente</Th>
                  <Th>Estado</Th>
                  <Th>Auto-Renovación</Th>
                  <Th>Fecha Expiración</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {domains.map((domain) => {
                  const client = mockUsers.find((u) => u.id === domain.clientId);
                  const expiringSoon = isExpiringSoon(domain.expiryDate);

                  return (
                    <Tr key={domain.id}>
                      <Td>
                        <div className='flex items-center gap-3'>
                          <Globe className='w-5 h-5 text-primary-600' />
                          <div>
                            <p className='font-medium'>{domain.name}</p>
                            {expiringSoon && (
                              <p className='text-xs text-warning-600'>Vence pronto</p>
                            )}
                          </div>
                        </div>
                      </Td>
                      <Td>{client?.name || '-'}</Td>
                      <Td>{getStatusBadge(domain.status)}</Td>
                      <Td>
                        <Badge variant={domain.autoRenew ? 'success' : 'default'}>
                          {domain.autoRenew ? 'Activo' : 'Inactivo'}
                        </Badge>
                      </Td>
                      <Td>
                        <span className={expiringSoon ? 'text-warning-600' : ''}>
                          {new Date(domain.expiryDate).toLocaleDateString('es-CO')}
                        </span>
                      </Td>
                      <Td>
                        <div className='flex items-center gap-2'>
                          <Button variant='ghost' size='sm'>
                            <Edit className='w-4 h-4' />
                          </Button>
                          <Button variant='ghost' size='sm'>
                            <Trash2 className='w-4 h-4 text-error-600' />
                          </Button>
                        </div>
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

export default DomainsPage;
