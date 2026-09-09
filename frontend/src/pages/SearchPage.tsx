import { useQuery } from '@tanstack/react-query'
import { Link, useSearchParams } from 'react-router-dom'
import { api } from '../api/client'
import type { SearchHit } from '../api/types'

export function SearchPage() {
  const [params] = useSearchParams()
  const q = params.get('q') ?? ''
  const { data = [] } = useQuery({
    queryKey: ['search', q],
    queryFn: async () => (await api.get<SearchHit[]>('/search', { params: { q } })).data,
    enabled: q.length > 0,
  })

  return (
    <div>
      <h1 className="text-2xl font-semibold">Wyniki: {q}</h1>
      <ul className="mt-4 divide-y divide-stone-200 rounded-xl border border-stone-200 bg-white">
        {data.length === 0 ? (
          <li className="px-4 py-6 text-stone-500">Brak wyników.</li>
        ) : (
          data.map((hit) => (
            <li key={`${hit.type}-${hit.path}`}>
              <Link to={hit.path} className="block px-4 py-3 hover:bg-stone-50">
                <span className="text-xs uppercase text-stone-500">{hit.type}</span>
                <div className="font-medium">{hit.name}</div>
              </Link>
            </li>
          ))
        )}
      </ul>
    </div>
  )
}
