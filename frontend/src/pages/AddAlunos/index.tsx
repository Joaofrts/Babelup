import { Link, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface Aluno {
  id: number;
  nome: string;
  email: string;
  curso: string;
  status: 'Ativo' | 'Inativo';
}

const alunos: Aluno[] = [
  {
    id: 1,
    nome: 'João Leonardo',
    email: 'jojoel@gmail.com',
    curso: 'Inglês - Nível Intermediário',
    status: 'Ativo',
  },
  {
    id: 2,
    nome: 'Rodrigo Santos',
    email: 'rodrigo@gmail.com',
    curso: 'Português - Nível Básico',
    status: 'Ativo',
  },
  {
    id: 3,
    nome: 'Lucas Lima',
    email: 'lucas@gmail.com',
    curso: 'Espanhol - Nível Inicial',
    status: 'Inativo',
  },
];

export default function AdminAlunos() {
  const navigate = useNavigate();
  const [busca, setBusca] = useState('');

  const alunosFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();

    if (!textoBusca) {
      return alunos;
    }

    return alunos.filter((aluno) => {
      return (
        aluno.nome.toLowerCase().includes(textoBusca) ||
        aluno.email.toLowerCase().includes(textoBusca) ||
        aluno.curso.toLowerCase().includes(textoBusca) ||
        aluno.status.toLowerCase().includes(textoBusca)
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
    <main className="admin-alunos-page">
      <aside className="admin-alunos-sidebar">
        <div className="admin-alunos-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="admin-alunos-logo" />
        </div>

        <nav className="admin-alunos-menu">
          <Link to="/dashboard-admin" className="admin-alunos-menu-item">
            <span>♧</span>
            Avisos
          </Link>

          <Link to="/admin/cursos" className="admin-alunos-menu-item">
            <span>▱</span>
            Cursos
          </Link>

          <Link to="/admin/alunos" className="admin-alunos-menu-item active">
            <span>🎓</span>
            Alunos
          </Link>

          <Link to="/admin/professores" className="admin-alunos-menu-item">
            <span>▦</span>
            Professores
          </Link>

          <Link to="/admin/estatisticas" className="admin-alunos-menu-item">
            <span>▤</span>
            Estatísticas
          </Link>

          <Link to="/admin/chat" className="admin-alunos-menu-item">
            <span>♡</span>
            Chat
          </Link>

          <Link to="/admin/forum" className="admin-alunos-menu-item">
            <span>♢</span>
            Fórum
          </Link>
        </nav>

        <button type="button" className="admin-alunos-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-alunos-main">
        <header className="admin-alunos-header">
          <div>
            <h1>Olá, Luciana Silva!</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="admin-alunos-header-actions">
            <button type="button" className="admin-alunos-bell">
              ♧
              <span />
            </button>

            <div className="admin-alunos-avatar">LS</div>
          </div>
        </header>

        <section className="admin-alunos-content">
          <div className="admin-alunos-card">
            <div className="admin-alunos-card-header">
              <h2>
                <span>⚭</span>
                Gerenciar alunos
              </h2>

              <div className="admin-alunos-actions">
                <button type="button" className="admin-alunos-filter">
                  ♢
                </button>

                <button type="button" className="admin-alunos-add">
                  Adicionar aluno
                </button>
              </div>
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
                    <th>Nome:</th>
                    <th>E-mail</th>
                    <th>Curso</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {alunosFiltrados.map((aluno) => (
                    <tr key={aluno.id}>
                      <td>{aluno.nome}</td>
                      <td>{aluno.email}</td>
                      <td>{aluno.curso}</td>
                      <td>
                        <span
                          className={
                            aluno.status === 'Ativo'
                              ? 'admin-alunos-status ativo'
                              : 'admin-alunos-status inativo'
                          }
                        >
                          {aluno.status}
                        </span>
                      </td>
                      <td>
                        <button type="button" className="admin-alunos-edit">
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
            <div className="bubble bubble-two">文</div>
            <div className="card-line card-line-one" />
            <div className="card-line card-line-two" />
          </div>
        </section>
      </section>
    </main>
  );
}