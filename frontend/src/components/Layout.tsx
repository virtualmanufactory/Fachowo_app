import { Link, NavLink, Outlet, useNavigate } from 'react-router-dom'
import { type FormEvent, useState } from 'react'
import { useAuth } from '../auth'

export function Layout() {
  const { me, logout } = useAuth()
  const [query, setQuery] = useState('')
  const navigate = useNavigate()

  function onSearch(event: FormEvent) {
    event.preventDefault()
    const q = query.trim()
    if (q) {
      navigate(`/szukaj?q=${encodeURIComponent(q)}`)
    }
  }

  return (
    <div className="min-h-screen">
      <header className="border-b border-stone-200 bg-white">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center gap-4 px-4 py-3">
          <Link to="/" className="text-xl font-semibold tracking-tight text-teal-800">
            Fachowo
          </Link>
          <form onSubmit={onSearch} className="min-w-56 flex-1">
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Szukaj usługi, firmy, miasta…"
              className="w-full rounded-lg border border-stone-300 bg-stone-50 px-3 py-2 text-sm outline-none focus:border-teal-600"
            />
          </form>
          <nav className="flex items-center gap-3 text-sm">
            {me ? (
              <>
                <NavLink to="/panel" className="text-stone-700 hover:text-teal-800">
                  Moja firma
                </NavLink>
                <button type="button" onClick={logout} className="text-stone-500 hover:text-stone-800">
                  Wyloguj
                </button>
              </>
            ) : (
              <>
                <NavLink to="/logowanie" className="text-stone-700 hover:text-teal-800">
                  Logowanie
                </NavLink>
                <NavLink
                  to="/rejestracja"
                  className="rounded-lg bg-teal-700 px-3 py-1.5 font-medium text-white hover:bg-teal-800"
                >
                  Dodaj firmę
                </NavLink>
              </>
            )}
          </nav>
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-4 py-8">
        <Outlet />
      </main>
    </div>
  )
}
