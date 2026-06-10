import { Link, useNavigate } from 'react-router-dom';
import { useMemo, useState } from 'react';
import type { FormEvent } from 'react';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface Cursos {
  id: number;
  nome: string;
  nivel: string;
  duracao: string;
  preco: string;
  descricao: string;
  alunos: number;
}

const cursosIniciais: Cursos[] = [
  {
    id: 1,
    nome: 'Inglês',
    nivel: 'Nível básico',
    duracao: '3 meses',
    preco: 'R$ 199,00',
    descricao: 'Curso introdutório para alunos iniciantes.',
    alunos: 32,
  },
  {
    id: 2,
    nome: 'Inglês',
    nivel: 'Nível intermediário',
    duracao: '4 meses',
    preco: 'R$ 249,00',
    descricao: 'Curso para desenvolvimento de conversação e gramática.',
    alunos: 21,
  },
  {
    id: 3,
    nome: 'Português',
    nivel: 'Nível avançado',
    duracao: '5 meses',
    preco: 'R$ 299,00',
    descricao: 'Curso focado em fluência, escrita e comunicação.',
    alunos: 15,
  },
];

export default function AdminCursos() {
  const navigate = useNavigate();

  const [cursos, setCursos] = useState<Cursos[]>(cursosIniciais);
  const [nomeCurso, setNomeCurso] = useState('');
  const [nivel, setNivel] = useState('');
  const [duracao, setDuracao] = useState('');
  const [preco, setPreco] = useState('');
  const [descricao, setDescricao] = useState('');
  const [busca, setBusca] = useState('');

  const cursosFiltrados = useMemo(() => {
    const textoBusca = busca.toLowerCase().trim();

    if (!textoBusca) {
      return cursos;
    }

    return cursos.filter((curso) => {
      return (
        curso.nome.toLowerCase().includes(textoBusca) ||
        curso.nivel.toLowerCase().includes(textoBusca) ||
        curso.descricao.toLowerCase().includes(textoBusca)
      );
    });
  }, [busca, cursos]);

  function criarCurso(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!nomeCurso.trim() || !nivel.trim() || !duracao.trim() || !preco.trim()) {
      return;
    }

    const novoCurso: Cursos = {
      id: Date.now(),
      nome: nomeCurso,
      nivel,
      duracao: `${duracao} meses`,
      preco: `R$ ${preco}`,
      descricao: descricao || 'Descrição não informada.',
      alunos: 0,
    };

    setCursos((cursosAtuais) => [novoCurso, ...cursosAtuais]);

    setNomeCurso('');
    setNivel('');
    setDuracao('');
    setPreco('');
    setDescricao('');
  }

  function sair() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('perfil');
    localStorage.removeItem('usuarioLogado');

    navigate('/login-admin');
  }

  return (
    <main className="admin-cursos-page">
      <aside className="admin-cursos-sidebar">
        <div className="admin-cursos-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="admin-cursos-logo" />
        </div>

        <nav className="admin-cursos-menu">
          <Link to="/dashboard-admin" className="admin-cursos-menu-item">
            <span>♧</span>
            Avisos
          </Link>

          <Link to="/admin/cursos" className="admin-cursos-menu-item active">
            <span>▱</span>
            Cursos
          </Link>

          <Link to="/admin/alunos" className="admin-cursos-menu-item">
            <span>🎓</span>
            Alunos
          </Link>

          <Link to="/admin/professores" className="admin-cursos-menu-item">
            <span>▦</span>
            Professores
          </Link>

          <Link to="/admin/estatisticas" className="admin-cursos-menu-item">
            <span>▤</span>
            Estatísticas
          </Link>

          <Link to="/admin/chat" className="admin-cursos-menu-item">
            <span>♡</span>
            Chat
          </Link>

          <Link to="/admin/forum" className="admin-cursos-menu-item">
            <span>♢</span>
            Fórum
          </Link>
        </nav>

        <button type="button" className="admin-cursos-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-cursos-main">
        <header className="admin-cursos-header">
          <div>
            <h1>Olá, Joao Marcelo!</h1>
            <p>Bem-vindo de volta!</p>
          </div>

          <div className="admin-cursos-header-actions">
            <button type="button" className="admin-cursos-bell">
              ♧
              <span />
            </button>

            <div className="admin-cursos-avatar">LS</div>
          </div>
        </header>

        <section className="admin-cursos-content">
          <form className="admin-cursos-card" onSubmit={criarCurso}>
            <h2>
              <span>▱</span>
              Criar novo curso
            </h2>

            <div className="admin-cursos-form-grid">
              <div className="admin-cursos-field">
                <label htmlFor="nomeCurso">Nome do curso</label>
                <input
                  id="nomeCurso"
                  type="text"
                  placeholder="Exemplo: Inglês - Avançado"
                  value={nomeCurso}
                  onChange={(event) => setNomeCurso(event.target.value)}
                />
              </div>

              <div className="admin-cursos-field">
                <label htmlFor="nivel">Nível</label>
                <select
                  id="nivel"
                  value={nivel}
                  onChange={(event) => setNivel(event.target.value)}
                >
                  <option value="">Selecione o nível</option>
                  <option value="Nível básico">Nível básico</option>
                  <option value="Nível intermediário">Nível intermediário</option>
                  <option value="Nível avançado">Nível avançado</option>
                </select>
              </div>

              <div className="admin-cursos-field">
                <label htmlFor="duracao">Duração (meses)</label>
                <input
                  id="duracao"
                  type="text"
                  placeholder="2, 3..."
                  value={duracao}
                  onChange={(event) => setDuracao(event.target.value)}
                />
              </div>

              <div className="admin-cursos-field">
                <label htmlFor="preco">Preço (R$/mês)</label>
                <input
                  id="preco"
                  type="text"
                  placeholder="299"
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

            <button type="submit" className="admin-cursos-submit">
              Criar curso
            </button>
          </form>

          <section className="admin-cursos-card admin-cursos-list-card">
            <div className="admin-cursos-list-header">
              <h2>
                <span>▱</span>
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
                    <th>Duração</th>
                    <th>Preço</th>
                    <th>Alunos</th>
                    <th>Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {cursosFiltrados.map((curso) => (
                    <tr key={curso.id}>
                      <td>
                        <strong>{curso.nome}</strong>
                        <p>{curso.descricao}</p>
                      </td>
                      <td>{curso.nivel}</td>
                      <td>{curso.duracao}</td>
                      <td>{curso.preco}</td>
                      <td>{curso.alunos}</td>
                      <td>
                        <button type="button" className="admin-cursos-edit">
                          Editar
                        </button>
                      </td>
                    </tr>
                  ))}

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
          </section>

          <div className="admin-cursos-illustration" aria-hidden="true">
            <div className="bubble bubble-one">A</div>
            <div className="bubble bubble-two">文</div>
            <div className="card-line card-line-one" />
            <div className="card-line card-line-two" />
          </div>
        </section>
      </section>
    </main>
  );
}