import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import { API } from '../../services/api';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface UsuarioDTO {
  id?: string;
  nome?: string;
  email?: string;
  perfil?: string;
}

interface AdminProfessoresLoaderData {
  admin: UsuarioDTO | null;
  professores: UsuarioDTO[];
}

function normalizarLista<T>(data: unknown): T[] {
  if (Array.isArray(data)) return data as T[];

  if (
    data &&
    typeof data === 'object' &&
    'content' in data &&
    Array.isArray((data as { content: unknown }).content)
  ) {
    return (data as { content: T[] }).content;
  }

  if (
    data &&
    typeof data === 'object' &&
    'data' in data &&
    Array.isArray((data as { data: unknown }).data)
  ) {
    return (data as { data: T[] }).data;
  }

  return [];
}

function pegarTokenPayload(token: string) {
  try {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(payloadJson);
  } catch {
    return null;
  }
}

function pegarEmailDoToken(token: string) {
  const payload = pegarTokenPayload(token);

  return (
    payload?.sub ||
    payload?.email ||
    payload?.username ||
    payload?.usuario ||
    ''
  );
}

function pegarIniciais(nome?: string) {
  if (!nome) return 'AD';

  return nome
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((parte) => parte[0])
    .join('')
    .toUpperCase();
}

function filtrarProfessores(usuarios: UsuarioDTO[]) {
  return usuarios.filter((usuario) => usuario.perfil === 'PROFESSOR');
}

export async function adminProfessoresLoader({ request }: { request: Request }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return redirect('/login-admin');
  }

  try {
    const headers = {
      Authorization: `Bearer ${token}`,
    };

    const emailLogado = pegarEmailDoToken(token);

    const usuariosResponse = await API.get('/admin/listarUsuarios', {
      signal: request.signal,
      headers,
    });

    const usuarios = normalizarLista<UsuarioDTO>(usuariosResponse.data);

    const adminEncontrado =
      usuarios.find((usuario) => usuario.email === emailLogado) ||
      usuarios.find((usuario) => usuario.perfil === 'ADMIN') ||
      usuarios.find((usuario) => usuario.perfil === 'ADMINISTRADOR') ||
      null;

    return {
      admin: adminEncontrado,
      professores: filtrarProfessores(usuarios),
    } as AdminProfessoresLoaderData;
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401 || error.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');

        return redirect('/login-admin');
      }

      throw new Response(
        error.response?.data?.message || 'Erro ao carregar professores.',
        { status: error.response?.status || 500 }
      );
    }

    throw new Response('Erro interno ao carregar professores.', {
      status: 500,
    });
  }
}

export default function AdminProfessores() {
  const navigate = useNavigate();
  const { admin, professores: professoresLoader } =
    useLoaderData() as AdminProfessoresLoaderData;

  const [professores, setProfessores] = useState<UsuarioDTO[]>(professoresLoader);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [idiomasLecionados, setIdiomasLecionados] = useState('');
  const [disponibilidade, setDisponibilidade] = useState('');
  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [salvando, setSalvando] = useState(false);

  const nomeAdmin = admin?.nome || 'Administrador';
  const iniciais = pegarIniciais(nomeAdmin);

  function sair() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    navigate('/login-admin');
  }

  async function recarregarProfessores() {
    const token = localStorage.getItem('token');

    if (!token) {
      navigate('/login-admin');
      return;
    }

    const response = await API.get('/admin/listarUsuarios', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    const usuarios = normalizarLista<UsuarioDTO>(response.data);
    setProfessores(filtrarProfessores(usuarios));
  }

  async function cadastrarProfessor(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setMensagemErro('');
    setMensagemSucesso('');

    if (!nome.trim()) {
      setMensagemErro('Informe o nome do professor.');
      return;
    }

    if (!email.trim()) {
      setMensagemErro('Informe o e-mail do professor.');
      return;
    }

    if (!senha.trim()) {
      setMensagemErro('Informe a senha do professor.');
      return;
    }

    if (senha !== confirmarSenha) {
      setMensagemErro('As senhas não conferem.');
      return;
    }

    const token = localStorage.getItem('token');

    if (!token) {
      navigate('/login-admin');
      return;
    }

    try {
      setSalvando(true);

      await API.post(
        '/admin/cadastroProfessor',
        {
          nome,
          email,
          senha,
          idiomasLecionados: idiomasLecionados || null,
          disponibilidade: disponibilidade || null,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMensagemSucesso('Professor cadastrado com sucesso.');

      setNome('');
      setEmail('');
      setSenha('');
      setConfirmarSenha('');
      setIdiomasLecionados('');
      setDisponibilidade('');

      await recarregarProfessores();
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 401 || error.response?.status === 403) {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          navigate('/login-admin');
          return;
        }

        setMensagemErro(
          error.response?.data?.message ||
            error.response?.data?.erro ||
            'Não foi possível cadastrar o professor.'
        );
        return;
      }

      setMensagemErro('Erro interno ao cadastrar professor.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <main className="admin-professores-page">
      <aside className="admin-professores-sidebar">
        <div className="admin-professores-sidebar-logo-area">
          <img
            src={logoAzul}
            alt="Logo BabelUp"
            className="admin-professores-sidebar-logo"
          />
        </div>

        <nav className="admin-professores-sidebar-menu">
          <Link to="/dashboard-admin" className="admin-professores-menu-item">
            <span>♧</span>
            Avisos
          </Link>

          <Link to="/admin/cursos" className="admin-professores-menu-item">
            <span>▱</span>
            Cursos
          </Link>

          <Link to="/admin/alunos" className="admin-professores-menu-item">
            <span>🎓</span>
            Alunos
          </Link>

          <Link
            to="/admin/professores"
            className="admin-professores-menu-item active"
          >
            <span>▦</span>
            Professores
          </Link>

          <Link to="/admin/estatisticas" className="admin-professores-menu-item">
            <span>▤</span>
            Estatísticas
          </Link>

          <Link to="/admin/chat" className="admin-professores-menu-item">
            <span>♡</span>
            Chat
          </Link>

          <Link to="/admin/forum" className="admin-professores-menu-item">
            <span>♢</span>
            Fórum
          </Link>
        </nav>

        <button
          type="button"
          className="admin-professores-logout-button"
          onClick={sair}
        >
          Sair
        </button>
      </aside>

      <section className="admin-professores-main">
        <header className="admin-professores-topbar">
          <div>
            <h1>Olá, {nomeAdmin}!</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="admin-professores-topbar-actions">
            <button type="button" className="admin-professores-notification-button">
              ♧
              <span />
            </button>

            <div className="admin-professores-avatar">{iniciais}</div>
          </div>
        </header>

        <section className="admin-professores-content">
          <form
            className="admin-professores-card"
            onSubmit={cadastrarProfessor}
          >
            <h2>
              <span>▦</span>
              Adicionar professor
            </h2>

            {mensagemErro && (
              <p className="admin-professores-message admin-professores-message-error">
                {mensagemErro}
              </p>
            )}

            {mensagemSucesso && (
              <p className="admin-professores-message admin-professores-message-success">
                {mensagemSucesso}
              </p>
            )}

            <div className="admin-professores-form-row">
              <div className="admin-professores-field">
                <label htmlFor="nome">Nome</label>
                <input
                  id="nome"
                  type="text"
                  placeholder="Digite o nome do professor..."
                  value={nome}
                  onChange={(event) => setNome(event.target.value)}
                />
              </div>

              <div className="admin-professores-field">
                <label htmlFor="email">E-mail</label>
                <input
                  id="email"
                  type="email"
                  placeholder="Digite o e-mail do professor..."
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                />
              </div>
            </div>

            <div className="admin-professores-form-row">
              <div className="admin-professores-field">
                <label htmlFor="senha">Senha</label>
                <input
                  id="senha"
                  type="password"
                  placeholder="Digite a senha..."
                  value={senha}
                  onChange={(event) => setSenha(event.target.value)}
                />
              </div>

              <div className="admin-professores-field">
                <label htmlFor="confirmarSenha">Confirmar senha</label>
                <input
                  id="confirmarSenha"
                  type="password"
                  placeholder="Confirme a senha..."
                  value={confirmarSenha}
                  onChange={(event) => setConfirmarSenha(event.target.value)}
                />
              </div>
            </div>

            <div className="admin-professores-form-row">
              <div className="admin-professores-field">
                <label htmlFor="idiomas">Idiomas lecionados</label>
                <input
                  id="idiomas"
                  type="text"
                  placeholder="Exemplo: Inglês, Espanhol..."
                  value={idiomasLecionados}
                  onChange={(event) => setIdiomasLecionados(event.target.value)}
                />
              </div>

              <div className="admin-professores-field">
                <label htmlFor="disponibilidade">Disponibilidade</label>
                <input
                  id="disponibilidade"
                  type="text"
                  placeholder="Exemplo: Segunda a sexta..."
                  value={disponibilidade}
                  onChange={(event) => setDisponibilidade(event.target.value)}
                />
              </div>
            </div>

            <button
              type="submit"
              className="admin-professores-submit-button"
              disabled={salvando}
            >
              {salvando ? 'Cadastrando...' : 'Cadastrar professor'}
            </button>
          </form>

          <section className="admin-professores-list-card">
            <h2>Professores cadastrados</h2>

            {professores.length === 0 ? (
              <p className="admin-professores-empty">
                Nenhum professor cadastrado no momento.
              </p>
            ) : (
              <div className="admin-professores-list">
                {professores.map((professor) => (
                  <article
                    className="admin-professores-item"
                    key={professor.id || professor.email}
                  >
                    <div>
                      <strong>{professor.nome || 'Professor sem nome'}</strong>
                      <p>{professor.email || 'E-mail não informado'}</p>
                    </div>

                    <span>{professor.perfil}</span>
                  </article>
                ))}
              </div>
            )}
          </section>
        </section>
      </section>
    </main>
  );
}