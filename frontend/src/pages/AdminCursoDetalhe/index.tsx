import { Link, redirect, useLoaderData, useNavigate, useParams } from 'react-router-dom';
import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  atualizarModulo,
  atualizarNivel,
  criarExercicio,
  criarMaterialApoio,
  criarModulo,
  criarVideoAula,
  deletarModulo,
  deletarNivel,
  listarExerciciosPorVideoAula,
  listarMateriaisPorModulo,
  listarModulosPorNivel,
  listarVideoAulasPorModulo,
  limparSessao,
  obterNivel,
  type ExercicioDTO,
  type MaterialApoioDTO,
  type ModuloDTO,
  type NivelDTO,
  type VideoAulaDTO,
} from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import '../AddCurso/style.css';

interface ConteudoModuloData {
  videoaulas: VideoAulaDTO[];
  materiais: MaterialApoioDTO[];
}

interface AdminCursoDetalheLoaderData {
  nivel: NivelDTO;
  modulos: ModuloDTO[];
  conteudos: Record<string, ConteudoModuloData>;
}

export async function adminCursoDetalheLoader({ params, request }: { params: { nivelId?: string }; request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  if (!params.nivelId) {
    throw new Response('Curso nao encontrado.', { status: 404 });
  }

  try {
    const nivel = await obterNivel(params.nivelId, request.signal);
    const modulos = await listarModulosPorNivel(params.nivelId, request.signal);
    const conteudosEntries = await Promise.all(
      modulos.map(async (modulo) => {
        const [videoaulas, materiais] = await Promise.all([
          listarVideoAulasPorModulo(modulo.id, request.signal),
          listarMateriaisPorModulo(modulo.id, request.signal),
        ]);

        return [modulo.id, { videoaulas, materiais }] as const;
      })
    );

    return {
      nivel,
      modulos,
      conteudos: Object.fromEntries(conteudosEntries),
    } as AdminCursoDetalheLoaderData;
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }

    throw new Response('Erro ao carregar curso.', { status: 500 });
  }
}

function valorCargaModulo(modulo?: ModuloDTO) {
  return modulo?.carga_horaria_minima ?? modulo?.cargaHorariaMinima ?? 0;
}

function valorNivelId(modulo?: ModuloDTO) {
  return modulo?.nivel_id ?? modulo?.nivelId ?? '';
}

export default function AdminCursoDetalhe() {
  const { nivelId = '' } = useParams();
  const navigate = useNavigate();
  const dados = useLoaderData() as AdminCursoDetalheLoaderData;

  const [nivel, setNivel] = useState<NivelDTO>(dados.nivel);
  const [modulos, setModulos] = useState<ModuloDTO[]>(dados.modulos);
  const [conteudos, setConteudos] = useState<Record<string, ConteudoModuloData>>(dados.conteudos);
  const [moduloSelecionadoId, setModuloSelecionadoId] = useState(dados.modulos[0]?.id || '');
  const [videoSelecionadoId, setVideoSelecionadoId] = useState('');
  const [mensagem, setMensagem] = useState('');
  const [erro, setErro] = useState('');
  const [salvando, setSalvando] = useState(false);

  const [nivelDescricao, setNivelDescricao] = useState(nivel.descricao || '');
  const [nivelCarga, setNivelCarga] = useState(String(nivel.cargaHorariaEstimada || ''));

  const [novoModuloTitulo, setNovoModuloTitulo] = useState('');
  const [novoModuloDescricao, setNovoModuloDescricao] = useState('');
  const [novoModuloOrdem, setNovoModuloOrdem] = useState('');
  const [novoModuloCarga, setNovoModuloCarga] = useState('');

  const moduloSelecionado = useMemo(
    () => modulos.find((modulo) => modulo.id === moduloSelecionadoId),
    [moduloSelecionadoId, modulos]
  );

  const [moduloTitulo, setModuloTitulo] = useState(moduloSelecionado?.titulo || '');
  const [moduloDescricao, setModuloDescricao] = useState(moduloSelecionado?.descricao || '');
  const [moduloOrdem, setModuloOrdem] = useState(String(moduloSelecionado?.ordem || ''));
  const [moduloCarga, setModuloCarga] = useState(String(valorCargaModulo(moduloSelecionado) || ''));

  const [materialTitulo, setMaterialTitulo] = useState('');
  const [materialUrl, setMaterialUrl] = useState('');
  const [videoTitulo, setVideoTitulo] = useState('');
  const [videoUrl, setVideoUrl] = useState('');
  const [videoDuracao, setVideoDuracao] = useState('');
  const [videoTipo, setVideoTipo] = useState<'GRAVADO' | 'AO_VIVO'>('GRAVADO');
  const [exercicioEnunciado, setExercicioEnunciado] = useState('');
  const [exercicioAlternativas, setExercicioAlternativas] = useState('');
  const [exercicioResposta, setExercicioResposta] = useState('');
  const [exercicios, setExercicios] = useState<ExercicioDTO[]>([]);

  const conteudoModulo = moduloSelecionadoId
    ? conteudos[moduloSelecionadoId] || { videoaulas: [], materiais: [] }
    : { videoaulas: [], materiais: [] };

  async function recarregarModulos() {
    const modulosAtualizados = await listarModulosPorNivel(nivelId);
    setModulos(modulosAtualizados);

    const conteudosEntries = await Promise.all(
      modulosAtualizados.map(async (modulo) => {
        const [videoaulas, materiais] = await Promise.all([
          listarVideoAulasPorModulo(modulo.id),
          listarMateriaisPorModulo(modulo.id),
        ]);
        return [modulo.id, { videoaulas, materiais }] as const;
      })
    );

    setConteudos(Object.fromEntries(conteudosEntries));
  }

  function selecionarModulo(modulo: ModuloDTO) {
    setModuloSelecionadoId(modulo.id);
    setModuloTitulo(modulo.titulo || '');
    setModuloDescricao(modulo.descricao || '');
    setModuloOrdem(String(modulo.ordem || ''));
    setModuloCarga(String(valorCargaModulo(modulo) || ''));
    setVideoSelecionadoId('');
    setExercicios([]);
  }

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  async function salvarNivel(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErro('');
    setMensagem('');

    const carga = Number(nivelCarga);
    if (Number.isNaN(carga)) {
      setErro('Informe uma carga horaria valida.');
      return;
    }

    try {
      setSalvando(true);
      const nivelAtualizado = await atualizarNivel(nivelId, {
        descricao: nivelDescricao,
        carga_horaria: carga,
      });
      setNivel(nivelAtualizado);
      setMensagem('Nivel atualizado com sucesso.');
    } catch {
      setErro('Nao foi possivel atualizar o nivel.');
    } finally {
      setSalvando(false);
    }
  }

  async function excluirNivel() {
    if (!window.confirm('Excluir este nivel e seus vinculos?')) return;

    try {
      setSalvando(true);
      await deletarNivel(nivelId);
      navigate('/admin/cursos');
    } catch {
      setErro('Nao foi possivel excluir o nivel.');
    } finally {
      setSalvando(false);
    }
  }

  async function adicionarModulo(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErro('');
    setMensagem('');

    if (!novoModuloTitulo.trim()) {
      setErro('Informe o titulo do modulo.');
      return;
    }

    try {
      setSalvando(true);
      const modulo = await criarModulo({
        nivel_id: nivelId,
        titulo: novoModuloTitulo.trim(),
        descricao: novoModuloDescricao.trim(),
        ordem: Number(novoModuloOrdem) || modulos.length + 1,
        carga_horaria_minima: Number(novoModuloCarga) || 1,
      });
      setNovoModuloTitulo('');
      setNovoModuloDescricao('');
      setNovoModuloOrdem('');
      setNovoModuloCarga('');
      await recarregarModulos();
      selecionarModulo(modulo);
      setMensagem('Modulo criado com sucesso.');
    } catch {
      setErro('Nao foi possivel criar o modulo.');
    } finally {
      setSalvando(false);
    }
  }

  async function salvarModulo(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!moduloSelecionado) return;

    setErro('');
    setMensagem('');

    try {
      setSalvando(true);
      const moduloAtualizado = await atualizarModulo(moduloSelecionado.id, {
        titulo: moduloTitulo.trim(),
        descricao: moduloDescricao.trim(),
        ordem: Number(moduloOrdem) || moduloSelecionado.ordem || 1,
        carga_horaria_minima: Number(moduloCarga) || valorCargaModulo(moduloSelecionado) || 1,
      });
      await recarregarModulos();
      selecionarModulo(moduloAtualizado);
      setMensagem('Modulo atualizado com sucesso.');
    } catch {
      setErro('Nao foi possivel atualizar o modulo.');
    } finally {
      setSalvando(false);
    }
  }

  async function excluirModulo() {
    if (!moduloSelecionado || !window.confirm('Excluir este modulo?')) return;

    try {
      setSalvando(true);
      await deletarModulo(moduloSelecionado.id);
      await recarregarModulos();
      setModuloSelecionadoId('');
      setMensagem('Modulo excluido com sucesso.');
    } catch {
      setErro('Nao foi possivel excluir o modulo.');
    } finally {
      setSalvando(false);
    }
  }

  async function adicionarMaterial(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!moduloSelecionadoId) return;

    try {
      setSalvando(true);
      await criarMaterialApoio({
        modulo_id: moduloSelecionadoId,
        titulo: materialTitulo.trim(),
        url_pdf: materialUrl.trim(),
      });
      setMaterialTitulo('');
      setMaterialUrl('');
      await recarregarModulos();
      setMensagem('PDF adicionado com sucesso.');
    } catch {
      setErro('Nao foi possivel adicionar o PDF.');
    } finally {
      setSalvando(false);
    }
  }

  async function adicionarVideo(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!moduloSelecionadoId) return;

    try {
      setSalvando(true);
      await criarVideoAula({
        modulo_id: moduloSelecionadoId,
        titulo: videoTitulo.trim(),
        url: videoUrl.trim(),
        duracao: Number(videoDuracao) || 1,
        tipo: videoTipo,
        data_transmissao: null,
      });
      setVideoTitulo('');
      setVideoUrl('');
      setVideoDuracao('');
      await recarregarModulos();
      setMensagem('Videoaula adicionada com sucesso.');
    } catch {
      setErro('Nao foi possivel adicionar a videoaula.');
    } finally {
      setSalvando(false);
    }
  }

  async function carregarExercicios(videoAulaId: string) {
    setVideoSelecionadoId(videoAulaId);
    setExercicios(await listarExerciciosPorVideoAula(videoAulaId));
  }

  async function adicionarExercicio(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!videoSelecionadoId) return;

    try {
      setSalvando(true);
      await criarExercicio({
        videoaula_id: videoSelecionadoId,
        enunciado: exercicioEnunciado.trim(),
        alternativas: exercicioAlternativas
          .split('\n')
          .map((alternativa) => alternativa.trim())
          .filter(Boolean),
        resposta_correta: exercicioResposta.trim(),
      });
      setExercicioEnunciado('');
      setExercicioAlternativas('');
      setExercicioResposta('');
      await carregarExercicios(videoSelecionadoId);
      setMensagem('Exercicio adicionado com sucesso.');
    } catch {
      setErro('Nao foi possivel adicionar o exercicio.');
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
          <Link to="/admin/cursos" className="admin-cursos-menu-item active">
            <span>CU</span>
            Cursos
          </Link>
          <Link to="/admin/agendamentos" className="admin-cursos-menu-item">
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
            <h1>{nivel.idioma || 'Curso'} - {nivel.nome || 'Nivel'}</h1>
            <p>Gerencie nivel, modulos e conteudos.</p>
          </div>

          <div className="admin-cursos-header-actions">
            <Link to="/admin/cursos" className="admin-cursos-back-link">Voltar</Link>
            <div className="admin-cursos-avatar">AD</div>
          </div>
        </header>

        <section className="admin-cursos-content admin-curso-detalhe-content">
          {(mensagem || erro) && (
            <div className={erro ? 'admin-curso-alert error' : 'admin-curso-alert'}>
              {erro || mensagem}
            </div>
          )}

          <form className="admin-cursos-card" onSubmit={salvarNivel}>
            <h2><span>NI</span> Rotas do nivel</h2>

            <div className="admin-cursos-form-grid">
              <div className="admin-cursos-field">
                <label>Idioma</label>
                <input value={nivel.idioma || ''} disabled />
              </div>
              <div className="admin-cursos-field">
                <label>Nivel</label>
                <input value={nivel.nome || ''} disabled />
              </div>
              <div className="admin-cursos-field">
                <label>Carga horaria</label>
                <input
                  type="number"
                  min="1"
                  value={nivelCarga}
                  onChange={(event) => setNivelCarga(event.target.value)}
                />
              </div>
            </div>

            <div className="admin-cursos-field">
              <label>Descricao</label>
              <textarea
                value={nivelDescricao}
                onChange={(event) => setNivelDescricao(event.target.value)}
              />
            </div>

            <div className="admin-curso-actions-row">
              <button type="submit" className="admin-cursos-submit" disabled={salvando}>
                Salvar nivel
              </button>
              <button type="button" className="admin-curso-danger-button" onClick={excluirNivel} disabled={salvando}>
                Excluir nivel
              </button>
            </div>
          </form>

          <section className="admin-cursos-card">
            <h2><span>MO</span> Rotas do modulo</h2>

            <form onSubmit={adicionarModulo}>
              <div className="admin-cursos-form-grid">
                <div className="admin-cursos-field">
                  <label>Novo modulo</label>
                  <input value={novoModuloTitulo} onChange={(event) => setNovoModuloTitulo(event.target.value)} />
                </div>
                <div className="admin-cursos-field">
                  <label>Ordem</label>
                  <input type="number" value={novoModuloOrdem} onChange={(event) => setNovoModuloOrdem(event.target.value)} />
                </div>
                <div className="admin-cursos-field">
                  <label>Carga minima</label>
                  <input type="number" value={novoModuloCarga} onChange={(event) => setNovoModuloCarga(event.target.value)} />
                </div>
              </div>
              <div className="admin-cursos-field">
                <label>Descricao</label>
                <textarea value={novoModuloDescricao} onChange={(event) => setNovoModuloDescricao(event.target.value)} />
              </div>
              <button type="submit" className="admin-cursos-submit" disabled={salvando}>
                Criar modulo
              </button>
            </form>

            <div className="admin-curso-module-list">
              {modulos.length === 0 ? (
                <p className="admin-cursos-empty">Nenhum modulo cadastrado.</p>
              ) : (
                modulos.map((modulo) => (
                  <button
                    key={modulo.id}
                    type="button"
                    className={modulo.id === moduloSelecionadoId ? 'admin-curso-module-button active' : 'admin-curso-module-button'}
                    onClick={() => selecionarModulo(modulo)}
                  >
                    {modulo.ordem || '-'} - {modulo.titulo || 'Modulo sem titulo'}
                  </button>
                ))
              )}
            </div>

            {moduloSelecionado && (
              <form onSubmit={salvarModulo} className="admin-curso-subform">
                <div className="admin-cursos-form-grid">
                  <div className="admin-cursos-field">
                    <label>Titulo</label>
                    <input value={moduloTitulo} onChange={(event) => setModuloTitulo(event.target.value)} />
                  </div>
                  <div className="admin-cursos-field">
                    <label>Nivel vinculado</label>
                    <input value={valorNivelId(moduloSelecionado)} disabled />
                  </div>
                  <div className="admin-cursos-field">
                    <label>Ordem</label>
                    <input type="number" value={moduloOrdem} onChange={(event) => setModuloOrdem(event.target.value)} />
                  </div>
                  <div className="admin-cursos-field">
                    <label>Carga minima</label>
                    <input type="number" value={moduloCarga} onChange={(event) => setModuloCarga(event.target.value)} />
                  </div>
                </div>
                <div className="admin-cursos-field">
                  <label>Descricao</label>
                  <textarea value={moduloDescricao} onChange={(event) => setModuloDescricao(event.target.value)} />
                </div>
                <div className="admin-curso-actions-row">
                  <button type="submit" className="admin-cursos-submit" disabled={salvando}>
                    Salvar modulo
                  </button>
                  <button type="button" className="admin-curso-danger-button" onClick={excluirModulo} disabled={salvando}>
                    Excluir modulo
                  </button>
                </div>
              </form>
            )}
          </section>

          <section className="admin-cursos-card">
            <h2><span>CO</span> Rotas do conteudo</h2>

            {!moduloSelecionadoId ? (
              <p className="admin-cursos-empty">Selecione um modulo para gerenciar conteudos.</p>
            ) : (
              <>
                <div className="admin-curso-content-grid">
                  <form onSubmit={adicionarMaterial}>
                    <h3>Adicionar PDF</h3>
                    <div className="admin-cursos-field">
                      <label>Titulo</label>
                      <input value={materialTitulo} onChange={(event) => setMaterialTitulo(event.target.value)} />
                    </div>
                    <div className="admin-cursos-field">
                      <label>URL do PDF</label>
                      <input value={materialUrl} onChange={(event) => setMaterialUrl(event.target.value)} />
                    </div>
                    <button type="submit" className="admin-cursos-submit" disabled={salvando}>Adicionar PDF</button>
                  </form>

                  <form onSubmit={adicionarVideo}>
                    <h3>Adicionar videoaula</h3>
                    <div className="admin-cursos-field">
                      <label>Titulo</label>
                      <input value={videoTitulo} onChange={(event) => setVideoTitulo(event.target.value)} />
                    </div>
                    <div className="admin-cursos-field">
                      <label>URL do video</label>
                      <input value={videoUrl} onChange={(event) => setVideoUrl(event.target.value)} />
                    </div>
                    <div className="admin-cursos-form-grid">
                      <div className="admin-cursos-field">
                        <label>Duracao</label>
                        <input type="number" value={videoDuracao} onChange={(event) => setVideoDuracao(event.target.value)} />
                      </div>
                      <div className="admin-cursos-field">
                        <label>Tipo</label>
                        <select value={videoTipo} onChange={(event) => setVideoTipo(event.target.value as 'GRAVADO' | 'AO_VIVO')}>
                          <option value="GRAVADO">Gravado</option>
                          <option value="AO_VIVO">Ao vivo</option>
                        </select>
                      </div>
                    </div>
                    <button type="submit" className="admin-cursos-submit" disabled={salvando}>Adicionar videoaula</button>
                  </form>
                </div>

                <div className="admin-curso-content-grid">
                  <section>
                    <h3>PDFs do modulo</h3>
                    {conteudoModulo.materiais.length === 0 ? (
                      <p className="admin-cursos-empty">Nenhum PDF cadastrado.</p>
                    ) : (
                      conteudoModulo.materiais.map((material) => (
                        <article className="admin-curso-content-item" key={material.id}>
                          <strong>{material.titulo}</strong>
                          <a href={material.url_pdf} target="_blank" rel="noreferrer">{material.url_pdf}</a>
                        </article>
                      ))
                    )}
                  </section>

                  <section>
                    <h3>Videoaulas do modulo</h3>
                    {conteudoModulo.videoaulas.length === 0 ? (
                      <p className="admin-cursos-empty">Nenhuma videoaula cadastrada.</p>
                    ) : (
                      conteudoModulo.videoaulas.map((video) => (
                        <article className="admin-curso-content-item" key={video.id}>
                          <strong>{video.titulo}</strong>
                          <span>{video.tipo || 'Tipo nao informado'} - {video.duracao || 0} min</span>
                          <button type="button" className="admin-cursos-edit" onClick={() => carregarExercicios(video.id)}>
                            Gerenciar exercicios
                          </button>
                        </article>
                      ))
                    )}
                  </section>
                </div>

                {videoSelecionadoId && (
                  <form className="admin-curso-subform" onSubmit={adicionarExercicio}>
                    <h3>Exercicios da videoaula</h3>
                    <div className="admin-cursos-field">
                      <label>Enunciado</label>
                      <textarea value={exercicioEnunciado} onChange={(event) => setExercicioEnunciado(event.target.value)} />
                    </div>
                    <div className="admin-cursos-field">
                      <label>Alternativas, uma por linha</label>
                      <textarea value={exercicioAlternativas} onChange={(event) => setExercicioAlternativas(event.target.value)} />
                    </div>
                    <div className="admin-cursos-field">
                      <label>Resposta correta</label>
                      <input value={exercicioResposta} onChange={(event) => setExercicioResposta(event.target.value)} />
                    </div>
                    <button type="submit" className="admin-cursos-submit" disabled={salvando}>Adicionar exercicio</button>

                    {exercicios.length > 0 && (
                      <div className="admin-curso-module-list">
                        {exercicios.map((exercicio) => (
                          <article className="admin-curso-content-item" key={exercicio.id}>
                            <strong>{exercicio.enunciado}</strong>
                            <span>{exercicio.alternativas}</span>
                          </article>
                        ))}
                      </div>
                    )}
                  </form>
                )}
              </>
            )}
          </section>
        </section>
      </section>
    </main>
  );
}
