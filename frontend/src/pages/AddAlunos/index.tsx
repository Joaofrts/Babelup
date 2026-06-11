import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import axios from 'axios';
import { listarUsuarios, limparSessao, type UsuarioDTO } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

export async function adminAlunosLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const usuarios = await listarUsuarios(request.signal);
    return usuarios.filter((usuario) => usuario.perfil === 'ALUNO');
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }

    throw new Response('Erro ao carregar alunos.', { status: 500 });
  }
}

export default function AdminAlunos() {
  const navigate = useNavigate();
  const alunos = useLoaderData() as UsuarioDTO[];
  const [busca, setBusca] = useState('');

  const alunosFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();

    if (!textoBusca) {
      return alunos;
    }

    return alunos.filter((aluno) => (
      (aluno.nome || '').toLowerCase().includes(textoBusca) ||
      (aluno.email || '').toLowerCase().includes(textoBusca)
    ));
  }, [busca, alunos]);

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  return (
    <main className="admin-alunos-page">
      <aside className="admin-alunos-sidebar">
        <div className="admin-alunos-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="admin-alunos-logo" />
        </div>

        <nav className="admin-alunos-menu">
          <Link to="/dashboard-admin" className="admin-alunos-menu-item">
            <span>AD</span>
            Avisos
          </Link>

          <Link to="/admin/cursos" className="admin-alunos-menu-item">
            <span>CU</span>
            Cursos
          </Link>

          <Link to="/admin/alunos" className="admin-alunos-menu-item active">
            <span>A</span>
            Alunos
          </Link>

          <Link to="/admin/professores" className="admin-alunos-menu-item">
            <span>PR</span>
            Professores
          </Link>

          <Link to="/admin/estatisticas" className="admin-alunos-menu-item">
            <span>ES</span>
            Estatisticas
          </Link>

          <Link to="/admin/chat" className="admin-alunos-menu-item">
            <span>CH</span>
            Chat
          </Link>

          <Link to="/admin/forum" className="admin-alunos-menu-item">
            <span>FO</span>
            Forum
          </Link>
        </nav>

        <button type="button" className="admin-alunos-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-alunos-main">
        <header className="admin-alunos-header">
          <div>
            <h1>Alunos</h1>
            <p>Usuarios cadastrados como alunos.</p>
          </div>

          <div className="admin-alunos-header-actions">
            <button type="button" className="admin-alunos-bell">
              AD
              <span />
            </button>

            <div className="admin-alunos-avatar">AD</div>
          </div>
        </header>

        <section className="admin-alunos-content">
          <div className="admin-alunos-card">
            <div className="admin-alunos-card-header">
              <h2>
                <span>AL</span>
                Gerenciar alunos
              </h2>
            </div>

            <input
              type="text"
              className="admin-alunos-search"
              placeholder="Pesquisar alunos..."
              value={busca}
              onChange={(event) => setBusca(event.target.value)}
            />

            <div className="admin-alunos-table-wrapper">
              <table className="admin-alunos-table">
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>E-mail</th>
                    <th>Perfil</th>
                    <th>Status</th>
                    <th>Acoes</th>
                  </tr>
                </thead>

                <tbody>
                  {alunosFiltrados.map((aluno) => (
                    <tr key={aluno.id || aluno.email}>
                      <td>{aluno.nome || 'Aluno sem nome'}</td>
                      <td>{aluno.email || 'E-mail nao informado'}</td>
                      <td>{aluno.perfil || 'ALUNO'}</td>
                      <td>
                        <span className="admin-alunos-status ativo">Cadastrado</span>
                      </td>
                      <td>
                        <button type="button" className="admin-alunos-edit" disabled>
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}

                  {alunosFiltrados.length === 0 && (
                    <tr>
                      <td colSpan={5} className="admin-alunos-empty">
                        Nenhum aluno encontrado.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          <div className="admin-alunos-illustration" aria-hidden="true">
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
