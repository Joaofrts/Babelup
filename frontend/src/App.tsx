import { createBrowserRouter, RouterProvider, Navigate } from 'react-router-dom';

// Importações de Páginas e Componentes (Mantidas do seu código)
import Login, { loginAction } from './pages/LoginAluno/loginAluno';
import LoginProfessor, { loginProfessorAction } from './pages/LoginProfessor/loginProfessor';
import LoginAdmin, { loginAdminAction } from './pages/LoginAdmin/loginAdmin';
import ErroGlobal from './pages/ErroGlobal/erroGlobal';
import DashboardProfessor, { dashboardProfessorLoader } from './pages/DashboardProfessor/dashboardProfessor';
import DashboardAluno, { dashboardAlunoLoader } from './pages/DashboardAluno/dashboardAluno';
import Cursos from './pages/Cursos/cursos';
import LayoutGeral from './components/LayoutGeral';
import Inicio from './pages/Inicio/inicio';
import DashboardAdmin, { adminProfessoresLoader } from './pages/DashboardAdmin/dashboardAdmin';
import AddProfessores, { adminProfessoresListaLoader } from './pages/AddProfessores/addProfessores';
import AddAluno, { adminAlunosLoader } from './pages/AddAlunos/addAlunos';
import AddCursos, { adminCursosLoader } from './pages/AddCurso/addCurso';
import CadastroUsuario from './pages/CadastroUsuario/cadastroUsuario';
import AdminCursoDetalhe, { adminCursoDetalheLoader } from './pages/AdminCursoDetalhe/adminCursoDetalhe';
import AdminAgendamentos, { adminAgendamentosLoader } from './pages/AdminAgendamentos/adminAgendamentos';

// NOVO IMPORT: O componente de Layout do Admin
import AdminLayout from './components/AdminLayout'; 

const router = createBrowserRouter([
  // ==========================================
  // BLOCO 1: Rotas Gerais, Alunos e Professores
  // ==========================================
  {
    element: <LayoutGeral />,
    errorElement: <ErroGlobal />,
    children: [
      { path: '/', element: <Inicio />, errorElement: <ErroGlobal /> },
      { path: '/login-aluno', element: <Login />, action: loginAction, errorElement: <ErroGlobal /> },
      { path: '/cadastro-aluno', element: <CadastroUsuario perfil="ALUNO" />, errorElement: <ErroGlobal /> },
      { path: '/login-professor', element: <LoginProfessor />, action: loginProfessorAction, errorElement: <ErroGlobal /> },
      { path: '/cadastro-professor', element: <CadastroUsuario perfil="PROFESSOR" />, errorElement: <ErroGlobal /> },
      { path: '/login-admin', element: <LoginAdmin />, action: loginAdminAction, errorElement: <ErroGlobal /> },
      { path: '/cadastro-admin', element: <CadastroUsuario perfil="ADMIN" />, errorElement: <ErroGlobal /> },
      
      { path: '/dashboard', element: <Navigate to="/dashboard-aluno" replace /> },
      { path: '/dashboard-aluno', element: <DashboardAluno />, loader: dashboardAlunoLoader, errorElement: <ErroGlobal /> },
      { path: '/dashboard-professor', element: <DashboardProfessor />, loader: dashboardProfessorLoader, errorElement: <ErroGlobal /> },
      { path: '/cursos', element: <Cursos />, errorElement: <ErroGlobal /> },
    ],
  },

  // ==========================================
  // BLOCO 2: Rotas de Administração
  // ==========================================
  {
    element: <AdminLayout />, // O Outlet dentro do AdminLayout vai renderizar as rotas filhas abaixo
    errorElement: <ErroGlobal />,
    children: [
      { path: '/dashboard-admin', element: <DashboardAdmin />, loader: adminProfessoresLoader, errorElement: <ErroGlobal /> },
      
      // Rotas do prefixo /admin
      { path: '/admin/professores', element: <AddProfessores />, loader: adminProfessoresListaLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/alunos', element: <AddAluno />, loader: adminAlunosLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/cursos', element: <AddCursos />, loader: adminCursosLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/cursos/:nivelId', element: <AdminCursoDetalhe />, loader: adminCursoDetalheLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/agendamentos', element: <AdminAgendamentos />, loader: adminAgendamentosLoader, errorElement: <ErroGlobal /> },

    
    ],
  },
]);

export default function App() {
  return <RouterProvider router={router} />;
}