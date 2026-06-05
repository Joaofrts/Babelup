import { useLoaderData, useNavigate, redirect } from 'react-router-dom';
import { API } from '../../services/api';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface PerfilAluno {
  nome?: string;
  email?: string;
  nivelAtual?: string;
  progressoGeral?: number;
  posicaoRanking?: number;
  proximaAula?: string;
}

export async function dashboardAlunoLoader({ request }: { request: Request }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return redirect('/login');
  }

  try {
    const response = await API.get('/alunos/meu-perfil', {
      signal: request.signal,
    });

    return response.data;
  } catch (error) {
    console.error('Erro ao carregar dashboard:', error);

    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');

    return redirect('/login');
  }
}

export default function DashboardAluno() {
  const dados = useLoaderData() as PerfilAluno;
  const navigate = useNavigate();

  const nomeAluno = dados?.nome || 'Aluno';
  const nivelAtual = dados?.nivelAtual || 'Iniciante';
  const progressoGeral = dados?.progressoGeral ?? 0;
  const posicaoRanking = dados?.posicaoRanking;
  const proximaAula = dados?.proximaAula || 'Nenhuma aula agendada';

  const lidarComLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    navigate('/login');
  };

  return (
    <main className="dashboard-page">
      <header className="dashboard-header">
        <div className="dashboard-header-content">
          <img src={logoAzul} alt="Logo BabelUp" className="dashboard-logo" />

          <div className="dashboard-title-area">
            <h1>Bem-vindo de volta, {nomeAluno}!</h1>
            <p>Continue sua jornada de aprendizado</p>
          </div>

          <button type="button" className="dashboard-logout" onClick={lidarComLogout}>
            Sair
          </button>
        </div>
      </header>

      <section className="dashboard-content">
        <section className="dashboard-cards">
          <article className="dashboard-info-card">
            <div className="dashboard-icon blue">📖</div>

            <div className="dashboard-card-row">
              <span>Course Progress</span>
              <strong>{progressoGeral}%</strong>
            </div>

            <div className="dashboard-progress-bar">
              <div
                className="dashboard-progress-fill"
                style={{ width: `${progressoGeral}%` }}
              />
            </div>
          </article>

          <article className="dashboard-info-card">
            <div className="dashboard-icon green">🎖️</div>

            <div className="dashboard-card-row">
              <span>Nível atual</span>
              <strong>{nivelAtual}</strong>
            </div>
          </article>

          <article className="dashboard-info-card">
            <div className="dashboard-icon yellow">🏆</div>

            <div className="dashboard-card-row">
              <span>Posição de classificação</span>
              <strong>{posicaoRanking ? `#${posicaoRanking}` : '—'}</strong>
            </div>

            <button type="button" className="dashboard-link-button">
              Ver classificação
            </button>
          </article>

          <article className="dashboard-info-card">
            <div className="dashboard-icon purple">📅</div>

            <div className="dashboard-card-row column">
              <span>Próxima aula</span>
              <strong>{proximaAula}</strong>
            </div>

            <button type="button" className="dashboard-link-button">
              Agende mais
            </button>
          </article>
        </section>

        <section className="dashboard-main-grid">
          <div className="dashboard-panel dashboard-lessons">
            <div className="dashboard-panel-header">
              <h2>Lições disponíveis</h2>
              <span>Dados do aluno</span>
            </div>

            <div className="dashboard-empty-box">
              <p>As lições reais do aluno ainda não foram carregadas pelo backend.</p>

              <button type="button" onClick={() => navigate('/cursos')}>
                Ver cursos
              </button>
            </div>
          </div>

          <aside className="dashboard-side">
            <div className="dashboard-panel">
              <h2>Materiais em PDF</h2>

              <div className="dashboard-empty-box small">
                <p>Nenhum material disponível no momento.</p>
              </div>
            </div>

            <div className="dashboard-panel">
              <h2>Ações rápidas</h2>

              <div className="dashboard-actions">
                <button type="button" onClick={() => navigate('/cursos')}>
                  📖 Ver cursos
                </button>

                <button type="button">
                  💬 Abrir bate-papo
                </button>

                <button type="button">
                  🏆 Agendar aula de conversação
                </button>
              </div>
            </div>
          </aside>
        </section>
      </section>
    </main>
  );
}