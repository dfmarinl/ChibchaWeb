import { useState, useEffect } from 'react';
import { Plus, MessageCircle, AlertCircle, Clock, CheckCircle } from 'lucide-react';
import { Button, Badge, Input, Select, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockTickets, mockUsers } from '../../mock';
import { TICKET_STATUS, TICKET_PRIORITY } from '../../constants';

const ClientSupportPage: React.FC = () => {
  const [user, setUser] = useState<any>(null);
  const [showNewTicket, setShowNewTicket] = useState(false);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  const myTickets = mockTickets.filter(t => t.clientId === user?.id);

  const getStatusBadge = (status: string) => {
    const styles: Record<string, 'success' | 'warning' | 'error' | 'primary' | 'info'> = {
      open: 'info',
      in_progress: 'primary',
      pending: 'warning',
      resolved: 'success',
      closed: 'default',
    };
    return <Badge variant={styles[status] || 'default'}>{status.replace('_', ' ')}</Badge>;
  };

  const getPriorityBadge = (priority: string) => {
    const styles: Record<string, 'error' | 'warning' | 'success' | 'default'> = {
      low: 'default',
      medium: 'warning',
      high: 'error',
      urgent: 'error',
    };
    return <Badge variant={styles[priority] || 'default'}>{priority}</Badge>;
  };

  return (
    <div>
      <PageHeader
        title='Soporte Técnico'
        description='Gestiona tus tickets de soporte'
        action={
          <Button
            variant='primary'
            leftIcon={<Plus className='w-4 h-4' />}
            onClick={() => setShowNewTicket(!showNewTicket)}
          >
            Nuevo Ticket
          </Button>
        }
      />

      {/* New Ticket Form */}
      {showNewTicket && (
        <Card variant='bordered' className='mb-6'>
          <CardHeader>
            <CardTitle>Crear Nuevo Ticket</CardTitle>
          </CardHeader>
          <CardContent>
            <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
              <Input label='Asunto' placeholder='Describe brevemente el problema' />
              <Select label='Prioridad'>
                <option value={TICKET_PRIORITY.LOW}>Baja</option>
                <option value={TICKET_PRIORITY.MEDIUM}>Media</option>
                <option value={TICKET_PRIORITY.HIGH}>Alta</option>
                <option value={TICKET_PRIORITY.URGENT}>Urgente</option>
              </Select>
              <div className='md:col-span-2'>
                <label className='block text-sm font-medium text-secondary-700 mb-1.5'>
                  Descripción
                </label>
                <textarea
                  className='w-full px-4 py-2.5 border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500 text-sm'
                  rows={4}
                  placeholder='Describe detalladamente el problema o solicitud...'
                />
              </div>
            </div>
            <div className='flex justify-end gap-3 mt-4'>
              <Button variant='ghost' onClick={() => setShowNewTicket(false)}>
                Cancelar
              </Button>
              <Button variant='primary'>
                Enviar Ticket
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Stats */}
      <div className='grid grid-cols-1 md:grid-cols-4 gap-4 mb-6'>
        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-3'>
              <MessageCircle className='w-5 h-5 text-blue-600' />
              <span className='text-sm font-medium'>Total: {myTickets.length}</span>
            </div>
          </CardContent>
        </Card>
        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-3'>
              <AlertCircle className='w-5 h-5 text-primary-600' />
              <span className='text-sm font-medium'>
                Abiertos: {myTickets.filter(t => t.status === TICKET_STATUS.OPEN).length}
              </span>
            </div>
          </CardContent>
        </Card>
        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-3'>
              <Clock className='w-5 h-5 text-warning-600' />
              <span className='text-sm font-medium'>
                En Progreso: {myTickets.filter(t => t.status === TICKET_STATUS.IN_PROGRESS).length}
              </span>
            </div>
          </CardContent>
        </Card>
        <Card variant='bordered'>
          <CardContent>
            <div className='flex items-center gap-3'>
              <CheckCircle className='w-5 h-5 text-success-600' />
              <span className='text-sm font-medium'>
                Resueltos: {myTickets.filter(t => t.status === TICKET_STATUS.RESOLVED).length}
              </span>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Tickets Table */}
      <Card variant='bordered'>
        <CardHeader>
          <CardTitle>Mis Tickets</CardTitle>
        </CardHeader>
        <CardContent padding='none'>
          {myTickets.length === 0 ? (
            <EmptyState
              message='No tienes tickets de soporte'
              description='Crea un nuevo ticket si necesitas ayuda.'
              action={
                <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />}>
                  Crear Ticket
                </Button>
              }
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>ID</Th>
                  <Th>Asunto</Th>
                  <Th>Prioridad</Th>
                  <Th>Estado</Th>
                  <Th>Creado</Th>
                  <Th>Actualizado</Th>
                </Tr>
              </Thead>
              <Tbody>
                {myTickets.map((ticket) => (
                  <Tr key={ticket.id}>
                    <Td>
                      <span className='font-mono text-sm'>{ticket.id.slice(0, 8)}</span>
                    </Td>
                    <Td>
                      <div>
                        <p className='font-medium'>{ticket.subject}</p>
                        <p className='text-xs text-secondary-500 truncate max-w-xs'>
                          {ticket.description}
                        </p>
                      </div>
                    </Td>
                    <Td>{getPriorityBadge(ticket.priority)}</Td>
                    <Td>{getStatusBadge(ticket.status)}</Td>
                    <Td>{new Date(ticket.createdAt).toLocaleDateString('es-CO')}</Td>
                    <Td>{new Date(ticket.updatedAt).toLocaleDateString('es-CO')}</Td>
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

export default ClientSupportPage;
