import { useState } from 'react';
import { Search, Filter, Eye, UserPlus, CheckCircle, AlertCircle, Clock, MessageCircle } from 'lucide-react';
import { Button, Badge, Select, Input, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import { mockTickets, mockUsers } from '../../mock';
import { TICKET_STATUS, TICKET_PRIORITY } from '../../constants';

const EmployeeTicketsPage: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [filterPriority, setFilterPriority] = useState('all');
  const [selectedTicket, setSelectedTicket] = useState<any>(null);

  const filteredTickets = mockTickets.filter((ticket) => {
    const matchesSearch =
      ticket.subject.toLowerCase().includes(searchTerm.toLowerCase()) ||
      ticket.description.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'all' || ticket.status === filterStatus;
    const matchesPriority = filterPriority === 'all' || ticket.priority === filterPriority;
    return matchesSearch && matchesStatus && matchesPriority;
  });

  const getPriorityBadge = (priority: string) => {
    const styles: Record<string, 'error' | 'warning' | 'success' | 'default'> = {
      low: 'default',
      medium: 'warning',
      high: 'error',
      urgent: 'error',
    };
    return <Badge variant={styles[priority] || 'default'} dot>{priority}</Badge>;
  };

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

  return (
    <div>
      <PageHeader
        title='Gestión de Tickets'
        description='Administra y resuelve tickets de soporte técnico'
      />

      {/* Filters */}
      <Card variant='bordered' className='mb-6'>
        <CardContent>
          <div className='flex flex-wrap items-center gap-4'>
            <div className='relative flex-1 min-w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar tickets...'
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
            <Select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className='w-40'
            >
              <option value='all'>Todos los estados</option>
              <option value={TICKET_STATUS.OPEN}>Abiertos</option>
              <option value={TICKET_STATUS.IN_PROGRESS}>En Progreso</option>
              <option value={TICKET_STATUS.PENDING}>Pendientes</option>
              <option value={TICKET_STATUS.RESOLVED}>Resueltos</option>
            </Select>
            <Select
              value={filterPriority}
              onChange={(e) => setFilterPriority(e.target.value)}
              className='w-40'
            >
              <option value='all'>Todas las prioridades</option>
              <option value={TICKET_PRIORITY.URGENT}>Urgente</option>
              <option value={TICKET_PRIORITY.HIGH}>Alta</option>
              <option value={TICKET_PRIORITY.MEDIUM}>Media</option>
              <option value={TICKET_PRIORITY.LOW}>Baja</option>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Stats Cards */}
      <div className='grid grid-cols-2 md:grid-cols-5 gap-4 mb-6'>
        <Card variant='bordered' className='p-4'>
          <div className='flex items-center gap-2'>
            <MessageCircle className='w-4 h-4 text-secondary-600' />
            <span className='text-sm text-secondary-600'>Total</span>
          </div>
          <p className='text-2xl font-bold mt-1'>{mockTickets.length}</p>
        </Card>
        <Card variant='bordered' className='p-4 border-info-200 bg-info-50/30'>
          <div className='flex items-center gap-2'>
            <AlertCircle className='w-4 h-4 text-blue-600' />
            <span className='text-sm text-secondary-600'>Abiertos</span>
          </div>
          <p className='text-2xl font-bold mt-1'>
            {mockTickets.filter(t => t.status === TICKET_STATUS.OPEN).length}
          </p>
        </Card>
        <Card variant='bordered' className='p-4 border-primary-200 bg-primary-50/30'>
          <div className='flex items-center gap-2'>
            <Clock className='w-4 h-4 text-primary-600' />
            <span className='text-sm text-secondary-600'>En Progreso</span>
          </div>
          <p className='text-2xl font-bold mt-1'>
            {mockTickets.filter(t => t.status === TICKET_STATUS.IN_PROGRESS).length}
          </p>
        </Card>
        <Card variant='bordered' className='p-4 border-success-200 bg-success-50/30'>
          <div className='flex items-center gap-2'>
            <CheckCircle className='w-4 h-4 text-success-600' />
            <span className='text-sm text-secondary-600'>Resueltos</span>
          </div>
          <p className='text-2xl font-bold mt-1'>
            {mockTickets.filter(t => t.status === TICKET_STATUS.RESOLVED).length}
          </p>
        </Card>
        <Card variant='bordered' className='p-4 border-error-200 bg-error-50/30'>
          <div className='flex items-center gap-2'>
            <AlertCircle className='w-4 h-4 text-error-600' />
            <span className='text-sm text-secondary-600'>Urgentes</span>
          </div>
          <p className='text-2xl font-bold mt-1'>
            {mockTickets.filter(t => t.priority === TICKET_PRIORITY.URGENT).length}
          </p>
        </Card>
      </div>

      {/* Tickets Table */}
      <Card variant='bordered'>
        <CardContent padding='none'>
          {filteredTickets.length === 0 ? (
            <EmptyState
              message='No hay tickets registrados'
              description='No hay tickets que coincidan con los filtros seleccionados.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>ID</Th>
                  <Th>Cliente</Th>
                  <Th>Asunto</Th>
                  <Th>Prioridad</Th>
                  <Th>Estado</Th>
                  <Th>Asignado</Th>
                  <Th>Creado</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {filteredTickets.map((ticket) => {
                  const client = mockUsers.find(u => u.id === ticket.clientId);
                  const assignedTo = mockUsers.find(u => u.id === ticket.assignedTo);

                  return (
                    <Tr key={ticket.id}>
                      <Td>
                        <span className='font-mono text-xs'>{ticket.id.slice(0, 8)}</span>
                      </Td>
                      <Td>
                        <div>
                          <p className='font-medium text-sm'>{client?.name || '-'}</p>
                          <p className='text-xs text-secondary-500'>{client?.company || '-'}</p>
                        </div>
                      </Td>
                      <Td>
                        <div>
                          <p className='font-medium text-sm'>{ticket.subject}</p>
                          <p className='text-xs text-secondary-500 truncate max-w-xs'>
                            {ticket.description}
                          </p>
                        </div>
                      </Td>
                      <Td>{getPriorityBadge(ticket.priority)}</Td>
                      <Td>{getStatusBadge(ticket.status)}</Td>
                      <Td>
                        {assignedTo ? (
                          <div className='flex items-center gap-2'>
                            <div className='w-6 h-6 bg-warning-100 rounded-full flex items-center justify-center'>
                              <span className='text-xs font-medium text-warning-700'>
                                {assignedTo.name.charAt(0)}
                              </span>
                            </div>
                            <span className='text-xs'>{assignedTo.name.split(' ')[0]}</span>
                          </div>
                        ) : (
                          <span className='text-xs text-secondary-400'>Sin asignar</span>
                        )}
                      </Td>
                      <Td>
                        <span className='text-sm'>{new Date(ticket.createdAt).toLocaleDateString('es-CO')}</span>
                      </Td>
                      <Td>
                        <div className='flex items-center gap-1'>
                          <Button variant='ghost' size='sm'>
                            <Eye className='w-4 h-4' />
                          </Button>
                          <Button variant='ghost' size='sm'>
                            <UserPlus className='w-4 h-4' />
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

export default EmployeeTicketsPage;
