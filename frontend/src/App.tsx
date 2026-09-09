import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { Layout } from './components/Layout'
import { HomePage } from './pages/HomePage'
import { CategoryPage } from './pages/CategoryPage'
import { VoivodeshipPage } from './pages/VoivodeshipPage'
import { ComparePage } from './pages/ComparePage'
import { CompanyPage } from './pages/CompanyPage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { PanelPage } from './pages/PanelPage'
import { SearchPage } from './pages/SearchPage'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/kategoria/:category" element={<CategoryPage />} />
          <Route path="/kategoria/:category/:voivodeship" element={<VoivodeshipPage />} />
          <Route path="/kategoria/:category/:voivodeship/:city" element={<ComparePage />} />
          <Route path="/firma/:slug" element={<CompanyPage />} />
          <Route path="/logowanie" element={<LoginPage />} />
          <Route path="/rejestracja" element={<RegisterPage />} />
          <Route path="/panel" element={<PanelPage />} />
          <Route path="/szukaj" element={<SearchPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}
