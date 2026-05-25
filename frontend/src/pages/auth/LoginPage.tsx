import { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { Server, Mail, Lock, Shield, Eye, EyeOff } from 'lucide-react';
import { Button, Input, Select, Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../components/ui';
import { APP_NAME, USER_ROLES } from '../../constants';
import { useAuth } from '../../hooks';
import { UserRole } from '../../constants';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { login, isLoading } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<UserRole>(USER_ROLES.CLIENT);
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');

    if (!email || !password) {
      setError('Por favor complete todos los campos');
      return;
    }

    try {
      const redirectPath = await login(email, password, role);
      navigate(redirectPath, { replace: true });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al iniciar sesión');
    }
  };

  const roles = [
    { value: USER_ROLES.ADMIN, label: 'Administrador' },
    { value: USER_ROLES.CLIENT, label: 'Cliente' },
    { value: USER_ROLES.EMPLOYEE, label: 'Empleado' },
  ];

  const getEmailPlaceholder = () => {
    switch (role) {
      case USER_ROLES.ADMIN:
        return 'admin@chibchaweb.com';
      case USER_ROLES.CLIENT:
        return 'cliente1@empresa.com';
      case USER_ROLES.EMPLOYEE:
        return 'empleado1@chibchaweb.com';
      default:
        return 'email@ejemplo.com';
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
          <p className='text-secondary-600 mt-2'>Plataforma de Gestión de Hosting</p>
        </div>

        {/* Login Card */}
        <Card variant='bordered' className='shadow-xl'>
          <CardHeader>
            <CardTitle className='text-center'>Iniciar Sesión</CardTitle>
            <CardDescription className='text-center'>
              Ingrese sus credenciales para acceder al sistema
            </CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSubmit} className='space-y-5'>
              {/* Role Selector */}
              <Select
                label='Seleccionar Rol'
                value={role}
                onChange={(e) => setRole(e.target.value as UserRole)}
                leftIcon={<Shield className='w-5 h-5' />}
              >
                {roles.map((r) => (
                  <option key={r.value} value={r.value}>
                    {r.label}
                  </option>
                ))}
              </Select>

              {/* Email */}
              <Input
                label='Correo Electrónico'
                type='email'
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder={getEmailPlaceholder()}
                leftIcon={<Mail className='w-5 h-5' />}
                required
              />

              {/* Password */}
              <div className='relative'>
                <Input
                  label='Contraseña'
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder='••••••••'
                  leftIcon={<Lock className='w-5 h-5' />}
                  required
                />
                <button
                  type='button'
                  onClick={() => setShowPassword(!showPassword)}
                  className='absolute right-3 top-9 text-secondary-500 hover:text-secondary-700'
                >
                  {showPassword ? <EyeOff className='w-5 h-5' /> : <Eye className='w-5 h-5' />}
                </button>
              </div>

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
                Iniciar Sesión
              </Button>
            </form>

            {/* Demo Credentials Info */}
            <div className='mt-6 pt-6 border-t border-secondary-200'>
              <p className='text-xs text-secondary-600 mb-2 font-medium'>Credenciales de prueba:</p>
              <div className='space-y-1 text-xs text-secondary-500'>
                <p><span className='font-medium'>Admin:</span> admin@chibchaweb.com</p>
                <p><span className='font-medium'>Cliente:</span> cliente1@empresa.com</p>
                <p><span className='font-medium'>Empleado:</span> empleado1@chibchaweb.com</p>
                <p className='mt-2 italic'>Cualquier contraseña es válida en modo demo</p>
              </div>
            </div>
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

export default LoginPage;
