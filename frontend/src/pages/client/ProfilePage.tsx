import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { CheckCircle, User, CreditCard, Globe, Plus, Trash2, AlertCircle, XCircle } from 'lucide-react';
import { Card, Button } from '../../components/ui';
import { PageHeader } from '../../components/common';
import Input from '../../components/ui/Input';
import Modal from '../../components/ui/Modal';
import ClientForm from '../admin/ClientForm';
import { clientsApi, UpdateClientData } from '../../api/clients';
import { Client } from '../../api/clients';
import { cardsApi, Card as CardInfo, CreateCardData } from '../../api/cards';
import { sitesApi, Site as SiteInfo, CreateSiteData } from '../../api/sites';
import { ROUTES } from '../../constants';

interface FormErrors {
  numero?: string;
  titular?: string;
  fechaVencimiento?: string;
  cvv?: string;
}

const ClientProfile: React.FC = () => {
  const navigate = useNavigate();
  const [client, setClient] = useState<Client | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  const [cards, setCards] = useState<CardInfo[]>([]);
  const [cardsLoading, setCardsLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [blocked, setBlocked] = useState(false);
  const [intentosRestantes, setIntentosRestantes] = useState<number | null>(null);
  const [errorMsg, setErrorMsg] = useState('');

  const [sites, setSites] = useState<SiteInfo[]>([]);
  const [sitesLoading, setSitesLoading] = useState(true);
  const [siteModalOpen, setSiteModalOpen] = useState(false);
  const [siteUrl, setSiteUrl] = useState('');
  const [siteSubmitting, setSiteSubmitting] = useState(false);
  const [siteError, setSiteError] = useState('');
  const [siteSuccess, setSiteSuccess] = useState('');

  const [cardForm, setCardForm] = useState<CreateCardData>({
    numero: '',
    titular: '',
    fechaVencimiento: '',
    cvv: '',
  });
  const [cardErrors, setCardErrors] = useState<FormErrors>({});
  const [cardTouched, setCardTouched] = useState<Record<string, boolean>>({});

  useEffect(() => {
    clientsApi
      .getMe()
      .then((data) => {
        setClient(data);
      })
      .catch(() => {
        setError('Error al cargar los datos del perfil');
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const loadCards = useCallback(async () => {
    try {
      const data = await cardsApi.getAll();
      setCards(data);
    } catch {
      // ignore
    } finally {
      setCardsLoading(false);
    }
  }, []);

  useEffect(() => {
    loadCards();
  }, [loadCards]);

  const loadSites = useCallback(async () => {
    try {
      const data = await sitesApi.getAll();
      setSites(data);
    } catch {
      // ignore
    } finally {
      setSitesLoading(false);
    }
  }, []);

  useEffect(() => {
    loadSites();
  }, [loadSites]);

  const handleSubmit = async (data: UpdateClientData) => {
    if (!client) return;
    setSaving(true);
    setError('');
    setSuccess(false);

    try {
      const updated = await clientsApi.update(client.id, data);
      setClient(updated);

      const userData = localStorage.getItem('user');
      if (userData) {
        const user = JSON.parse(userData);
        user.name = updated.nombre;
        localStorage.setItem('user', JSON.stringify(user));
      }

      setSuccess(true);
      setTimeout(() => setSuccess(false), 3000);
    } catch {
      setError('Error al actualizar el perfil');
    } finally {
      setSaving(false);
    }
  };

  const resetCardForm = () => {
    setCardForm({ numero: '', titular: '', fechaVencimiento: '', cvv: '' });
    setCardErrors({});
    setCardTouched({});
    setIntentosRestantes(null);
    setErrorMsg('');
  };

  const openModal = () => {
    resetCardForm();
    setModalOpen(true);
  };

  const closeModal = () => {
    if (!submitting) {
      setModalOpen(false);
      resetCardForm();
    }
  };

  const validateCardField = (field: keyof CreateCardData, currentForm: CreateCardData) => {
    let error = '';
    switch (field) {
      case 'numero': {
        const n = currentForm.numero.replace(/\s/g, '');
        if (!n) error = 'El número es obligatorio';
        else if (!/^\d{13,19}$/.test(n)) error = 'Número inválido (13-19 dígitos)';
        break;
      }
      case 'titular':
        if (!currentForm.titular.trim()) error = 'El titular es obligatorio';
        break;
      case 'fechaVencimiento': {
        const v = currentForm.fechaVencimiento.trim();
        if (!v) error = 'La fecha de vencimiento es obligatoria';
        else if (!/^\d{2}\/\d{2}$/.test(v)) error = 'Formato MM/AA';
        break;
      }
      case 'cvv': {
        const c = currentForm.cvv.trim();
        if (!c) error = 'El CVV es obligatorio';
        else if (!/^\d{3,4}$/.test(c)) error = 'CVV inválido (3-4 dígitos)';
        break;
      }
    }
    setCardErrors(prev => ({ ...prev, [field]: error }));
  };

  const handleCardChange = (field: keyof CreateCardData, value: string) => {
    const newForm = { ...cardForm, [field]: value };
    setCardForm(newForm);
    if (cardTouched[field]) {
      validateCardField(field, newForm);
    }
  };

  const handleCardBlur = (field: keyof CreateCardData) => {
    setCardTouched({ ...cardTouched, [field]: true });
    validateCardField(field, cardForm);
  };

  const validateCardForm = (): boolean => {
    const newErrors: FormErrors = {};

    const numero = cardForm.numero.replace(/\s/g, '');
    if (!numero) {
      newErrors.numero = 'El número es obligatorio';
    } else if (!/^\d{13,19}$/.test(numero)) {
      newErrors.numero = 'Número inválido (13-19 dígitos)';
    }

    if (!cardForm.titular.trim()) {
      newErrors.titular = 'El titular es obligatorio';
    }

    const venc = cardForm.fechaVencimiento.trim();
    if (!venc) {
      newErrors.fechaVencimiento = 'La fecha de vencimiento es obligatoria';
    } else if (!/^\d{2}\/\d{2}$/.test(venc)) {
      newErrors.fechaVencimiento = 'Formato MM/AA';
    }

    const cvv = cardForm.cvv.trim();
    if (!cvv) {
      newErrors.cvv = 'El CVV es obligatorio';
    } else if (!/^\d{3,4}$/.test(cvv)) {
      newErrors.cvv = 'CVV inválido (3-4 dígitos)';
    }

    setCardErrors(newErrors);
    setCardTouched({ numero: true, titular: true, fechaVencimiento: true, cvv: true });
    return Object.keys(newErrors).length === 0;
  };

  const handleCardSubmit = async () => {
    if (!validateCardForm()) return;
    setSubmitting(true);
    setErrorMsg('');

    try {
      const response = await cardsApi.create({
        ...cardForm,
        numero: cardForm.numero.replace(/\s/g, ''),
      });

      if (response.exitoso && response.tarjeta) {
        setCards(prev => [...prev, response.tarjeta!]);
        closeModal();
        setSuccessMsg(response.mensaje);
        setTimeout(() => setSuccessMsg(''), 3000);
      }
    } catch (error: any) {
      const data = error.response?.data;
      if (data) {
        if (data.limiteExcedido) {
          setBlocked(true);
          setErrorMsg(data.mensaje || 'Has superado el límite de intentos');
        } else if (data.intentosRestantes !== undefined) {
          setIntentosRestantes(data.intentosRestantes);
          setErrorMsg(data.mensaje || 'Número de tarjeta inválido');
        } else {
          setErrorMsg(data.mensaje || 'Error al asociar tarjeta');
        }
      } else {
        setErrorMsg('Error de conexión');
      }
    } finally {
      setSubmitting(false);
    }
  };

  const handleCardDelete = async (id: number) => {
    if (!window.confirm('¿Eliminar esta tarjeta?')) return;
    try {
      await cardsApi.delete(id);
      setCards(prev => prev.filter(c => c.id !== id));
    } catch {
      // ignore
    }
  };

  const handleSiteSubmit = async () => {
    const name = siteUrl.trim();
    if (!name) {
      setSiteError('El nombre del sitio es obligatorio');
      return;
    }
    if (!/^[a-z0-9]([a-z0-9-]*[a-z0-9])?$/.test(name)) {
      setSiteError('Solo minúsculas, números y guiones');
      return;
    }
    setSiteSubmitting(true);
    setSiteError('');
    try {
      const site = await sitesApi.create({ urlSitio: name });
      setSites(prev => [...prev, site]);
      setSiteModalOpen(false);
      setSiteUrl('');
      setSiteSuccess('Sitio web registrado exitosamente');
      setTimeout(() => setSiteSuccess(''), 3000);
    } catch {
      setSiteError('Error al registrar el sitio web');
    } finally {
      setSiteSubmitting(false);
    }
  };

  const handleSiteDelete = async (id: number) => {
    if (!window.confirm('¿Eliminar este sitio web?')) return;
    try {
      await sitesApi.delete(id);
      setSites(prev => prev.filter(s => s.id !== id));
    } catch {
      // ignore
    }
  };

  const formatDate = (dateStr: string) => {
    const d = new Date(dateStr);
    return d.toLocaleDateString('es-CO', { year: 'numeric', month: '2-digit', day: '2-digit' });
  };

  if (loading) {
    return (
      <div>
        <PageHeader title='Mi Perfil' description='Gestiona tu información personal' />
        <Card>
          <div className='flex items-center justify-center py-12'>
            <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
            <span className='ml-3 text-sm text-secondary-600'>Cargando...</span>
          </div>
        </Card>
      </div>
    );
  }

  if (error && !client) {
    return (
      <div>
        <PageHeader title='Mi Perfil' />
        <Card>
          <div className='text-center py-12'>
            <p className='text-sm text-error-600 mb-4'>{error}</p>
            <Button variant='outline' onClick={() => navigate(ROUTES.CLIENT.DASHBOARD)}>
              Volver al Dashboard
            </Button>
          </div>
        </Card>
      </div>
    );
  }

  return (
    <div>
      <PageHeader
        title='Mi Perfil'
        description='Actualiza tu información personal y gestiona tus tarjetas'
        breadcrumbs={[
          { label: 'Dashboard', href: ROUTES.CLIENT.DASHBOARD },
          { label: 'Mi Perfil' },
        ]}
      />

      {success && (
        <div className='mb-6 p-4 rounded-lg bg-success-50 border border-success-200 flex items-center gap-3'>
          <CheckCircle className='w-5 h-5 text-success-600' />
          <p className='text-sm font-medium text-success-700'>
            Perfil actualizado exitosamente
          </p>
        </div>
      )}

      {/* Profile Section */}
      <Card>
        <div className='flex items-center gap-4 mb-6 pb-6 border-b border-secondary-200'>
          <div className='w-16 h-16 bg-primary-100 rounded-full flex items-center justify-center'>
            <User className='w-8 h-8 text-primary-600' />
          </div>
          <div>
            <h2 className='text-lg font-semibold text-secondary-900'>
              {client?.nombre}
            </h2>
            <p className='text-sm text-secondary-500'>{client?.email}</p>
          </div>
        </div>

        {client && (
          <ClientForm
            id='profile-form'
            initialData={client}
            onSubmit={handleSubmit}
            isLoading={saving}
          />
        )}

        <div className='flex justify-end gap-3 mt-6 pt-6 border-t border-secondary-200'>
          <Button
            variant='outline'
            onClick={() => navigate(ROUTES.CLIENT.DASHBOARD)}
          >
            Cancelar
          </Button>
          <Button
            type='submit'
            form='profile-form'
            isLoading={saving}
          >
            Guardar Cambios
          </Button>
        </div>
      </Card>

      {/* Cards Section */}
      <div className='mt-10'>
        <div className='flex items-center justify-between mb-6'>
          <div>
            <h2 className='text-xl font-bold text-secondary-900'>Mis Tarjetas</h2>
            <p className='text-secondary-500 mt-1'>Gestiona tus tarjetas de crédito</p>
          </div>
          <button
            onClick={openModal}
            disabled={blocked}
            className='flex items-center gap-2 px-4 py-2.5 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed'
          >
            <Plus className='w-4 h-4' />
            Agregar Tarjeta
          </button>
        </div>

        {successMsg && (
          <div className='mb-6 flex items-center gap-2 px-4 py-3 bg-success-50 text-success-700 rounded-lg border border-success-200'>
            <CheckCircle className='w-5 h-5 flex-shrink-0' />
            <span className='text-sm font-medium'>{successMsg}</span>
          </div>
        )}

        {cardsLoading ? (
          <Card>
            <div className='flex items-center justify-center py-12'>
              <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
            </div>
          </Card>
        ) : cards.length === 0 ? (
          <Card>
            <div className='text-center py-12'>
              <CreditCard className='w-12 h-12 text-secondary-300 mx-auto mb-4' />
              <h3 className='text-lg font-medium text-secondary-700'>No hay tarjetas</h3>
              <p className='text-secondary-500 mt-1'>Agrega una tarjeta de crédito para realizar pagos</p>
            </div>
          </Card>
        ) : (
          <div className='grid gap-4 md:grid-cols-2'>
            {cards.map(card => (
              <div
                key={card.id}
                className='bg-white rounded-xl border border-secondary-200 p-5 hover:shadow-sm transition-shadow'
              >
                <div className='flex items-start justify-between'>
                  <div className='flex items-center gap-3'>
                    <div className='w-10 h-10 bg-primary-50 rounded-lg flex items-center justify-center'>
                      <CreditCard className='w-5 h-5 text-primary-600' />
                    </div>
                    <div>
                      <p className='font-medium text-secondary-900'>{card.titular}</p>
                      <p className='text-sm text-secondary-500'>{card.numeroEnmascarado}</p>
                      <div className='flex items-center gap-3 mt-1'>
                        <span className='text-xs text-secondary-400'>Vence: {card.fechaVencimiento}</span>
                        <span className='text-xs font-medium text-primary-600'>{card.tipoTarjeta}</span>
                      </div>
                    </div>
                  </div>
                  <button
                    onClick={() => handleCardDelete(card.id)}
                    className='p-2 rounded-lg hover:bg-error-50 text-secondary-400 hover:text-error-600 transition-colors'
                    title='Eliminar'
                  >
                    <Trash2 className='w-4 h-4' />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Modal
        open={modalOpen}
        onClose={closeModal}
        title='Agregar Tarjeta'
        footer={
          <>
            <button
              onClick={closeModal}
              disabled={submitting}
              className='px-4 py-2.5 text-sm font-medium text-secondary-700 bg-white border border-secondary-300 rounded-lg hover:bg-secondary-50 transition-colors'
            >
              Cancelar
            </button>
            <button
              onClick={handleCardSubmit}
              disabled={submitting || blocked}
              className='flex items-center gap-2 px-4 py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed'
            >
              {submitting ? (
                <>
                  <div className='animate-spin rounded-full h-4 w-4 border-b-2 border-white' />
                  Asociando...
                </>
              ) : (
                'Asociar Tarjeta'
              )}
            </button>
          </>
        }
      >
        {blocked && (
          <div className='mb-4 flex items-center gap-2 px-4 py-3 bg-error-50 text-error-700 rounded-lg border border-error-200'>
            <XCircle className='w-5 h-5 flex-shrink-0' />
            <span className='text-sm font-medium'>Has superado el límite de intentos. Intenta más tarde.</span>
          </div>
        )}

        {intentosRestantes !== null && !blocked && (
          <div className='mb-4 flex items-center gap-2 px-4 py-3 bg-warning-50 text-warning-700 rounded-lg border border-warning-200'>
            <AlertCircle className='w-5 h-5 flex-shrink-0' />
            <span className='text-sm font-medium'>
              {errorMsg} — Te quedan {intentosRestantes} intento{intentosRestantes !== 1 ? 's' : ''}
            </span>
          </div>
        )}

        <div className='space-y-4'>
          <Input
            label='Número de tarjeta'
            placeholder='4532457890123456'
            value={cardForm.numero}
            onChange={e => handleCardChange('numero', e.target.value)}
            onBlur={() => handleCardBlur('numero')}
            error={cardErrors.numero}
            disabled={blocked}
            maxLength={19}
          />
          <Input
            label='Titular'
            placeholder='Juan Pérez'
            value={cardForm.titular}
            onChange={e => handleCardChange('titular', e.target.value)}
            onBlur={() => handleCardBlur('titular')}
            error={cardErrors.titular}
            disabled={blocked}
          />
          <div className='grid grid-cols-2 gap-4'>
            <Input
              label='Vencimiento'
              placeholder='MM/AA'
              value={cardForm.fechaVencimiento}
              onChange={e => handleCardChange('fechaVencimiento', e.target.value)}
              onBlur={() => handleCardBlur('fechaVencimiento')}
              error={cardErrors.fechaVencimiento}
              disabled={blocked}
              maxLength={5}
            />
            <Input
              label='CVV'
              placeholder='123'
              value={cardForm.cvv}
              onChange={e => handleCardChange('cvv', e.target.value)}
              onBlur={() => handleCardBlur('cvv')}
              error={cardErrors.cvv}
              disabled={blocked}
              maxLength={4}
              type='password'
            />
          </div>
        </div>
      </Modal>

      {/* Sites Section */}
      <div className='mt-10'>
        <div className='flex items-center justify-between mb-6'>
          <div>
            <h2 className='text-xl font-bold text-secondary-900'>Mis Sitios Web</h2>
            <p className='text-secondary-500 mt-1'>Registra y gestiona tus sitios web</p>
          </div>
          <button
            onClick={() => { setSiteUrl(''); setSiteError(''); setSiteModalOpen(true); }}
            className='flex items-center gap-2 px-4 py-2.5 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors text-sm font-medium'
          >
            <Plus className='w-4 h-4' />
            Registrar Sitio
          </button>
        </div>

        {siteSuccess && (
          <div className='mb-6 flex items-center gap-2 px-4 py-3 bg-success-50 text-success-700 rounded-lg border border-success-200'>
            <CheckCircle className='w-5 h-5 flex-shrink-0' />
            <span className='text-sm font-medium'>{siteSuccess}</span>
          </div>
        )}

        {sitesLoading ? (
          <Card>
            <div className='flex items-center justify-center py-12'>
              <div className='w-6 h-6 border-2 border-primary-600 border-t-transparent rounded-full animate-spin' />
            </div>
          </Card>
        ) : sites.length === 0 ? (
          <Card>
            <div className='text-center py-12'>
              <Globe className='w-12 h-12 text-secondary-300 mx-auto mb-4' />
              <h3 className='text-lg font-medium text-secondary-700'>No hay sitios web</h3>
              <p className='text-secondary-500 mt-1'>Registra un sitio web para empezar</p>
            </div>
          </Card>
        ) : (
          <div className='grid gap-4 md:grid-cols-2'>
            {sites.map(site => (
              <div
                key={site.id}
                className='bg-white rounded-xl border border-secondary-200 p-5 hover:shadow-sm transition-shadow'
              >
                <div className='flex items-start justify-between'>
                  <div className='flex items-center gap-3'>
                    <div className='w-10 h-10 bg-primary-50 rounded-lg flex items-center justify-center'>
                      <Globe className='w-5 h-5 text-primary-600' />
                    </div>
                    <div>
                      <p className='font-medium text-secondary-900'>{site.urlSitio}</p>
                      <div className='flex items-center gap-3 mt-1'>
                        <span className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${site.estadoActivo ? 'bg-success-50 text-success-700' : 'bg-secondary-100 text-secondary-600'}`}>
                          {site.estadoActivo ? 'Activo' : 'Inactivo'}
                        </span>
                        <span className='text-xs text-secondary-400'>Creado: {formatDate(site.fechaCreacion)}</span>
                      </div>
                    </div>
                  </div>
                  <button
                    onClick={() => handleSiteDelete(site.id)}
                    className='p-2 rounded-lg hover:bg-error-50 text-secondary-400 hover:text-error-600 transition-colors'
                    title='Eliminar'
                  >
                    <Trash2 className='w-4 h-4' />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

      <Modal
        open={siteModalOpen}
        onClose={() => { if (!siteSubmitting) { setSiteModalOpen(false); setSiteError(''); } }}
        title='Registrar Sitio Web'
        footer={
          <>
            <button
              onClick={() => { setSiteModalOpen(false); setSiteError(''); }}
              disabled={siteSubmitting}
              className='px-4 py-2.5 text-sm font-medium text-secondary-700 bg-white border border-secondary-300 rounded-lg hover:bg-secondary-50 transition-colors'
            >
              Cancelar
            </button>
            <button
              onClick={handleSiteSubmit}
              disabled={siteSubmitting}
              className='flex items-center gap-2 px-4 py-2.5 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed'
            >
              {siteSubmitting ? (
                <>
                  <div className='animate-spin rounded-full h-4 w-4 border-b-2 border-white' />
                  Registrando...
                </>
              ) : (
                'Registrar'
              )}
            </button>
          </>
        }
      >
        <div className='space-y-4'>
          <Input
            label='Nombre del sitio'
            placeholder='mi-proyecto'
            value={siteUrl}
            onChange={e => setSiteUrl(e.target.value.toLowerCase().replace(/[^a-z0-9-]/g, ''))}
            error={siteError}
            disabled={siteSubmitting}
            maxLength={30}
          />
          <p className='text-xs text-secondary-400'>
            El sitio se creará como
            {siteUrl ? (
              <span className='text-primary-600 font-medium'> {siteUrl}.chibchaweb.com</span>
            ) : (
              ' [nombre].chibchaweb.com'
            )}
          </p>
        </div>
      </Modal>
    </div>
  );
};

export default ClientProfile;
