import { useLoaderData, useNavigate } from 'react-router-dom';
import { API } from '../../services/api';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

// 1. Tipagem: O "espelho" dos dados que o Java vai nos devolver
interface DadosAluno {
  nome: string;
  email: string;
  nivelAtual: string;
  progressoGeral: number;
}

interface DashboardLoaderData {
  perfil: DadosAluno;
  niveis: unknown;
  modulos: unknown;
}

// 2. O Loader: Executa ANTES da tela renderizar para buscar os dados
export async function dashboardAlunoLoader({ request }: { request: Request }) {
  // Mantém a lógica antiga: busca perfil, níveis e módulos
  const [progressoResponse, niveisResponse, modulosResponse] = await Promise.all([
    API.get('/alunos/meu-perfil', { signal: request.signal }),
    API.get('/niveis/listar', { signal: request.signal }),
    API.get('/modulos/nivel/1', { signal: request.signal }),
  ]);

  return {
    perfil: progressoResponse.data,
    niveis: niveisResponse,
    modulos: modulosResponse,
  };
}

// 3. O Componente Visual
export default function DashboardAluno() {
  const dadosLoader = useLoaderData() as DashboardLoaderData | undefined;
  const navigate = useNavigate();

  // Mantém a tela funcionando mesmo quando o loader estiver comentado no App.tsx
  const dados: DadosAluno = dadosLoader?.perfil || {
    nome: 'João',
    email: 'aluno@email.com',
    nivelAtual: 'B1',
    progressoGeral: 65,
  };

  const lidarComLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    navigate('/');
  };

  return (
    <main className="dashboard-page">
      <header className="dashboard-header">
        <div className="dashboard-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="dashboard-logo" />
        </div>

        <div className="dashboard-welcome">
          <h1>Bem-vindo de volta, {dados.nome}!</h1>
          <p>Continue sua jornada de aprendizado</p>
        </div>

        <div className="dashboard-actions">
          <button className="notification-button" type="button">
            🔔
          </button>

          <button className="logout-button" onClick={lidarComLogout}>
            Sair
          </button>
        </div>
      </header>

      <section className="dashboard-content">
        <section className="dashboard-stats">
          <div className="stat-card">
            <div className="stat-icon blue">📖</div>
            <div>
              <strong>{dados.progressoGeral}%</strong>
              <span>Course Progress</span>

              <div className="progress-track">
                <div
                  className="progress-fill"
                  style={{ width: `${dados.progressoGeral}%` }}
                />
              </div>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon green">🎖️</div>
            <div>
              <strong>{dados.nivelAtual}</strong>
              <span>Nível atual</span>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon yellow">🏆</div>
            <div>
              <strong>#12</strong>
              <span>Posição de classificação</span>
              <a href="#">Ver classificação</a>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon purple">📅</div>
            <div>
              <strong>Próxima aula</strong>
              <span>Amanhã, às 10h da manhã.</span>
              <a href="#">Agende mais</a>
            </div>
          </div>
        </section>

        <section className="dashboard-grid">
          <div className="lessons-card">
            <div className="section-title">
              <h2>Lições disponíveis</h2>
              <span>26/40 concluído</span>
            </div>

            <div className="lesson-item completed">
              <div className="lesson-icon">↗</div>
              <div className="lesson-info">
                <strong>Presente Perfeito</strong>
                <span>15 min</span>
              </div>
              <p>✓ Concluído</p>
            </div>

            <div className="lesson-item completed">
              <div className="lesson-icon">↗</div>
              <div className="lesson-info">
                <strong>Prática de Conversação: Viagens</strong>
                <span>20 min</span>
              </div>
              <p>✓ Concluído</p>
            </div>

            <div className="lesson-item">
              <div className="lesson-icon play">▷</div>
              <div className="lesson-info">
                <strong>Vocabulário: Inglês Comercial</strong>
                <span>12 min</span>
              </div>
              <button>Assistir aula</button>
            </div>

            <div className="lesson-item">
              <div className="lesson-icon play">▷</div>
              <div className="lesson-info">
                <strong>Exercício de Escuta: Reportagem Jornalística</strong>
                <span>18 min</span>
              </div>
              <button>Assistir aula</button>
            </div>
          </div>

          <aside className="dashboard-side">
            <div className="pdf-card">
              <h2>Materiais em PDF</h2>

              <div className="pdf-item">
                <span>📄</span>
                <div>
                  <strong>Grammar Guide - Present Perfect</strong>
                  <p>2.3 MB</p>
                </div>
              </div>

              <div className="pdf-item">
                <span>📄</span>
                <div>
                  <strong>Vocabulary List - Business Terms</strong>
                  <p>1.8 MB</p>
                </div>
              </div>

              <div className="pdf-item">
                <span>📄</span>
                <div>
                  <strong>Practice Exercises - Week 7</strong>
                  <p>3.1 MB</p>
                </div>
              </div>
            </div>

            <div className="quick-card">
              <h2>Ações rápidas</h2>

              <button className="quick-action blue-action">
                📖 Pratique exercícios
              </button>

              <button className="quick-action green-action">
                💬 Abrir bate-papo
              </button>

              <button className="quick-action purple-action">
                🎧 Visite o Fórum
              </button>

              <button className="quick-action yellow-action">
                🏆 Agendamento de aulas de conversação
              </button>
            </div>
          </aside>
        </section>
      </section>
    </main>
  );
}