export const validators = {
  required: (value: string, label: string): string =>
    !value.trim() ? `${label} es obligatorio` : '',

  minLength: (value: string, min: number, label: string): string =>
    value.trim().length < min ? `${label} debe tener al menos ${min} caracteres` : '',

  email: (value: string): string => {
    if (!value.trim()) return 'El correo es obligatorio';
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value.trim())
      ? ''
      : 'Ingrese un correo electrónico válido';
  },

  documentoIdentidad: (value: string): string => {
    if (!value.trim()) return 'El documento de identidad es obligatorio';
    return /^\d+$/.test(value.trim()) ? '' : 'Documento no válido (solo números)';
  },

  telefono: (value: string): string => {
    if (!value.trim()) return '';
    return /^\d{7,}$/.test(value.trim()) ? '' : 'Teléfono no válido (solo números, mínimo 7)';
  },

  region: (value: string): string => {
    if (!value.trim()) return '';
    return /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(value.trim()) ? '' : 'Región no válida (solo letras)';
  },

  contrasena: (value: string): string => {
    if (!value.trim()) return 'La contraseña es obligatoria';
    return value.length < 6 ? 'La contraseña debe tener al menos 6 caracteres' : '';
  },
};

export const validateField = (
  field: string,
  value: string,
  extra?: { label?: string; min?: number }
): string => {
  switch (field) {
    case 'nombre':
      return validators.required(value, 'El nombre') || validators.minLength(value, extra?.min || 2, 'El nombre');
    case 'email':
      return validators.email(value);
    case 'documentoIdentidad':
      return validators.documentoIdentidad(value);
    case 'contrasena':
      return validators.contrasena(value);
    case 'telefono':
      return validators.telefono(value);
    case 'region':
      return validators.region(value);
    default:
      return '';
  }
};
