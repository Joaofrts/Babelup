import { redirect, useLoaderData, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  listarUsuarios,
  cadastrarProfessor,
  atualizarUsuario, 
  limparSessao,
  type UsuarioDTO,
} from '../../services/babelup';
import './style.css';

export async function adminProfessoresListaLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const usuarios = await listarUsuarios(request.signal);
    return usuarios.filter((usuario) => usuario.perfil === 'PROFESSOR');
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }
    throw new Response('Erro ao carregar professores.', { status: 500 });
  }
}

export default function AdminProfessores() {
  const professoresIniciais = useLoaderData() as UsuarioDTO[];
  const navigate = useNavigate();

  const [professores, setProfessores] = useState<UsuarioDTO[]>(professoresIniciais);
  const [busca, setBusca] = useState('');

  // Controle do Modal de Adicionar
  const [modalAddAberto, setModalAddAberto] = useState(false);
  const [addNome, setAddNome] = useState('');
  const [addEmail, setAddEmail] = useState('');
  const [addSenha, setAddSenha] = useState('');
  const [addConfirmarSenha, setAddConfirmarSenha] = useState('');

  // Controle do Modal de Editar
  const [professorEmEdicao, setProfessorEmEdicao] = useState<UsuarioDTO | null>(null);
  const [editNome, setEditNome] = useState('');
  const [editEmail, setEditEmail] = useState('');

  // Estados Globais de Feedback
  const [mensagemErro, setMensagemErro] = useState('');
  const [mensagemSucesso, setMensagemSucesso] = useState('');
  const [salvando, setSalvando] = useState(false);

  const professoresFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();
    if (!textoBusca) return professores;

    return professores.filter((professor) => (
      (professor.nome || '').toLowerCase().includes(textoBusca) ||
      (professor.email || '').toLowerCase().includes(textoBusca)
    ));
  }, [busca, professores]);

  async function recarregarProfessores() {
    const usuarios = await listarUsuarios();
    setProfessores(usuarios.filter((u) => u.perfil === 'PROFESSOR'));
  }

  // Ações do Modal Adicionar
  function abrirModalAdd() {
    setMensagemErro('');
    setMensagemSucesso('');
    setAddNome('');
    setAddEmail('');
    setAddSenha('');
    setAddConfirmarSenha('');
    setModalAddAberto(true);
  }

  function fecharModalAdd() {
    setModalAddAberto(false);
  }

  async function handleCadastrar(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMensagemErro('');
    setMensagemSucesso('');

    if (!addNome.trim() || !addEmail.trim() || !addSenha.trim()) {
      setMensagemErro('Informe nome, e-mail e senha.');
      return;
    }

    if (addSenha !== addConfirmarSenha) {
      setMensagemErro('As senhas não conferem.');
      return;
    }

    try {
      setSalvando(true);
      await cadastrarProfessor({
        nome: addNome.trim(),
        email: addEmail.trim(),
        senha: addSenha,
      });

      setMensagemSucesso('Professor cadastrado com sucesso.');
      await recarregarProfessores();
      setTimeout(() => fecharModalAdd(), 1500); // Fecha após 1.5s
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
        limparSessao();
        navigate('/login-admin');
        return;
      }
      setMensagemErro('Não foi possível cadastrar o professor.');
    } finally {
      setSalvando(false);
    }
  }

  // Ações do Modal Editar
  function abrirModalEdit(professor: UsuarioDTO) {
    setMensagemErro('');
    setMensagemSucesso('');
    setProfessorEmEdicao(professor);
    setEditNome(professor.nome || '');
    setEditEmail(professor.email || '');
  }

  function fecharModalEdit() {
    setProfessorEmEdicao(null);
  }

  async function handleEditar(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!professorEmEdicao?.id) return;

    setMensagemErro('');
    setMensagemSucesso('');

    if (!editNome.trim() || !editEmail.trim()) {
      setMensagemErro('Nome e e-mail são obrigatórios.');
      return;
    }

    try {
      setSalvando(true);
      await atualizarUsuario(professorEmEdicao.id, {
        nome: editNome.trim(),
        email: editEmail.trim(),
      });

      setMensagemSucesso('Professor atualizado com sucesso.');
      await recarregarProfessores();
      setTimeout(() => fecharModalEdit(), 1500);
    } catch (error: unknown) {
      if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
        limparSessao();
        navigate('/login-admin');
        return;
      }
      setMensagemErro('Não foi possível atualizar o professor.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <>
      <div className="admin-page-titles" style={{ marginBottom: '24px' }}>
        <h1 style={{ margin: 0, fontSize: '23px', color: '#111827', fontWeight: 700 }}>Professores</h1>
        <p style={{ margin: '4px 0 0', fontSize: '11px', color: '#5f6470' }}>Usuários cadastrados como professores.</p>
      </div>

      <div className="admin-professores-card">
        <div className="admin-professores-card-header">
          <h2>
            <span>PR</span>
            Gerenciar professores
          </h2>

          <div className="admin-professores-actions">
            <button type="button" className="admin-professores-add-button" onClick={abrirModalAdd}>
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
                <th>Perfil</th>
                <th style={{ textAlign: 'center' }}>Ações</th>
              </tr>
            </thead>

            <tbody>
              {professoresFiltrados.map((professor) => (
                <tr key={professor.id || professor.email}>
                  <td>{professor.nome || 'Professor sem nome'}</td>
                  <td>{professor.email || 'E-mail não informado'}</td>
                  <td>{professor.perfil || 'PROFESSOR'}</td>
                  <td style={{ textAlign: 'center' }}>
                    <button 
                      type="button" 
                      className="admin-professores-kebab-button" 
                      onClick={() => abrirModalEdit(professor)}
                      title="Editar professor"
                    >
                      {/* Ícone SVG de 3 pontos verticais */}
                      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 13C12.5523 13 13 12.5523 13 12C13 11.4477 12.5523 11 12 11C11.4477 11 11 11.4477 11 12C11 12.5523 11.4477 13 12 13Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M12 6C12.5523 6 13 5.55228 13 5C13 4.44772 12.5523 4 12 4C11.4477 4 11 4.44772 11 5C11 5.55228 11.4477 6 12 6Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                        <path d="M12 20C12.5523 20 13 19.5523 13 19C13 18.4477 12.5523 18 12 18C11.4477 18 11 18.4477 11 19C11 19.5523 11.4477 20 12 20Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                      </svg>
                    </button>
                  </td>
                </tr>
              ))}

              {professoresFiltrados.length === 0 && (
                <tr>
                  <td colSpan={4} className="admin-professores-empty">
                    Nenhum professor encontrado.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>


      {/* ================= MODAIS DE SOBREPOSIÇÃO ================= */}

      {/* Modal Adicionar Professor */}
      {modalAddAberto && (
        <div className="admin-modal-overlay">
          <div className="admin-modal-content">
            <div className="admin-modal-header">
              <h2>Adicionar Professor</h2>
              <button type="button" className="admin-modal-close" onClick={fecharModalAdd}>&times;</button>
            </div>
            
            <form onSubmit={handleCadastrar} className="admin-modal-body">
              {mensagemErro && <p className="admin-modal-alert error">{mensagemErro}</p>}
              {mensagemSucesso && <p className="admin-modal-alert success">{mensagemSucesso}</p>}

              <div className="admin-modal-field">
                <label>Nome</label>
                <input type="text" placeholder="Nome do professor..." value={addNome} onChange={(e) => setAddNome(e.target.value)} />
              </div>
              <div className="admin-modal-field">
                <label>E-mail</label>
                <input type="email" placeholder="E-mail de acesso..." value={addEmail} onChange={(e) => setAddEmail(e.target.value)} />
              </div>
              <div className="admin-modal-field">
                <label>Senha</label>
                <input type="password" placeholder="Senha provisória..." value={addSenha} onChange={(e) => setAddSenha(e.target.value)} />
              </div>
              <div className="admin-modal-field">
                <label>Confirmar Senha</label>
                <input type="password" placeholder="Repita a senha..." value={addConfirmarSenha} onChange={(e) => setAddConfirmarSenha(e.target.value)} />
              </div>

              <button type="submit" className="admin-modal-submit" disabled={salvando}>
                {salvando ? 'Cadastrando...' : 'Cadastrar Professor'}
              </button>
            </form>
          </div>
        </div>
      )}

      {/* Modal Editar Professor */}
      {professorEmEdicao && (
        <div className="admin-modal-overlay">
          <div className="admin-modal-content">
            <div className="admin-modal-header">
              <h2>Editar Professor</h2>
              <button type="button" className="admin-modal-close" onClick={fecharModalEdit}>&times;</button>
            </div>
            
            <form onSubmit={handleEditar} className="admin-modal-body">
              {mensagemErro && <p className="admin-modal-alert error">{mensagemErro}</p>}
              {mensagemSucesso && <p className="admin-modal-alert success">{mensagemSucesso}</p>}

              <div className="admin-modal-field">
                <label>Nome</label>
                <input type="text" value={editNome} onChange={(e) => setEditNome(e.target.value)} />
              </div>
              <div className="admin-modal-field">
                <label>E-mail</label>
                <input type="email" value={editEmail} onChange={(e) => setEditEmail(e.target.value)} />
              </div>

              <button type="submit" className="admin-modal-submit" disabled={salvando}>
                {salvando ? 'Salvando...' : 'Salvar Alterações'}
              </button>
            </form>
          </div>
        </div>
      )}
    </>
  );
}