# 🔗 ShortURL — Plataforma de Encurtamento de URLs

O **ShortURL** é uma plataforma escalável e preparada para produção, desenvolvida com foco em performance, arquitetura limpa e boas práticas de mercado.

O sistema combina:

- 🔙 Backend robusto em Java (Spring Boot)
- 🎨 Frontend moderno em React
- ⚡ Cache com Redis
- 🗄️ Persistência com MongoDB
- ☁️ Deploy cloud-ready (Render + Vercel)

---

# 🏛️ Visão Geral da Arquitetura

O projeto segue o padrão **Client–Server**, com separação clara de responsabilidades:

- **Frontend (Camada de Apresentação)** → React + TypeScript
- **Backend (Camada de Aplicação)** → API REST com Spring Boot
- **Banco de Dados (Persistência)** → MongoDB
- **Camada de Cache** → Redis
- **Infraestrutura** → Docker + Deploy em Nuvem

---

# 🧱 Decisões Arquiteturais

## 🔹 Separação de Responsabilidades
- API REST stateless
- Frontend desacoplado do backend
- Configuração baseada em variáveis de ambiente
- Serviços containerizados

## 🔹 Estratégia de Performance
- Redis como primeira camada de busca para redirecionamentos
- MongoDB como armazenamento persistente
- TTL configurável para otimização de memória
- Redirecionamento em tempo de milissegundos quando cacheado

## 🔹 Estratégia de Segurança
- Rate Limiting por IP com Bucket4j
- Validação de entrada
- Redirecionamento controlado (HTTP 302)
- Variáveis sensíveis isoladas por ambiente

---

# 🛠️ Stack Tecnológica

## 🔙 Backend (API)

Repositório:  
👉 https://github.com/Pedro-Vitor-Ribeiro-Silva/shorturl_backend.git

| Categoria | Tecnologia |
|------------|------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.4.2 |
| Banco de Dados | MongoDB |
| Cache | Redis |
| Rate Limiting | Bucket4j |
| Documentação | OpenAPI / Swagger |
| Containerização | Docker & Docker Compose |
| Deploy | Render |

---

## 🎨 Frontend (Aplicação Web)

Repositório:  
👉 https://github.com/Pedro-Vitor-Ribeiro-Silva/shorturl_front.git

| Categoria | Tecnologia |
|------------|------------|
| Framework | React 19 |
| Linguagem | TypeScript |
| Build Tool | Vite |
| Estilização | Tailwind CSS 4 |
| Ícones | Lucide React |
| Sistema de Temas | next-themes (Dark/Light Mode) |
| Deploy | Vercel |

---

# 🚀 Funcionalidades Principais

### 🔗 Encurtamento de URLs
- Geração de código curto em Base62
- Verificação de URLs duplicadas
- Persistência em MongoDB

### ⚡ Redirecionamento de Alta Performance
- Busca prioritária no Redis
- Atualização automática de cache em caso de miss
- Resposta rápida com HTTP 302

### 🛡️ Proteção da API
- Rate Limiting configurável
- Controle por IP
- Prevenção contra abuso e ataques automatizados

### 🌙 Interface Moderna
- Layout responsivo
- Alternância entre modo claro e escuro
- Experiência otimizada para mobile e desktop

### ☁️ Preparado para Produção
- Backend containerizado
- Configuração via variáveis de ambiente
- Deploy automatizado
- Estrutura preparada para escalabilidade horizontal

---

# 🔄 Ciclo de Vida da Requisição

## 📌 Fluxo de Criação do Link

1. O cliente envia a URL original.
2. O backend verifica se já existe no MongoDB.
3. Caso não exista:
    - Gera um código Base62 único.
    - Salva no MongoDB.
    - Armazena no Redis com TTL configurado.
4. Retorna a URL encurtada.

---

## 🔁 Fluxo de Redirecionamento

1. O usuário acessa o link curto.
2. O backend consulta o Redis:
    - Cache hit → redirecionamento imediato.
    - Cache miss → consulta MongoDB.
3. Atualiza o cache.
4. Retorna HTTP 302 para o destino original.

---

# 📦 Execução Local

## ✅ Pré-requisitos

Recomendado:
- Docker
- Docker Compose

Ou:
- Java 21
- Maven
- Node.js 18+

---

# 🐳 Backend

```bash
git clone https://github.com/Pedro-Vitor-Ribeiro-Silva/shorturl_backend.git
cd shorturl_backend
docker-compose up --build
```

API:
```
http://localhost:8080
```

Swagger:
```
http://localhost:8080/swagger-ui.html
```

---

# 💻 Frontend

```bash
git clone https://github.com/Pedro-Vitor-Ribeiro-Silva/shorturl_front.git
cd shorturl_front

npm install

echo "VITE_API_URL=http://localhost:8080" > .env

npm run dev
```

Aplicação:
```
http://localhost:5173
```

---

# ⚙️ Variáveis de Ambiente

## Backend

| Variável | Descrição | Padrão |
|----------|------------|---------|
| MONGO_URI | String de conexão do MongoDB | mongodb://localhost:27017/shorturl_db |
| REDIS_HOST | Host do Redis | localhost |
| REDIS_PORT | Porta do Redis | 6379 |
| CACHE_TTL_HOURS | Tempo de vida do cache | 24 |
| RATE_LIMIT_CAPACITY | Máximo de requisições por bucket | 20 |

---

## Frontend

| Variável | Descrição |
|----------|------------|
| VITE_API_URL | URL base da API |

Exemplo:
```
VITE_API_URL=https://seu-backend.onrender.com
```

---

# ☁️ Deploy

## Frontend
- Hospedado na Vercel
- CI/CD automático via GitHub
- Configuração de rotas via vercel.json

## Backend
- Hospedado no Render
- Build via Dockerfile
- Integrado com:
    - MongoDB Atlas
    - Redis gerenciado (Upstash / Redis Labs)

---

# 📈 Escalabilidade

O sistema foi pensado para crescimento:

- Backend stateless
- Cache externo
- Banco gerenciado na nuvem
- Configuração desacoplada
- Pronto para balanceamento de carga

---

# 🧪 Melhorias Futuras

- Sistema de autenticação (JWT)
- Dashboard administrativo
- Estatísticas de cliques
- Links personalizados
- Expiração individual por link
- Observabilidade com Prometheus + Grafana

---

# 👨‍💻 Autor

Pedro Vitor Ribeiro Silva  
Desenvolvedor Full Stack | Java & React  
Foco em sistemas escaláveis, arquitetura limpa e boas práticas de engenharia.

---

# 📄 Licença

MIT License

---

# ⭐ Diferenciais Técnicos

Este projeto demonstra:

- Arquitetura limpa e organizada
- Estratégia real de cache
- Proteção contra abuso
- Separação clara entre frontend e backend
- Deploy em ambiente real de produção
- Mentalidade cloud-native
