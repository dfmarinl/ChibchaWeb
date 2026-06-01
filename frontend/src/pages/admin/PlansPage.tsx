import { useState, useEffect, useCallback } from 'react';
import { Plus, Search, Edit, Trash2, Eye, X } from 'lucide-react';
import { Button, Badge, Card, CardHeader, CardTitle, CardContent } from '../../components/ui';
import { PageHeader } from '../../components/common';
import { Table, Thead, Tbody, Th, Tr, Td, EmptyState } from '../../components/tables';
import Input from '../../components/ui/Input';
import Select from '../../components/ui/Select';
import Modal from '../../components/ui/Modal';
import { plansApi } from '../../api/plans';
import { PlanHosting, CrearPlanData } from '../../types/plan';

type ModalMode = 'create' | 'edit';

const tierBadgeVariant: Record<string, 'primary' | 'warning' | 'default'> = {
  PLATINO: 'primary',
  ORO: 'warning',
  PLATA: 'default',
};

const PlansPage: React.FC = () => {
  const [plans, setPlans] = useState<PlanHosting[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');

  const [detailPlan, setDetailPlan] = useState<PlanHosting | null>(null);

  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<ModalMode>('create');
  const [editId, setEditId] = useState<number | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [formErrors, setFormErrors] = useState<Record<string, string>>({});

  const [form, setForm] = useState<CrearPlanData>({
    nombre: '',
    precioMensual: 0,
    espacioDisco: 0,
    anchoBanda: 0,
    cuentasEmail: 0,
    tipoPlan: 'PLATINO',
    plataforma: 'UNIX',
    limiteSitios: 3,
  });

  const loadData = useCallback(async () => {
    try {
      const data = await plansApi.getAll();
      setPlans(data);
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
    setForm({
      nombre: '',
      precioMensual: 0,
      espacioDisco: 0,
      anchoBanda: 0,
      cuentasEmail: 0,
      tipoPlan: 'PLATINO',
      plataforma: 'UNIX',
      limiteSitios: 3,
    });
    setFormErrors({});
  };

  const openCreateModal = () => {
    setModalMode('create');
    setEditId(null);
    resetForm();
    setModalOpen(true);
  };

  const openEditModal = (plan: PlanHosting) => {
    setModalMode('edit');
    setEditId(plan.id);
    setForm({
      nombre: plan.nombre,
      precioMensual: plan.precioMensual,
      espacioDisco: plan.espacioDisco,
      anchoBanda: plan.anchoBanda,
      cuentasEmail: plan.cuentasEmail,
      tipoPlan: plan.tipoPlan,
      plataforma: plan.plataforma,
      limiteSitios: plan.limiteSitios,
      mysqlIncluido: plan.mysqlIncluido ?? undefined,
      phpVersion: plan.phpVersion ?? undefined,
      sqlServerIncluido: plan.sqlServerIncluido ?? undefined,
      iisVersion: plan.iisVersion ?? undefined,
      pythonIncluido: plan.pythonIncluido ?? undefined,
      aspNetVersion: plan.aspNetVersion ?? undefined,
    });
    setFormErrors({});
    setModalOpen(true);
  };

  const validate = (): boolean => {
    const errors: Record<string, string> = {};
    if (!form.nombre.trim()) errors.nombre = 'El nombre es obligatorio';
    if (form.precioMensual <= 0) errors.precioMensual = 'Debe ser mayor a 0';
    if (form.espacioDisco <= 0) errors.espacioDisco = 'Debe ser mayor a 0';
    if (form.anchoBanda <= 0) errors.anchoBanda = 'Debe ser mayor a 0';
    if (form.cuentasEmail <= 0) errors.cuentasEmail = 'Debe ser mayor a 0';
    if (form.limiteSitios <= 0) errors.limiteSitios = 'Debe ser mayor a 0';
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validate()) return;
    setSubmitting(true);
    try {
      if (modalMode === 'create') {
        await plansApi.create(form);
      } else if (editId !== null) {
        await plansApi.update(editId, form);
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
    if (!window.confirm(`¿Eliminar el plan "${nombre}"?`)) return;
    try {
      await plansApi.delete(id);
      loadData();
    } catch {
      // ignore
    }
  };

  const filtered = plans.filter(
    p =>
      p.nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.tipoPlan.toLowerCase().includes(searchTerm.toLowerCase()) ||
      p.plataforma.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div>
      <PageHeader
        title='Gestión de Planes de Hosting'
        description='Administra los planes de hosting disponibles'
        action={
          <Button variant='primary' leftIcon={<Plus className='w-4 h-4' />} onClick={openCreateModal}>
            Nuevo Plan
          </Button>
        }
      />

      <Card variant='bordered'>
        <CardHeader>
          <div className='flex items-center justify-between'>
            <CardTitle>Lista de Planes</CardTitle>
            <div className='relative w-64'>
              <Search className='absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-secondary-400' />
              <input
                type='text'
                placeholder='Buscar plan...'
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
              message='No hay planes registrados'
              description={searchTerm ? 'No hay planes que coincidan con tu búsqueda.' : 'Crea tu primer plan para empezar.'}
            />
          ) : (
            <Table>
              <Thead>
                <Tr>
                  <Th>Nombre</Th>
                  <Th>Tier</Th>
                  <Th>Plataforma</Th>
                  <Th>Precio</Th>
                  <Th>Disco</Th>
                  <Th>Banda</Th>
                  <Th>Email</Th>
                  <Th>Acciones</Th>
                </Tr>
              </Thead>
              <Tbody>
                {filtered.map(p => (
                  <Tr key={p.id}>
                    <Td>
                      <span className='font-medium'>{p.nombre}</span>
                    </Td>
                    <Td>
                      <Badge variant={tierBadgeVariant[p.tipoPlan] || 'default'} size='sm'>
                        {p.tipoPlan}
                      </Badge>
                    </Td>
                    <Td>{p.plataforma}</Td>
                    <Td>${p.precioMensual.toLocaleString('es-CO')}/mes</Td>
                    <Td>{p.espacioDisco} MB</Td>
                    <Td>{p.anchoBanda} MB</Td>
                    <Td>{p.cuentasEmail}</Td>
                    <Td>
                      <div className='flex items-center gap-1'>
                        <Button variant='ghost' size='sm' onClick={() => setDetailPlan(p)} title='Ver Detalles'>
                          <Eye className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => openEditModal(p)}>
                          <Edit className='w-4 h-4' />
                        </Button>
                        <Button variant='ghost' size='sm' onClick={() => handleDelete(p.id, p.nombre)}>
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
        open={detailPlan !== null}
        onClose={() => setDetailPlan(null)}
        title={`Detalles: ${detailPlan?.nombre ?? ''}`}
        footer={
          <Button variant='outline' onClick={() => setDetailPlan(null)}>
            Cerrar
          </Button>
        }
      >
        {detailPlan && (
          <div className='space-y-4'>
            <div className='grid grid-cols-2 gap-4'>
              <div>
                <p className='text-xs text-secondary-500'>Nombre</p>
                <p className='text-sm font-medium'>{detailPlan.nombre}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Tipo</p>
                <Badge variant={tierBadgeVariant[detailPlan.tipoPlan] || 'default'} size='sm'>
                  {detailPlan.tipoPlan}
                </Badge>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Plataforma</p>
                <p className='text-sm font-medium'>{detailPlan.plataforma}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Precio Mensual</p>
                <p className='text-sm font-medium'>${detailPlan.precioMensual.toLocaleString('es-CO')}</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Espacio en Disco</p>
                <p className='text-sm font-medium'>{detailPlan.espacioDisco} MB</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Ancho de Banda</p>
                <p className='text-sm font-medium'>{detailPlan.anchoBanda} MB</p>
              </div>
              <div>
                <p className='text-xs text-secondary-500'>Cuentas de Email</p>
                <p className='text-sm font-medium'>{detailPlan.cuentasEmail}</p>
              </div>
            </div>

            <div className='border-t border-secondary-200 pt-4'>
              <p className='text-xs text-secondary-500 mb-2'>Especificaciones de Plataforma</p>
              <div className='grid grid-cols-2 gap-4'>
                {detailPlan.mysqlIncluido !== null && (
                  <div>
                    <p className='text-xs text-secondary-500'>MySQL</p>
                    <p className='text-sm font-medium'>{detailPlan.mysqlIncluido ? 'Incluido' : 'No incluido'}</p>
                  </div>
                )}
                {detailPlan.phpVersion && (
                  <div>
                    <p className='text-xs text-secondary-500'>PHP</p>
                    <p className='text-sm font-medium'>{detailPlan.phpVersion}</p>
                  </div>
                )}
                {detailPlan.sqlServerIncluido !== null && (
                  <div>
                    <p className='text-xs text-secondary-500'>SQL Server</p>
                    <p className='text-sm font-medium'>{detailPlan.sqlServerIncluido ? 'Incluido' : 'No incluido'}</p>
                  </div>
                )}
                {detailPlan.iisVersion && (
                  <div>
                    <p className='text-xs text-secondary-500'>IIS</p>
                    <p className='text-sm font-medium'>{detailPlan.iisVersion}</p>
                  </div>
                )}
                {detailPlan.pythonIncluido !== null && (
                  <div>
                    <p className='text-xs text-secondary-500'>Python</p>
                    <p className='text-sm font-medium'>{detailPlan.pythonIncluido ? 'Incluido' : 'No incluido'}</p>
                  </div>
                )}
                {detailPlan.aspNetVersion && (
                  <div>
                    <p className='text-xs text-secondary-500'>ASP.NET</p>
                    <p className='text-sm font-medium'>{detailPlan.aspNetVersion}</p>
                  </div>
                )}
              </div>
            </div>

            <div className='border-t border-secondary-200 pt-4'>
              <p className='text-xs text-secondary-500 mb-2'>Características</p>
              <ul className='space-y-1'>
                {Object.entries(detailPlan.caracteristicas).map(([key, value]) => (
                  <li key={key} className='flex items-center gap-2 text-sm'>
                    <span className='text-secondary-700 capitalize'>{key.replace(/([A-Z])/g, ' $1').trim()}:</span>
                    <span className='font-medium'>{value}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        )}
      </Modal>

      <Modal
        open={modalOpen}
        onClose={() => { if (!submitting) { setModalOpen(false); resetForm(); } }}
        title={modalMode === 'create' ? 'Nuevo Plan de Hosting' : 'Editar Plan de Hosting'}
        footer={
          <>
            <Button variant='outline' onClick={() => { setModalOpen(false); resetForm(); }} disabled={submitting}>
              Cancelar
            </Button>
            <Button onClick={handleSubmit} isLoading={submitting}>
              {modalMode === 'create' ? 'Crear Plan' : 'Guardar Cambios'}
            </Button>
          </>
        }
      >
        <div className='space-y-4'>
          <Input
            label='Nombre'
            placeholder='Ej: Platino Unix'
            value={form.nombre}
            onChange={e => setForm({ ...form, nombre: e.target.value })}
            error={formErrors.nombre}
            disabled={submitting}
          />
          <div className='grid grid-cols-2 gap-4'>
            <Select
              label='Tipo de Plan'
              value={form.tipoPlan}
              onChange={e => setForm({ ...form, tipoPlan: e.target.value })}
              disabled={submitting || modalMode === 'edit'}
            >
              <option value='PLATINO'>Platino</option>
              <option value='ORO'>Oro</option>
              <option value='PLATA'>Plata</option>
            </Select>
            <Select
              label='Plataforma'
              value={form.plataforma}
              onChange={e => {
                const plat = e.target.value;
                setForm({
                  ...form,
                  plataforma: plat,
                  mysqlIncluido: plat === 'UNIX' ? form.mysqlIncluido : undefined,
                  phpVersion: plat === 'UNIX' ? form.phpVersion : undefined,
                  sqlServerIncluido: plat === 'WINDOWS' ? form.sqlServerIncluido : undefined,
                  iisVersion: plat === 'WINDOWS' ? form.iisVersion : undefined,
                  pythonIncluido: plat === 'UNIX' ? form.pythonIncluido : undefined,
                  aspNetVersion: plat === 'WINDOWS' ? form.aspNetVersion : undefined,
                });
              }}
              disabled={submitting || modalMode === 'edit'}
            >
              <option value='UNIX'>Unix/Linux</option>
              <option value='WINDOWS'>Windows</option>
            </Select>
          </div>
          <div className='grid grid-cols-2 gap-4'>
            <Input
              label='Precio Mensual'
              type='number'
              min={0}
              step={0.01}
              value={String(form.precioMensual)}
              onChange={e => setForm({ ...form, precioMensual: parseFloat(e.target.value) || 0 })}
              error={formErrors.precioMensual}
              disabled={submitting}
            />
            <Input
              label='Espacio en Disco (MB)'
              type='number'
              min={0}
              value={String(form.espacioDisco)}
              onChange={e => setForm({ ...form, espacioDisco: parseInt(e.target.value) || 0 })}
              error={formErrors.espacioDisco}
              disabled={submitting}
            />
          </div>
          <div className='grid grid-cols-2 gap-4'>
            <Input
              label='Ancho de Banda (MB)'
              type='number'
              min={0}
              value={String(form.anchoBanda)}
              onChange={e => setForm({ ...form, anchoBanda: parseInt(e.target.value) || 0 })}
              error={formErrors.anchoBanda}
              disabled={submitting}
            />
            <Input
              label='Cuentas de Email'
              type='number'
              min={0}
              value={String(form.cuentasEmail)}
              onChange={e => setForm({ ...form, cuentasEmail: parseInt(e.target.value) || 0 })}
              error={formErrors.cuentasEmail}
              disabled={submitting}
            />
            <Input
              label='Límite de Sitios'
              type='number'
              min={1}
              value={String(form.limiteSitios)}
              onChange={e => setForm({ ...form, limiteSitios: parseInt(e.target.value) || 0 })}
              error={formErrors.limiteSitios}
              disabled={submitting}
            />
          </div>

          {form.plataforma === 'UNIX' && (
            <>
              {form.tipoPlan === 'PLATINO' && (
                <>
                  <div className='flex items-center gap-3 p-3 bg-secondary-50 rounded-lg'>
                    <input
                      type='checkbox'
                      id='mysqlIncluido'
                      checked={form.mysqlIncluido ?? false}
                      onChange={e => setForm({ ...form, mysqlIncluido: e.target.checked })}
                      className='w-4 h-4 rounded border-secondary-300 text-primary-600 focus:ring-primary-500'
                    />
                    <label htmlFor='mysqlIncluido' className='text-sm text-secondary-700'>MySQL Incluido</label>
                  </div>
                  <Input
                    label='Versión de PHP'
                    placeholder='8.2'
                    value={form.phpVersion ?? ''}
                    onChange={e => setForm({ ...form, phpVersion: e.target.value })}
                    disabled={submitting}
                  />
                </>
              )}
              {form.tipoPlan === 'ORO' && (
                <div className='flex items-center gap-3 p-3 bg-secondary-50 rounded-lg'>
                  <input
                    type='checkbox'
                    id='pythonIncluido'
                    checked={form.pythonIncluido ?? false}
                    onChange={e => setForm({ ...form, pythonIncluido: e.target.checked })}
                    className='w-4 h-4 rounded border-secondary-300 text-primary-600 focus:ring-primary-500'
                  />
                  <label htmlFor='pythonIncluido' className='text-sm text-secondary-700'>Python Incluido</label>
                </div>
              )}
            </>
          )}

          {form.plataforma === 'WINDOWS' && (
            <>
              {form.tipoPlan === 'PLATINO' && (
                <>
                  <div className='flex items-center gap-3 p-3 bg-secondary-50 rounded-lg'>
                    <input
                      type='checkbox'
                      id='sqlServerIncluido'
                      checked={form.sqlServerIncluido ?? false}
                      onChange={e => setForm({ ...form, sqlServerIncluido: e.target.checked })}
                      className='w-4 h-4 rounded border-secondary-300 text-primary-600 focus:ring-primary-500'
                    />
                    <label htmlFor='sqlServerIncluido' className='text-sm text-secondary-700'>SQL Server Incluido</label>
                  </div>
                  <Input
                    label='Versión de IIS'
                    placeholder='10.0'
                    value={form.iisVersion ?? ''}
                    onChange={e => setForm({ ...form, iisVersion: e.target.value })}
                    disabled={submitting}
                  />
                </>
              )}
              {form.tipoPlan === 'ORO' && (
                <Input
                  label='Versión de ASP.NET'
                  placeholder='4.8'
                  value={form.aspNetVersion ?? ''}
                  onChange={e => setForm({ ...form, aspNetVersion: e.target.value })}
                  disabled={submitting}
                />
              )}
            </>
          )}
        </div>
      </Modal>
    </div>
  );
};

export default PlansPage;
