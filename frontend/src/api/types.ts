export type Category = { id: string; slug: string; name: string }
export type Voivodeship = { id: string; slug: string; name: string }
export type City = { id: string; slug: string; name: string; voivodeshipSlug: string }

export type CompareRow = {
  companyId: string
  slug: string
  name: string
  city: string
  price: number | null
  unit: string | null
  available: boolean
  servesCustomersAtHome: boolean
  verified: boolean
}

export type CompareResponse = {
  categoryName: string
  voivodeshipName: string
  cityName: string
  rows: CompareRow[]
}

export type ServiceDto = {
  id: string
  name: string
  price: number
  unit: string
  available: boolean
}

export type ImageDto = { id: string; url: string }

export type CompanyProfile = {
  id: string
  slug: string
  name: string
  nip: string
  description: string | null
  categoryId: string
  categorySlug: string
  categoryName: string
  voivodeshipSlug: string
  voivodeshipName: string
  cityId: string
  citySlug: string
  cityName: string
  address: string | null
  servesCustomersAtHome: boolean
  phone: string | null
  email: string | null
  website: string | null
  verified: boolean
  available: boolean
  createdAt: string
  services: ServiceDto[]
  images: ImageDto[]
}

export type SearchHit = { type: string; name: string; path: string }

export type AuthResponse = { token: string; userId: string; email: string }

export type MeResponse = {
  id: string
  email: string
  role: string
  companyId: string | null
  companySlug: string | null
}

export type RegistryCompany = {
  nip: string
  name: string
  regon: string | null
  address: string | null
  status: string | null
  source: string
}
