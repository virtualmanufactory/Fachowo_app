import { type FormEvent, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client'
import { apiErrorMessage } from '../api/errors'
import { ActionButton } from '../components/ActionButton'

const fullButton = 'w-full rounded-lg bg-teal-700 py-2 font-medium text-white disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'

export function ForgotPasswordPage() {
  const [email, setEmail] = useState('')
  const [message, setMessage] = useState<string | null>(null)
  const [resetToken, setResetToken] = useState<string | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function onSubmit(event: FormEvent) {
    event.preventDefault()
    setError(null)
    setMessage(null)
    setResetToken(null)
    setLoading(true)
    try {
      const res = await api.post<{ message: string; resetToken?: string }>('/auth/forgot-password', { email })
      setMessage(res.data.message)
      setResetToken(res.data.resetToken ?? null)
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się wysłać prośby o reset hasła.'))
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-md rounded-xl border border-stone-200 bg-white p-6">
      <h1 className="text-2xl font-semibold">Reset hasła</h1>
      <p className="mt-1 text-sm text-stone-600">Podaj e-mail konta. Jeśli istnieje, przygotujemy link do nowego hasła.</p>
      <form onSubmit={onSubmit} className="mt-4 space-y-3">
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="email"
          required
          placeholder="E-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        {error ? <p className="text-sm text-rose-700">{error}</p> : null}
        {message ? <p className="text-sm text-teal-800">{message}</p> : null}
        {resetToken ? (
          <p className="text-sm text-stone-700">
            Tryb lokalny:{' '}
            <Link className="text-teal-800 underline" to={`/reset-hasla/nowe?token=${encodeURIComponent(resetToken)}`}>
              ustaw nowe hasło
            </Link>
          </p>
        ) : null}
        <ActionButton className={fullButton} loading={loading} loadingLabel="Wysyłanie…">
          Wyślij link
        </ActionButton>
      </form>
      <p className="mt-4 text-sm text-stone-600">
        <Link to="/logowanie" className="text-teal-800">
          Wróć do logowania
        </Link>
      </p>
    </div>
  )
}
