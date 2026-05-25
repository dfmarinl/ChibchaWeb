import { useState, useEffect } from 'react';
import { Plus, Globe, RefreshCw, ExternalLink } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockDomains } from '../../mock';
import { DOMAIN_STATUS } from '../../constants';

const ClientDomainsPage: React.FC = () => {
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const myDomains = mockDomains.filter(d => d.clientId === user?.id);

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

  const getDaysUntilExpiry = (expiryDate: string) => {
    return Math.ceil((new Date(expiryDate).getTime() - Date.now()) / (1000 * 60 * 60 * 24));
  };

  return (
    <div>
      <PageHeader
        title='Mis Dominios'
        description='Administra los dominios de tu cuenta'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
            Registrar Dominio
          </Button>
        }
      />

      {/* Stats */}
      <div className='grid grid-cols-1 md:grid-cols-3 gap-6 mb-8'>
        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-4'>
              <div className='p-3 rounded-lg bg-success-100'>
                <Globe className='w-6 h-6 text-success-600' />
              </div>
              <div>
                <p className='text-sm text-secondary-600'>Dominios Activos</p>
                <p className='text-2xl font-bold text-secondary-900'>
                  {myDomains.filter(d => d.status === DOMAIN_STATUS.ACTIVE).length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-4'>
              <div className='p-3 rounded-lg bg-warning-100'>
                <RefreshCw className='w-6 h-6 text-warning-600' />
              </div>
              <div>
                <p className='text-sm text-secondary-600'>Por Vencer (30 días)</p>
                <p className='text-2xl font-bold text-secondary-900'>
                  {myDomains.filter(d => getDaysUntilExpiry(d.expiryDate) <= 30 && getDaysUntilExpiry(d.expiryDate) > 0).length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-4'>
              <div className='p-3 rounded-lg bg-error-100'>
                <Globe className='w-6 h-6 text-error-600' />
              </div>
              <div>
                <p className='text-sm text-secondary-600'>Dominios Vencidos</p>
                <p className='text-2xl font-bold text-secondary-900'>
                  {myDomains.filter(d => d.status === DOMAIN_STATUS.EXPIRED).length}
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Domains Table */}
      <Card variant='bordered'>
        <CardHeader>
          <CardTitle>Lista de Dominios</CardTitle>
        </CardHeader>
        <CardContent padding='none'>
          {myDomains.length === 0 ? (
            <EmptyState
              message='No tienes dominios registrados'
              description='Registra tu primer dominio para comenzar.'
              action={
                <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
                  Registrar Dominio
                </Button>
              }
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Dominio</Th>
                  <Th>Estado</Th>
                  <Th>Auto-Renovación</Th>
                  <Th>Fecha Registro</Th>
                  <Th>Fecha Expiración</Th>
                  <Th>Días Restantes</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {myDomains.map((domain) => {
                  const days = getDaysUntilExpiry(domain.expiryDate);
                  const isExpiringSoon = days <= 30 && days > 0;

                  return (
                    <Tr key={domain.id}>
                      <Td>
                        <div className='flex items-center gap-3'>
                          <Globe className='w-5 h-5 text-primary-600' />
                          <span className='font-medium'>{domain.name}</span>
                        </div>
                      </Td>
                      <Td>{getStatusBadge(domain.status)}</Td>
                      <Td>
                        <Badge variant={domain.autoRenew ? 'success' : 'default'}>
                          {domain.autoRenew ? 'Activo' : 'Inactivo'}
                        </Badge>
                      </Td>
                      <Td>{new Date(domain.registrationDate).toLocaleDateString('es-CO')}</Td>
                      <Td>{new Date(domain.expiryDate).toLocaleDateString('es-CO')}</Td>
                      <Td>
                        <span className={`font-medium ${days <= 0 ? 'text-error-600' : isExpiringSoon ? 'text-warning-600' : 'text-success-600'}`}>
                          {days > 0 ? `${days} días` : 'Vencido'}
                        </span>
                      </Td>
                      <Td>
                        <Button variant='ghost' size='sm'>
                          <ExternalLink className='w-4 h-4' />
                        </Button>
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

export default ClientDomainsPage;
