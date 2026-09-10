type ActionButtonProps = {
  children: string
  loading?: boolean
  loadingLabel?: string
  disabled?: boolean
  className?: string
  type?: 'submit' | 'button'
  onClick?: () => void
}

const defaultClass =
  'rounded-lg bg-teal-700 px-4 py-2 text-sm font-medium text-white hover:bg-teal-800 disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'

export function ActionButton({
  children,
  loading = false,
  loadingLabel = 'Wysyłanie…',
  disabled,
  className = defaultClass,
  type = 'submit',
  onClick,
}: ActionButtonProps) {
  return (
    <button type={type} onClick={onClick} disabled={disabled || loading} className={className}>
      {loading ? loadingLabel : children}
    </button>
  )
}
