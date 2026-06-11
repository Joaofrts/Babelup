import { createBrowserRouter, RouterProvider, Navigate } from 'react-router-dom';
import Login, { loginAction } from './pages/LoginAluno';
import LoginProfessor, { loginProfessorAction } from './pages/LoginProfessor';
import LoginAdmin, { loginAdminAction } from './pages/LoginAdmin';
import ErroGlobal from './pages/ErroGlobal';
import DashboardProfessor, { dashboardProfessorLoader } from './pages/DashboardProfessor';
import DashboardAluno, { dashboardAlunoLoader } from './pages/DashboardAluno';
import Cursos from './pages/Cursos';
import LayoutGeral from './components/LayoutGeral';
import Inicio from './pages/Inicio';
import DashboardAdmin, { adminProfessoresLoader } from './pages/DashboardAdmin';
import AddProfessores, { adminProfessoresListaLoader } from './pages/AddProfessores';
import AddAluno, { adminAlunosLoader } from './pages/AddAlunos';
import AddCursos, { adminCursosLoader } from './pages/AddCurso';
import CadastroUsuario from './pages/CadastroUsuario';
import AdminCursoDetalhe, { adminCursoDetalheLoader } from './pages/AdminCursoDetalhe';
import AdminAgendamentos, { adminAgendamentosLoader } from './pages/AdminAgendamentos';
const router = createBrowserRouter([
  {
    element: <LayoutGeral />,
    errorElement: <ErroGlobal />,
    children: [
      {
        path: '/login-aluno',
        element: <Login />,
        action: loginAction,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/cadastro-aluno',
        element: <CadastroUsuario perfil="ALUNO" />,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/login-professor',
        element: <LoginProfessor />,
        action: loginProfessorAction,
        errorElement: <ErroGlobal />
      },
      {
        path: '/cadastro-professor',
        element: <CadastroUsuario perfil="PROFESSOR" />,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/login-admin',
        element: <LoginAdmin />,
        action: loginAdminAction,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/cadastro-admin',
        element: <CadastroUsuario perfil="ADMIN" />,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/dashboard',
        element: <Navigate to="/dashboard-aluno" replace />,
      },
      {
        path: '/dashboard-aluno',
        element: <DashboardAluno />,
        loader: dashboardAlunoLoader,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/dashboard-professor',
        element: <DashboardProfessor />,
        loader: dashboardProfessorLoader,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/dashboard-admin',
        element: <DashboardAdmin />,
        loader: adminProfessoresLoader,
        errorElement: <ErroGlobal />,
      },
      {
        path:'/add/professores',
        element: <AddProfessores />,
        loader: adminProfessoresListaLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/add/aluno',
        element: <AddAluno />,
        loader: adminAlunosLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/add/curso',
        element: <AddCursos />,
        loader: adminCursosLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/admin/professores',
        element: <AddProfessores />,
        loader: adminProfessoresListaLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/admin/alunos',
        element: <AddAluno />,
        loader: adminAlunosLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/admin/cursos',
        element: <AddCursos />,
        loader: adminCursosLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/admin/cursos/:nivelId',
        element: <AdminCursoDetalhe />,
        loader: adminCursoDetalheLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/admin/agendamentos',
        element: <AdminAgendamentos />,
        loader: adminAgendamentosLoader,
        errorElement: <ErroGlobal />
      },
      {
        path: '/cursos',
        element: <Cursos />,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/',
        element: <Inicio />,
        errorElement: <ErroGlobal />,
      }
    ],
  },
]);

export default function App() {
  return <RouterProvider router={router} />;
}
