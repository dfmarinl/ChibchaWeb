import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { CheckCircle, User } from 'lucide-react';
import { Card, Button } from '../../components/ui';
import { PageHeader } from '../../components/common';
import ClientForm from '../admin/ClientForm';
import { clientsApi, UpdateClientData } from '../../api/clients';
import { Client } from '../../api/clients';
import { ROUTES } from '../../constants';

const ClientProfile: React.FC = () => {
  const navigate = useNavigate();
  const [client, setClient] = useState<Client | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (!userData) return;

    const user = JSON.parse(userData);
    const clientId = Number(user.id);
    if (!clientId) return;

    clientsApi
      .getById(clientId)
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
        description='Actualiza tu información personal'
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
    </div>
  );
};

export default ClientProfile;
