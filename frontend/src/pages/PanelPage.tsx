import { type FormEvent, useEffect, useState } from 'react'
import { Link, Navigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { api } from '../api/client'
import { apiErrorMessage } from '../api/errors'
import type { Category, City, CompanyProfile, ImageDto, RegistryCompany, ServiceDto, Voivodeship } from '../api/types'
import { ActionButton } from '../components/ActionButton'
import { useAuth } from '../auth'

const MAX_SERVICES = 4
const MAX_IMAGES = 4

const primaryButton =
  'rounded-lg bg-teal-700 px-4 py-2 text-sm font-medium text-white hover:bg-teal-800 disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'
const secondaryButton =
  'rounded-lg bg-stone-800 px-4 py-2 text-sm font-medium text-white hover:bg-stone-900 disabled:cursor-not-allowed disabled:bg-stone-300 disabled:text-stone-500'
const dangerButton =
  'rounded-lg border border-rose-200 px-3 py-1.5 text-sm text-rose-700 hover:bg-rose-50 disabled:cursor-not-allowed disabled:opacity-50'

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
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [phone, setPhone] = useState('')
  const [email, setEmail] = useState('')
  const [website, setWebsite] = useState('')
  const [address, setAddress] = useState('')
  const [categoryId, setCategoryId] = useState('')
  const [voivodeship, setVoivodeship] = useState('')
  const [cityId, setCityId] = useState('')
  const [available, setAvailable] = useState(true)
  const [serves, setServes] = useState(false)
  const [message, setMessage] = useState<string | null>(null)
  const [serviceName, setServiceName] = useState('Wizyta')
  const [servicePrice, setServicePrice] = useState('100')
  const [error, setError] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)
  const [addingService, setAddingService] = useState(false)
  const [addingImage, setAddingImage] = useState(false)

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

  useEffect(() => {
    if (!data) {
      return
    }
    setName(data.name)
    setDescription(data.description ?? '')
    setPhone(data.phone ?? '')
    setEmail(data.email ?? '')
    setWebsite(data.website ?? '')
    setAddress(data.address ?? '')
    setCategoryId(data.categoryId)
    setVoivodeship(data.voivodeshipSlug)
    setCityId(data.cityId)
    setAvailable(data.available)
    setServes(data.servesCustomersAtHome)
  }, [data])

  useEffect(() => {
    if (!cities.length) {
      return
    }
    if (!cities.some((city) => city.id === cityId)) {
      setCityId(cities[0].id)
    }
  }, [cities, cityId])

  if (!data) {
    return <p>Ładowanie panelu…</p>
  }

  const company = data

  async function save(event: FormEvent) {
    event.preventDefault()
    setError(null)
    setSaving(true)
    try {
      await api.put(`/companies/${company.id}`, {
        name,
        description,
        phone,
        email,
        website,
        address,
        categoryId,
        cityId,
        available,
        servesCustomersAtHome: serves,
      })
      setMessage('Zapisano zmiany.')
      await refetch()
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się zapisać wizytówki.'))
    } finally {
      setSaving(false)
    }
  }

  async function addService(event: FormEvent) {
    event.preventDefault()
    setError(null)
    if (company.services.length >= MAX_SERVICES) {
      setError(`Możesz dodać maksymalnie ${MAX_SERVICES} usługi.`)
      return
    }
    setAddingService(true)
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
    } finally {
      setAddingService(false)
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
    setAddingImage(true)
    try {
      const form = new FormData()
      form.append('file', file)
      await api.post(`/companies/${company.id}/images`, form)
      fileInput.value = ''
      await refetch()
    } catch (err) {
      setError(apiErrorMessage(err, 'Nie udało się dodać zdjęcia.'))
    } finally {
      setAddingImage(false)
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
        <p className="mt-1 text-sm text-stone-500">NIP: {data.nip}</p>
      </div>
      <form onSubmit={save} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <h2 className="font-semibold">Dane firmy</h2>
        <input
          className="w-full rounded-lg border border-stone-300 px-3 py-2"
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="Nazwa firmy"
          required
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
        <label className="flex items-center gap-2 text-sm">
          <input type="checkbox" checked={serves} onChange={(e) => setServes(e.target.checked)} />
          Dojazd do klienta
        </label>
        <label className="flex items-center gap-2 text-sm">
          <input type="checkbox" checked={available} onChange={(e) => setAvailable(e.target.checked)} />
          Dostępność (auto)
        </label>
        {message ? <p className="text-sm text-teal-800">{message}</p> : null}
        <ActionButton className={primaryButton} loading={saving} loadingLabel="Zapisywanie…">
          Zapisz wizytówkę
        </ActionButton>
      </form>
      {error ? <p className="text-sm text-rose-700">{error}</p> : null}
      <form onSubmit={addService} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <h2 className="font-semibold">
          Usługi ({company.services.length}/{MAX_SERVICES})
        </h2>
        {company.services.length > 0 ? (
          <ul className="space-y-3">
            {company.services.map((service) => (
              <li key={service.id}>
                <ServiceEditor
                  companyId={company.id}
                  service={service}
                  onChanged={async () => {
                    setError(null)
                    await refetch()
                  }}
                  onError={setError}
                />
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
        <ActionButton
          className={secondaryButton}
          loading={addingService}
          loadingLabel="Dodawanie…"
          disabled={company.services.length >= MAX_SERVICES}
        >
          Dodaj usługę
        </ActionButton>
        {company.services.length >= MAX_SERVICES ? (
          <p className="text-sm text-stone-500">Osiągnięto limit 4 usług.</p>
        ) : null}
      </form>
      <form onSubmit={upload} className="space-y-3 rounded-xl border border-stone-200 bg-white p-5">
        <h2 className="font-semibold">
          Zdjęcia ({company.images.length}/{MAX_IMAGES})
        </h2>
        {company.images.length > 0 ? (
          <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
            {company.images.map((image) => (
              <ImageEditor
                key={image.id}
                companyId={company.id}
                image={image}
                onChanged={async () => {
                  setError(null)
                  await refetch()
                }}
                onError={setError}
              />
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
        <ActionButton
          className={secondaryButton}
          loading={addingImage}
          loadingLabel="Wysyłanie…"
          disabled={company.images.length >= MAX_IMAGES}
        >
          Dodaj zdjęcie
        </ActionButton>
        {company.images.length >= MAX_IMAGES ? (
          <p className="text-sm text-stone-500">Osiągnięto limit 4 zdjęć.</p>
        ) : null}
      </form>
    </div>
  )
}

function ServiceEditor({
  companyId,
  service,
  onChanged,
  onError,
}: {
  companyId: string
  service: ServiceDto
  onChanged: () => Promise<void>
  onError: (message: string) => void
}) {
  const [name, setName] = useState(service.name)
  const [price, setPrice] = useState(String(service.price))
  const [available, setAvailable] = useState(service.available)
  const [saving, setSaving] = useState(false)
  const [removing, setRemoving] = useState(false)

  useEffect(() => {
    setName(service.name)
    setPrice(String(service.price))
    setAvailable(service.available)
  }, [service])

  async function save(event: FormEvent) {
    event.preventDefault()
    setSaving(true)
    try {
      await api.put(`/companies/${companyId}/services/${service.id}`, {
        name,
        price: Number(price),
        unit: service.unit || 'wizyta',
        available,
      })
      await onChanged()
    } catch (err) {
      onError(apiErrorMessage(err, 'Nie udało się zapisać usługi.'))
    } finally {
      setSaving(false)
    }
  }

  async function remove() {
    setRemoving(true)
    try {
      await api.delete(`/companies/${companyId}/services/${service.id}`)
      await onChanged()
    } catch (err) {
      onError(apiErrorMessage(err, 'Nie udało się usunąć usługi.'))
    } finally {
      setRemoving(false)
    }
  }

  return (
    <form onSubmit={save} className="space-y-2 rounded-lg border border-stone-200 p-3">
      <input
        className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <input
        className="w-full rounded-lg border border-stone-300 px-3 py-2 text-sm"
        type="number"
        min="0"
        value={price}
        onChange={(e) => setPrice(e.target.value)}
        required
      />
      <label className="flex items-center gap-2 text-sm">
        <input type="checkbox" checked={available} onChange={(e) => setAvailable(e.target.checked)} />
        Usługa dostępna
      </label>
      <div className="flex flex-wrap gap-2">
        <ActionButton className={primaryButton} loading={saving} loadingLabel="Zapisywanie…">
          Zapisz usługę
        </ActionButton>
        <ActionButton
          type="button"
          className={dangerButton}
          loading={removing}
          loadingLabel="Usuwanie…"
          onClick={remove}
        >
          Usuń
        </ActionButton>
      </div>
    </form>
  )
}

function ImageEditor({
  companyId,
  image,
  onChanged,
  onError,
}: {
  companyId: string
  image: ImageDto
  onChanged: () => Promise<void>
  onError: (message: string) => void
}) {
  const [replacing, setReplacing] = useState(false)
  const [removing, setRemoving] = useState(false)

  async function replace(file: File) {
    setReplacing(true)
    try {
      const form = new FormData()
      form.append('file', file)
      await api.put(`/companies/${companyId}/images/${image.id}`, form)
      await onChanged()
    } catch (err) {
      onError(apiErrorMessage(err, 'Nie udało się zamienić zdjęcia.'))
    } finally {
      setReplacing(false)
    }
  }

  async function remove() {
    setRemoving(true)
    try {
      await api.delete(`/companies/${companyId}/images/${image.id}`)
      await onChanged()
    } catch (err) {
      onError(apiErrorMessage(err, 'Nie udało się usunąć zdjęcia.'))
    } finally {
      setRemoving(false)
    }
  }

  return (
    <div className="space-y-2">
      <img src={image.url} alt="" className="h-24 w-full rounded-lg object-cover" />
      <label className="block text-xs text-stone-600">
        {replacing ? 'Wysyłanie…' : 'Zamień zdjęcie'}
        <input
          className="mt-1 block w-full text-xs"
          type="file"
          accept="image/jpeg,image/png,image/webp"
          disabled={replacing || removing}
          onChange={(event) => {
            const file = event.target.files?.[0]
            if (file) {
              void replace(file)
            }
            event.target.value = ''
          }}
        />
      </label>
      <ActionButton
        type="button"
        className={dangerButton + ' w-full'}
        loading={removing}
        loadingLabel="Usuwanie…"
        disabled={replacing}
        onClick={remove}
      >
        Usuń
      </ActionButton>
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
  const [lookingUp, setLookingUp] = useState(false)
  const [creating, setCreating] = useState(false)

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
    setLookingUp(true)
    try {
      const res = await api.post<RegistryCompany>('/verifications/nip', { nip })
      setRegistry(res.data)
      setName(res.data.name)
      if (res.data.address) {
        setAddress(res.data.address)
      }
    } catch {
      setError('Nie udało się zweryfikować NIP. Sprawdź numer.')
    } finally {
      setLookingUp(false)
    }
  }

  async function create(event: FormEvent) {
    event.preventDefault()
    setError(null)
    setCreating(true)
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
    } finally {
      setCreating(false)
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
          <ActionButton className={primaryButton} loading={lookingUp} loadingLabel="Weryfikowanie…">
            Weryfikuj NIP
          </ActionButton>
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
          <ActionButton className={primaryButton} loading={creating} loadingLabel="Publikowanie…">
            Opublikuj wizytówkę
          </ActionButton>
        </form>
      ) : null}
      {error ? <p className="text-sm text-rose-700">{error}</p> : null}
    </div>
  )
}
