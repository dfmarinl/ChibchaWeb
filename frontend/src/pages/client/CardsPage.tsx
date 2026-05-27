import { useState, useEffect, useCallback } from 'react';
import { CreditCard, Plus, Trash2, AlertCircle, CheckCircle, XCircle } from 'lucide-react';
import { cardsApi, Card, CreateCardData } from '../../api/cards';
import Input from '../../components/ui/Input';
import Modal from '../../components/ui/Modal';

interface FormErrors {
  numero?: string;
  titular?: string;
  fechaVencimiento?: string;
  cvv?: string;
}

const CardsPage: React.FC = () => {
  const [cards, setCards] = useState<Card[]>([]);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [blocked, setBlocked] = useState(false);
  const [intentosRestantes, setIntentosRestantes] = useState<number | null>(null);
  const [errorMsg, setErrorMsg] = useState('');

  const [form, setForm] = useState<CreateCardData>({
    numero: '',
    titular: '',
    fechaVencimiento: '',
    cvv: '',
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  const loadCards = useCallback(async () => {
    try {
      const data = await cardsApi.getAll();
      setCards(data);
    } catch {
      // ignore
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadCards();
  }, [loadCards]);

  const resetForm = () => {
    setForm({ numero: '', titular: '', fechaVencimiento: '', cvv: '' });
    setErrors({});
    setTouched({});
    setIntentosRestantes(null);
    setErrorMsg('');
  };

  const openModal = () => {
    resetForm();
    setModalOpen(true);
  };

  const closeModal = () => {
    if (!submitting) {
      setModalOpen(false);
      resetForm();
    }
  };

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};

    const numero = form.numero.replace(/\s/g, '');
    if (!numero) {
      newErrors.numero = 'El número es obligatorio';
    } else if (!/^\d{13,19}$/.test(numero)) {
      newErrors.numero = 'Número inválido (13-19 dígitos)';
    }

    if (!form.titular.trim()) {
      newErrors.titular = 'El titular es obligatorio';
    }

    const venc = form.fechaVencimiento.trim();
    if (!venc) {
      newErrors.fechaVencimiento = 'La fecha de vencimiento es obligatoria';
    } else if (!/^\d{2}\/\d{2}$/.test(venc)) {
      newErrors.fechaVencimiento = 'Formato MM/AA';
    }

    const cvv = form.cvv.trim();
    if (!cvv) {
      newErrors.cvv = 'El CVV es obligatorio';
    } else if (!/^\d{3,4}$/.test(cvv)) {
      newErrors.cvv = 'CVV inválido (3-4 dígitos)';
    }

    setErrors(newErrors);
    setTouched({ numero: true, titular: true, fechaVencimiento: true, cvv: true });
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (field: keyof CreateCardData, value: string) => {
    const newForm = { ...form, [field]: value };
    setForm(newForm);
    if (touched[field]) {
      validateField(field, newForm);
    }
  };

  const handleBlur = (field: keyof CreateCardData) => {
    setTouched({ ...touched, [field]: true });
    validateField(field, form);
  };

  const validateField = (field: keyof CreateCardData, currentForm: CreateCardData) => {
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
    setErrors(prev => ({ ...prev, [field]: error }));
  };

  const handleSubmit = async () => {
    if (!validateForm()) return;
    setSubmitting(true);
    setErrorMsg('');

    try {
      const response = await cardsApi.create({
        ...form,
        numero: form.numero.replace(/\s/g, ''),
      });

      if (response.exitoso && response.tarjeta) {
        setCards(prev => [...prev, response.tarjeta!]);
        setSuccessMsg(response.mensaje);
        setTimeout(() => {
          setSuccessMsg('');
          closeModal();
        }, 3000);
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

  const handleDelete = async (id: number) => {
    if (!window.confirm('¿Eliminar esta tarjeta?')) return;
    try {
      await cardsApi.delete(id);
      setCards(prev => prev.filter(c => c.id !== id));
    } catch {
      // ignore
    }
  };

  if (loading) {
    return (
      <div className='flex items-center justify-center h-64'>
        <div className='animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600' />
      </div>
    );
  }

  return (
    <div>
      <div className='flex items-center justify-between mb-6'>
        <div>
          <h1 className='text-2xl font-bold text-secondary-900'>Mis Tarjetas</h1>
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

      {cards.length === 0 ? (
        <div className='text-center py-16 bg-white rounded-xl border border-secondary-200'>
          <CreditCard className='w-12 h-12 text-secondary-300 mx-auto mb-4' />
          <h3 className='text-lg font-medium text-secondary-700'>No hay tarjetas</h3>
          <p className='text-secondary-500 mt-1'>Agrega una tarjeta de crédito para realizar pagos</p>
        </div>
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
                  onClick={() => handleDelete(card.id)}
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
              onClick={handleSubmit}
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
            value={form.numero}
            onChange={e => handleChange('numero', e.target.value)}
            onBlur={() => handleBlur('numero')}
            error={errors.numero}
            disabled={blocked}
            maxLength={19}
          />
          <Input
            label='Titular'
            placeholder='Juan Pérez'
            value={form.titular}
            onChange={e => handleChange('titular', e.target.value)}
            onBlur={() => handleBlur('titular')}
            error={errors.titular}
            disabled={blocked}
          />
          <div className='grid grid-cols-2 gap-4'>
            <Input
              label='Vencimiento'
              placeholder='MM/AA'
              value={form.fechaVencimiento}
              onChange={e => handleChange('fechaVencimiento', e.target.value)}
              onBlur={() => handleBlur('fechaVencimiento')}
              error={errors.fechaVencimiento}
              disabled={blocked}
              maxLength={5}
            />
            <Input
              label='CVV'
              placeholder='123'
              value={form.cvv}
              onChange={e => handleChange('cvv', e.target.value)}
              onBlur={() => handleBlur('cvv')}
              error={errors.cvv}
              disabled={blocked}
              maxLength={4}
              type='password'
            />
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default CardsPage;
