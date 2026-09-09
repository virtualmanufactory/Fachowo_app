import axios from 'axios'

export function apiErrorMessage(error: unknown, fallback: string): string {
  if (axios.isAxiosError(error)) {
    const detail = error.response?.data?.detail
    if (typeof detail === 'string' && detail.trim()) {
      return detail
    }
    if (error.response?.status === 409) {
      return 'Konto z tym adresem e-mail już istnieje'
    }
    if (!error.response) {
      return 'Brak połączenia z API. Uruchom backend na porcie 8080.'
    }
  }
  return fallback
}
