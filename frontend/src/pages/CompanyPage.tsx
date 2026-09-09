import { useQuery } from '@tanstack/react-query'
import { Link, useParams } from 'react-router-dom'
import { api } from '../api/client'
import type { CompanyProfile } from '../api/types'

export function CompanyPage() {
  const { slug } = useParams()
  const { data, isLoading } = useQuery({
    queryKey: ['company', slug],
    queryFn: async () => (await api.get<CompanyProfile>(`/companies/${slug}`)).data,
    enabled: Boolean(slug),
  })

  if (isLoading) {
    return <p>Ładowanie wizytówki…</p>
  }
  if (!data) {
    return <p>Nie znaleziono firmy.</p>
  }

  return (
    <article>
      <p className="text-sm text-stone-500">
        <Link
          to={`/kategoria/${data.categorySlug}/${data.voivodeshipSlug}/${data.citySlug}`}
          className="hover:text-teal-800"
        >
          {data.categoryName} — {data.cityName}
        </Link>
      </p>
      <div className="mt-2 flex flex-wrap items-center gap-3">
        <h1 className="text-3xl font-semibold">{data.name}</h1>
        {data.verified ? (
          <span className="rounded-full bg-teal-50 px-3 py-1 text-sm text-teal-800">NIP zweryfikowany</span>
        ) : null}
      </div>
      <p className="mt-2 text-stone-600">
        {data.address ? `${data.address}, ` : ''}
        {data.cityName}
      </p>
      {data.description ? <p className="mt-4 max-w-2xl leading-relaxed">{data.description}</p> : null}
      <dl className="mt-6 grid max-w-xl grid-cols-1 gap-3 text-sm sm:grid-cols-2">
        <div>
          <dt className="text-stone-500">Telefon</dt>
          <dd>
            {data.phone ? (
              <a className="font-medium text-teal-800" href={`tel:${data.phone}`}>
                {data.phone}
              </a>
            ) : (
              '—'
            )}
          </dd>
        </div>
        <div>
          <dt className="text-stone-500">Dojazd do klienta</dt>
          <dd>{data.servesCustomersAtHome ? 'tak' : 'nie'}</dd>
        </div>
        <div>
          <dt className="text-stone-500">Dostępność</dt>
          <dd>{data.available ? 'auto' : 'brak'}</dd>
        </div>
        <div>
          <dt className="text-stone-500">NIP</dt>
          <dd>{data.nip}</dd>
        </div>
      </dl>
      {data.images.length > 0 ? (
        <div className="mt-8 grid grid-cols-2 gap-3 md:grid-cols-3">
          {data.images.map((image) => (
            <img key={image.id} src={image.url} alt="" className="h-40 w-full rounded-lg object-cover" />
          ))}
        </div>
      ) : null}
      <h2 className="mt-8 text-lg font-semibold">Usługi</h2>
      <ul className="mt-3 divide-y divide-stone-200 rounded-xl border border-stone-200 bg-white">
        {data.services.map((service) => (
          <li key={service.id} className="flex items-center justify-between px-4 py-3">
            <span>{service.name}</span>
            <span className="font-medium">
              {new Intl.NumberFormat('pl-PL', { style: 'currency', currency: 'PLN', maximumFractionDigits: 0 }).format(
                service.price,
              )}
            </span>
          </li>
        ))}
      </ul>
    </article>
  )
}
