import { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash2, Eye, Loader2 } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import Modal from '../../components/ui/Modal';
import ClientForm from './ClientForm';
import ClientDetail from './ClientDetail';
import { clientsApi, Client, CreateClientData, UpdateClientData } from '../../api/clients';
import { authApi } from '../../api/auth';

const ClientsPage: React.FC = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');

  const [modalMode, setModalMode] = useState<'create' | 'edit' | null>(null);
  const [selectedClient, setSelectedClient] = useState<Client | null>(null);
  const [viewTarget, setViewTarget] = useState<Client | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Client | null>(null);
  const [submitting, setSubmitting] = useState(false);

  const fetchClients = async () => {
    try {
      setError('');
      const data = await clientsApi.getAll();
      setClients(data);
    } catch {
      setError('Error al cargar clientes');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClients();
  }, []);

  const filtered = clients.filter(
    (c) =>
      c.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
      c.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreate = async (data: CreateClientData | UpdateClientData) => {
    setSubmitting(true);
    try {
      await authApi.registerCliente(data as CreateClientData);
      setModalMode(null);
      await fetchClients();
    } catch (err: any) {
      const msg = err?.response?.data?.error || err?.response?.data?.mensaje;
      setError(msg || 'Error al crear cliente');
    } finally {
      setSubmitting(false);
    }
  };

  const handleUpdate = async (data: CreateClientData | UpdateClientData) => {
    if (!selectedClient) return;
    setSubmitting(true);
    try {
      await clientsApi.update(selectedClient.id, data);
      setModalMode(null);
      setSelectedClient(null);
      await fetchClients();
    } catch (err: any) {
      const msg = err?.response?.data?.error || err?.response?.data?.mensaje;
      setError(msg || 'Error al actualizar cliente');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    try {
      await clientsApi.delete(deleteTarget.id);
      setDeleteTarget(null);
      await fetchClients();
    } catch (err: any) {
      const msg = err?.response?.data?.error || err?.response?.data?.mensaje;
      setError(msg || 'Error al eliminar cliente');
    }
  };

  const openEdit = (client: Client) => {
    setSelectedClient(client);
    setModalMode('edit');
  };

  const closeModal = () => {
    setModalMode(null);
    setSelectedClient(null);
  };

  const getStatusBadge = () => (
    <Badge variant='success' dot>Activo</Badge>
  );

  if (loading) {
    return (
      <div className='flex items-center justify-center h-64'>
        <Loader2 className='w-8 h-8 animate-spin text-primary-600' />
      </div>
    );
  }

  return (
    <div>
      <PageHeader
        title='Gestión de Clientes'
        description='Administra los clientes registrados en la plataforma'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />} onClick={() => setModalMode('create')}>
            Nuevo Cliente
          </Button>
        }
      />

      {error && (
        <div className='mb-4 p-3 rounded-lg bg-error-50 border border-error-200'>
          <p className='text-sm text-error-700'>{error}</p>
        </div>
      )}

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
        <CardContent className='p-0'>
          {filtered.length === 0 ? (
            <EmptyState
              message='No hay clientes registrados'
              description='No hay clientes que coincidan con tu búsqueda.'
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Cliente</Th>
                  <Th>Email</Th>
                  <Th>Documento</Th>
                  <Th>Teléfono</Th>
                  <Th>Región</Th>
                  <Th>Estado</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {filtered.map((client) => (
                  <Tr key={client.id}>
                    <Td>
                      <div className='flex items-center gap-3'>
                        <div className='w-10 h-10 bg-success-100 rounded-full flex items-center justify-center'>
                          <span className='text-sm font-medium text-success-700'>
                            {client.nombre.charAt(0)}
                          </span>
                        </div>
                        <span className='font-medium'>{client.nombre}</span>
                      </div>
                    </Td>
                    <Td>{client.email}</Td>
                    <Td>{client.documentoIdentidad}</Td>
                    <Td>{client.telefono || '-'}</Td>
                    <Td>{client.region || '-'}</Td>
                    <Td>{getStatusBadge()}</Td>
                    <Td>
                      <div className='flex items-center gap-1'>
                        <Button variant='ghost' size='sm' onClick={() => setViewTarget(client)}>
                          <Eye className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => openEdit(client)}>
                          <Edit className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => setDeleteTarget(client)}>
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

      {/* Create Modal */}
      <Modal
        open={modalMode === 'create'}
        onClose={closeModal}
        title='Nuevo Cliente'
        footer={
          <>
            <Button variant='outline' onClick={closeModal}>Cancelar</Button>
            <Button
              variant='primary'
              isLoading={submitting}
              onClick={() => {
                const form = document.querySelector('#client-form') as HTMLFormElement;
                form?.requestSubmit();
              }}
            >
              Crear
            </Button>
          </>
        }
      >
        <ClientForm id='client-form' onSubmit={handleCreate} isLoading={submitting} />
      </Modal>

      {/* Edit Modal */}
      <Modal
        open={modalMode === 'edit'}
        onClose={closeModal}
        title='Editar Cliente'
        footer={
          <>
            <Button variant='outline' onClick={closeModal}>Cancelar</Button>
            <Button
              variant='primary'
              isLoading={submitting}
              onClick={() => {
                const form = document.querySelector('#client-form') as HTMLFormElement;
                form?.requestSubmit();
              }}
            >
              Guardar
            </Button>
          </>
        }
      >
        {selectedClient && (
          <ClientForm id='client-form' initialData={selectedClient} onSubmit={handleUpdate} isLoading={submitting} />
        )}
      </Modal>

      {/* View Details */}
      <Modal
        open={viewTarget !== null}
        onClose={() => setViewTarget(null)}
        title='Detalles del Cliente'
        footer={
          <Button variant='outline' onClick={() => setViewTarget(null)}>Cerrar</Button>
        }
      >
        {viewTarget && <ClientDetail client={viewTarget} />}
      </Modal>

      {/* Delete Confirmation */}
      <Modal
        open={deleteTarget !== null}
        onClose={() => setDeleteTarget(null)}
        title='Eliminar Cliente'
        footer={
          <>
            <Button variant='outline' onClick={() => setDeleteTarget(null)}>Cancelar</Button>
            <Button variant='danger' onClick={handleDelete}>Eliminar</Button>
          </>
        }
      >
        {deleteTarget && (
          <p className='text-secondary-700'>
            ¿Estás seguro de eliminar a <span className='font-semibold'>{deleteTarget.nombre}</span>?
            Esta acción no se puede deshacer.
          </p>
        )}
      </Modal>
    </div>
  );
};

export default ClientsPage;
