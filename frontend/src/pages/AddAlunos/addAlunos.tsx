import { redirect, useLoaderData } from 'react-router-dom';
import { useMemo, useState } from 'react';
import axios from 'axios';
import { listarUsuarios, limparSessao, type UsuarioDTO } from '../../services/babelup';
import './style.css'; // Mantenha aqui apenas o CSS específico da tabela de alunos

export async function adminAlunosLoader({ request }: { request: Request }) {
  if (!localStorage.getItem('token')) {
    return redirect('/login-admin');
  }

  try {
    const usuarios = await listarUsuarios(request.signal);
    return usuarios.filter((usuario) => usuario.perfil === 'ALUNO');
  } catch (error: unknown) {
    if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 403)) {
      limparSessao();
      return redirect('/login-admin');
    }
    throw new Response('Erro ao carregar alunos.', { status: 500 });
  }
}

export default function AdminAlunos() {
  const alunos = useLoaderData() as UsuarioDTO[];
  const [busca, setBusca] = useState('');

  const alunosFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();
    if (!textoBusca) return alunos;

    return alunos.filter((aluno) => (
      (aluno.nome || '').toLowerCase().includes(textoBusca) ||
      (aluno.email || '').toLowerCase().includes(textoBusca)
    ));
  }, [busca, alunos]);

  return (
    <section className="admin-alunos-content">
      {/* Você pode colocar o h1 e p específicos da página aqui dentro, logo acima do card */}
      <div className="admin-page-titles">
        <h1>Alunos</h1>
        <p>Usuários cadastrados como alunos.</p>
      </div>

      <div className="admin-alunos-card">
        <div className="admin-alunos-card-header">
          <h2>
            <span>AL</span>
            Gerenciar alunos
          </h2>
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
                <th>Nome</th>
                <th>E-mail</th>
                <th>Perfil</th>
                <th>Status</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {alunosFiltrados.map((aluno) => (
                <tr key={aluno.id || aluno.email}>
                  <td>{aluno.nome || 'Aluno sem nome'}</td>
                  <td>{aluno.email || 'E-mail não informado'}</td>
                  <td>{aluno.perfil || 'ALUNO'}</td>
                  <td>
                    <span className="admin-alunos-status ativo">Cadastrado</span>
                  </td>
                  <td>
                    <button type="button" className="admin-alunos-edit" disabled>
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
        <div className="bubble bubble-two">B</div>
        <div className="card-line card-line-one" />
        <div className="card-line card-line-two" />
      </div>
    </section>
  );
}