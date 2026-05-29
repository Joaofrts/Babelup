# 🌍 Babelup

**Uma plataforma inovadora e completa de educação digital** que conecta alunos e professores em um ambiente interativo e inteligente, oferecendo aprendizado estruturado com módulos progressivos, avaliações adaptativas, gamificação, prática com exercícios, monitoramento de evasão e comunicação integrada.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Características Principais](#-características-principais)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitetura do Sistema](#-arquitetura-do-sistema)
- [Estrutura de Domínios](#-estrutura-de-domínios)
- [Componentes Principais](#-componentes-principais)
- [Padrões de Design](#-padrões-de-design)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Setup](#-instalação-e-setup)
- [Executando a Aplicação](#-executando-a-aplicação)
- [Segurança e Credenciais](#-segurança-e-credenciais)
- [API Endpoints](#-api-endpoints)
- [Desenvolvendo](#-desenvolvendo)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)

---

## 🎯 Visão Geral

**Babelup** é uma plataforma educacional de última geração que oferece:

### Funcionalidades Principais

#### 👥 Gestão de Usuários e Matrículas
- Autenticação e autorização baseada em papéis (Aluno, Professor, Administrador)
- Criação dinâmica de usuários utilizando padrão Factory
- Gestão de matrículas com rastreamento de status
- Validação robusta de dados de usuários

#### 📚 Estrutura Acadêmica
- Módulos de aprendizado organizados por níveis de dificuldade
- Conteúdo em múltiplos formatos: vídeoaulas, exercícios, materiais de apoio
- Organização sequencial e progressiva do conteúdo
- Suporte a diferentes tipos de vídeos (explanação, revisão, complementar)

#### 📊 Sistema de Avaliação e Nivelamento
- Testes diagnósticos para avaliar conhecimento inicial
- Avaliação contínua através de exercícios
- Sessões de conversação para feedback personalizado
- Rastreamento de respostas do aluno com histórico completo

#### 🏆 Gamificação e Engajamento
- Sistema de ranking com pontuação
- Progresso visual do aprendizado
- Incentivos baseados em desempenho

#### ⚠️ Monitoramento de Evasão
- Detecção automática de padrões de evasão
- Alertas e intervenções proativas
- Análise de engajamento do aluno

#### 💬 Sistema de Comunicação
- Chat em tempo real entre alunos e professores
- Fórum para discussões em grupo
- Sistema de notificações inteligente
- Gerenciamento de mensagens

#### 💰 Gestão Financeira
- Controle de pagamentos de matrículas
- Rastreamento de status de pagamento
- Histórico de transações

#### 📝 Prática e Exercícios
- Exercícios práticos com diferentes níveis de dificuldade
- Validação automática de respostas
- Histórico de tentativas e desempenho

---

## ✨ Características Principais por Aspecto

### 🎓 Para Professores
- Criar e organizar módulos e conteúdos
- Acompanhar progresso de alunos em tempo real
- Identificar alunos em risco de evasão
- Comunicação direta com alunos
- Análise de desempenho da turma

### 🧑‍🎓 Para Alunos
- Aprendizado personalizado e adaptativo
- Acesso a múltiplos formatos de conteúdo
- Prática com exercícios e feedback
- Visualização de progresso e ranking
- Comunicação com professores e colegas

### ⚙️ Para Administradores
- Gerenciamento de usuários
- Monitoramento de matrículas e pagamentos
- Visibilidade sobre todo o sistema
- Controle de acesso e segurança

---

## 🛠 Stack Tecnológico

### Backend
- **Framework**: Spring Boot 4.0.6
- **Linguagem**: Java 25
- **ORM**: Spring Data JPA
- **Banco de Dados**: MySQL 9.7
- **Segurança**: Spring Security + JWT
- **Build Tool**: Maven
- **API**: REST
- **Padrões**: Factory, Strategy, DTO, Repository, Service
- **Enums**: Múltiplos enums para tipos e status

### Frontend
- **Framework**: React 19.2.6
- **Linguagem**: TypeScript 6.0.2
- **Build Tool**: Vite 8.0.12
- **HTTP Client**: Axios 1.16.1
- **Roteamento**: React Router v7.15.1
- **Linting**: ESLint 10.3.0
- **Styling**: CSS

### DevOps & Containerização
- **Docker**: Containerização completa
- **Docker Compose**: Orquestração de serviços
- **MySQL**: Banco de dados relacional
- **Nginx**: Servidor web (via Docker)

---

## 🏗 Arquitetura do Sistema

### Padrão em Camadas

```
┌─────────────────────────────────────┐
│   Frontend (React + TypeScript)     │
│   - Componentes UI                  │
│   - Axios HTTP Client               │
│   - React Router                    │
└────────────────┬────────────────────┘
                 │ HTTP/REST + JWT
┌────────────────▼────────────────────┐
│  Backend (Spring Boot REST API)     │
│  ├─ Controller (REST Endpoints)     │
│  ├─ Service (Business Logic)        │
│  ├─ Repository (Data Access)        │
│  ├─ Entity (JPA Models)             │
│  ├─ DTO (Data Transfer)             │
│  ├─ Configuration (Security, JWT)   │
│  └─ Exception Handling              │
└────────────────┬────────────────────┘
                 │ JDBC
┌────────────────▼────────────────────┐
│   Banco de Dados (MySQL)            │
│   - Tabelas de domínios             │
│   - Índices otimizados              │
│   - Soft delete support             │
└─────────────────────────────────────┘
```

### Fluxo de Autenticação
1. Usuário faz login via `/api/auth/login`
2. Backend valida credenciais e gera JWT
3. Frontend armazena token
4. Requisições posteriores incluem token no header
5. JwtAuthFilter valida token a cada requisição
6. Acesso permitido/negado baseado em roles

---

## 🗂 Estrutura de Domínios

O backend está organizado em domínios de negócio, cada um com suas entidades, repositórios e serviços:

### 👥 Domínio: Usuários e Acesso
**Pacote**: `entities/usuarios`, `repository/usuarios`, `service`

**Entidades**:
- `Usuario` (classe base com autenticação)
- `Aluno` (estende Usuario)
- `Professor` (estende Usuario)
- `Administrador` (estende Usuario)
- `Matricula` (relacionamento Aluno-Modulo)

**Padrões**:
- Factory Pattern para criação de usuários (`UsuarioFactory`)
- Strategy Pattern para validação (`UsuarioValidator`)
- Strategies específicas: `AlunoCreationStrategy`, `ProfessorCreationStrategy`, `AdminCreationStrategy`

**Serviços**:
- `UsuarioService`: CRUD e autenticação de usuários
- `AutenticacaoService`: Lógica de login/registro

### 📚 Domínio: Estrutura Acadêmica
**Pacote**: `entities/estruturaAcademica`, `service`

**Entidades**:
- `Nivel` (Iniciante, Intermediário, Avançado)
- `Modulo` (organizados por Nivel)

**Serviços**:
- `NivelService`: Gerenciamento de níveis
- `ModuloService`: CRUD e organização de módulos

### 📝 Domínio: Prática e Conteúdo
**Pacote**: `entities/pratica`, `service`

**Entidades**:
- `VideoAula` (com tipos: explanação, revisão, complementar)
- `Exercicio` (com níveis de dificuldade)
- `MaterialApoio` (PDFs, documentos, recursos)

**Serviços**:
- `PraticaService`: Gerenciamento de exercícios e validação
- `ConteudoAulaService`: Organização de conteúdo

### 📊 Domínio: Avaliação e Nivelamento
**Pacote**: `entities/avaliacao`, `service`

**Entidades**:
- `TesteDiagnostico` (avaliação inicial com tipos)
- `SessaoConversacao` (modalidades: texto, vídeo, áudio)
- `RespostaAluno` (respostas com feedback)

**Serviços**:
- `AvaliacaoNivelamentoService`: Processamento de avaliações
- `SessaoConversacaoService`: Gerenciamento de sessões

### 🏆 Domínio: Progresso e Gamificação
**Pacote**: `entities/progressoGamificacao`, `service`

**Entidades**:
- `ProgressoAluno` (status e pontuação)
- `Ranking` (posicionamento entre alunos)

**Serviços**:
- `ProgressoService`: Rastreamento de progresso
- `GamificacaoService`: Cálculo de pontos e ranking

### 💬 Domínio: Comunicação
**Pacote**: `entities/comunicacao`, `service`

**Entidades**:
- `MensagemChat` (comunicação privada)
- `PostForum` (discussões em grupo)
- `Notificacao` (tipos de notificação)
- `AlertaEvasao` (detecção de evasão)

**Serviços**:
- `EvasaoService`: Análise de padrões de evasão
- `MonitoramentoEvasaoService`: Monitoramento contínuo

### 💰 Domínio: Financeiro
**Pacote**: `entities/financeiro`

**Entidades**:
- `Pagamento` (status de pagamento)

### 🔧 Estruturas Base
**Pacote**: `entities/base`

**Classes Base**:
- `EntidadeBase` (atributos comuns)
- `EntidadeAuditavel` (auditoria de criação/modificação)
- `SoftDeleteEntity` (suporte a soft delete)

### 📋 Enumerações
**Pacote**: `entities/enumEntities`

Definem tipos e status:
- `EnumPerfil`: ALUNO, PROFESSOR, ADMIN
- `EnumStatusMatricula`: ATIVA, CONCLUIDA, CANCELADA, SUSPENSA
- `EnumStatusProgresso`: NAO_INICIADO, EM_PROGRESSO, CONCLUIDO, REPROVADO
- `EnumStatusPagamento`: PENDENTE, APROVADO, RECUSADO, REEMBOLSADO
- `EnumTipoNotificacao`: MENSAGEM, ALERTA, ATUALIZACAO, SISTEMA
- `EnumTipoSessao`: INDIVIDUAL, GRUPO
- `EnumModalidadeSessao`: TEXTO, VIDEO, AUDIO
- `EnumTipoTeste`: DIAGNOSTICO, PROFICIENCIA, CERTIFICACAO
- `EnumTipoVideo`: EXPLANACAO, REVISAO, COMPLEMENTAR
- `EnumStatusSessao`: AGENDADA, ATIVA, CONCLUIDA, CANCELADA

---

## 🔌 Componentes Principais

### Controllers (REST Endpoints)
- `AutenticacaoController`: Login, registro, refresh de tokens
- `AlunoController`: Funcionalidades específicas de alunos
- `AdminController`: Funções administrativas
- `CatalogoController`: Catálogo de cursos e módulos
- `ModuloController`: Gerenciamento de módulos
- `NivelController`: Gerenciamento de níveis

### Serviços (14 no total)
1. `AutenticacaoService` - Autenticação e autorização
2. `UsuarioService` - Gerenciamento de usuários
3. `ModuloService` - Módulos educacionais
4. `NivelService` - Níveis de dificuldade
5. `PraticaService` - Exercícios e prática
6. `ProgressoService` - Rastreamento de progresso
7. `GamificacaoService` - Ranking e pontuação
8. `AvaliacaoNivelamentoService` - Avaliações
9. `SessaoConversacaoService` - Conversas e feedback
10. `EvasaoService` - Detecção de evasão
11. `MonitoramentoEvasaoService` - Monitoramento contínuo
12. `ConteudoAulaService` - Organização de conteúdo
13. `CatalogoService` - Catálogo de cursos
14. `JwtService` - Gerenciamento de tokens JWT

### Repositórios (Data Access)
Cada entidade possui um repositório Spring Data JPA:
- `UsuarioRepository`, `AlunoRepository`, `ProfessorRepository`, `AdministradorRepository`
- `ModuloRepository`, `NivelRepository`
- `VideoAulaRepository`, `ExercicioRepository`, `MaterialApoioRepository`
- `TesteDiagnosticoRepository`, `SessaoConversacaoRepository`, `RespostaAlunoRepository`
- `ProgressoAlunoRepository`, `RankingRepository`
- `MensagemChatRepository`, `PostForumRepository`, `NotificacaoRepository`, `AlertaEvasaoRepository`
- `PagamentoRepository`, `MatriculaRepository`

### DTOs (Data Transfer Objects)
Padronização de dados trafegados:
- `LoginDto`, `AutenticacaoRespostaDto`, `RefreshTokenRequisicaoDto`
- `NovoUsuarioDto`, `UsuarioRespostaDTO`
- `ProfessorCadastroDto`, `PerfilAlunoDto`
- `ModuloDto`, `UpdateModuloDto`, `AdicionarModuloDto`
- `RespostaModuloDto`, `RespostaProgressoDto`
- `NivelDto`
- `CursoCatalogoDTO`

### Configuração
- `SecurityConfig`: Configuração de Spring Security
- `JwtAuthFilter`: Filtro de autenticação JWT
- `Config`: Configurações gerais
- `WebConfig`: Configuração CORS e web

---

## 🎯 Padrões de Design

### Factory Pattern
Criação dinâmica de usuários baseada em tipo:
```java
UsuarioFactory factory = new UsuarioFactory();
Usuario usuario = factory.criarUsuario(tipo, dados);
```

### Strategy Pattern
Validações específicas por tipo de usuário:
- `AlunoCreationStrategy`
- `ProfessorCreationStrategy`
- `AdminCreationStrategy`

### DTO Pattern
Transferência segura de dados entre camadas

### Repository Pattern
Abstração de acesso a dados via Spring Data JPA

### Service Layer
Isolamento de lógica de negócio

### Soft Delete
Entidades suportam exclusão lógica (não física)

### Auditoria
Rastreamento automático de criação/modificação

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

> ⚠️ **Nota de Segurança**: As credenciais padrão fornecidas no arquivo `docker-compose.yml` são apenas para desenvolvimento. Para produção, substitua-as por credenciais seguras e use variáveis de ambiente.

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
   - Configure as credenciais do banco de dados em `application.properties` (use credenciais seguras, diferentes das padrões do projeto)
   - **Importante**: Nunca use as credenciais padrão do repositório em ambientes que não sejam locais

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

# Logs de um serviço específico
docker-compose logs -f backend
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

### Acessar a Aplicação

- **Frontend**: [http://localhost:5173](http://localhost:5173)
- **Backend API**: [http://localhost:8080](http://localhost:8080)
- **Banco de Dados**: `localhost:3307` (quando usando Docker)

---

## 🔒 Segurança e Credenciais

### ⚠️ IMPORTANTE - Desenvolvimento vs Produção

O repositório contém configurações padrão apenas para fins de desenvolvimento local usando Docker. **Essas credenciais padrão nunca devem ser usadas em ambientes de produção!**

### Configuração Segura para Produção

- ✅ Sempre defina senhas fortes e únicas para todas as credenciais
- ✅ Utilize variáveis de ambiente para armazenar valores sensíveis
- ✅ Use um gerenciador de secrets (AWS Secrets Manager, HashiCorp Vault, etc.)
- ✅ Nunca comite credenciais reais no repositório (adicione-as ao .gitignore)
- ✅ Implemente rotação regular de tokens e chaves JWT
- ✅ Configure JWT com chaves seguras e únicas
- ✅ Valide e sanitize todas as entradas de usuário
- ✅ Sempre revise os arquivos de configuração antes de fazer deploy
- ✅ Use HTTPS em produção
- ✅ Implemente rate limiting nos endpoints
- ✅ Adicione CORS configurado adequadamente
- ✅ Utilize soft delete para manter auditoria de dados

### Autenticação e Autorização

O sistema implementa:
- **Spring Security** para gerenciamento de segurança
- **JWT (JSON Web Tokens)** para autenticação stateless
- **Role-Based Access Control (RBAC)** com três papéis: ALUNO, PROFESSOR, ADMIN
- **JwtAuthFilter** para validação de tokens em todas as requisições
- **Validação de entrada** em todos os endpoints

---

## 📡 API Endpoints

### Autenticação
```
POST   /api/auth/login              - Autenticar usuário
POST   /api/auth/register           - Registrar novo usuário
POST   /api/auth/refresh-token      - Renovar token JWT
```

### Usuários (Admin)
```
GET    /api/admin/usuarios          - Listar todos os usuários
GET    /api/admin/usuarios/{id}     - Obter usuário específico
POST   /api/admin/usuarios          - Criar novo usuário
PUT    /api/admin/usuarios/{id}     - Atualizar usuário
DELETE /api/admin/usuarios/{id}     - Deletar usuário
```

### Alunos
```
GET    /api/aluno/perfil            - Obter perfil do aluno
PUT    /api/aluno/perfil            - Atualizar perfil do aluno
GET    /api/aluno/progresso         - Visualizar progresso
GET    /api/aluno/modulos           - Listar módulos disponíveis
GET    /api/aluno/ranking           - Visualizar ranking
```

### Módulos
```
GET    /api/modulo                  - Listar todos os módulos
GET    /api/modulo/{id}             - Obter módulo específico
POST   /api/modulo                  - Criar novo módulo (Professor/Admin)
PUT    /api/modulo/{id}             - Atualizar módulo
DELETE /api/modulo/{id}             - Deletar módulo
```

### Níveis
```
GET    /api/nivel                   - Listar níveis de dificuldade
GET    /api/nivel/{id}              - Obter nível específico
POST   /api/nivel                   - Criar novo nível
PUT    /api/nivel/{id}              - Atualizar nível
```

### Catálogo
```
GET    /api/catalogo                - Listar cursos disponíveis no catálogo
GET    /api/catalogo/{id}           - Detaldes de um curso
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

### Testes

**Backend:**
```bash
cd backend
mvn test
```

**Frontend:**
```bash
cd frontend
npm run test    # (se configurado)
```

---

## 🐳 Docker Commands

```bash
# Construir imagens
docker-compose build

# Reconstruir e reiniciar
docker-compose up -d --build

# Visualizar containers rodando
docker-compose ps

# Acessar o shell de um container
docker-compose exec backend bash
docker-compose exec frontend sh

# Ver logs de um serviço específico
docker-compose logs backend

# Remover containers e volumes
docker-compose down -v

# Remover imagens
docker rmi babelup_backend babelup_frontend
```

---

## 🔧 Troubleshooting

### Porto já em uso
```bash
# Encontrar processo usando a porta
lsof -i :8080      # Backend
lsof -i :5173      # Frontend
lsof -i :3307      # MySQL

# Matar o processo (com cuidado)
kill -9 <PID>
```

### Problemas de conexão MySQL
```bash
# Verificar se MySQL está rodando
docker-compose ps

# Reiniciar o serviço
docker-compose restart db

# Verificar logs
docker-compose logs db
```

### Problemas de autenticação
- Verifique se o token JWT está sendo enviado no header `Authorization: ******
- Confirme se o token não expirou
- Verifique a chave secreta do JWT em `application.properties`

### Limpar Docker
```bash
# Remover containers e volumes
docker-compose down -v

# Remover imagens
docker rmi babelup_backend babelup_frontend

# Remover tudo (cuidado!)
docker system prune -a --volumes
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
- Não comita credenciais ou dados sensíveis

---

## 📚 Recursos Adicionais

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [React Documentation](https://react.dev/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Vite Documentation](https://vitejs.dev/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)
- [JWT.io](https://jwt.io/)

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

✅ **Democratiza o acesso à educação** - Oferecendo uma plataforma acessível para alunos de todos os níveis
✅ **Empodera professores** - Com ferramentas para criar, organizar e acompanhar conteúdo
✅ **Rastreia progresso inteligentemente** - Permitindo análise detalhada do aprendizado com gamificação
✅ **Detecta e previne evasão** - Através de monitoramento proativo e alertas
✅ **Facilita comunicação** - Via chat, fórum e notificações em tempo real
✅ **Escala globalmente** - Com arquitetura robusta baseada em padrões de design modernos

Contribuindo para uma educação mais inclusiva, tecnológica, eficaz e equitativa. 🌟

---

**Última atualização**: Maio de 2026
**Versão**: 0.0.1-SNAPSHOT

> **Status do Projeto**: Em desenvolvimento ativo com foco em funcionalidades principais de educação, avaliação, gamificação, prática e monitoramento de evasão.
