import { useState } from 'react';
import { Plus, Search, Edit, Trash2, MoreVertical } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockUsers } from '../../mock';
import { USER_ROLES } from '../../constants';

const ClientsPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const clients = mockUsers.filter(
    (u) =>
      u.role === USER_ROLES.CLIENT &&
      (u.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        u.email.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  const getStatusBadge = (status: string) => {
    return status === 'active' ? (
      <Badge variant='success' dot>Activo</Badge>
    ) : (
      <Badge variant='error' dot>Inactivo</Badge>
    );
  };

  return (
    <div>
      <PageHeader
        title='Gestión de Clientes'
        description='Administra los clientes registrados en la plataforma'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
            Nuevo Cliente
          </Button>
        }
      />

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Lista de Clientes</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar cliente...'
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {clients.length === 0 ? (
            <EmptyState
              message='No se encontraron clientes'
              description='No hay clientes que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Cliente</Th>
                  <Th>Email</Th>
                  <Th>Empresa</Th>
                  <Th>Teléfono</Th>
                  <Th>Estado</Th>
                  <Th>Fecha Registro</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {clients.map((client) => (
                  <Tr key={client.id}>
                    <Td>
                      <div className='flex items-center gap-3'>
                        <div className='w-10 h-10 bg-success-100 rounded-full flex items-center justify-center'>
                          <span className='text-sm font-medium text-success-700'>
                            {client.name.charAt(0)}
                          </span>
                        </div>
                        <span className='font-medium'>{client.name}</span>
                      </div>
                    </Td>
                    <Td>{client.email}</Td>
                    <Td>{client.company || '-'}</Td>
                    <Td>{client.phone || '-'}</Td>
                    <Td>{getStatusBadge(client.status)}</Td>
                    <Td>{new Date(client.createdAt).toLocaleDateString('es-CO')}</Td>
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
                ))}
              </Tbody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default ClientsPage;
