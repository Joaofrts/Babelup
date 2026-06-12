import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { limparSessao, type UsuarioDTO } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import GlobalSpinner from '../GlobalSpinner';
import './style.css'; // Sugiro renomear as classes no CSS tirando o "-alunos" para ficar genérico

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

  // ATENÇÃO: Aqui você deve buscar os dados do usuário logado do seu contexto, 
  // estado global (Redux/Zustand) ou do loader da rota pai. 
  // Estou simulando um objeto para o exemplo.
  const usuarioLogado: Partial<UsuarioDTO> = { nome: 'Administrador BabelUp' }; 
  const iniciais = obterIniciais(usuarioLogado.nome);

  function sair() {
    limparSessao();
    navigate('/login-admin');
  }

  // Função para padronizar a classe do NavLink
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
          <NavLink to="/dashboard-admin" className={navLinkClass}>
            <span>AD</span> Avisos
          </NavLink>

          <NavLink to="/admin/cursos" className={navLinkClass}>
            <span>CU</span> Cursos
          </NavLink>

          <NavLink to="/admin/alunos" className={navLinkClass}>
            <span>A</span> Alunos
          </NavLink>

          <NavLink to="/admin/professores" className={navLinkClass}>
            <span>PR</span> Professores
          </NavLink>

          {/* Link de Agendamentos adicionado conforme solicitado */}
          <NavLink to="/admin/agendamentos" className={navLinkClass}>
            <span>AG</span> Agendamentos
          </NavLink>

          <NavLink to="/admin/estatisticas" className={navLinkClass}>
            <span>ES</span> Estatísticas
          </NavLink>

          <NavLink to="/admin/chat" className={navLinkClass}>
            <span>CH</span> Chat
          </NavLink>

          <NavLink to="/admin/forum" className={navLinkClass}>
            <span>FO</span> Fórum
          </NavLink>
        </nav>

        <button type="button" className="admin-sair" onClick={sair}>
          Sair
        </button>
      </aside>

      <section className="admin-main">
        <header className="admin-header">
          <div>
            {/* O título pode virar um componente dinâmico depois, ou ficar na página filha */}
            <h1>Painel Administrativo</h1> 
          </div>

          <div className="admin-header-actions">
            <button type="button" className="admin-bell">
              AD
              <span />
            </button>

            {/* Iniciais dinâmicas do usuário logado */}
            <div className="admin-avatar" title={usuarioLogado.nome}>{iniciais}</div>
          </div>
        </header>

        {/* O <Outlet /> É A MÁGICA: É aqui que o conteúdo de Alunos, Cursos, etc. vai aparecer */}
        <div className="admin-content-wrapper">
          <Outlet />
        </div>

      </section>
    </main>
  );
}