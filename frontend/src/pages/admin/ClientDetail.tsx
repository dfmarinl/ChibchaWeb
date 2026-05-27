import { User, Mail, FileText, Phone, MapPin, Globe, Calendar, Server } from 'lucide-react';
import { Client } from '../../api/clients';

interface ClientDetailProps {
  client: Client;
}

const DetailRow: React.FC<{ icon: React.ReactNode; label: string; value: string }> = ({
  icon,
  label,
  value,
}) => (
  <div className='flex items-start gap-3 py-3 border-b border-secondary-100 last:border-b-0'>
    <div className='p-2 rounded-lg bg-primary-50 text-primary-600'>{icon}</div>
    <div>
      <p className='text-xs text-secondary-500 uppercase tracking-wide'>{label}</p>
      <p className='text-sm font-medium text-secondary-900 mt-0.5'>{value}</p>
    </div>
  </div>
);

const ClientDetail: React.FC<ClientDetailProps> = ({ client }) => {
  return (
    <div className='divide-y divide-secondary-100'>
      <DetailRow icon={<User className='w-4 h-4' />} label='Nombre' value={client.nombre} />
      <DetailRow icon={<Mail className='w-4 h-4' />} label='Email' value={client.email} />
      <DetailRow icon={<FileText className='w-4 h-4' />} label='Documento Identidad' value={client.documentoIdentidad} />
      <DetailRow icon={<Phone className='w-4 h-4' />} label='Teléfono' value={client.telefono || '-'} />
      <DetailRow icon={<MapPin className='w-4 h-4' />} label='Dirección' value={client.direccion || '-'} />
      <DetailRow icon={<Globe className='w-4 h-4' />} label='Región' value={client.region || '-'} />
      <DetailRow icon={<Calendar className='w-4 h-4' />} label='Fecha Registro' value={new Date(client.fechaRegistro).toLocaleDateString('es-CR', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' })} />
      <DetailRow icon={<Server className='w-4 h-4' />} label='Límite de Sitios' value={String(client.limitesSitios)} />
    </div>
  );
};

export default ClientDetail;
