import { useState } from 'react';
import { Plus, Search, Edit, Trash2 } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockUsers } from '../../mock';
import { USER_ROLES } from '../../constants';

const EmployeesPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');

  const employees = mockUsers.filter(
    (u) =>
      u.role === USER_ROLES.EMPLOYEE &&
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
        title='Gestión de Empleados'
        description='Administra el personal de soporte técnico'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
            Nuevo Empleado
          </Button>
        }
      />

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Lista de Empleados</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar empleado...'
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {employees.length === 0 ? (
            <EmptyState
              message='No hay empleados registrados'
              description='No hay empleados que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Empleado</Th>
                  <Th>Email</Th>
                  <Th>Teléfono</Th>
                  <Th>Estado</Th>
                  <Th>Fecha Contratación</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {employees.map((employee) => (
                  <Tr key={employee.id}>
                    <Td>
                      <div className='flex items-center gap-3'>
                        <div className='w-10 h-10 bg-warning-100 rounded-full flex items-center justify-center'>
                          <span className='text-sm font-medium text-warning-700'>
                            {employee.name.charAt(0)}
                          </span>
                        </div>
                        <span className='font-medium'>{employee.name}</span>
                      </div>
                    </Td>
                    <Td>{employee.email}</Td>
                    <Td>{employee.phone || '-'}</Td>
                    <Td>{getStatusBadge(employee.status)}</Td>
                    <Td>{new Date(employee.createdAt).toLocaleDateString('es-CO')}</Td>
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

export default EmployeesPage;
