import { type FormEvent, useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { api } from '../api/client'
import { apiErrorMessage } from '../api/errors'
import type { Category, City, CompanyProfile, RegistryCompany, Voivodeship } from '../api/types'
import { useAuth } from '../auth'

const MAX_SERVICES = 4
const MAX_IMAGES = 4

const primaryButton =
  'rounded-lg bg-teal-700 px-4 py-2 text-sm font-medium text-white hover:bg-teal-800 disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'
const secondaryButton =
  'rounded-lg bg-stone-800 px-4 py-2 text-sm font-medium text-white hover:bg-stone-900 disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'

export function PanelPage() {
  const { token, me } = useAuth()
  if (!token) {
    return <Navigate to="/logowanie" replace />
  }
  if (!me) {
    return <p>Ładowanie konta…</p>
  }

  if (me.companyId) {
    return <ExistingCompany />
  }
  return <CreateCompanyForm />
}

function ExistingCompany() {
  const { data, refetch } = useQuery({
    queryKey: ['my-company'],
    queryFn: async () => (await api.get<CompanyProfile>('/companies/me/current')).data,
  })
  const [description, setDescription] = useState('')
  const [phone, setPhone] = useState('')
  const [available, setAvailable] = useState(true)
  const [serves, setServes] = useState(false)
  const [message, setMessage] = useState<string | null>(null)
  const [serviceName, setServiceName] = useState('Wizyta')
  const [servicePrice, setServicePrice] = useState('100')
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!data) {
      return
    }
    setDescription(data.description ?? '')
    setPhone(data.phone ?? '')
    setAvailable(data.available)
    setServes(data.servesCustomersAtHome)
  }, [data])

  if (!data) {
    return <p>Ładowanie panelu…</p>
  }

  const company = data

  async function save(event: FormEvent) {
    event.preventDefault()
    await api.put(`/companies/${company.id}`, {
      description,
      phone,
      available,
      servesCustomersAtHome: serves,
    })
    setMessage('Zapisano zmiany.')
    await refetch()
  }

  async function addService(event: FormEvent) {
    event.preventDefault()
    setError(null)
    if (company.services.length >= MAX_SERVICES) {
      setError(`Możesz dodać maksymalnie ${MAX_SERVICES} usługi.`)
      return
    }
    try {
      await api.post(`/companies/${company.id}/services`, {
        name: serviceName,
        price: Number(servicePrice),
        unit: 'wizyta',
        available: true,
      })
      setServiceName('')
      await refetch()
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się dodać usługi.'))
    }
  }

  async function upload(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    setError(null)
    if (company.images.length >= MAX_IMAGES) {
      setError(`Możesz dodać maksymalnie ${MAX_IMAGES} zdjęcia.`)
      return
    }
    const fileInput = event.currentTarget.elements.namedItem('file') as HTMLInputElement
    const file = fileInput.files?.[0]
    if (!file) {
      setError('Wybierz zdjęcie, zanim je dodasz.')
      return
    }
    try {
      const form = new FormData()
      form.append('file', file)
      await api.post(`/companies/${company.id}/images`, form)
      fileInput.value = ''
      await refetch()
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się dodać zdjęcia.'))
    }
  }

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-2xl font-semibold">Moja wizytówka</h1>
        <p className="text-sm text-stone-600">
          Publiczny profil:{' '}
          <Link className="text-teal-800" to={`/firma/${data.slug}`}>
            /firma/{data.slug}
          </Link>
        </p>
      </div>
      <form onSubmit={save} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <textarea
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          rows={4}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
          placeholder="Telefon"
        />
        <label className="flex items-center gap-2 text-sm">
          <input type="checkbox" checked={serves} onChange={(e) => setServes(e.target.checked)} />
          Dojazd do klienta
        </label>
        <label className="flex items-center gap-2 text-sm">
          <input type="checkbox" checked={available} onChange={(e) => setAvailable(e.target.checked)} />
          Dostępność (auto)
        </label>
        {message ? <p className="text-sm text-teal-800">{message}</p> : null}
        <button type="submit" className={primaryButton}>
          Zapisz wizytówkę
        </button>
      </form>
      {error ? <p className="text-sm text-rose-700">{error}</p> : null}
      <form onSubmit={addService} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <h2 className="font-semibold">
          Usługi ({company.services.length}/{MAX_SERVICES})
        </h2>
        {company.services.length > 0 ? (
          <ul className="divide-y divide-stone-200 rounded-lg border border-stone-200">
            {company.services.map((service) => (
              <li key={service.id} className="flex items-center justify-between px-3 py-2 text-sm">
                <span>{service.name}</span>
                <span className="font-medium">
                  {new Intl.NumberFormat('pl-PL', {
                    style: 'currency',
                    currency: 'PLN',
                    maximumFractionDigits: 0,
                  }).format(service.price)}
                </span>
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-sm text-stone-500">Nie dodałeś jeszcze usług.</p>
        )}
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          value={serviceName}
          onChange={(e) => setServiceName(e.target.value)}
          placeholder="Nazwa usługi"
          disabled={company.services.length >= MAX_SERVICES}
        />
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          type="number"
          min="0"
          value={servicePrice}
          onChange={(e) => setServicePrice(e.target.value)}
          placeholder="Cena (zł)"
          disabled={company.services.length >= MAX_SERVICES}
        />
        <button type="submit" className={secondaryButton} disabled={company.services.length >= MAX_SERVICES}>
          Dodaj usługę
        </button>
        {company.services.length >= MAX_SERVICES ? (
          <p className="text-sm text-stone-500">Osiągnięto limit 4 usług.</p>
        ) : null}
      </form>
      <form onSubmit={upload} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <h2 className="font-semibold">
          Zdjęcia ({company.images.length}/{MAX_IMAGES})
        </h2>
        {company.images.length > 0 ? (
          <div className="grid grid-cols-2 gap-2 sm:grid-cols-4">
            {company.images.map((image) => (
              <img key={image.id} src={image.url} alt="" className="h-24 w-full rounded-lg object-cover" />
            ))}
          </div>
        ) : (
          <p className="text-sm text-stone-500">Nie dodałeś jeszcze zdjęć.</p>
        )}
        <label className="block text-sm text-stone-600">
          Wybierz plik (JPEG, PNG lub WebP)
          <input
            className="mt-1 block w-full text-sm"
            type="file"
            name="file"
            accept="image/jpeg,image/png,image/webp"
            disabled={company.images.length >= MAX_IMAGES}
          />
        </label>
        <button type="submit" className={secondaryButton} disabled={company.images.length >= MAX_IMAGES}>
          Dodaj zdjęcie
        </button>
        {company.images.length >= MAX_IMAGES ? (
          <p className="text-sm text-stone-500">Osiągnięto limit 4 zdjęć.</p>
        ) : null}
      </form>
    </div>
  )
}

function CreateCompanyForm() {
  const { login, token } = useAuth()
  const [nip, setNip] = useState('')
  const [registry, setRegistry] = useState<RegistryCompany | null>(null)
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [categoryId, setCategoryId] = useState('')
  const [voivodeship, setVoivodeship] = useState('wielkopolskie')
  const [cityId, setCityId] = useState('')
  const [address, setAddress] = useState('')
  const [phone, setPhone] = useState('')
  const [email, setEmail] = useState('')
  const [website, setWebsite] = useState('')
  const [serves, setServes] = useState(false)
  const [price, setPrice] = useState('100')
  const [error, setError] = useState<string | null>(null)

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
  })

  useEffect(() => {
    if (categories[0] && !categoryId) {
      setCategoryId(categories[0].id)
    }
  }, [categories, categoryId])

  useEffect(() => {
    if (cities[0]) {
      setCityId(cities[0].id)
    }
  }, [cities])

  async function lookup(event: FormEvent) {
    event.preventDefault()
    setError(null)
    try {
      const res = await api.post<RegistryCompany>('/verifications/nip', { nip })
      setRegistry(res.data)
      setName(res.data.name)
      if (res.data.address) {
        setAddress(res.data.address)
      }
    } catch {
      setError('Nie udało się zweryfikować NIP. Sprawdź numer.')
    }
  }

  async function create(event: FormEvent) {
    event.preventDefault()
    setError(null)
    try {
      await api.post('/companies', {
        nip,
        name,
        description,
        categoryId,
        cityId,
        address,
        servesCustomersAtHome: serves,
        phone,
        email,
        website: website || null,
        available: true,
        serviceName: 'Wizyta',
        servicePrice: Number(price),
        serviceUnit: 'wizyta',
      })
      if (token) {
        await login(token)
      }
    } catch {
      setError('Nie udało się utworzyć wizytówki.')
    }
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-semibold">Nowa wizytówka</h1>
      <form onSubmit={lookup} className="rounded-xl border border-stone-200 bg-white p-5">
        <label className="text-sm font-medium">NIP</label>
        <div className="mt-2 flex gap-2">
          <input
            className="flex-1 rounded-lg border border-stone-300 px-3 py-2"
            value={nip}
            onChange={(e) => setNip(e.target.value)}
            placeholder="10 cyfr"
            required
          />
          <button type="submit" className="rounded-lg bg-teal-700 px-4 py-2 text-sm font-medium text-white hover:bg-teal-800">
            Weryfikuj NIP
          </button>
        </div>
        {registry ? (
          <p className="mt-3 text-sm text-teal-800">
            Znaleziono: {registry.name} ({registry.source})
          </p>
        ) : null}
      </form>
      {registry ? (
        <form onSubmit={create} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
            placeholder="Nazwa firmy"
          />
          <textarea
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            rows={4}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Opis oferty"
          />
          <select
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={categoryId}
            onChange={(e) => setCategoryId(e.target.value)}
          >
            {categories.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>
          <select
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={voivodeship}
            onChange={(e) => setVoivodeship(e.target.value)}
          >
            {voivodeships.map((item) => (
              <option key={item.id} value={item.slug}>
                {item.name}
              </option>
            ))}
          </select>
          <select
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={cityId}
            onChange={(e) => setCityId(e.target.value)}
          >
            {cities.map((item) => (
              <option key={item.id} value={item.id}>
                {item.name}
              </option>
            ))}
          </select>
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            placeholder="Adres"
          />
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            required
            placeholder="Telefon"
          />
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="E-mail kontaktowy"
          />
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            value={website}
            onChange={(e) => setWebsite(e.target.value)}
            placeholder="Strona WWW (opcjonalnie)"
          />
          <input
            className="w-full rounded-lg border border-stone-300 px-3 py-2"
            type="number"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
            placeholder="Cena wizyty (zł)"
          />
          <label className="flex items-center gap-2 text-sm">
            <input type="checkbox" checked={serves} onChange={(e) => setServes(e.target.checked)} />
            Opcja dojazdu do klienta
          </label>
          <button type="submit" className="rounded-lg bg-teal-700 px-4 py-2 text-sm font-medium text-white hover:bg-teal-800">
            Opublikuj wizytówkę
          </button>
        </form>
      ) : null}
      {error ? <p className="text-sm text-rose-700">{error}</p> : null}
    </div>
  )
}
