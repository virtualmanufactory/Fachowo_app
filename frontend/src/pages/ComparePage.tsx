import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import type { CompareResponse } from '../api/types'

function formatPrice(price: number | null) {
  if (price == null) {
    return '—'
  }
  return new Intl.NumberFormat('pl-PL', {
    style: 'currency',
    currency: 'PLN',
    maximumFractionDigits: 0,
  }).format(price)
}

export function ComparePage() {
  const { category, voivodeship, city } = useParams()
  const { data, isLoading, error } = useQuery({
    queryKey: ['compare', category, voivodeship, city],
    queryFn: async () =>
      (
        await api.get<CompareResponse>('/compare', {
          params: { category, voivodeship, city },
        })
      ).data,
    enabled: Boolean(category && voivodeship && city),
  })

  if (isLoading) {
    return <p>Ładowanie porównywarki…</p>
  }
  if (error || !data) {
    return <p>Nie udało się wczytać ofert.</p>
  }

  return (
    <div>
      <p className="text-sm text-stone-500">
        <Link to="/" className="hover:text-teal-800">
          Start
        </Link>
        {' / '}
        <Link to={`/kategoria/${category}`} className="hover:text-teal-800">
          {data.categoryName}
        </Link>
        {' / '}
        <Link to={`/kategoria/${category}/${voivodeship}`} className="hover:text-teal-800">
          {data.voivodeshipName}
        </Link>
        <span> / {data.cityName}</span>
      </p>
      <h1 className="mt-2 text-3xl font-semibold">
        {data.categoryName} — {data.cityName}
      </h1>
      <p className="mt-1 text-stone-600">
        Porównanie lokalnych firm: cena wizyty i bieżąca dostępność. Firmy z dojazdami też trafiają na listę.
      </p>
      <div className="mt-6 overflow-x-auto rounded-xl border border-stone-200 bg-white shadow-sm">
        <table className="w-full min-w-[40rem] text-left text-sm">
          <thead className="bg-stone-100 text-stone-600">
            <tr>
              <th className="px-4 py-3 font-medium">Firma</th>
              <th className="px-4 py-3 font-medium">Miejscowość</th>
              <th className="px-4 py-3 font-medium">Cena</th>
              <th className="px-4 py-3 font-medium">Dostępność</th>
            </tr>
          </thead>
          <tbody>
            {data.rows.length === 0 ? (
              <tr>
                <td colSpan={4} className="px-4 py-8 text-center text-stone-500">
                  Brak ofert w tej lokalizacji.
                </td>
              </tr>
            ) : (
              data.rows.map((row) => (
                <tr key={row.companyId} className="border-t border-stone-100 hover:bg-stone-50">
                  <td className="px-4 py-3">
                    <Link to={`/firma/${row.slug}`} className="font-medium text-teal-800 hover:underline">
                      {row.name}
                    </Link>
                    {row.verified ? (
                      <span className="ml-2 rounded-full bg-teal-50 px-2 py-0.5 text-xs text-teal-800">NIP</span>
                    ) : null}
                    {row.servesCustomersAtHome ? (
                      <span className="ml-1 text-xs text-stone-500">dojazd</span>
                    ) : null}
                  </td>
                  <td className="px-4 py-3">{row.city}</td>
                  <td className="px-4 py-3">{formatPrice(row.price)}</td>
                  <td className="px-4 py-3">
                    {row.available ? (
                      <span className="font-medium text-teal-700">auto</span>
                    ) : (
                      <span className="font-medium text-rose-700">brak</span>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}
