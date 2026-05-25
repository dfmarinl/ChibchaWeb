import { Card } from '../ui';
import { LucideIcon } from 'lucide-react';

interface StatsCardProps {
  title: string;
  value: string | number;
  icon: LucideIcon;
  change?: string;
  changeType?: 'increase' | 'decrease' | 'neutral';
  iconColor?: string;
}

const StatsCard: React.FC<StatsCardProps> = ({
  title,
  value,
  icon: Icon,
  change,
  changeType = 'neutral',
  iconColor = 'text-primary-600',
}) => {
  const changeColors = {
    increase: 'text-success-600',
    decrease: 'text-error-600',
    neutral: 'text-secondary-600',
  };

  return (
    <Card variant='bordered'>
      <div className='flex items-start justify-between'>
        <div>
          <p className='text-sm font-medium text-secondary-600'>{title}</p>
          <p className='mt-2 text-3xl font-bold text-secondary-900'>{value}</p>
          {change && (
            <p className={`mt-1 text-sm ${changeColors[changeType]}`}>
              {change}
            </p>
          )}
        </div>
        <div className={`p-3 rounded-lg bg-secondary-100 ${iconColor}`}>
          <Icon className='w-6 h-6' />
        </div>
      </div>
    </Card>
  );
};

export default StatsCard;
