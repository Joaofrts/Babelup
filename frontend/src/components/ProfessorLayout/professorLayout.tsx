import { NavLink, Outlet, useNavigate, useLoaderData, redirect } from 'react-router-dom';
import { limparSessao, pegarTokenPayload } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import GlobalSpinner from '../GlobalSpinner';
import './style.css';

interface ProfessorLayoutData {
  nome: string;
}

// Loader exclusivo para proteger as rotas de professor e buscar o nome
export function professorLayoutLoader() {
  const token = localStorage.getItem('token');
  
  if (!token) {
    return redirect('/login-professor');
  }

  const payload = pegarTokenPayload();
  
  return { 
    nome: payload?.nome || 'Professor' 
  } as ProfessorLayoutData;
}

function obterIniciais(nome: string | undefined) {
  if (!nome) return 'PR';
  const partes = nome.trim().split(' ').filter(Boolean);
  if (partes.length >= 2) {
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  }
  return nome.substring(0, 2).toUpperCase();
}

export default function ProfessorLayout() {
  const navigate = useNavigate();

  const data = useLoaderData() as ProfessorLayoutData;
  const nome = data?.nome || 'Professor';
  const iniciais = obterIniciais(nome);

  function sair() {
    limparSessao();
    navigate('/login-professor');
  }

  const navLinkClass = ({ isActive }: { isActive: boolean }) => 
    isActive ? "professor-menu-item active" : "professor-menu-item";

  return (
    <main className="professor-page-container">
      <GlobalSpinner />
      
      <aside className="professor-sidebar">
        <div className="professor-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="professor-logo" />
        </div>

        <nav className="professor-menu">
          <NavLink to="/dashboard-professor" className={navLinkClass} end>
            <span>⌘</span> Dashboard
          </NavLink>

          <NavLink to="/professor/turmas" className={navLinkClass}>
            <span>♙</span> Turmas
          </NavLink>

          <NavLink to="/professor/alunos" className={navLinkClass}>
            <span>🎓</span> Alunos
          </NavLink>

          <NavLink to="/professor/atividades" className={navLinkClass}>
            <span>▣</span> Atividades
          </NavLink>

          <NavLink to="/professor/conteudos" className={navLinkClass}>
            <span>▭</span> Gerenciar conteúdo
          </NavLink>

          <NavLink to="/professor/notas" className={navLinkClass}>
            <span>▤</span> Notas
          </NavLink>

          <NavLink to="/professor/chat" className={navLinkClass}>
            <span>♡</span> Chat
          </NavLink>

          <NavLink to="/professor/forum" className={navLinkClass}>
            <span>♧</span> Fórum
          </NavLink>
        </nav>

        <button type="button" className="professor-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="professor-main">
        <header className="professor-header">
          <div>
            <h1>Portal do Professor</h1> 
            <p>Área exclusiva para colaboradores BabelUp.</p>
          </div>

          <div className="professor-header-actions">
            <button type="button" className="professor-bell">
              ♧
              <span />
            </button>

            <div className="professor-avatar" title={nome}>{iniciais}</div>
          </div>
        </header>

        <div className="professor-content-wrapper">
          <Outlet />
        </div>
      </section>
    </main>
  );
}