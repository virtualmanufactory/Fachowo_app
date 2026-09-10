import { type FormEvent, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api/client'
import type { AuthResponse } from '../api/types'
import { ActionButton } from '../components/ActionButton'
import { useAuth } from '../auth'

const fullButton = 'w-full rounded-lg bg-teal-700 py-2 font-medium text-white disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  async function onSubmit(event: FormEvent) {
    event.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const res = await api.post<AuthResponse>('/auth/login', { email, password })
      await login(res.data.token)
      navigate('/panel')
    } catch {
      setError('Nieprawidłowy e-mail lub hasło')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="mx-auto max-w-md rounded-xl border border-stone-200 bg-white p-6">
      <h1 className="text-2xl font-semibold">Logowanie</h1>
      <form onSubmit={onSubmit} className="mt-4 space-y-3">
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="email"
          required
          placeholder="E-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="password"
          required
          placeholder="Hasło"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        {error ? <p className="text-sm text-rose-700">{error}</p> : null}
        <ActionButton className={fullButton} loading={loading} loadingLabel="Logowanie…">
          Zaloguj się
        </ActionButton>
      </form>
      <p className="mt-4 text-sm text-stone-600">
        <Link to="/reset-hasla" className="text-teal-800">
          Nie pamiętasz hasła?
        </Link>
      </p>
      <p className="mt-2 text-sm text-stone-600">
        Nie masz konta?{' '}
        <Link to="/rejestracja" className="text-teal-800">
          Zarejestruj firmę
        </Link>
      </p>
    </div>
  )
}
