import { redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  criarNivel,
  listarCursosCatalogo,
  listarNiveis,
  limparSessao,
  type CursoCatalogoDTO,
  type NivelDTO,
} from '../../services/babelup';
import './style.css';

interface AdminCursosLoaderData {
  niveis: NivelDTO[];
  catalogo: CursoCatalogoDTO[];
}

export async function adminCursosLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const [niveis, catalogo] = await Promise.all([
      listarNiveis(request.signal),
      listarCursosCatalogo(request.signal),
    ]);

    return { niveis, catalogo } as AdminCursosLoaderData;
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }
    throw new Response('Erro ao carregar cursos.', { status: 500 });
  }
}

function formatarPreco(valor?: number) {
  if (valor == null) return 'Não informado';
  return Number(valor).toLocaleString('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  });
}

function parsePreco(valor: string) {
  const normalizado = valor.replace(/\./g, '').replace(',', '.');
  return Number(normalizado);
}

export default function AdminCursos() {
  const navigate = useNavigate();
  const dados = useLoaderData() as AdminCursosLoaderData;

  const [niveis, setNiveis] = useState<NivelDTO[]>(dados.niveis);
  const [catalogo, setCatalogo] = useState<CursoCatalogoDTO[]>(dados.catalogo);
  const [nomeCurso, setNomeCurso] = useState('');
  const [nivel, setNivel] = useState('');
  const [duracao, setDuracao] = useState('');
  const [preco, setPreco] = useState('');
  const [descricao, setDescricao] = useState('');
  const [busca, setBusca] = useState('');
  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [salvando, setSalvando] = useState(false);

  const catalogoPorId = useMemo(() => new Map(catalogo.map((curso) => [curso.id, curso])), [catalogo]);

  const cursosFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();
    if (!textoBusca) return niveis;

    return niveis.filter((curso) => (
      (curso.idioma || '').toLowerCase().includes(textoBusca) ||
      (curso.nome || '').toLowerCase().includes(textoBusca) ||
      (curso.descricao || '').toLowerCase().includes(textoBusca)
    ));
  }, [busca, niveis]);

  async function recarregarCursos() {
    const [niveisAtualizados, catalogoAtualizado] = await Promise.all([
      listarNiveis(),
      listarCursosCatalogo(),
    ]);
    setNiveis(niveisAtualizados);
    setCatalogo(catalogoAtualizado);
  }

  async function criarCurso(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMensagemErro('');
    setMensagemSucesso('');

    const cargaHoraria = Number(duracao);
    const precoMensal = parsePreco(preco);

    if (!nomeCurso.trim() || !nivel.trim() || Number.isNaN(cargaHoraria) || Number.isNaN(precoMensal)) {
      setMensagemErro('Informe idioma, nível, carga horária e preço válidos.');
      return;
    }

    const proximaOrdem = Math.max(0, ...niveis.map((item) => item.ordem || 0)) + 1;

    try {
      setSalvando(true);
      await criarNivel({
        idioma: nomeCurso.trim(),
        nome: nivel.trim(),
        ordem: proximaOrdem,
        carga_horaria: cargaHoraria,
        descricao: descricao.trim() || 'Descrição não informada.',
        preco_mensal: precoMensal,
      });

      setMensagemSucesso('Curso cadastrado com sucesso.');
      setNomeCurso('');
      setNivel('');
      setDuracao('');
      setPreco('');
      setDescricao('');

      await recarregarCursos();
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
        limparSessao();
        navigate('/login-admin');
        return;
      }
      setMensagemErro('Não foi possível cadastrar o curso.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <>
      <div className="admin-page-titles" style={{ marginBottom: '24px' }}>
        <h1 style={{ margin: 0, fontSize: '23px', color: '#111827', fontWeight: 700 }}>Cursos</h1>
        <p style={{ margin: '4px 0 0', fontSize: '11px', color: '#5f6470' }}>Cursos e níveis cadastrados.</p>
      </div>

      <form className="admin-cursos-card" onSubmit={criarCurso}>
        <h2>
          <span></span>
          Criar novo curso
        </h2>

        {mensagemErro && <p className="admin-cursos-empty">{mensagemErro}</p>}
        {mensagemSucesso && <p className="admin-cursos-empty">{mensagemSucesso}</p>}

        <div className="admin-cursos-form-grid">
          <div className="admin-cursos-field">
            <label htmlFor="nomeCurso">Idioma</label>
            <input
              id="nomeCurso"
              type="text"
              placeholder="Exemplo: Inglês"
              value={nomeCurso}
              onChange={(event) => setNomeCurso(event.target.value)}
            />
          </div>

          <div className="admin-cursos-field">
            <label htmlFor="nivel">Nível</label>
            <input
              id="nivel"
              type="text"
              placeholder="Exemplo: A1"
              value={nivel}
              onChange={(event) => setNivel(event.target.value)}
            />
          </div>

          <div className="admin-cursos-field">
            <label htmlFor="duracao">Carga horária</label>
            <input
              id="duracao"
              type="number"
              min="1"
              placeholder="40"
              value={duracao}
              onChange={(event) => setDuracao(event.target.value)}
            />
          </div>

          <div className="admin-cursos-field">
            <label htmlFor="preco">Preço mensal</label>
            <input
              id="preco"
              type="text"
              placeholder="99,90"
              value={preco}
              onChange={(event) => setPreco(event.target.value)}
            />
          </div>
        </div>

        <div className="admin-cursos-field">
          <label htmlFor="descricao">Descrição</label>
          <textarea
            id="descricao"
            placeholder="Insira a descrição do curso..."
            value={descricao}
            onChange={(event) => setDescricao(event.target.value)}
          />
        </div>

        <button type="submit" className="admin-cursos-submit" disabled={salvando}>
          {salvando ? 'Criando...' : 'Criar curso'}
        </button>
      </form>

      <div className="admin-cursos-card admin-cursos-list-card">
        <div className="admin-cursos-list-header">
          <h2>
            <span>CU</span>
            Cursos existentes
          </h2>

          <input
            type="text"
            placeholder="Pesquisar cursos..."
            value={busca}
            onChange={(event) => setBusca(event.target.value)}
          />
        </div>

        <div className="admin-cursos-table-wrapper">
          <table className="admin-cursos-table">
            <thead>
              <tr>
                <th>Curso</th>
                <th>Nível</th>
                <th>Carga horária</th>
                <th>Preço</th>
                <th>Módulos</th>
                <th>Ações</th>
              </tr>
            </thead>

            <tbody>
              {cursosFiltrados.map((curso) => {
                const cursoCatalogo = catalogoPorId.get(curso.id);

                return (
                  <tr key={curso.id}>
                    <td>
                      <strong>{curso.idioma || cursoCatalogo?.titulo || 'Curso sem idioma'}</strong>
                      <p>{curso.descricao || cursoCatalogo?.descricao || 'Descrição não informada.'}</p>
                    </td>
                    <td>{curso.nome || 'Não informado'}</td>
                    <td>{curso.cargaHorariaEstimada ? `${curso.cargaHorariaEstimada}h` : 'Não informado'}</td>
                    <td>{formatarPreco(cursoCatalogo?.precoMensal)}</td>
                    <td>{curso.modulos?.length ?? 0}</td>
                    <td>
                      <button
                        type="button"
                        className="admin-cursos-edit"
                        onClick={() => navigate(`/admin/cursos/${curso.id}`)}
                      >
                        Editar
                      </button>
                    </td>
                  </tr>
                );
              })}

              {cursosFiltrados.length === 0 && (
                <tr>
                  <td colSpan={6} className="admin-cursos-empty">
                    Nenhum curso encontrado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      
    </>
  );
}