import { Link, redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  atualizarAgendamentoData,
  criarAgendamento,
  deletarAgendamento,
  finalizarAgendamento,
  inscreverAlunoAgendamento,
  listarAgendamentos,
  listarModulos,
  listarUsuarios,
  limparSessao,
  removerAlunoAgendamento,
  type ModuloDTO,
  type SessaoDTO,
  type UsuarioDTO,
} from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import '../AddCurso/style.css';

interface AdminAgendamentosLoaderData {
  agendamentos: SessaoDTO[];
  professores: UsuarioDTO[];
  alunos: UsuarioDTO[];
  modulos: ModuloDTO[];
}

export async function adminAgendamentosLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const [agendamentos, usuarios, modulos] = await Promise.all([
      listarAgendamentos(request.signal),
      listarUsuarios(request.signal),
      listarModulos(request.signal),
    ]);

    return {
      agendamentos,
      professores: usuarios.filter((usuario) => usuario.perfil === 'PROFESSOR'),
      alunos: usuarios.filter((usuario) => usuario.perfil === 'ALUNO'),
      modulos,
    } as AdminAgendamentosLoaderData;
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }

    throw new Response('Erro ao carregar agendamentos.', { status: 500 });
  }
}

function formatarData(data?: string) {
  if (!data) return 'Nao informado';

  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(data));
}

function nomePorId(lista: Array<UsuarioDTO | ModuloDTO>, id?: string | null) {
  if (!id) return 'Nao vinculado';
  const item = lista.find((registro) => registro.id === id);

  if (!item) return id;
  if ('perfil' in item) return item.nome || id;
  return (item as ModuloDTO).titulo || id;
}

export default function AdminAgendamentos() {
  const navigate = useNavigate();
  const dados = useLoaderData() as AdminAgendamentosLoaderData;

  const [agendamentos, setAgendamentos] = useState<SessaoDTO[]>(dados.agendamentos);
  const [professorId, setProfessorId] = useState(dados.professores[0]?.id || '');
  const [moduloId, setModuloId] = useState(dados.modulos[0]?.id || '');
  const [tipoSessao, setTipoSessao] = useState<'PRATICA_MODULO' | 'NIVELAMENTO_INICIAL' | 'AVALIACAO_FINAL_NIVEL'>('PRATICA_MODULO');
  const [modalidade, setModalidade] = useState<'INDIVIDUAL' | 'GRUPO'>('INDIVIDUAL');
  const [dataHora, setDataHora] = useState('');
  const [maxAlunos, setMaxAlunos] = useState('1');
  const [alunoSelecionado, setAlunoSelecionado] = useState(dados.alunos[0]?.id || '');
  const [reagendamento, setReagendamento] = useState<Record<string, string>>({});
  const [gravacoes, setGravacoes] = useState<Record<string, string>>({});
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');
  const [salvando, setSalvando] = useState(false);

  const agendamentosOrdenados = useMemo(
    () => [...agendamentos].sort((a, b) => String(a.data_hora || '').localeCompare(String(b.data_hora || ''))),
    [agendamentos]
  );

  async function recarregarAgendamentos() {
    setAgendamentos(await listarAgendamentos());
  }

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  async function agendarSessao(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMensagem('');
    setErro('');

    if (!professorId || !dataHora) {
      setErro('Informe professor e data/hora.');
      return;
    }

    if (tipoSessao !== 'NIVELAMENTO_INICIAL' && !moduloId) {
      setErro('Informe o modulo para este tipo de sessao.');
      return;
    }

    try {
      setSalvando(true);
      await criarAgendamento({
        professor_id: professorId,
        modulo_id: tipoSessao === 'NIVELAMENTO_INICIAL' ? null : moduloId,
        tipo_sessao: tipoSessao,
        modalidade,
        data_hora: dataHora,
        max_alunos: Number(maxAlunos) || 1,
      });

      setDataHora('');
      await recarregarAgendamentos();
      setMensagem('Sessao agendada com sucesso.');
    } catch {
      setErro('Nao foi possivel agendar a sessao.');
    } finally {
      setSalvando(false);
    }
  }

  async function reagendar(sessaoId: string) {
    const novaDataHora = reagendamento[sessaoId];
    if (!novaDataHora) return;

    try {
      setSalvando(true);
      await atualizarAgendamentoData(sessaoId, novaDataHora);
      await recarregarAgendamentos();
      setMensagem('Sessao reagendada com sucesso.');
    } catch {
      setErro('Nao foi possivel reagendar a sessao.');
    } finally {
      setSalvando(false);
    }
  }

  async function finalizar(sessaoId: string) {
    try {
      setSalvando(true);
      await finalizarAgendamento(sessaoId, gravacoes[sessaoId] || '');
      await recarregarAgendamentos();
      setMensagem('Sessao finalizada com sucesso.');
    } catch {
      setErro('Nao foi possivel finalizar a sessao.');
    } finally {
      setSalvando(false);
    }
  }

  async function excluir(sessaoId: string) {
    if (!window.confirm('Excluir este agendamento?')) return;

    try {
      setSalvando(true);
      await deletarAgendamento(sessaoId);
      await recarregarAgendamentos();
      setMensagem('Agendamento excluido.');
    } catch {
      setErro('Nao foi possivel excluir o agendamento.');
    } finally {
      setSalvando(false);
    }
  }

  async function inscreverAluno(sessaoId: string) {
    if (!alunoSelecionado) return;

    try {
      setSalvando(true);
      await inscreverAlunoAgendamento(sessaoId, alunoSelecionado);
      await recarregarAgendamentos();
      setMensagem('Aluno inscrito na sessao.');
    } catch {
      setErro('Nao foi possivel inscrever o aluno.');
    } finally {
      setSalvando(false);
    }
  }

  async function removerAluno(sessaoId: string, alunoId: string) {
    try {
      setSalvando(true);
      await removerAlunoAgendamento(sessaoId, alunoId);
      await recarregarAgendamentos();
      setMensagem('Aluno removido da sessao.');
    } catch {
      setErro('Nao foi possivel remover o aluno.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <main className="admin-cursos-page">
      <aside className="admin-cursos-sidebar">
        <div className="admin-cursos-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="admin-cursos-logo" />
        </div>

        <nav className="admin-cursos-menu">
          <Link to="/dashboard-admin" className="admin-cursos-menu-item">
            <span>AD</span>
            Avisos
          </Link>
          <Link to="/admin/cursos" className="admin-cursos-menu-item">
            <span>CU</span>
            Cursos
          </Link>
          <Link to="/admin/agendamentos" className="admin-cursos-menu-item active">
            <span>AG</span>
            Agendamentos
          </Link>
          <Link to="/admin/alunos" className="admin-cursos-menu-item">
            <span>AL</span>
            Alunos
          </Link>
          <Link to="/admin/professores" className="admin-cursos-menu-item">
            <span>PR</span>
            Professores
          </Link>
        </nav>

        <button type="button" className="admin-cursos-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-cursos-main">
        <header className="admin-cursos-header">
          <div>
            <h1>Agendamentos</h1>
            <p>Sessoes de conversa, pratica e nivelamento.</p>
          </div>

          <div className="admin-cursos-header-actions">
            <div className="admin-cursos-avatar">AD</div>
          </div>
        </header>

        <section className="admin-cursos-content admin-curso-detalhe-content">
          {(mensagem || erro) && (
            <div className={erro ? 'admin-curso-alert error' : 'admin-curso-alert'}>
              {erro || mensagem}
            </div>
          )}

          <form className="admin-cursos-card" onSubmit={agendarSessao}>
            <h2><span>AG</span> Agendar sessao</h2>

            <div className="admin-cursos-form-grid">
              <div className="admin-cursos-field">
                <label>Professor</label>
                <select value={professorId} onChange={(event) => setProfessorId(event.target.value)}>
                  <option value="">Selecione</option>
                  {dados.professores.map((professor) => (
                    <option key={professor.id} value={professor.id}>{professor.nome}</option>
                  ))}
                </select>
              </div>

              <div className="admin-cursos-field">
                <label>Tipo</label>
                <select value={tipoSessao} onChange={(event) => setTipoSessao(event.target.value as typeof tipoSessao)}>
                  <option value="PRATICA_MODULO">Pratica de modulo</option>
                  <option value="NIVELAMENTO_INICIAL">Nivelamento inicial</option>
                  <option value="AVALIACAO_FINAL_NIVEL">Avaliacao final de nivel</option>
                </select>
              </div>

              <div className="admin-cursos-field">
                <label>Modulo</label>
                <select
                  value={moduloId}
                  onChange={(event) => setModuloId(event.target.value)}
                  disabled={tipoSessao === 'NIVELAMENTO_INICIAL'}
                >
                  <option value="">Sem modulo</option>
                  {dados.modulos.map((modulo) => (
                    <option key={modulo.id} value={modulo.id}>{modulo.titulo}</option>
                  ))}
                </select>
              </div>

              <div className="admin-cursos-field">
                <label>Modalidade</label>
                <select value={modalidade} onChange={(event) => setModalidade(event.target.value as typeof modalidade)}>
                  <option value="INDIVIDUAL">Individual</option>
                  <option value="GRUPO">Grupo</option>
                </select>
              </div>

              <div className="admin-cursos-field">
                <label>Data e hora</label>
                <input type="datetime-local" value={dataHora} onChange={(event) => setDataHora(event.target.value)} />
              </div>

              <div className="admin-cursos-field">
                <label>Maximo de alunos</label>
                <input type="number" min="1" value={maxAlunos} onChange={(event) => setMaxAlunos(event.target.value)} />
              </div>
            </div>

            <button type="submit" className="admin-cursos-submit" disabled={salvando}>
              {salvando ? 'Salvando...' : 'Agendar sessao'}
            </button>
          </form>

          <section className="admin-cursos-card admin-cursos-list-card">
            <div className="admin-cursos-list-header">
              <h2><span>SE</span> Sessoes agendadas</h2>
              <select value={alunoSelecionado} onChange={(event) => setAlunoSelecionado(event.target.value)}>
                <option value="">Aluno para inscricao</option>
                {dados.alunos.map((aluno) => (
                  <option key={aluno.id} value={aluno.id}>{aluno.nome}</option>
                ))}
              </select>
            </div>

            {agendamentosOrdenados.length === 0 ? (
              <p className="admin-cursos-empty">Nenhuma sessao agendada.</p>
            ) : (
              <div className="admin-agendamento-list">
                {agendamentosOrdenados.map((sessao) => (
                  <article className="admin-agendamento-item" key={sessao.id}>
                    <div className="admin-agendamento-main">
                      <strong>{formatarData(sessao.data_hora)}</strong>
                      <span>{sessao.tipo_sessao} - {sessao.modalidade}</span>
                      <span>Professor: {nomePorId(dados.professores, sessao.professor_id)}</span>
                      <span>Modulo: {nomePorId(dados.modulos, sessao.modulo_id)}</span>
                      <span>Status: {sessao.status || 'Nao informado'} | Vagas: {sessao.qtd_alunos ?? 0}/{sessao.max_alunos ?? 0}</span>
                    </div>

                    <div className="admin-agendamento-actions">
                      <input
                        type="datetime-local"
                        value={reagendamento[sessao.id] || ''}
                        onChange={(event) => setReagendamento((atual) => ({ ...atual, [sessao.id]: event.target.value }))}
                      />
                      <button type="button" className="admin-cursos-edit" onClick={() => reagendar(sessao.id)} disabled={salvando}>
                        Reagendar
                      </button>
                      <button type="button" className="admin-cursos-edit" onClick={() => inscreverAluno(sessao.id)} disabled={salvando || !alunoSelecionado}>
                        Inscrever aluno
                      </button>
                      <input
                        type="text"
                        placeholder="URL da gravacao"
                        value={gravacoes[sessao.id] || ''}
                        onChange={(event) => setGravacoes((atual) => ({ ...atual, [sessao.id]: event.target.value }))}
                      />
                      <button type="button" className="admin-cursos-edit" onClick={() => finalizar(sessao.id)} disabled={salvando}>
                        Finalizar
                      </button>
                      <button type="button" className="admin-curso-danger-button" onClick={() => excluir(sessao.id)} disabled={salvando}>
                        Excluir
                      </button>
                    </div>

                    {(sessao.aluno_ids || []).length > 0 && (
                      <div className="admin-agendamento-alunos">
                        {(sessao.aluno_ids || []).map((alunoId) => (
                          <button key={alunoId} type="button" onClick={() => removerAluno(sessao.id, alunoId)} disabled={salvando}>
                            Remover {nomePorId(dados.alunos, alunoId)}
                          </button>
                        ))}
                      </div>
                    )}
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
