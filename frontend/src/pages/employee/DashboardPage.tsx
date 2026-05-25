import { Ticket, Clock, AlertCircle, CheckCircle, TrendingUp } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent, Badge } from '../../components/ui';
import { StatsCard, PageHeader } from '../../components/common';
import { mockTickets, mockUsers, mockTicketStats } from '../../mock';
import { TICKET_STATUS, TICKET_PRIORITY, USER_ROLES } from '../../constants';

const EmployeeDashboard: React.FC = () => {
  const employees = mockUsers.filter(u => u.role === USER_ROLES.EMPLOYEE);

  const getPriorityBadge = (priority: string) => {
    const styles: Record<string, 'error' | 'warning' | 'success' | 'default'> = {
      low: 'default',
      medium: 'warning',
      high: 'error',
      urgent: 'error',
    };
    return <Badge variant={styles[priority] || 'default'}>{priority}</Badge>;
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

  const urgentTickets = mockTickets.filter(t => t.priority === TICKET_PRIORITY.URGENT);
  const highPriorityTickets = mockTickets.filter(t => t.priority === TICKET_PRIORITY.HIGH && t.status !== TICKET_STATUS.RESOLVED);

  return (
    <div>
      <PageHeader
        title='Panel de Soporte'
        description='Gestiona los tickets de soporte técnico'
      />

      {/* Stats */}
      <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8'>
        <StatsCard
          title='Total Tickets'
          value={mockTicketStats.total}
          icon={Ticket}
          iconColor='text-primary-600'
        />
        <StatsCard
          title='Abiertos'
          value={mockTicketStats.open}
          icon={AlertCircle}
          iconColor='text-blue-600'
        />
        <StatsCard
          title='En Progreso'
          value={mockTicketStats.inProgress}
          icon={TrendingUp}
          iconColor='text-warning-600'
        />
        <StatsCard
          title='Resueltos'
          value={mockTicketStats.resolved}
          icon={CheckCircle}
          iconColor='text-success-600'
        />
      </div>

      {/* Urgent Tickets */}
      {urgentTickets.length > 0 && (
        <Card variant='bordered' className='mb-6 border-error-200 bg-error-50/30'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2 text-error-700'>
              <AlertCircle className='w-5 h-5' />
              Tickets Urgentes ({urgentTickets.length})
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-3'>
              {urgentTickets.map((ticket) => {
                const client = mockUsers.find(u => u.id === ticket.clientId);
                return (
                  <div key={ticket.id} className='p-4 bg-white rounded-lg border border-error-200'>
                    <div className='flex items-start justify-between'>
                      <div className='flex-1'>
                        <div className='flex items-center gap-2 mb-1'>
                          <span className='font-semibold'>{ticket.subject}</span>
                          {getPriorityBadge(ticket.priority)}
                        </div>
                        <p className='text-sm text-secondary-600 mb-2'>
                          Cliente: {client?.name || 'Desconocido'}
                        </p>
                        <p className='text-sm text-secondary-500 line-clamp-2'>
                          {ticket.description}
                        </p>
                      </div>
                      <div className='text-right ml-4'>
                        {getStatusBadge(ticket.status)}
                        <p className='text-xs text-secondary-500 mt-1'>
                          {new Date(ticket.createdAt).toLocaleDateString('es-CO')}
                        </p>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Recent and High Priority Tickets */}
      <div className='grid grid-cols-1 lg:grid-cols-2 gap-6'>
        {/* Recent Tickets */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <Clock className='w-5 h-5 text-primary-600' />
              Tickets Recientes
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-3'>
              {mockTickets.slice(0, 5).map((ticket) => {
                const client = mockUsers.find(u => u.id === ticket.clientId);
                return (
                  <div key={ticket.id} className='py-3 border-b border-secondary-100 last:border-0'>
                    <div className='flex items-start justify-between'>
                      <div className='flex-1'>
                        <p className='font-medium text-sm mb-1'>{ticket.subject}</p>
                        <p className='text-xs text-secondary-500'>
                          {client?.name} - {new Date(ticket.createdAt).toLocaleDateString('es-CO')}
                        </p>
                      </div>
                      <div className='text-right ml-3'>
                        {getStatusBadge(ticket.status)}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>

        {/* Team Stats */}
        <Card variant='bordered'>
          <CardHeader>
            <CardTitle className='flex items-center gap-2'>
              <TrendingUp className='w-5 h-5 text-success-600' />
              Actividad del Equipo
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className='space-y-4'>
              {employees.map((employee) => {
                const assignedTickets = mockTickets.filter(t => t.assignedTo === employee.id);
                const inProgress = assignedTickets.filter(t => t.status === TICKET_STATUS.IN_PROGRESS).length;
                const resolved = assignedTickets.filter(t => t.status === TICKET_STATUS.RESOLVED).length;

                return (
                  <div key={employee.id} className='flex items-center justify-between py-3 border-b border-secondary-100 last:border-0'>
                    <div className='flex items-center gap-3'>
                      <div className='w-10 h-10 bg-warning-100 rounded-full flex items-center justify-center'>
                        <span className='text-sm font-medium text-warning-700'>
                          {employee.name.charAt(0)}
                        </span>
                      </div>
                      <div>
                        <p className='text-sm font-medium'>{employee.name}</p>
                        <p className='text-xs text-secondary-500'>
                          {assignedTickets.length} tickets asignados
                        </p>
                      </div>
                    </div>
                    <div className='flex items-center gap-4 text-sm'>
                      <div className='text-center'>
                        <p className='font-semibold text-warning-600'>{inProgress}</p>
                        <p className='text-xs text-secondary-500'>En progreso</p>
                      </div>
                      <div className='text-center'>
                        <p className='font-semibold text-success-600'>{resolved}</p>
                        <p className='text-xs text-secondary-500'>Resueltos</p>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default EmployeeDashboard;
