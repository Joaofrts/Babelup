import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import axios from 'axios';
import { listarUsuarios, limparSessao, type UsuarioDTO } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

export async function adminProfessoresListaLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const usuarios = await listarUsuarios(request.signal);
    return usuarios.filter((usuario) => usuario.perfil === 'PROFESSOR');
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
  const professores = useLoaderData() as UsuarioDTO[];
  const [busca, setBusca] = useState('');

  const professoresFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();

    if (!textoBusca) {
      return professores;
    }

    return professores.filter((professor) => (
      (professor.nome || '').toLowerCase().includes(textoBusca) ||
      (professor.email || '').toLowerCase().includes(textoBusca)
    ));
  }, [busca, professores]);

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  return (
    <main className="admin-professores-page">
      <aside className="admin-professores-sidebar">
        <div className="admin-professores-logo-area">
          <img
            src={logoAzul}
            alt="Logo BabelUp"
            className="admin-professores-logo"
          />
        </div>

        <nav className="admin-professores-menu">
          <Link to="/dashboard-admin" className="admin-professores-menu-item">
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

          <Link to="/admin/professores" className="admin-professores-menu-item active">
            <span>PR</span>
            Professores
          </Link>

          <Link to="/admin/estatisticas" className="admin-professores-menu-item">
            <span>ES</span>
            Estatisticas
          </Link>

          <Link to="/admin/chat" className="admin-professores-menu-item">
            <span>CH</span>
            Chat
          </Link>

          <Link to="/admin/forum" className="admin-professores-menu-item">
            <span>FO</span>
            Forum
          </Link>
        </nav>

        <button type="button" className="admin-professores-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-professores-main">
        <header className="admin-professores-header">
          <div>
            <h1>Professores</h1>
            <p>Usuarios cadastrados como professores.</p>
          </div>

          <div className="admin-professores-header-actions">
            <button type="button" className="admin-professores-bell">
              AD
              <span />
            </button>

            <div className="admin-professores-avatar">AD</div>
          </div>
        </header>

        <section className="admin-professores-content">
          <div className="admin-professores-card">
            <div className="admin-professores-card-header">
              <h2>
                <span>PR</span>
                Gerenciar professores
              </h2>

              <div className="admin-professores-actions">
                <Link to="/dashboard-admin" className="admin-professores-add">
                  Adicionar professor
                </Link>
              </div>
            </div>

            <input
              type="text"
              className="admin-professores-search"
              placeholder="Pesquisar professores..."
              value={busca}
              onChange={(event) => setBusca(event.target.value)}
            />

            <div className="admin-professores-table-wrapper">
              <table className="admin-professores-table">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Perfil</th>
                    <th>Acoes</th>
                  </tr>
                </thead>

                <tbody>
                  {professoresFiltrados.map((professor) => (
                    <tr key={professor.id || professor.email}>
                      <td>{professor.nome || 'Professor sem nome'}</td>
                      <td>{professor.email || 'E-mail nao informado'}</td>
                      <td>{professor.perfil || 'PROFESSOR'}</td>
                      <td>
                        <button type="button" className="admin-professores-edit" disabled>
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}

                  {professoresFiltrados.length === 0 && (
                    <tr>
                      <td colSpan={4} className="admin-professores-empty">
                        Nenhum professor encontrado.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <div className="admin-professores-illustration" aria-hidden="true">
            <div className="bubble bubble-one">A</div>
            <div className="bubble bubble-two">B</div>
            <div className="card-line card-line-one" />
            <div className="card-line card-line-two" />
          </div>
        </section>
      </section>
    </main>
  );
}
