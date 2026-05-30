import { useState, useEffect, useCallback } from 'react';
import { Plus, Search, Eye, Edit, Trash2 } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import Input from '../../components/ui/Input';
import Modal from '../../components/ui/Modal';
import { distribuidoresApi, Distribuidor, CreateDistribuidorData, UpdateDistribuidorData } from '../../api/distribuidores';

type ModalMode = 'create' | 'edit';

const levelBadgeVariant: Record<string, 'default' | 'warning' | 'primary' | 'info' | 'success'> = {
  BASICO: 'default',
  PLATA: 'warning',
  ORO: 'primary',
  DIAMANTE: 'info',
  PREMIUM: 'success',
};

const DistribuidoresPage: React.FC = () => {
  const [distribuidores, setDistribuidores] = useState<Distribuidor[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<ModalMode>('create');
  const [editId, setEditId] = useState<number | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});
  const [detailDistribuidor, setDetailDistribuidor] = useState<Distribuidor | null>(null);

  const [form, setForm] = useState<CreateDistribuidorData>({
    nombre: '',
    email: '',
    region: '',
    codigoDistribuidor: '',
    maxDominios: 0,
  });

  const loadData = useCallback(async () => {
    try {
      const data = await distribuidoresApi.getAll();
      setDistribuidores(data);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const resetForm = () => {
    setForm({ nombre: '', email: '', region: '', codigoDistribuidor: '', maxDominios: 0 });
    setFormErrors({});
  };

  const openCreateModal = () => {
    setModalMode('create');
    setEditId(null);
    resetForm();
    setModalOpen(true);
  };

  const openEditModal = async (distribuidor: Distribuidor) => {
    setModalMode('edit');
    setEditId(distribuidor.id);
    setForm({
      nombre: distribuidor.nombre,
      email: distribuidor.email,
      region: distribuidor.region || '',
      codigoDistribuidor: distribuidor.codigoDistribuidor,
      maxDominios: distribuidor.maxDominios,
    });
    setFormErrors({});
    setModalOpen(true);
  };

  const validate = (): boolean => {
    const errors: Record<string, string> = {};
    if (!form.nombre.trim()) errors.nombre = 'El nombre es obligatorio';
    if (!form.email.trim()) errors.email = 'El email es obligatorio';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) errors.email = 'Email inválido';
    if (!form.codigoDistribuidor.trim()) errors.codigoDistribuidor = 'El código es obligatorio';
    if (form.maxDominios < 0) errors.maxDominios = 'Debe ser 0 o mayor';
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setSubmitting(true);
    try {
      if (modalMode === 'create') {
        await distribuidoresApi.create(form);
      } else if (editId !== null) {
        const updateData: UpdateDistribuidorData = { ...form };
        if (!updateData.region) updateData.region = undefined;
        await distribuidoresApi.update(editId, updateData);
      }
      setModalOpen(false);
      resetForm();
      loadData();
    } catch {
      // ignore
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async (id: number, nombre: string) => {
    if (!window.confirm(`¿Eliminar el distribuidor "${nombre}"?`)) return;
    try {
      await distribuidoresApi.delete(id);
      loadData();
    } catch {
      // ignore
    }
  };

  const filtered = distribuidores.filter(
    d =>
      d.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
      d.codigoDistribuidor.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const previewNivel = () => {
    if (form.maxDominios <= 100) return 'BASICO';
    return 'PREMIUM';
  };

  return (
    <div>
      <PageHeader
        title='Gestión de Distribuidores'
        description='Administra los distribuidores y sus niveles según dominios soportados'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />} onClick={openCreateModal}>
            Nuevo Distribuidor
          </Button>
        }
      />

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Lista de Distribuidores</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar distribuidor...'
                value={searchTerm}
                onChange={e => setSearchTerm(e.target.value)}
                className='w-full pl-9 pr-3 py-2 text-sm border border-secondary-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-primary-500'
              />
            </div>
          </div>
        </CardHeader>
        <CardContent padding='none'>
          {loading ? (
            <div className='flex items-center justify-center py-12'>
              <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
            </div>
          ) : filtered.length === 0 ? (
            <EmptyState
              message='No hay distribuidores registrados'
              description={searchTerm ? 'No hay distribuidores que coincidan con tu búsqueda.' : 'Crea tu primer distribuidor para empezar.'}
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Distribuidor</Th>
                  <Th>Email</Th>
                  <Th>Código</Th>
                  <Th>Región</Th>
                  <Th>Dominios</Th>
                  <Th>Nivel</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {filtered.map(d => (
                  <Tr key={d.id}>
                    <Td>
                      <span className='font-medium'>{d.nombre}</span>
                    </Td>
                    <Td>{d.email}</Td>
                    <Td><code className='text-xs bg-secondary-100 px-2 py-0.5 rounded'>{d.codigoDistribuidor}</code></Td>
                    <Td>{d.region || '-'}</Td>
                    <Td>{d.maxDominios}</Td>
                    <Td>
                      <Badge variant={levelBadgeVariant[d.nivelDistribuidor] || 'default'} size='sm'>
                        {d.nivelDistribuidor}
                      </Badge>
                    </Td>
                    <Td>
                      <div className='flex items-center gap-2'>
                        <Button variant='ghost' size='sm' onClick={() => setDetailDistribuidor(d)} title='Ver Detalles'>
                          <Eye className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => openEditModal(d)}>
                          <Edit className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => handleDelete(d.id, d.nombre)}>
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

      <Modal
        open={detailDistribuidor !== null}
        onClose={() => setDetailDistribuidor(null)}
        title={`Distribuidor: ${detailDistribuidor?.nombre ?? ''}`}
        footer={
          <Button variant='outline' onClick={() => setDetailDistribuidor(null)}>
            Cerrar
          </Button>
        }
      >
        {detailDistribuidor && (
          <div className='space-y-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-xs text-secondary-500'>Nombre</p>
                <p className='text-sm font-medium'>{detailDistribuidor.nombre}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Email</p>
                <p className='text-sm font-medium'>{detailDistribuidor.email}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Código</p>
                <p className='text-sm font-medium'>
                  <code className='text-xs bg-secondary-100 px-2 py-0.5 rounded'>{detailDistribuidor.codigoDistribuidor}</code>
                </p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Región</p>
                <p className='text-sm font-medium'>{detailDistribuidor.region || '-'}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Máx. Dominios</p>
                <p className='text-sm font-medium'>{detailDistribuidor.maxDominios}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Nivel</p>
                <Badge variant={levelBadgeVariant[detailDistribuidor.nivelDistribuidor] || 'default'} size='sm'>
                  {detailDistribuidor.nivelDistribuidor}
                </Badge>
              </div>
            </div>
          </div>
        )}
      </Modal>

      <Modal
        open={modalOpen}
        onClose={() => { if (!submitting) { setModalOpen(false); resetForm(); } }}
        title={modalMode === 'create' ? 'Nuevo Distribuidor' : 'Editar Distribuidor'}
        footer={
          <>
            <Button variant='outline' onClick={() => { setModalOpen(false); resetForm(); }} disabled={submitting}>
              Cancelar
            </Button>
            <Button onClick={handleSubmit} isLoading={submitting}>
              {modalMode === 'create' ? 'Crear Distribuidor' : 'Guardar Cambios'}
            </Button>
          </>
        }
      >
        <div className='space-y-4'>
          <Input
            label='Nombre'
            placeholder='Nombre del distribuidor'
            value={form.nombre}
            onChange={e => setForm({ ...form, nombre: e.target.value })}
            error={formErrors.nombre}
            disabled={submitting}
          />
          <Input
            label='Email'
            type='email'
            placeholder='correo@ejemplo.com'
            value={form.email}
            onChange={e => setForm({ ...form, email: e.target.value })}
            error={formErrors.email}
            disabled={submitting}
          />
          <div className='grid grid-cols-2 gap-4'>
            <Input
              label='Código Distribuidor'
              placeholder='DIST-001'
              value={form.codigoDistribuidor}
              onChange={e => setForm({ ...form, codigoDistribuidor: e.target.value })}
              error={formErrors.codigoDistribuidor}
              disabled={submitting}
            />
            <Input
              label='Región'
              placeholder='Opcional'
              value={form.region}
              onChange={e => setForm({ ...form, region: e.target.value })}
              disabled={submitting}
            />
          </div>
          <Input
            label='Máximo de dominios soportados'
            type='number'
            min={0}
            placeholder='100'
            value={String(form.maxDominios)}
            onChange={e => setForm({ ...form, maxDominios: Math.max(0, parseInt(e.target.value) || 0) })}
            error={formErrors.maxDominios}
            disabled={submitting}
          />
          <div className='flex items-center gap-3 p-3 bg-secondary-50 rounded-lg'>
            <span className='text-sm text-secondary-600'>Nivel calculado:</span>
            <Badge variant={levelBadgeVariant[previewNivel()] || 'default'} size='sm'>
              {previewNivel()}
            </Badge>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default DistribuidoresPage;
