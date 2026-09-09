import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import type { Category, Voivodeship } from '../api/types'

export function CategoryPage() {
  const { category } = useParams()
  const { data: categories = [] } = useQuery({
    queryKey: ['categories'],
    queryFn: async () => (await api.get<Category[]>('/categories')).data,
  })
  const { data: voivodeships = [] } = useQuery({
    queryKey: ['voivodeships'],
    queryFn: async () => (await api.get<Voivodeship[]>('/voivodeships')).data,
  })
  const current = categories.find((item) => item.slug === category)

  return (
    <div>
      <p className="text-sm text-stone-500">
        <Link to="/" className="hover:text-teal-800">
          Start
        </Link>
        <span> / {current?.name ?? category}</span>
      </p>
      <h1 className="mt-2 text-3xl font-semibold">{current?.name ?? category} — wybierz województwo</h1>
      <div className="mt-6 grid grid-cols-1 gap-2 sm:grid-cols-2 md:grid-cols-3">
        {voivodeships.map((item) => (
          <Link
            key={item.id}
            to={`/kategoria/${category}/${item.slug}`}
            className="rounded-lg border border-stone-200 bg-white px-4 py-3 hover:border-teal-600"
          >
            {item.name}
          </Link>
        ))}
      </div>
    </div>
  )
}
