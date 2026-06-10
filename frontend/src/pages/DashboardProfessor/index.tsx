import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API } from '../../services/api';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface AulaHojeDTO {
  id: string;
  horario: string;
  titulo: string;
  subtitulo?: string;
}

interface DashboardProfessorDTO {
  nomeProfessor: string;
  totalTurmas: number;
  totalAlunos: number;
  aulasHoje: AulaHojeDTO[];
}

export async function dashboardProfessorLoader({ request }: { request: Request }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return redirect('/login-professor');
  }

  try {
    const response = await API.get('/professor/dashboard', {
      signal: request.signal,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    return response.data as DashboardProfessorDTO;
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401 || error.response?.status === 403) {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');

        return redirect('/login-professor');
      }

      throw new Response(
        error.response?.data?.message || 'Erro ao carregar dashboard do professor.',
        { status: error.response?.status || 500 }
      );
    }

    throw new Response('Erro interno ao carregar dashboard do professor.', {
      status: 500,
    });
  }
}

export default function DashboardProfessor() {
  const navigate = useNavigate();
  const dados = useLoaderData() as DashboardProfessorDTO;

  function sair() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    navigate('/login-professor');
  }

  const iniciais = dados.nomeProfessor
    ? dados.nomeProfessor
        .split(' ')
        .filter(Boolean)
        .slice(0, 2)
        .map((nome) => nome[0])
        .join('')
        .toUpperCase()
    : 'PR';

  return (
    <main className="professor-dashboard-page">
      <aside className="professor-sidebar">
        <div className="professor-sidebar-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="professor-sidebar-logo" />
        </div>

        <nav className="professor-sidebar-menu">
          <Link to="/dashboard-professor" className="professor-menu-item active">
            <span>⌘</span>
            Dashboard
          </Link>

          <Link to="/professor/turmas" className="professor-menu-item">
            <span>♙</span>
            Turmas
          </Link>

          <Link to="/professor/alunos" className="professor-menu-item">
            <span>🎓</span>
            Alunos
          </Link>

          <Link to="/professor/atividades" className="professor-menu-item">
            <span>▣</span>
            Atividades
          </Link>

          <Link to="/professor/conteudos" className="professor-menu-item">
            <span>▭</span>
            Gerenciar conteúdo
          </Link>

          <Link to="/professor/notas" className="professor-menu-item">
            <span>▤</span>
            Notas
          </Link>

          <Link to="/professor/chat" className="professor-menu-item">
            <span>♡</span>
            Chat
          </Link>

          <Link to="/professor/forum" className="professor-menu-item">
            <span>♧</span>
            Fórum
          </Link>
        </nav>

        <button type="button" className="professor-logout-button" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="professor-main">
        <header className="professor-topbar">
          <div>
            <h1>Olá, Prof. {dados.nomeProfessor}</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="professor-topbar-actions">
            <button type="button" className="professor-notification-button">
              ♧
              <span />
            </button>

            <div className="professor-avatar">{iniciais}</div>
          </div>
        </header>

        <section className="professor-content">
          <div className="professor-welcome-card">
            <h2>Bom dia, Professora!</h2>
            <p>
              Você tem {dados.aulasHoje.length} aula
              {dados.aulasHoje.length === 1 ? '' : 's'} agendada
              {dados.aulasHoje.length === 1 ? '' : 's'} para hoje. Boa sorte!
            </p>
          </div>

          <div className="professor-stats-grid">
            <article className="professor-stat-card">
              <div>
                <p>Total de Turmas</p>
                <strong>{dados.totalTurmas}</strong>
              </div>

              <div className="professor-stat-icon professor-stat-blue">♙</div>
            </article>

            <article className="professor-stat-card">
              <div>
                <p>Total de Alunos</p>
                <strong>{dados.totalAlunos}</strong>
              </div>

              <div className="professor-stat-icon professor-stat-green">🎓</div>
            </article>
          </div>

          <section className="professor-lessons-card">
            <h2>
              <span>◷</span>
              Aulas de Hoje
            </h2>

            {dados.aulasHoje.length === 0 ? (
              <p className="professor-empty-message">
                Nenhuma aula agendada para hoje.
              </p>
            ) : (
              <div className="professor-lessons-list">
                {dados.aulasHoje.map((aula) => (
                  <article className="professor-lesson-item" key={aula.id}>
                    <div className="professor-lesson-hour">
                      <span>Horário</span>
                      <strong>{aula.horario}</strong>
                    </div>

                    <div className="professor-lesson-info">
                      <h3>{aula.titulo}</h3>
                      {aula.subtitulo && <p>{aula.subtitulo}</p>}
                    </div>

                    <button type="button">Iniciar</button>
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