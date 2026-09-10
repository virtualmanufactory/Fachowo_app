import { type FormEvent, useState } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
import { api } from '../api/client'
import { apiErrorMessage } from '../api/errors'
import { ActionButton } from '../components/ActionButton'

const fullButton = 'w-full rounded-lg bg-teal-700 py-2 font-medium text-white disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'

export function ResetPasswordPage() {
  const [params] = useSearchParams()
  const navigate = useNavigate()
  const token = params.get('token') ?? ''
  const [password, setPassword] = useState('')
  const [repeat, setRepeat] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function onSubmit(event: FormEvent) {
    event.preventDefault()
    setError(null)
    if (password !== repeat) {
      setError('Hasła nie są takie same.')
      return
    }
    setLoading(true)
    try {
      await api.post('/auth/reset-password', { token, password })
      navigate('/logowanie')
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się ustawić nowego hasła.'))
    } finally {
      setLoading(false)
    }
  }

  if (!token) {
    return (
      <div className="mx-auto max-w-md rounded-xl border border-stone-200 bg-white p-6">
        <h1 className="text-2xl font-semibold">Reset hasła</h1>
        <p className="mt-3 text-sm text-stone-600">Brak tokenu. Poproś o nowy link.</p>
        <Link to="/reset-hasla" className="mt-4 inline-block text-sm text-teal-800">
          Wyślij link ponownie
        </Link>
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-md rounded-xl border border-stone-200 bg-white p-6">
      <h1 className="text-2xl font-semibold">Nowe hasło</h1>
      <form onSubmit={onSubmit} className="mt-4 space-y-3">
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="password"
          required
          minLength={8}
          placeholder="Nowe hasło (min. 8 znaków)"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="password"
          required
          minLength={8}
          placeholder="Powtórz hasło"
          value={repeat}
          onChange={(e) => setRepeat(e.target.value)}
        />
        {error ? <p className="text-sm text-rose-700">{error}</p> : null}
        <ActionButton className={fullButton} loading={loading} loadingLabel="Zapisywanie…">
          Zapisz hasło
        </ActionButton>
      </form>
    </div>
  )
}
