import { Link, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface Professor {
  id: number;
  nome: string;
  email: string;
  especializacao: string;
  alunos: number;
}

const professores: Professor[] = [
  {
    id: 1,
    nome: 'Maria Silva',
    email: 'maria@babelup.com',
    especializacao: 'Inglês - Todos os níveis',
    alunos: 24,
  },
  {
    id: 2,
    nome: 'Ludmila Santos',
    email: 'ludmila@babelup.com',
    especializacao: 'Espanhol - Conversação',
    alunos: 12,
  },
];

export default function AdminProfessores() {
  const navigate = useNavigate();

  const [busca, setBusca] = useState('');

  const professoresFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();

    if (!textoBusca) {
      return professores;
    }

    return professores.filter((professor) => {
      return (
        professor.nome.toLowerCase().includes(textoBusca) ||
        professor.email.toLowerCase().includes(textoBusca) ||
        professor.especializacao.toLowerCase().includes(textoBusca)
      );
    });
  }, [busca]);

  function sair() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('perfil');
    localStorage.removeItem('usuarioLogado');

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

          <Link to="/admin/professores" className="admin-professores-menu-item active">
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

        <button type="button" className="admin-professores-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-professores-main">
        <header className="admin-professores-header">
          <div>
            <h1>Olá, Ludmila Santos!</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="admin-professores-header-actions">
            <button type="button" className="admin-professores-bell">
              ♧
              <span />
            </button>

            <div className="admin-professores-avatar">LS</div>
          </div>
        </header>

        <section className="admin-professores-content">
          <div className="admin-professores-card">
            <div className="admin-professores-card-header">
              <h2>
                <span>⚭</span>
                Gerenciar professores
              </h2>

              <div className="admin-professores-actions">
                <button type="button" className="admin-professores-filter">
                  ♢
                </button>

                <button type="button" className="admin-professores-add">
                  Adicionar professor
                </button>
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
                    <th>Especialização</th>
                    <th>Alunos</th>
                    <th>Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {professoresFiltrados.map((professor) => (
                    <tr key={professor.id}>
                      <td>{professor.nome}</td>
                      <td>{professor.email}</td>
                      <td>{professor.especializacao}</td>
                      <td>{professor.alunos} Alunos</td>
                      <td>
                        <button type="button" className="admin-professores-edit">
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}

                  {professoresFiltrados.length === 0 && (
                    <tr>
                      <td colSpan={5} className="admin-professores-empty">
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
            <div className="bubble bubble-two">文</div>
            <div className="card-line card-line-one" />
            <div className="card-line card-line-two" />
          </div>
        </section>
      </section>
    </main>
  );
}