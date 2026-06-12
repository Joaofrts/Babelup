import { redirect, useLoaderData } from 'react-router-dom';
import axios from 'axios';
import {
  listarAgendamentos,
  limparSessao,
  pegarTokenPayload,
  type SessaoDTO,
} from '../../services/babelup';
import './style.css'; // Aqui ficará o CSS exclusivo dos cards de estatísticas e aulas

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

function formatarHorario(dataHora?: string) {
  if (!dataHora) return '--:--';

  return new Intl.DateTimeFormat('pt-BR', {
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(dataHora));
}

function formatarSessao(sessao: SessaoDTO): AulaHojeDTO {
  return {
    id: sessao.id,
    horario: formatarHorario(sessao.data_hora),
    titulo: sessao.tipo_sessao || 'Sessão de conversa',
    subtitulo: `${sessao.modalidade || 'Modalidade não informada'} - ${sessao.qtd_alunos ?? 0}/${sessao.max_alunos ?? 0} alunos`,
  };
}

export async function dashboardProfessorLoader({ request }: { request: Request }) {
  const token = localStorage.getItem('token');

  if (!token) {
    return redirect('/login-professor');
  }

  try {
    const payload = pegarTokenPayload();
    const agendamentos = await listarAgendamentos(request.signal);

    const hoje = new Date().toISOString().slice(0, 10);
    const aulasHoje = agendamentos
      .filter((sessao) => sessao.data_hora?.slice(0, 10) === hoje)
      .map(formatarSessao);
    
    const alunoIds = new Set(
      agendamentos.flatMap((sessao) => sessao.aluno_ids || [])
    );
    const modulos = new Set(
      agendamentos.map((sessao) => sessao.modulo_id).filter(Boolean)
    );

    return {
      nomeProfessor: payload?.nome || 'Professor',
      totalTurmas: modulos.size,
      totalAlunos: alunoIds.size,
      aulasHoje,
    } as DashboardProfessorDTO;
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response?.status === 401 || error.response?.status === 403) {
        limparSessao();
        return redirect('/login-professor');
      }
      throw new Response(
        error.response?.data?.message || 'Erro ao carregar dashboard do professor.',
        { status: error.response?.status || 500 }
      );
    }
    throw new Response('Erro interno ao carregar dashboard do professor.', { status: 500 });
  }
}

export default function DashboardProfessor() {
  const dados = useLoaderData() as DashboardProfessorDTO;

  return (
    <>
      <div className="professor-page-titles" style={{ marginBottom: '24px' }}>
        <h1 style={{ margin: 0, fontSize: '26px', color: '#111827', fontWeight: 700 }}>Olá, Prof. {dados.nomeProfessor}!</h1>
        <p style={{ margin: '4px 0 0', fontSize: '13px', color: '#5f6470' }}>Bem-vindo ao seu painel de controle diário.</p>
      </div>

      <div className="professor-welcome-card" style={{ background: '#214f8f', color: '#fff', padding: '24px', borderRadius: '10px', marginBottom: '24px' }}>
        <h2 style={{ margin: '0 0 10px', fontSize: '20px' }}>Bom dia!</h2>
        <p style={{ margin: 0, fontSize: '14px', opacity: 0.9 }}>
          Você tem {dados.aulasHoje.length} aula{dados.aulasHoje.length === 1 ? '' : 's'} agendada{dados.aulasHoje.length === 1 ? '' : 's'} para hoje. Boa sorte!
        </p>
      </div>

      <div className="professor-stats-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginBottom: '24px' }}>
        <article className="professor-stat-card" style={{ background: '#fff7ee', padding: '20px', borderRadius: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', boxShadow: '0 4px 9px rgba(0,0,0,0.1)' }}>
          <div>
            <p style={{ margin: '0 0 5px', fontSize: '12px', color: '#6b7280', fontWeight: 'bold' }}>Total de Turmas</p>
            <strong style={{ fontSize: '24px', color: '#111827' }}>{dados.totalTurmas}</strong>
          </div>
          <div className="professor-stat-icon" style={{ fontSize: '30px', color: '#214f8f' }}>♙</div>
        </article>

        <article className="professor-stat-card" style={{ background: '#fff7ee', padding: '20px', borderRadius: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center', boxShadow: '0 4px 9px rgba(0,0,0,0.1)' }}>
          <div>
            <p style={{ margin: '0 0 5px', fontSize: '12px', color: '#6b7280', fontWeight: 'bold' }}>Total de Alunos</p>
            <strong style={{ fontSize: '24px', color: '#111827' }}>{dados.totalAlunos}</strong>
          </div>
          <div className="professor-stat-icon" style={{ fontSize: '30px', color: '#10b981' }}>🎓</div>
        </article>
      </div>

      <section className="professor-lessons-card" style={{ background: '#fff7ee', padding: '24px', borderRadius: '10px', boxShadow: '0 4px 9px rgba(0,0,0,0.1)' }}>
        <h2 style={{ margin: '0 0 20px', display: 'flex', alignItems: 'center', gap: '8px', fontSize: '18px', color: '#111827' }}>
          <span style={{ color: '#214f8f' }}>◷</span> Aulas de Hoje
        </h2>

        {dados.aulasHoje.length === 0 ? (
          <p style={{ textAlign: 'center', color: '#6b7280', padding: '20px 0' }}>
            Nenhuma aula agendada para hoje. Aproveite o descanso!
          </p>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {dados.aulasHoje.map((aula) => (
              <article key={aula.id} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '16px', background: '#eaf4ff', borderRadius: '8px' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
                  <div style={{ textAlign: 'center', minWidth: '60px', paddingRight: '20px', borderRight: '2px solid #cbd5e1' }}>
                    <span style={{ display: 'block', fontSize: '11px', color: '#64748b', fontWeight: 'bold' }}>Horário</span>
                    <strong style={{ fontSize: '16px', color: '#214f8f' }}>{aula.horario}</strong>
                  </div>
                  <div>
                    <h3 style={{ margin: '0 0 4px', fontSize: '15px', color: '#111827' }}>{aula.titulo}</h3>
                    {aula.subtitulo && <p style={{ margin: 0, fontSize: '12px', color: '#64748b' }}>{aula.subtitulo}</p>}
                  </div>
                </div>
                
                <button type="button" style={{ padding: '8px 16px', background: '#214f8f', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}>
                  Iniciar
                </button>
              </article>
            ))}
          </div>
        )}
      </section>
    </>
  );
}