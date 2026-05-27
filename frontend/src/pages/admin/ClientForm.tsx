import { useState, FormEvent } from 'react';
import { User, Mail, FileText, Phone, MapPin, Globe, Lock } from 'lucide-react';
import { Input } from '../../components/ui';
import { Client, CreateClientData, UpdateClientData } from '../../api/clients';

interface ClientFormProps {
  initialData?: Client;
  onSubmit: (data: CreateClientData | UpdateClientData) => void | Promise<void>;
  isLoading: boolean;
  id?: string;
}

const ClientForm: React.FC<ClientFormProps> = ({ initialData, onSubmit, isLoading, id }) => {
  const [nombre, setNombre] = useState(initialData?.nombre || '');
  const [email, setEmail] = useState(initialData?.email || '');
  const [documentoIdentidad, setDocumentoIdentidad] = useState(initialData?.documentoIdentidad || '');
  const [telefono, setTelefono] = useState(initialData?.telefono || '');
  const [direccion, setDireccion] = useState(initialData?.direccion || '');
  const [contrasena, setContrasena] = useState('');
  const [region, setRegion] = useState(initialData?.region || '');
  const [error, setError] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    setError('');

    if (!nombre.trim() || !email.trim() || !documentoIdentidad.trim()) {
      setError('Nombre, email y documento de identidad son obligatorios');
      return;
    }

    if (!initialData && !contrasena.trim()) {
      setError('La contraseña es obligatoria para crear un cliente');
      return;
    }

    if (!initialData && contrasena.trim().length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    const data: Record<string, string | undefined> = {
      nombre: nombre.trim(),
      email: email.trim(),
      documentoIdentidad: documentoIdentidad.trim(),
      telefono: telefono.trim() || undefined,
      direccion: direccion.trim() || undefined,
      region: region.trim() || undefined,
    };

    if (!initialData) {
      data.contrasena = contrasena.trim();
    }

    onSubmit(data);
  };

  return (
    <form id={id} onSubmit={handleSubmit} className='space-y-4'>
      <Input
        label='Nombre Completo'
        value={nombre}
        onChange={(e) => setNombre(e.target.value)}
        placeholder='Juan Pérez'
        leftIcon={<User className='w-5 h-5' />}
        required
      />
      <Input
        label='Correo Electrónico'
        type='email'
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        placeholder='email@ejemplo.com'
        leftIcon={<Mail className='w-5 h-5' />}
        required
      />
      <Input
        label='Documento de Identidad'
        value={documentoIdentidad}
        onChange={(e) => setDocumentoIdentidad(e.target.value)}
        placeholder='1-2345-6789'
        leftIcon={<FileText className='w-5 h-5' />}
        required
      />
      <Input
        label='Teléfono'
        type='tel'
        value={telefono}
        onChange={(e) => setTelefono(e.target.value)}
        placeholder='8888-0000'
        leftIcon={<Phone className='w-5 h-5' />}
      />
      <Input
        label='Dirección'
        value={direccion}
        onChange={(e) => setDireccion(e.target.value)}
        placeholder='San José, Costa Rica'
        leftIcon={<MapPin className='w-5 h-5' />}
      />
      <Input
        label='Región'
        value={region}
        onChange={(e) => setRegion(e.target.value)}
        placeholder='San José'
        leftIcon={<Globe className='w-5 h-5' />}
      />

      {!initialData && (
        <Input
          label='Contraseña'
          type='password'
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          placeholder='Mínimo 6 caracteres'
          leftIcon={<Lock className='w-5 h-5' />}
          required
        />
      )}

      {error && (
        <div className='p-3 rounded-lg bg-error-50 border border-error-200'>
          <p className='text-sm text-error-700'>{error}</p>
        </div>
      )}

      <button type='submit' hidden disabled={isLoading} />
    </form>
  );
};

export default ClientForm;
