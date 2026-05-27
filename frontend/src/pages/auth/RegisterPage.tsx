import { useState, FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Server, Mail, Lock, User, Phone, MapPin, FileText, Globe } from 'lucide-react';
import { Button, Input, Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../components/ui';
import { APP_NAME } from '../../constants';
import { useAuth } from '../../hooks';

const RegisterPage: React.FC = () => {
  const navigate = useNavigate();
  const { register, isLoading } = useAuth();
  const [nombre, setNombre] = useState('');
  const [email, setEmail] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [telefono, setTelefono] = useState('');
  const [direccion, setDireccion] = useState('');
  const [documentoIdentidad, setDocumentoIdentidad] = useState('');
  const [region, setRegion] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');

    if (!nombre || !email || !contrasena || !documentoIdentidad) {
      setError('Complete los campos obligatorios');
      return;
    }

    if (contrasena.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      return;
    }

    try {
      const redirectPath = await register({
        nombre,
        email,
        contrasena,
        telefono: telefono || undefined,
        direccion: direccion || undefined,
        documentoIdentidad,
        region: region || undefined,
      });
      navigate(redirectPath, { replace: true });
    } catch (err) {
      if (err instanceof Error) {
        const msg = err.message.toLowerCase();
        if (msg.includes('email')) {
          setError('El correo electrónico ya está registrado');
        } else if (msg.includes('documento')) {
          setError('El documento de identidad ya está registrado');
        } else {
          setError(err.message);
        }
      } else {
        setError('Error al registrar usuario');
      }
    }
  };

  return (
    <div className='min-h-screen bg-gradient-to-br from-primary-50 via-white to-secondary-50 flex items-center justify-center p-4'>
      <div className='w-full max-w-md'>
        {/* Logo Section */}
        <div className='text-center mb-8'>
          <div className='inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-primary-600 to-primary-700 rounded-2xl shadow-lg mb-4'>
            <Server className='w-8 h-8 text-white' />
          </div>
          <h1 className='text-3xl font-bold text-secondary-900'>{APP_NAME}</h1>
          <p className='text-secondary-600 mt-2'>Crear una cuenta nueva</p>
        </div>

        {/* Register Card */}
        <Card variant='bordered' className='shadow-xl'>
          <CardHeader>
            <CardTitle className='text-center'>Registro</CardTitle>
            <CardDescription className='text-center'>
              Complete los datos para crear su cuenta
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className='space-y-4'>
              {/* Nombre */}
              <Input
                label='Nombre Completo'
                type='text'
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
                placeholder='Juan Pérez'
                leftIcon={<User className='w-5 h-5' />}
                required
              />

              {/* Email */}
              <Input
                label='Correo Electrónico'
                type='email'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder='email@ejemplo.com'
                leftIcon={<Mail className='w-5 h-5' />}
                required
              />

              {/* Contraseña */}
              <Input
                label='Contraseña'
                type='password'
                value={contrasena}
                onChange={(e) => setContrasena(e.target.value)}
                placeholder='Mínimo 6 caracteres'
                leftIcon={<Lock className='w-5 h-5' />}
                required
              />

              {/* Documento Identidad */}
              <Input
                label='Documento de Identidad'
                type='text'
                value={documentoIdentidad}
                onChange={(e) => setDocumentoIdentidad(e.target.value)}
                placeholder='1-2345-6789'
                leftIcon={<FileText className='w-5 h-5' />}
                required
              />

              {/* Teléfono */}
              <Input
                label='Teléfono'
                type='tel'
                value={telefono}
                onChange={(e) => setTelefono(e.target.value)}
                placeholder='8888-0000'
                leftIcon={<Phone className='w-5 h-5' />}
              />

              {/* Dirección */}
              <Input
                label='Dirección'
                type='text'
                value={direccion}
                onChange={(e) => setDireccion(e.target.value)}
                placeholder='San José, Costa Rica'
                leftIcon={<MapPin className='w-5 h-5' />}
              />

              {/* Región */}
              <Input
                label='Región'
                type='text'
                value={region}
                onChange={(e) => setRegion(e.target.value)}
                placeholder='San José'
                leftIcon={<Globe className='w-5 h-5' />}
              />

              {/* Error Message */}
              {error && (
                <div className='p-3 rounded-lg bg-error-50 border border-error-200'>
                  <p className='text-sm text-error-700'>{error}</p>
                </div>
              )}

              {/* Submit Button */}
              <Button
                type='submit'
                variant='primary'
                size='lg'
                className='w-full'
                isLoading={isLoading}
              >
                Crear Cuenta
              </Button>

              {/* Link to Login */}
              <p className='text-center text-sm text-secondary-600 mt-4'>
                ¿Ya tienes cuenta?{' '}
                <Link to='/login' className='text-primary-600 hover:text-primary-700 font-medium'>
                  Inicia sesión aquí
                </Link>
              </p>
            </form>
          </CardContent>
        </Card>

        {/* Footer */}
        <p className='text-center text-sm text-secondary-500 mt-6'>
          © 2024 {APP_NAME}. Todos los derechos reservados.
        </p>
      </div>
    </div>
  );
};

export default RegisterPage;
