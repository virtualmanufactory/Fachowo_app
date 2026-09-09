import { useQuery } from '@tanstack/react-query'
import { Link } from 'react-router-dom'
import { api } from '../api/client'
import type { Category } from '../api/types'

export function HomePage() {
  const { data: categories = [], isError, refetch } = useQuery({
    queryKey: ['categories'],
    queryFn: async () => (await api.get<Category[]>('/categories')).data,
  })

  return (
    <div>
      <section className="rounded-2xl bg-teal-800 px-6 py-12 text-white">
        <p className="text-sm uppercase tracking-widest text-teal-100">Porównywarka lokalnych usług</p>
        <h1 className="mt-2 max-w-2xl text-4xl font-semibold tracking-tight">
          Znajdź fachowca w swoim mieście
        </h1>
        <p className="mt-3 max-w-xl text-teal-50">
          Bezpłatne wizytówki małych firm. Porównaj cenę, miejscowość i dostępność — zacznij od kategorii.
        </p>
      </section>
      <h2 className="mt-10 text-lg font-semibold text-stone-800">Kategorie</h2>
      {isError ? (
        <p className="mt-3 text-sm text-rose-700">
          Nie udało się wczytać kategorii.{' '}
          <button type="button" className="underline" onClick={() => refetch()}>
            Spróbuj ponownie
          </button>
        </p>
      ) : null}
      <div className="mt-4 grid grid-cols-2 gap-3 sm:grid-cols-3 md:grid-cols-4">
        {categories.map((category) => (
          <Link
            key={category.id}
            to={`/kategoria/${category.slug}`}
            className="rounded-xl border border-stone-200 bg-white px-4 py-5 font-medium text-stone-800 shadow-sm hover:border-teal-600 hover:text-teal-800"
          >
            {category.name}
          </Link>
        ))}
      </div>
    </div>
  )
}
