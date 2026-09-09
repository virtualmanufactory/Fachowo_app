import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import type { Category, City, Voivodeship } from '../api/types'

export function VoivodeshipPage() {
  const { category, voivodeship } = useParams()
  const { data: categories = [] } = useQuery({
    queryKey: ['categories'],
    queryFn: async () => (await api.get<Category[]>('/categories')).data,
  })
  const { data: voivodeships = [] } = useQuery({
    queryKey: ['voivodeships'],
    queryFn: async () => (await api.get<Voivodeship[]>('/voivodeships')).data,
  })
  const { data: cities = [] } = useQuery({
    queryKey: ['cities', voivodeship],
    queryFn: async () => (await api.get<City[]>(`/voivodeships/${voivodeship}/cities`)).data,
    enabled: Boolean(voivodeship),
  })
  const currentCategory = categories.find((item) => item.slug === category)
  const currentVoivodeship = voivodeships.find((item) => item.slug === voivodeship)

  return (
    <div>
      <p className="text-sm text-stone-500">
        <Link to="/" className="hover:text-teal-800">
          Start
        </Link>
        {' / '}
        <Link to={`/kategoria/${category}`} className="hover:text-teal-800">
          {currentCategory?.name}
        </Link>
        <span> / {currentVoivodeship?.name}</span>
      </p>
      <h1 className="mt-2 text-3xl font-semibold">
        {currentCategory?.name} — {currentVoivodeship?.name}
      </h1>
      <p className="mt-1 text-stone-600">Wybierz miasto, aby porównać oferty.</p>
      <div className="mt-6 grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4">
        {cities.map((city) => (
          <Link
            key={city.id}
            to={`/kategoria/${category}/${voivodeship}/${city.slug}`}
            className="rounded-lg border border-stone-200 bg-white px-4 py-3 hover:border-teal-600"
          >
            {city.name}
          </Link>
        ))}
      </div>
    </div>
  )
}
