# 🌍 Babelup

**Uma plataforma inovadora de educação e desenvolvimento de habilidades** que conecta alunos e professores em um ambiente interativo e organizado, permitindo o aprendizado estruturado com módulos progressivos, videoaulas, materiais em PDF e agendamentos de aulas.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Características Principais](#-características-principais)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Setup](#-instalação-e-setup)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [API Endpoints](#-api-endpoints)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Visão Geral

**Babelup** é uma solução completa para educação moderna que permite:

- **Professores** criarem e gerenciarem módulos de aprendizado estruturados por níveis de dificuldade
- **Alunos** acessarem conteúdos educacionais (videoaulas, PDFs) de forma progressiva
- **Rastreamento de Progresso** para monitorar o desenvolvimento dos alunos
- **Sistema de Agendamento** para aulas e conversas entre professores e alunos
- **Gerenciamento de Usuários** com controle de perfil baseado em funções

O projeto foi idealizado como uma plataforma escalável, segura e intuitiva que democratiza o acesso à educação de qualidade.

---

## ✨ Características Principais

### 👥 Gerenciamento de Usuários
- Registro e autenticação segura
- Roles diferenciadas: Aluno, Professor, Administrador
- Perfis personalizáveis
- Rastreamento de data de cadastro

### 📚 Sistema de Módulos
- Módulos organizados por níveis de dificuldade
- Ordem sequencial para aprendizado estruturado
- Suporte a videoaulas (URL configurável)
- Recursos em PDF para complementação
- Níveis progressivos (Iniciante → Intermediário → Avançado)

### 📊 Progresso do Aluno
- Rastreamento individual de progresso
- Histórico de aprendizado
- Identificação de áreas de melhoria

### 📅 Sistema de Agendamento
- Agendamento de aulas entre professores e alunos
- Suporte a múltiplos alunos por sessão
- Gerenciamento de horários

### 🔒 Segurança
- Autenticação com Spring Security
- Proteção de endpoints
- Validação de dados

---

## 🛠 Stack Tecnológico

### Backend
- **Framework**: Spring Boot 4.0.6
- **Linguagem**: Java 25
- **ORM**: Spring Data JPA
- **Banco de Dados**: MySQL 9.7
- **Segurança**: Spring Security
- **Build Tool**: Maven
- **API**: REST

### Frontend
- **Framework**: React 19.2.6
- **Linguagem**: TypeScript 6.0.2
- **Build Tool**: Vite 8.0.12
- **HTTP Client**: Axios 1.16.0
- **Linting**: ESLint 10.3.0
- **Styling**: CSS

### DevOps & Containerização
- **Docker**: Containerização completa
- **Docker Compose**: Orquestração de serviços
- **MySQL**: Banco de dados relacional
- **Nginx**: Servidor web (via Docker)

---

## 📁 Estrutura do Projeto

```
Babelup/
├── backend/
│   ├── src/
│   │   ├── main/java/com/example/babelup/
│   │   │   ├── entities/           # Entidades JPA
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Modulo.java
│   │   │   │   ├── Nivel.java
│   │   │   │   ├── Agendamento.java
│   │   │   │   ├── Progresso.java
│   │   │   │   └── Perfil.java
│   │   │   ├── controller/         # Controllers REST
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── ModuloController.java
│   │   │   │   └── AgendamentoController.java
│   │   │   └── dto/                # Data Transfer Objects
│   │   ├── test/                   # Testes unitários
│   ├── pom.xml                     # Dependências Maven
│   ├── Dockerfile                  # Imagem Docker do backend
│   └── HELP.md
│
├── frontend/
│   ├── src/
│   │   ├── App.tsx                 # Componente principal
│   │   ├── main.tsx                # Entrada da aplicação
│   │   ├── App.css                 # Estilos globais
│   │   └── index.css
│   ├── public/                     # Arquivos estáticos
│   ├── package.json                # Dependências npm
│   ├── tsconfig.json               # Configuração TypeScript
│   ├── vite.config.ts              # Configuração Vite
│   ├── Dockerfile                  # Imagem Docker do frontend
│   └── README.md
│
├── docker-compose.yml              # Orquestração dos serviços
├── .gitignore
└── .git/
```

---

## 📦 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

### Opção 1: Usando Docker (Recomendado)
- **Docker** (v20.10+)
- **Docker Compose** (v1.29+)

### Opção 2: Desenvolvimento Local
- **Java** 25+
- **Node.js** 18+
- **npm** 9+
- **Maven** 3.8+
- **MySQL** 9.7+ (ou usar Docker apenas para o DB)
- **Git**

---

## 🚀 Instalação e Setup

### Opção 1: Com Docker (Mais Fácil)

1. **Clone o repositório**
```bash
git clone https://github.com/Joaofrts/Babelup.git
cd Babelup
```

2. **Inicie os serviços com Docker Compose**
```bash
docker-compose up -d
```

Isso iniciará:
- 🗄️ **MySQL** na porta `3307`
- 🖥️ **Backend** (Spring Boot) na porta `8080`
- 🌐 **Frontend** (React) na porta `5173`

3. **Aguarde a inicialização** (2-3 minutos)

### Opção 2: Desenvolvimento Local

#### Backend Setup

1. **Navegue para o diretório do backend**
```bash
cd backend
```

2. **Instale as dependências com Maven**
```bash
mvn clean install
```

3. **Configure o banco de dados**
   - Certifique-se de que MySQL está rodando
   - Crie um banco de dados chamado `Babelup`
   - Atualize `application.properties` (se necessário):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Babelup
spring.datasource.username=root
spring.datasource.password=admin
```

4. **Inicie o backend**
```bash
mvn spring-boot:run
```

Backend rodará em: `http://localhost:8080`

#### Frontend Setup

1. **Em outro terminal, navegue para o diretório do frontend**
```bash
cd frontend
```

2. **Instale as dependências npm**
```bash
npm install
```

3. **Configure a variável de ambiente** (se necessário)
Crie um arquivo `.env`:
```
VITE_API_URL=http://localhost:8080
```

4. **Inicie o servidor de desenvolvimento**
```bash
npm run dev
```

Frontend rodará em: `http://localhost:5173`

---

## 🏃 Executando a Aplicação

### Com Docker Compose (Recomendado)

```bash
# Iniciar todos os serviços
docker-compose up

# Iniciar em background
docker-compose up -d

# Parar os serviços
docker-compose down

# Visualizar logs
docker-compose logs -f
```

### Desenvolvimento Local

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm run dev
```

**Terminal 3 (Opcional) - MySQL:**
```bash
# Se usar MySQL localmente
mysql -u root -p
```

### Acessar a Aplicação

- **Frontend**: [http://localhost:5173](http://localhost:5173)
- **Backend**: [http://localhost:8080](http://localhost:8080)
- **Banco de Dados**: `localhost:3307` (quando usando Docker)

### Credenciais Padrão (Docker)
- **MySQL Root Password**: `admin`
- **Database**: `Babelup`

---

## 🏗 Arquitetura do Projeto

### Diagrama de Camadas

```
┌─────────────────────────────────────┐
│   Frontend (React + TypeScript)     │
│   - Componentes UI                  │
│   - Axios HTTP Client               │
└────────────────┬────────────────────┘
                 │ HTTP/REST
┌────────────────▼────────────────────┐
│  Backend (Spring Boot REST API)     │
│  ├─ Controllers                     │
│  ├─ Services                        │
│  ├─ DTOs                            │
│  └─ Security                        │
└────────────────┬────────────────────┘
                 │ JDBC
┌────────────────▼────────────────────┐
│   Banco de Dados (MySQL)            │
│   - Usuários                        │
│   - Módulos                         │
│   - Níveis                          │
│   - Agendamentos                    │
│   - Progresso                       │
└─────────────────────────────────────┘
```

### Entidades e Relacionamentos

**Usuario**
- Atributos: id, nome, email, senha, perfil, dataCadastro
- Relacionamentos: pode ser Professor em Agendamentos, Aluno em Progresso

**Modulo**
- Atributos: id, titulo, urlVideoaula, urlPdf, ordemSequencial
- Relacionamentos: ManyToOne com Nivel

**Nivel**
- Atributos: Representa níveis de dificuldade
- Relacionamentos: OneToMany com Modulo

**Agendamento**
- Atributos: id, dataHora, professor, modulo
- Relacionamentos: ManyToOne com Usuario (professor), ManyToMany com Usuario (alunos)

**Progresso**
- Atributos: Rastreia o progresso dos alunos
- Relacionamentos: Conecta Usuario e Modulo

**Perfil**
- Enum: ALUNO, PROFESSOR, ADMIN

---

## 📡 API Endpoints

### Autenticação
```
POST /api/auth/login       - Fazer login
POST /api/auth/register    - Registrar novo usuário
```

### Usuários
```
GET    /api/usuarios       - Listar todos os usuários
GET    /api/usuarios/{id}  - Obter usuário específico
POST   /api/usuarios       - Criar novo usuário
PUT    /api/usuarios/{id}  - Atualizar usuário
DELETE /api/usuarios/{id}  - Deletar usuário
```

### Módulos
```
GET    /api/modulos        - Listar todos os módulos
GET    /api/modulos/{id}   - Obter módulo específico
POST   /api/modulos        - Criar novo módulo
PUT    /api/modulos/{id}   - Atualizar módulo
DELETE /api/modulos/{id}   - Deletar módulo
```

### Agendamentos
```
GET    /api/agendamentos   - Listar agendamentos
POST   /api/agendamentos   - Criar novo agendamento
GET    /api/agendamentos/{id} - Obter agendamento específico
PUT    /api/agendamentos/{id} - Atualizar agendamento
DELETE /api/agendamentos/{id} - Deletar agendamento
```

### Progresso
```
GET    /api/progresso      - Listar progresso
GET    /api/progresso/{usuarioId} - Obter progresso do usuário
```

---

## 🧪 Testing

### Backend
```bash
cd backend
mvn test
```

### Frontend
```bash
cd frontend
npm run test    # (se configurado)
```

---

## 📝 Desenvolvendo

### Build

**Backend:**
```bash
cd backend
mvn clean package
```

**Frontend:**
```bash
cd frontend
npm run build
```

### Linting

**Backend:**
```bash
cd backend
mvn checkstyle:check
```

**Frontend:**
```bash
cd frontend
npm run lint
```

### Code Style

Siga as convenções:
- **Backend**: Google Java Style Guide
- **Frontend**: ESLint configuration

---

## 🐳 Docker Commands

```bash
# Construir imagens
docker-compose build

# Visualizar containers rodando
docker-compose ps

# Acessar o shell de um container
docker-compose exec backend bash
docker-compose exec frontend sh
docker-compose exec db mysql -u root -padmin

# Ver logs de um serviço específico
docker-compose logs backend

# Reconstruir e reiniciar
docker-compose up -d --build
```

---

## 🔧 Troubleshooting

### Porto já em uso
```bash
# Encontrar processo usando a porta
lsof -i :8080      # Backend
lsof -i :5173      # Frontend
lsof -i :3307      # MySQL

# Matar o processo
kill -9 <PID>
```

### Problemas de conexão MySQL
```bash
# Verificar se MySQL está rodando
docker-compose ps

# Reiniciar o serviço
docker-compose restart db
```

### Limpar Docker
```bash
# Remover containers e volumes
docker-compose down -v

# Remover imagens
docker rmi babelup_backend babelup_frontend
```

---

## 🤝 Contribuindo

1. **Faça um Fork** do repositório
2. **Crie uma branch** para sua feature (`git checkout -b feature/amazing-feature`)
3. **Commit suas mudanças** (`git commit -m 'Add amazing feature'`)
4. **Push para a branch** (`git push origin feature/amazing-feature`)
5. **Abra um Pull Request**

### Guidelines
- Siga as convenções de código do projeto
- Adicione testes para novas funcionalidades
- Mantenha a documentação atualizada
- Use commits descritivos

---

## 📚 Recursos Adicionais

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [React Documentation](https://react.dev/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Vite Documentation](https://vitejs.dev/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

## 👨‍💻 Autor

**Babelup** foi desenvolvido por [Joaofrts](https://github.com/Joaofrts)

---

## 📞 Suporte

Para suporte, questões ou sugestões:
- Abra uma [Issue](https://github.com/Joaofrts/Babelup/issues)
- Entre em contato através dos canais de comunicação do projeto

---

## 🎓 Visão do Projeto

Babelup foi concebido como uma solução educacional moderna que:

✅ **Democratiza o acesso à educação** - Oferecendo uma plataforma acessível para alunos
✅ **Empodera professores** - Com ferramentas para criar e gerenciar conteúdo
✅ **Rastreia progresso** - Permitindo análise detalhada do aprendizado
✅ **Facilita interação** - Através de agendamentos e suporte direto
✅ **Escala globalmente** - Com arquitetura robusta e preparada para crescimento

Contribuindo para uma educação mais inclusiva, tecnológica e eficaz. 🌟

---

**Última atualização**: Maio de 2026
**Versão**: 0.0.1-SNAPSHOT
