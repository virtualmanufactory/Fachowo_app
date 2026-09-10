-- Idempotentny seed trybu stage. Nie kasuje danych użytkownika.
-- Hasło wszystkich kont demo: password (bcrypt jak w 003-seed-demo).

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666004', '55555555-5555-5555-5555-555555555001',
       'Wymiana baterii', 180.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555001')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666004')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555001') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666005', '55555555-5555-5555-5555-555555555001',
       'Udrażnianie rur', 250.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555001')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666005')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555001') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666006', '55555555-5555-5555-5555-555555555001',
       'Montaż WC', 320.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555001')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666006')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555001') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666007', '55555555-5555-5555-5555-555555555002',
       'Awaria 24h', 350.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555002')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666007')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555002') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666008', '55555555-5555-5555-5555-555555555002',
       'Przepchany zlew', 200.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555002')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666008')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555002') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666009', '55555555-5555-5555-5555-555555555002',
       'Instalacja kuchenna', 450.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555002')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666009')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555002') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666010', '55555555-5555-5555-5555-555555555003',
       'Wymiana zaworu', 150.00, 'usługa', FALSE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555003')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666010')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555003') < 4;

-- Konta i firmy stage
INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444004', 'nowak@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444004' OR email = 'nowak@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444005', 'rurka@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444005' OR email = 'rurka@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444006', 'aqua@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444006' OR email = 'aqua@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444007', 'klima@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444007' OR email = 'klima@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444008', 'chlod@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444008' OR email = 'chlod@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444009', 'arctic@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444009' OR email = 'arctic@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444010', 'frost@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444010' OR email = 'frost@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444011', 'elektro@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444011' OR email = 'elektro@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444012', 'prad@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444012' OR email = 'prad@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444013', 'malarz@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444013' OR email = 'malarz@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444014', 'czysty@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444014' OR email = 'czysty@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444015', 'drewno@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444015' OR email = 'drewno@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444016', 'ogrod@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444016' OR email = 'ogrod@demo.fachowo.pl'
);

INSERT INTO users (id, email, password_hash, role, created_at)
SELECT '44444444-4444-4444-4444-444444444017', 'dach@demo.fachowo.pl',
       '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM users
    WHERE id = '44444444-4444-4444-4444-444444444017' OR email = 'dach@demo.fachowo.pl'
);

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555004', '44444444-4444-4444-4444-444444444004',
       '11111111-1111-1111-1111-111111111001', '33333333-3333-3333-3333-333333333001',
       'Hydraulika Nowak', 'hydraulika-nowak', '2000000001',
       'Drugi hydraulik w Poznaniu. Wymiany, awarie, instalacje w blokach.',
       'ul. Święty Marcin 40', FALSE, '603400500', 'nowak@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444004')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555004'
         OR slug = 'hydraulika-nowak'
         OR nip = '2000000001'
         OR owner_id = '44444444-4444-4444-4444-444444444004'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555005', '44444444-4444-4444-4444-444444444005',
       '11111111-1111-1111-1111-111111111001', '33333333-3333-3333-3333-333333333007',
       'Rurka Express', 'rurka-express', '2000000018',
       'Ekipy hydrauliczne z Warszawy. Dojazd do domu klienta w całym województwie.',
       'ul. Marszałkowska 82', TRUE, '604500600', 'rurka@demo.fachowo.pl', 'https://rurka-express.example', TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444005')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555005'
         OR slug = 'rurka-express'
         OR nip = '2000000018'
         OR owner_id = '44444444-4444-4444-4444-444444444005'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555006', '44444444-4444-4444-4444-444444444006',
       '11111111-1111-1111-1111-111111111001', '33333333-3333-3333-3333-333333333010',
       'Aqua-Fach', 'aqua-fach', '2000000024',
       'Instalacje wod-kan w Krakowie i okolicach. Dojazd do klienta.',
       'ul. Dietla 15', TRUE, '605600700', 'aqua@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444006')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555006'
         OR slug = 'aqua-fach'
         OR nip = '2000000024'
         OR owner_id = '44444444-4444-4444-4444-444444444006'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555007', '44444444-4444-4444-4444-444444444007',
       '11111111-1111-1111-1111-111111111007', '33333333-3333-3333-3333-333333333001',
       'Klima-Max', 'klima-max', '2000000030',
       'Montaż i serwis klimatyzacji w Poznaniu. Dojazd, przeglądy sezonowe.',
       'ul. Półwiejska 22', TRUE, '606700800', 'klima@demo.fachowo.pl', 'https://klima-max.example', TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444007')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555007'
         OR slug = 'klima-max'
         OR nip = '2000000030'
         OR owner_id = '44444444-4444-4444-4444-444444444007'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555008', '44444444-4444-4444-4444-444444444008',
       '11111111-1111-1111-1111-111111111007', '33333333-3333-3333-3333-333333333007',
       'Chłodnictwo Wiśniewski', 'chlodnictwo-wisniewski', '2000000047',
       'Klimatyzacja i chłodnictwo w Warszawie. Serwis split i multi-split.',
       'ul. Puławska 140', TRUE, '607800900', 'chlod@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444008')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555008'
         OR slug = 'chlodnictwo-wisniewski'
         OR nip = '2000000047'
         OR owner_id = '44444444-4444-4444-4444-444444444008'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555009', '44444444-4444-4444-4444-444444444009',
       '11111111-1111-1111-1111-111111111007', '33333333-3333-3333-3333-333333333010',
       'Arctic Klimat', 'arctic-klimat', '2000000053',
       'Klimatyzacja w Krakowie. Montaż stacjonarny, bez wyjazdów poza miasto.',
       'ul. Karmelicka 8', FALSE, '608900100', 'arctic@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444009')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555009'
         OR slug = 'arctic-klimat'
         OR nip = '2000000053'
         OR owner_id = '44444444-4444-4444-4444-444444444009'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555010', '44444444-4444-4444-4444-444444444010',
       '11111111-1111-1111-1111-111111111007', '33333333-3333-3333-3333-333333333013',
       'Frost-Tech', 'frost-tech', '2000000076',
       'Serwis klimatyzacji we Wrocławiu. Dojazd do domu i biura.',
       'ul. Świdnicka 33', TRUE, '609000200', 'frost@demo.fachowo.pl', 'https://frost-tech.example', TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444010')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555010'
         OR slug = 'frost-tech'
         OR nip = '2000000076'
         OR owner_id = '44444444-4444-4444-4444-444444444010'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555011', '44444444-4444-4444-4444-444444444011',
       '11111111-1111-1111-1111-111111111002', '33333333-3333-3333-3333-333333333001',
       'Elektro-Plus', 'elektro-plus', '2000000082',
       'Elektryk z Poznania. Instalacje, gniazdka, rozdzielnice. Dojazd do klienta.',
       'ul. Grochowska 9', TRUE, '610100300', 'elektro@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444011')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555011'
         OR slug = 'elektro-plus'
         OR nip = '2000000082'
         OR owner_id = '44444444-4444-4444-4444-444444444011'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555012', '44444444-4444-4444-4444-444444444012',
       '11111111-1111-1111-1111-111111111002', '33333333-3333-3333-3333-333333333007',
       'Prąd i Co', 'prad-i-co', '2000000099',
       'Warszawski elektryk. Awaria, wymiana licznika, oświetlenie LED.',
       'ul. Nowy Świat 18', TRUE, '611200400', 'prad@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444012')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555012'
         OR slug = 'prad-i-co'
         OR nip = '2000000099'
         OR owner_id = '44444444-4444-4444-4444-444444444012'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555013', '44444444-4444-4444-4444-444444444013',
       '11111111-1111-1111-1111-111111111003', '33333333-3333-3333-3333-333333333001',
       'Mal-Dom', 'mal-dom', '2000000107',
       'Malowanie mieszkań i klatek w Poznaniu. Dojazd z farbami.',
       'ul. Głogowska 55', TRUE, '612300500', 'malarz@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444013')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555013'
         OR slug = 'mal-dom'
         OR nip = '2000000107'
         OR owner_id = '44444444-4444-4444-4444-444444444013'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555014', '44444444-4444-4444-4444-444444444014',
       '11111111-1111-1111-1111-111111111004', '33333333-3333-3333-3333-333333333007',
       'Czysty Kąt', 'czysty-kat', '2000000113',
       'Sprzątanie mieszkań i biur w Warszawie. Jednorazowo albo cyklicznie.',
       'ul. Targowa 12', TRUE, '613400600', 'czysty@demo.fachowo.pl', 'https://czysty-kat.example', TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444014')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555014'
         OR slug = 'czysty-kat'
         OR nip = '2000000113'
         OR owner_id = '44444444-4444-4444-4444-444444444014'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555015', '44444444-4444-4444-4444-444444444015',
       '11111111-1111-1111-1111-111111111005', '33333333-3333-3333-3333-333333333010',
       'Drewno i Co', 'drewno-co', '2000000136',
       'Stolarz z Krakowa. Meble na wymiar, drzwi, drobne naprawy.',
       'ul. Długa 21', FALSE, '614500700', 'drewno@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444015')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555015'
         OR slug = 'drewno-co'
         OR nip = '2000000136'
         OR owner_id = '44444444-4444-4444-4444-444444444015'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555016', '44444444-4444-4444-4444-444444444016',
       '11111111-1111-1111-1111-111111111006', '33333333-3333-3333-3333-333333333014',
       'Zielony Ogród', 'zielony-ogrod', '2000000142',
       'Pielęgnacja ogrodów i trawników w Gdańsku. Dojazd z kosiarką.',
       'ul. Długa 45', TRUE, '615600800', 'ogrod@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444016')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555016'
         OR slug = 'zielony-ogrod'
         OR nip = '2000000142'
         OR owner_id = '44444444-4444-4444-4444-444444444016'
  );

INSERT INTO companies (id, owner_id, category_id, city_id, name, slug, nip, description, address,
    serves_customers_at_home, phone, email, website, verified, available, created_at)
SELECT '55555555-5555-5555-5555-555555555017', '44444444-4444-4444-4444-444444444017',
       '11111111-1111-1111-1111-111111111008', '33333333-3333-3333-3333-333333333012',
       'Dach-Master', 'dach-master', '2000000159',
       'Dekarz ze Śląska. Naprawy dachów, orynnowanie, kominy. Dojazd.',
       'ul. Mariacka 6', TRUE, '616700900', 'dach@demo.fachowo.pl', NULL, TRUE, TRUE, NOW()
WHERE EXISTS (SELECT 1 FROM users WHERE id = '44444444-4444-4444-4444-444444444017')
  AND NOT EXISTS (
      SELECT 1 FROM companies
      WHERE id = '55555555-5555-5555-5555-555555555017'
         OR slug = 'dach-master'
         OR nip = '2000000159'
         OR owner_id = '44444444-4444-4444-4444-444444444017'
  );

-- Weryfikacje stub
INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777004', '55555555-5555-5555-5555-555555555004',
       '2000000001', 'STUB', 'VERIFIED', '{"name":"Hydraulika Nowak"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555004')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777004');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777005', '55555555-5555-5555-5555-555555555005',
       '2000000018', 'STUB', 'VERIFIED', '{"name":"Rurka Express"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555005')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777005');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777006', '55555555-5555-5555-5555-555555555006',
       '2000000024', 'STUB', 'VERIFIED', '{"name":"Aqua-Fach"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555006')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777006');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777007', '55555555-5555-5555-5555-555555555007',
       '2000000030', 'STUB', 'VERIFIED', '{"name":"Klima-Max"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555007')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777007');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777008', '55555555-5555-5555-5555-555555555008',
       '2000000047', 'STUB', 'VERIFIED', '{"name":"Chłodnictwo Wiśniewski"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555008')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777008');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777009', '55555555-5555-5555-5555-555555555009',
       '2000000053', 'STUB', 'VERIFIED', '{"name":"Arctic Klimat"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555009')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777009');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777010', '55555555-5555-5555-5555-555555555010',
       '2000000076', 'STUB', 'VERIFIED', '{"name":"Frost-Tech"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555010')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777010');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777011', '55555555-5555-5555-5555-555555555011',
       '2000000082', 'STUB', 'VERIFIED', '{"name":"Elektro-Plus"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555011')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777011');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777012', '55555555-5555-5555-5555-555555555012',
       '2000000099', 'STUB', 'VERIFIED', '{"name":"Prąd i Co"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555012')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777012');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777013', '55555555-5555-5555-5555-555555555013',
       '2000000107', 'STUB', 'VERIFIED', '{"name":"Mal-Dom"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555013')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777013');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777014', '55555555-5555-5555-5555-555555555014',
       '2000000113', 'STUB', 'VERIFIED', '{"name":"Czysty Kąt"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555014')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777014');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777015', '55555555-5555-5555-5555-555555555015',
       '2000000136', 'STUB', 'VERIFIED', '{"name":"Drewno i Co"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555015')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777015');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777016', '55555555-5555-5555-5555-555555555016',
       '2000000142', 'STUB', 'VERIFIED', '{"name":"Zielony Ogród"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555016')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777016');

INSERT INTO company_verifications (id, company_id, nip, source, status, payload, verified_at)
SELECT '77777777-7777-7777-7777-777777777017', '55555555-5555-5555-5555-555555555017',
       '2000000159', 'STUB', 'VERIFIED', '{"name":"Dach-Master"}', NOW()
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555017')
  AND NOT EXISTS (SELECT 1 FROM company_verifications WHERE id = '77777777-7777-7777-7777-777777777017');

-- Usługi nowych firm (max 4)
INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666011', '55555555-5555-5555-5555-555555555004',
       'Wizyta hydraulika', 110.00, 'wizyta', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555004')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666011')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555004') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666012', '55555555-5555-5555-5555-555555555004',
       'Wymiana syfonu', 160.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555004')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666012')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555004') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666013', '55555555-5555-5555-5555-555555555004',
       'Montaż pralki', 200.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555004')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666013')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555004') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666014', '55555555-5555-5555-5555-555555555005',
       'Wizyta hydraulika', 130.00, 'wizyta', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555005')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666014')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555005') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666015', '55555555-5555-5555-5555-555555555005',
       'Dojazd awaryjny', 280.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555005')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666015')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555005') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666016', '55555555-5555-5555-5555-555555555006',
       'Wizyta hydraulika', 125.00, 'wizyta', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555006')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666016')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555006') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666017', '55555555-5555-5555-5555-555555555006',
       'Montaż ogrzewania', 900.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555006')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666017')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555006') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666018', '55555555-5555-5555-5555-555555555007',
       'Montaż klimatyzacji', 1800.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555007')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666018')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555007') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666019', '55555555-5555-5555-5555-555555555007',
       'Przegląd sezonowy', 220.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555007')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666019')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555007') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666020', '55555555-5555-5555-5555-555555555007',
       'Uzupełnienie czynnika', 350.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555007')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666020')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555007') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666021', '55555555-5555-5555-5555-555555555008',
       'Montaż klimatyzacji', 2100.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555008')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666021')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555008') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666022', '55555555-5555-5555-5555-555555555008',
       'Serwis split', 280.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555008')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666022')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555008') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666023', '55555555-5555-5555-5555-555555555008',
       'Czyszczenie jednostki', 190.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555008')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666023')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555008') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666024', '55555555-5555-5555-5555-555555555009',
       'Montaż klimatyzacji', 1950.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555009')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666024')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555009') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666025', '55555555-5555-5555-5555-555555555009',
       'Przegląd', 200.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555009')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666025')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555009') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666026', '55555555-5555-5555-5555-555555555010',
       'Montaż klimatyzacji', 1700.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555010')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666026')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555010') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666027', '55555555-5555-5555-5555-555555555010',
       'Serwis gwarancyjny', 250.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555010')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666027')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555010') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666028', '55555555-5555-5555-5555-555555555011',
       'Wizyta elektryka', 120.00, 'wizyta', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555011')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666028')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555011') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666029', '55555555-5555-5555-5555-555555555011',
       'Wymiana rozdzielnicy', 650.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555011')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666029')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555011') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666030', '55555555-5555-5555-5555-555555555012',
       'Wizyta elektryka', 140.00, 'wizyta', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555012')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666030')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555012') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666031', '55555555-5555-5555-5555-555555555012',
       'Oświetlenie LED', 300.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555012')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666031')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555012') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666032', '55555555-5555-5555-5555-555555555013',
       'Malowanie pokoju', 400.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555013')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666032')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555013') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666033', '55555555-5555-5555-5555-555555555013',
       'Gładź i malowanie', 55.00, 'm2', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555013')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666033')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555013') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666034', '55555555-5555-5555-5555-555555555014',
       'Sprzątanie mieszkania', 180.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555014')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666034')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555014') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666035', '55555555-5555-5555-5555-555555555014',
       'Sprzątanie biura', 25.00, 'godz.', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555014')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666035')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555014') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666036', '55555555-5555-5555-5555-555555555015',
       'Meble na wymiar', 2500.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555015')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666036')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555015') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666037', '55555555-5555-5555-5555-555555555015',
       'Regulacja drzwi', 150.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555015')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666037')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555015') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666038', '55555555-5555-5555-5555-555555555016',
       'Koszenie trawnika', 80.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555016')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666038')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555016') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666039', '55555555-5555-5555-5555-555555555016',
       'Przycinanie żywopłotu', 120.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555016')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666039')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555016') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666040', '55555555-5555-5555-5555-555555555017',
       'Naprawa dachu', 450.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555017')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666040')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555017') < 4;

INSERT INTO services (id, company_id, name, price, unit, available)
SELECT '66666666-6666-6666-6666-666666666041', '55555555-5555-5555-5555-555555555017',
       'Czyszczenie rynien', 180.00, 'usługa', TRUE
WHERE EXISTS (SELECT 1 FROM companies WHERE id = '55555555-5555-5555-5555-555555555017')
  AND NOT EXISTS (SELECT 1 FROM services WHERE id = '66666666-6666-6666-6666-666666666041')
  AND (SELECT COUNT(*) FROM services WHERE company_id = '55555555-5555-5555-5555-555555555017') < 4;
