import { Outlet, useNavigate, useLoaderData, redirect, Link } from 'react-router-dom';
import { limparSessao, pegarTokenPayload } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import GlobalSpinner from '../GlobalSpinner';
import './style.css';

interface AlunoLayoutData {
  nome: string;
}

// Loader exclusivo para proteger as rotas do aluno
export function alunoLayoutLoader() {
  const token = localStorage.getItem('token');
  
  if (!token) {
    return redirect('/login-aluno');
  }

  const payload = pegarTokenPayload();
  
  return { 
    nome: payload?.nome || 'Aluno' 
  } as AlunoLayoutData;
}

export default function AlunoLayout() {
  const navigate = useNavigate();
  const data = useLoaderData() as AlunoLayoutData;
  const nomeAluno = data?.nome || 'Aluno';

  function lidarComLogout() {
    limparSessao();
    navigate('/login-aluno');
  }

  return (
    <main className="aluno-page-container">
      <GlobalSpinner />
      
      <header className="aluno-header">
        <div className="aluno-header-content">
          <Link to="/dashboard-aluno">
            <img src={logoAzul} alt="Logo BabelUp" className="aluno-logo" />
          </Link>

          <div className="aluno-title-area">
            <h1>Bem-vindo de volta, {nomeAluno}!</h1>
            <p>Continue sua jornada de aprendizado</p>
          </div>

          <div className="aluno-header-actions">
            {/* Espaço reservado caso queira adicionar um menu de navegação (Cursos, Perfil) no futuro */}
            <nav className="aluno-nav">
              <Link to="/">Pagina Inicial</Link>
              <Link to="/cursos">Cursos</Link>
            </nav>

            <button type="button" className="aluno-logout" onClick={lidarComLogout}>
              Sair
            </button>
          </div>
        </div>
      </header>

      <section className="aluno-content-wrapper">
        <Outlet />
      </section>
    </main>
  );
}