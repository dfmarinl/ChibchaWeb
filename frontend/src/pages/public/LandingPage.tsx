import { Link } from 'react-router-dom';
import { Server, Globe, Shield, Headphones, Cloud, RefreshCw, CheckCircle, ArrowRight } from 'lucide-react';
import { ROUTES, APP_NAME } from '../../constants';

const features = [
  { icon: Server, title: 'Hosting Compartido', desc: 'Planes optimizados para tu sitio web con alto rendimiento y estabilidad.' },
  { icon: Globe, title: 'Registro de Dominios', desc: 'Encuentra y registra el dominio perfecto para tu marca o negocio.' },
  { icon: Shield, title: 'SSL Incluido', desc: 'Certificado SSL gratuito en todos los planes para máxima seguridad.' },
  { icon: Headphones, title: 'Soporte 24/7', desc: 'Equipo de soporte técnico disponible todos los días del año.' },
  { icon: Cloud, title: 'Backups Automáticos', desc: 'Copias de seguridad diarias para que nunca pierdas tu información.' },
  { icon: RefreshCw, title: 'Migración Gratuita', desc: 'Te ayudamos a migrar tu sitio web sin costo desde cualquier proveedor.' },
];

const plans = [
  {
    name: 'Básico',
    price: '$5',
    period: '/mes',
    desc: 'Perfecto para empezar',
    features: ['1 Sitio Web', '10 GB Almacenamiento', '50 GB Transferencia', '5 Cuentas de Correo', 'SSL Gratuito'],
    highlighted: false,
  },
  {
    name: 'Profesional',
    price: '$15',
    period: '/mes',
    desc: 'Para negocios en crecimiento',
    features: ['10 Sitios Web', '50 GB Almacenamiento', '500 GB Transferencia', '50 Cuentas de Correo', 'SSL Gratuito', 'Dominio Gratis'],
    highlighted: true,
  },
  {
    name: 'Empresarial',
    price: '$30',
    period: '/mes',
    desc: 'Solución empresarial completa',
    features: ['Sitios Ilimitados', '200 GB Almacenamiento', 'Transferencia Ilimitada', 'Correos Ilimitados', 'SSL Gratuito', 'Prioridad Soporte'],
    highlighted: false,
  },
];

const LandingPage: React.FC = () => {
  return (
    <div className='min-h-screen bg-white'>
      {/* Navbar */}
      <header className='fixed top-0 left-0 right-0 z-50 bg-white/80 backdrop-blur-md border-b border-secondary-100'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8 h-16 flex items-center justify-between'>
          <Link to='/' className='flex items-center gap-2.5'>
            <div className='w-9 h-9 bg-gradient-to-br from-primary-600 to-primary-700 rounded-lg flex items-center justify-center'>
              <Server className='w-5 h-5 text-white' />
            </div>
            <span className='text-lg font-bold text-secondary-900'>{APP_NAME}</span>
          </Link>
          <div className='flex items-center gap-3'>
            <Link
              to={ROUTES.PUBLIC.LOGIN}
              className='px-4 py-2 text-sm font-medium text-secondary-700 hover:text-secondary-900 transition-colors'
            >
              Iniciar Sesión
            </Link>
            <Link
              to={ROUTES.PUBLIC.REGISTER}
              className='px-4 py-2 text-sm font-medium text-white bg-primary-600 rounded-lg hover:bg-primary-700 transition-colors'
            >
              Registrarse
            </Link>
          </div>
        </div>
      </header>

      {/* Hero */}
      <section className='pt-32 pb-20 lg:pt-40 lg:pb-28 relative overflow-hidden'>
        <div className='absolute inset-0 bg-gradient-to-br from-primary-50 via-white to-accent-50' />
        <div className='absolute top-0 left-1/2 -translate-x-1/2 w-[800px] h-[800px] bg-gradient-to-br from-primary-200/30 to-accent-200/30 rounded-full blur-3xl' />
        <div className='relative max-w-7xl mx-auto px-4 lg:px-8 text-center'>
          <h1 className='text-4xl lg:text-6xl font-bold text-secondary-900 leading-tight'>
            Tu hosting web
            <span className='text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-accent-600'> de confianza</span>
          </h1>
          <p className='mt-6 text-lg lg:text-xl text-secondary-600 max-w-2xl mx-auto leading-relaxed'>
            Soluciones de alojamiento web confiables y rápidas para tu negocio. 
            Registra tu dominio, elige tu plan y haz crecer tu presencia en línea.
          </p>
          <div className='mt-10 flex items-center justify-center gap-4'>
            <Link
              to={ROUTES.PUBLIC.REGISTER}
              className='flex items-center gap-2 px-6 py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition-colors text-sm font-medium'
            >
              Comenzar Ahora
              <ArrowRight className='w-4 h-4' />
            </Link>
            <Link
              to={ROUTES.PUBLIC.LOGIN}
              className='px-6 py-3 bg-white text-secondary-700 border border-secondary-300 rounded-lg hover:bg-secondary-50 transition-colors text-sm font-medium'
            >
              Iniciar Sesión
            </Link>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className='py-20 lg:py-28'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8'>
          <div className='text-center mb-16'>
            <h2 className='text-3xl lg:text-4xl font-bold text-secondary-900'>Todo lo que necesitas</h2>
            <p className='mt-4 text-secondary-500 max-w-xl mx-auto'>
              Ofrecemos herramientas y servicios diseñados para impulsar tu presencia en línea.
            </p>
          </div>
          <div className='grid md:grid-cols-2 lg:grid-cols-3 gap-8'>
            {features.map((feature) => (
              <div
                key={feature.title}
                className='group p-6 rounded-xl border border-secondary-200 hover:border-primary-200 hover:shadow-sm transition-all'
              >
                <div className='w-12 h-12 bg-primary-50 rounded-lg flex items-center justify-center group-hover:bg-primary-100 transition-colors'>
                  <feature.icon className='w-6 h-6 text-primary-600' />
                </div>
                <h3 className='mt-4 text-lg font-semibold text-secondary-900'>{feature.title}</h3>
                <p className='mt-2 text-secondary-500 text-sm leading-relaxed'>{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Plans */}
      <section className='py-20 lg:py-28 bg-secondary-50'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8'>
          <div className='text-center mb-16'>
            <h2 className='text-3xl lg:text-4xl font-bold text-secondary-900'>Planes de Hosting</h2>
            <p className='mt-4 text-secondary-500 max-w-xl mx-auto'>
              Elegí el plan que mejor se adapte a tus necesidades. Todos incluyen SSL y soporte técnico.
            </p>
          </div>
          <div className='grid md:grid-cols-3 gap-8 max-w-5xl mx-auto'>
            {plans.map((plan) => (
              <div
                key={plan.name}
                className={`relative rounded-xl border p-8 ${
                  plan.highlighted
                    ? 'border-primary-300 bg-white shadow-md'
                    : 'border-secondary-200 bg-white'
                }`}
              >
                {plan.highlighted && (
                  <div className='absolute -top-3 left-1/2 -translate-x-1/2 px-3 py-1 bg-primary-600 text-white text-xs font-medium rounded-full'>
                    Más Popular
                  </div>
                )}
                <h3 className='text-lg font-semibold text-secondary-900'>{plan.name}</h3>
                <p className='text-sm text-secondary-500 mt-1'>{plan.desc}</p>
                <div className='mt-6 flex items-baseline gap-1'>
                  <span className='text-4xl font-bold text-secondary-900'>{plan.price}</span>
                  <span className='text-secondary-500'>{plan.period}</span>
                </div>
                <ul className='mt-8 space-y-3'>
                  {plan.features.map((feat) => (
                    <li key={feat} className='flex items-center gap-2 text-sm text-secondary-600'>
                      <CheckCircle className='w-4 h-4 text-success-500 flex-shrink-0' />
                      {feat}
                    </li>
                  ))}
                </ul>
                <Link
                  to={ROUTES.PUBLIC.REGISTER}
                  className={`mt-8 flex items-center justify-center gap-2 w-full py-2.5 rounded-lg text-sm font-medium transition-colors ${
                    plan.highlighted
                      ? 'bg-primary-600 text-white hover:bg-primary-700'
                      : 'bg-white text-primary-600 border border-primary-300 hover:bg-primary-50'
                  }`}
                >
                  Seleccionar Plan
                  <ArrowRight className='w-4 h-4' />
                </Link>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Contact */}
      <section className='py-20 lg:py-28'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8 text-center'>
          <h2 className='text-3xl lg:text-4xl font-bold text-secondary-900'>Contáctanos</h2>
          <p className='mt-4 text-secondary-500 max-w-xl mx-auto'>
            ¿Tenés preguntas? Estamos aquí para ayudarte.
          </p>
          <div className='mt-12 flex flex-col sm:flex-row items-center justify-center gap-8 text-secondary-600'>
            <div className='flex items-center gap-3'>
              <div className='w-10 h-10 bg-primary-50 rounded-full flex items-center justify-center'>
                <Server className='w-5 h-5 text-primary-600' />
              </div>
              <span className='text-sm'>info@chibchaweb.com</span>
            </div>
            <div className='flex items-center gap-3'>
              <div className='w-10 h-10 bg-primary-50 rounded-full flex items-center justify-center'>
                <Server className='w-5 h-5 text-primary-600' />
              </div>
              <span className='text-sm'>+506 2222-3333</span>
            </div>
            <div className='flex items-center gap-3'>
              <div className='w-10 h-10 bg-primary-50 rounded-full flex items-center justify-center'>
                <Server className='w-5 h-5 text-primary-600' />
              </div>
              <span className='text-sm'>San José, Costa Rica</span>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className='py-12 bg-secondary-900 text-secondary-400'>
        <div className='max-w-7xl mx-auto px-4 lg:px-8'>
          <div className='flex flex-col md:flex-row items-center justify-between gap-4'>
            <div className='flex items-center gap-2.5'>
              <div className='w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-600 rounded-lg flex items-center justify-center'>
                <Server className='w-4 h-4 text-white' />
              </div>
              <span className='text-base font-bold text-white'>{APP_NAME}</span>
            </div>
            <p className='text-sm'>
              &copy; {new Date().getFullYear()} {APP_NAME}. Todos los derechos reservados.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

export default LandingPage;
