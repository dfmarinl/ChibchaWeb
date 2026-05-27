import { useState, FormEvent } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Server, Mail, Lock, Eye, EyeOff } from 'lucide-react';
import { Button, Input, Card, CardHeader, CardTitle, CardDescription, CardContent } from '../../components/ui';
import { APP_NAME } from '../../constants';
import { useAuth } from '../../hooks';
import { validators } from '../../utils/validation';

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { login, isLoading } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [error, setError] = useState('');

  const handleBlur = (field: string, value: string) => {
    const err = field === 'email' ? validators.email(value) : validators.required(value, 'La contraseña');
    setFieldErrors((prev) => ({ ...prev, [field]: err }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');

    const errors: Record<string, string> = {
      email: validators.email(email),
      password: validators.required(password, 'La contraseña'),
    };

    setFieldErrors(errors);
    if (Object.values(errors).some((e) => e)) return;

    try {
      const redirectPath = await login(email, password);
      navigate(redirectPath, { replace: true });
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al iniciar sesión');
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
              {/* Email */}
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
              />

              {/* Password */}
              <div className='relative'>
                <Input
                  label='Contraseña'
                  type={showPassword ? 'text' : 'password'}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  onBlur={() => handleBlur('password', password)}
                  placeholder='••••••••'
                  leftIcon={<Lock className='w-5 h-5' />}
                  error={fieldErrors.password}
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

              {/* Link to Register */}
              <p className='text-center text-sm text-secondary-600'>
                ¿No tienes cuenta?{' '}
                <Link to='/register' className='text-primary-600 hover:text-primary-700 font-medium'>
                  Regístrate aquí
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

export default LoginPage;
