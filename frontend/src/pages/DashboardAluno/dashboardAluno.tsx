import { useLoaderData, useNavigate, redirect } from 'react-router-dom';
import { buscarPerfilAluno, limparSessao, type PerfilAlunoDTO } from '../../services/babelup';
import './style.css';

interface DashboardAlunoData {
  perfil: PerfilAlunoDTO;
  posicaoRanking?: number;
  proximaAula?: string;
}

export async function dashboardAlunoLoader({ request }: { request: Request }) {

  try {
    const perfil = await buscarPerfilAluno(request.signal);
    return { perfil } as DashboardAlunoData;
  } catch (error) {
    console.error('Erro ao carregar dashboard:', error);
    limparSessao();
    return redirect('/login-aluno');
  }
}

export default function DashboardAluno() {
  const dados = useLoaderData() as DashboardAlunoData;
  const navigate = useNavigate();

  const idiomaAtual = dados.perfil?.curso || 'Não informado';
  const nivelAtual = dados.perfil?.nivelAtual || 'Não informado';
  const progressoGeral = dados.perfil?.progressoGeral ?? 0;
  const posicaoRanking = dados?.posicaoRanking;
  const proximaAula = dados?.proximaAula || 'Nenhuma aula agendada';

  return (
    <>
      <section className="dashboard-cards">
        <article className="dashboard-info-card">
          <div className="dashboard-icon blue">📖</div>
          <div className="dashboard-card-row">
            <span>Progresso do Curso</span>
            <strong>{progressoGeral}%</strong>
          </div>
          <div className="dashboard-progress-bar">
            <div className="dashboard-progress-fill" style={{ width: `${progressoGeral}%` }} />
          </div>
        </article>

        <article className="dashboard-info-card">
          <div className="dashboard-icon green">🎖️</div>
          <div className="dashboard-card-row">
            <span>Nível atual</span>
            <strong>{idiomaAtual}</strong>
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
            <button type="button" className="dashboard-action-btn" onClick={() => navigate('/cursos')}>
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
              <button type="button" className="dashboard-action-btn" onClick={() => navigate('/cursos')}>
                📖 Ver cursos
              </button>
              <button type="button" className="dashboard-action-btn">
                💬 Abrir bate-papo
              </button>
              <button type="button" className="dashboard-action-btn">
                🏆 Agendar aula de conversação
              </button>
            </div>
          </div>
        </aside>
      </section>
    </>
  );
}