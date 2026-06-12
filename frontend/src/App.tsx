import { createBrowserRouter, RouterProvider, Navigate } from 'react-router-dom';

// Importações de Páginas e Componentes
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

import AlunoLayout, { alunoLayoutLoader } from './components/AlunoLayout/alunoLayout';
import ProfessorLayout, { professorLayoutLoader } from './components/ProfessorLayout/professorLayout';
import AdminLayout, { adminLayoutLoader } from './components/AdminLayout'; 
import EmDesenvolvimento from './pages/EmConstrucaoError/emDesenvolvimento'; 

const router = createBrowserRouter([
  // ==========================================
  // BLOCO 1: Rotas de Professores (Layout Exclusivo)
  // ==========================================
  {
    element: <ProfessorLayout />, // <--- A casca visual foi adicionada aqui
    loader: professorLayoutLoader,
    errorElement: <ErroGlobal />,
    children: [
      { path: '/dashboard-professor', element: <DashboardProfessor />, loader: dashboardProfessorLoader },
      { path: '/professor/turmas', element: <EmDesenvolvimento /> },
      { path: '/professor/alunos', element: <EmDesenvolvimento /> },
      { path: '/professor/atividades', element: <EmDesenvolvimento /> },
      { path: '/professor/conteudos', element: <EmDesenvolvimento /> },
      { path: '/professor/notas', element: <EmDesenvolvimento /> },
      { path: '/professor/chat', element: <EmDesenvolvimento /> },
      { path: '/professor/forum', element: <EmDesenvolvimento /> },
    ],
  },

  // ==========================================
  // BLOCO 2: Rotas Gerais (Layout Geral)
  // ==========================================
  {
    element: <LayoutGeral />,
    errorElement: <ErroGlobal />,
    children: [
      { path: '/', element: <Inicio />, errorElement: <ErroGlobal /> },
      { path: '/login-aluno', element: <Login />, action: loginAction, errorElement: <ErroGlobal /> },
      { path: '/cadastro-aluno', element: <CadastroUsuario perfil="ALUNO" />, errorElement: <ErroGlobal /> },
      { path: '/login-professor', element: <LoginProfessor />, action: loginProfessorAction, errorElement: <ErroGlobal /> },
      { path: '/login-admin', element: <LoginAdmin />, action: loginAdminAction, errorElement: <ErroGlobal /> },
      { path: '/cursos', element: <Cursos />, errorElement: <ErroGlobal /> },
    ],
  },

  // ==========================================
  // BLOCO 3: Rotas de Administração (Layout Admin)
  // ==========================================
  {
    element: <AdminLayout />,
    loader: adminLayoutLoader,
    errorElement: <ErroGlobal />,
    children: [
      { path: '/dashboard-admin', element: <DashboardAdmin />, loader: adminProfessoresLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/professores', element: <AddProfessores />, loader: adminProfessoresListaLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/alunos', element: <AddAluno />, loader: adminAlunosLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/cursos', element: <AddCursos />, loader: adminCursosLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/cursos/:nivelId', element: <AdminCursoDetalhe />, loader: adminCursoDetalheLoader, errorElement: <ErroGlobal /> },
      { path: '/admin/agendamentos', element: <AdminAgendamentos />, loader: adminAgendamentosLoader, errorElement: <ErroGlobal /> },
      
      // Rotas em Desenvolvimento (Aproveitando a tela nova)
      { path: '/admin/estatisticas', element: <EmDesenvolvimento /> },
      { path: '/admin/chat', element: <EmDesenvolvimento /> },
      { path: '/admin/forum', element: <EmDesenvolvimento /> },
    ],
  },
  {element: <AlunoLayout />,
  loader: alunoLayoutLoader,
  errorElement: <ErroGlobal />,
  children: [
    { path: '/dashboard-aluno', element: <DashboardAluno />, loader: dashboardAlunoLoader,errorElement:<ErroGlobal/> },
    
  ]
  }
]);

export default function App() {
  return <RouterProvider router={router} />;
}