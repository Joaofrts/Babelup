import { NavLink, Outlet, useNavigate, useLoaderData, redirect } from 'react-router-dom';
import { limparSessao, pegarTokenPayload } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import GlobalSpinner from '../GlobalSpinner';
import './style.css';

interface AdminLayoutData {
  nome: string;
}

export function adminLayoutLoader() {
  const token = localStorage.getItem('token');
  
  if (!token) {
    return redirect('/login-admin');
  }

  const payload = pegarTokenPayload();
  
  return { 
    nome: payload?.nome || 'Administrador' 
  } as AdminLayoutData;
}

// Função auxiliar para extrair as iniciais (ex: "João Freitas" -> "JF")
function obterIniciais(nome: string | undefined) {
  if (!nome) return 'AD';
  const partes = nome.trim().split(' ');
  if (partes.length >= 2) {
    return (partes[0][0] + partes[partes.length - 1][0]).toUpperCase();
  }
  return nome.substring(0, 2).toUpperCase();
}

export default function AdminLayout() {
  const navigate = useNavigate();

  // CORREÇÃO 1: Evita o erro de "Cannot destructure property 'nome' of 'useLoaderData(...)'"
  const data = useLoaderData() as AdminLayoutData;
  const nome = data?.nome || 'Administrador';
  const iniciais = obterIniciais(nome);

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  const navLinkClass = ({ isActive }: { isActive: boolean }) => 
    isActive ? "admin-menu-item active" : "admin-menu-item";

  return (
    <main className="admin-page-container">
      <GlobalSpinner />
      <aside className="admin-sidebar">
        <div className="admin-logo-area">
          <img src={logoAzul} alt="Logo BabelUp" className="admin-logo" />
        </div>

        <nav className="admin-menu">

          <NavLink to="/admin/agendamentos" className={navLinkClass}>
            <span></span> Agendamentos
          </NavLink>
          
          <NavLink to="/admin/alunos" className={navLinkClass}>
            <span></span> Alunos
          </NavLink>

          <NavLink to="/admin/chat" className={navLinkClass}>
            <span></span> Chat
          </NavLink>
          
          <NavLink to="/admin/cursos" className={navLinkClass}>
            <span></span> Cursos
          </NavLink>          

          <NavLink to="/admin/estatisticas" className={navLinkClass}>
            <span></span> Estatísticas
          </NavLink>    

          <NavLink to="/admin/forum" className={navLinkClass}>
            <span></span> Fórum
          </NavLink>

          <NavLink to="/admin/professores" className={navLinkClass}>
            <span></span> Professores
          </NavLink>
        </nav>

        <button type="button" className="admin-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-main">
        <header className="admin-header">
          <div>
            <h1>Painel Administrativo</h1> 
          </div>

          <div className="admin-header-actions">
            <button type="button" className="admin-bell">
              ADM
              <span />
            </button>

            <div className="admin-avatar" title={nome}>{iniciais}</div>
          </div>
        </header>

        <div className="admin-content-wrapper">
          <Outlet />
        </div>
      </section>
    </main>
  );
}