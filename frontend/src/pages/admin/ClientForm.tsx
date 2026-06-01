import { useState, FormEvent } from 'react';
import { User, Mail, FileText, Phone, MapPin, Globe, Lock } from 'lucide-react';
import { Input } from '../../components/ui';
import { Client, CreateClientData, UpdateClientData } from '../../api/clients';
import { validateField } from '../../utils/validation';

interface ClientFormProps {
  initialData?: Client;
  onSubmit: (data: CreateClientData | UpdateClientData) => void | Promise<void>;
  isLoading: boolean;
  id?: string;
  blocked?: boolean;
}

const ClientForm: React.FC<ClientFormProps> = ({ initialData, onSubmit, isLoading, id, blocked }) => {
  const [nombre, setNombre] = useState(initialData?.nombre || '');
  const [email, setEmail] = useState(initialData?.email || '');
  const [documentoIdentidad, setDocumentoIdentidad] = useState(initialData?.documentoIdentidad || '');
  const [telefono, setTelefono] = useState(initialData?.telefono || '');
  const [direccion, setDireccion] = useState(initialData?.direccion || '');
  const [contrasena, setContrasena] = useState('');
  const [region, setRegion] = useState(initialData?.region || '');
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [error, setError] = useState('');

  const handleBlur = (field: string, value: string, min?: number) => {
    const error = validateField(field, value, min ? { min } : undefined);
    setFieldErrors((prev) => ({ ...prev, [field]: error }));
  };

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    setError('');

    const errors: Record<string, string> = {
      nombre: validateField('nombre', nombre, { min: 2 }),
      email: validateField('email', email),
      documentoIdentidad: validateField('documentoIdentidad', documentoIdentidad),
      telefono: validateField('telefono', telefono),
    };

    if (!initialData) {
      errors.contrasena = validateField('contrasena', contrasena);
    }

    setFieldErrors(errors);

    const hasErrors = Object.values(errors).some((e) => e);
    if (hasErrors) return;

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
        onBlur={() => handleBlur('nombre', nombre, 2)}
        placeholder='Juan Pérez'
        leftIcon={<User className='w-5 h-5' />}
        error={fieldErrors.nombre}
        required
        disabled={blocked}
      />
      <Input
        label='Correo Electrónico'
        type='email'
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        onBlur={() => handleBlur('email', email)}
        placeholder='email@ejemplo.com'
        leftIcon={<Mail className='w-5 h-5' />}
        error={fieldErrors.email}
        required
        disabled={blocked}
      />
      <Input
        label='Documento de Identidad'
        value={documentoIdentidad}
        onChange={(e) => setDocumentoIdentidad(e.target.value)}
        onBlur={() => handleBlur('documentoIdentidad', documentoIdentidad)}
        placeholder='123456789'
        leftIcon={<FileText className='w-5 h-5' />}
        error={fieldErrors.documentoIdentidad}
        required
        disabled={blocked}
      />
      <Input
        label='Teléfono'
        type='tel'
        value={telefono}
        onChange={(e) => setTelefono(e.target.value)}
        onBlur={() => handleBlur('telefono', telefono)}
        placeholder='8888-0000'
        leftIcon={<Phone className='w-5 h-5' />}
        error={fieldErrors.telefono}
        disabled={blocked}
      />
      <Input
        label='Dirección'
        value={direccion}
        onChange={(e) => setDireccion(e.target.value)}
        placeholder='San José, Costa Rica'
        leftIcon={<MapPin className='w-5 h-5' />}
        disabled={blocked}
      />
      <Input
        label='Región'
        value={region}
        onChange={(e) => setRegion(e.target.value)}
        onBlur={() => handleBlur('region', region)}
        placeholder='San José'
        leftIcon={<Globe className='w-5 h-5' />}
        error={fieldErrors.region}
        disabled={blocked}
      />

      {!initialData && (
        <Input
          label='Contraseña'
          type='password'
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          onBlur={() => handleBlur('contrasena', contrasena)}
          placeholder='Mínimo 6 caracteres'
          leftIcon={<Lock className='w-5 h-5' />}
          error={fieldErrors.contrasena}
          required
          disabled={blocked}
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
