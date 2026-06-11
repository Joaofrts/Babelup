import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  cadastrarProfessor as cadastrarProfessorBackend,
  listarUsuarios,
  limparSessao,
  pegarTokenPayload,
  type UsuarioDTO,
} from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface AdminProfessoresLoaderData {
  admin: UsuarioDTO | null;
  professores: UsuarioDTO[];
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
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const payload = pegarTokenPayload();
    const usuarios = await listarUsuarios(request.signal);
    const adminEncontrado =
      usuarios.find((usuario) => usuario.email === payload?.sub) ||
      usuarios.find((usuario) => usuario.perfil === 'ADMIN') ||
      null;

    return {
      admin: adminEncontrado,
      professores: filtrarProfessores(usuarios),
    } as AdminProfessoresLoaderData;
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }

    throw new Response('Erro ao carregar professores.', { status: 500 });
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
  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [salvando, setSalvando] = useState(false);

  const nomeAdmin = admin?.nome || pegarTokenPayload()?.nome || 'Administrador';
  const iniciais = pegarIniciais(nomeAdmin);

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  async function recarregarProfessores() {
    const usuarios = await listarUsuarios();
    setProfessores(filtrarProfessores(usuarios));
  }

  async function cadastrarProfessor(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMensagemErro('');
    setMensagemSucesso('');

    if (!nome.trim() || !email.trim() || !senha.trim()) {
      setMensagemErro('Informe nome, e-mail e senha.');
      return;
    }

    if (senha !== confirmarSenha) {
      setMensagemErro('As senhas nao conferem.');
      return;
    }

    try {
      setSalvando(true);
      await cadastrarProfessorBackend({
        nome: nome.trim(),
        email: email.trim(),
        senha,
      });

      setMensagemSucesso('Professor cadastrado com sucesso.');
      setNome('');
      setEmail('');
      setSenha('');
      setConfirmarSenha('');

      await recarregarProfessores();
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
        limparSessao();
        navigate('/login-admin');
        return;
      }

      setMensagemErro('Nao foi possivel cadastrar o professor.');
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
          <Link to="/dashboard-admin" className="admin-professores-menu-item active">
            <span>AD</span>
            Avisos
          </Link>

          <Link to="/admin/cursos" className="admin-professores-menu-item">
            <span>CU</span>
            Cursos
          </Link>

          <Link to="/admin/alunos" className="admin-professores-menu-item">
            <span>AL</span>
            Alunos
          </Link>

          <Link to="/admin/professores" className="admin-professores-menu-item">
            <span>PR</span>
            Professores
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
            <h1>Ola, {nomeAdmin}!</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="admin-professores-topbar-actions">
            <button type="button" className="admin-professores-notification-button">
              AD
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
              <span>PR</span>
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
                      <p>{professor.email || 'E-mail nao informado'}</p>
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
