import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api } from './api/client'
import type { MeResponse } from './api/types'

type AuthState = {
  token: string | null
  me: MeResponse | null
  login: (token: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthState | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('fachowo_token'))
  const [me, setMe] = useState<MeResponse | null>(null)

  useEffect(() => {
    if (!token) {
      setMe(null)
      return
    }
    api
      .get<MeResponse>('/me')
      .then((res) => setMe(res.data))
      .catch(() => {
        localStorage.removeItem('fachowo_token')
        setToken(null)
        setMe(null)
      })
  }, [token])

  const value = useMemo<AuthState>(
    () => ({
      token,
      me,
      login: async (nextToken) => {
        localStorage.setItem('fachowo_token', nextToken)
        setToken(nextToken)
        const res = await api.get<MeResponse>('/me')
        setMe(res.data)
      },
      logout: () => {
        localStorage.removeItem('fachowo_token')
        setToken(null)
        setMe(null)
      },
    }),
    [token, me],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('AuthProvider missing')
  }
  return ctx
}
