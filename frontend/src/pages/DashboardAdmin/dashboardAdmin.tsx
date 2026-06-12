import { redirect, useLoaderData, useNavigate } from 'react-router-dom';
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
import './style.css';

interface AdminProfessoresLoaderData {
  admin: UsuarioDTO | null;
  professores: UsuarioDTO[];
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
  const { admin, professores: professoresLoader } = useLoaderData() as AdminProfessoresLoaderData;

  const [professores, setProfessores] = useState<UsuarioDTO[]>(professoresLoader);
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [salvando, setSalvando] = useState(false);

  const nomeAdmin = admin?.nome || pegarTokenPayload()?.nome || 'Administrador';

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
      setMensagemErro('As senhas não conferem.');
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

      setMensagemErro('Não foi possível cadastrar o professor.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <>
      <div className="admin-page-titles" style={{ marginBottom: '24px' }}>
        <h1 style={{ margin: 0, fontSize: '23px', color: '#111827', fontWeight: 700 }}>Olá, {nomeAdmin}!</h1>
        <p style={{ margin: '4px 0 0', fontSize: '11px', color: '#5f6470' }}>Bem-vindo de volta! Adicione e gerencie os professores da plataforma.</p>
      </div>

      <form className="admin-professores-card" onSubmit={cadastrarProfessor}>
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

        <button type="submit" className="admin-professores-submit-button" disabled={salvando}>
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
              <article className="admin-professores-item" key={professor.id || professor.email}>
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
    </>
  );
}