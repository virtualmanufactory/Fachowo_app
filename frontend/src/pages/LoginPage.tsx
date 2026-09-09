import { type FormEvent, useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { api } from '../api/client'
import type { AuthResponse } from '../api/types'
import { useAuth } from '../auth'

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | null>(null)

  async function onSubmit(event: FormEvent) {
    event.preventDefault()
    setError(null)
    try {
      const res = await api.post<AuthResponse>('/auth/login', { email, password })
      await login(res.data.token)
      navigate('/panel')
    } catch {
      setError('Nieprawidłowy e-mail lub hasło')
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
        <button type="submit" className="w-full rounded-lg bg-teal-700 py-2 font-medium text-white">
          Zaloguj się
        </button>
      </form>
      <p className="mt-4 text-sm text-stone-600">
        Nie masz konta?{' '}
        <Link to="/rejestracja" className="text-teal-800">
          Zarejestruj firmę
        </Link>
      </p>
    </div>
  )
}
