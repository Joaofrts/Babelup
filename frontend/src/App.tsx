import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login, { loginAction } from './pages/LoginAluno';
import LoginProfessor, { loginProfessorAction } from './pages/LoginProfessor';
import LoginAdmin, { loginAdminAction } from './pages/LoginAdmin';
import ErroGlobal from './pages/ErroGlobal';
import DashboardProfessor, { dashboardProfessorLoader } from './pages/DashboardProfessor';
import DashboardAluno, { dashboardAlunoLoader } from './pages/DashboardAluno';
import Cursos from './pages/Cursos';
import LayoutGeral from './components/LayoutGeral';
import Inicio from './pages/Inicio';

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
        path: '/login-professor',
        element: <LoginProfessor />,
        action: loginProfessorAction,
        errorElement: <ErroGlobal />
      },
      {
        path: '/login-admin',
        element: <LoginAdmin />,
        action: loginAdminAction,
        errorElement: <ErroGlobal />,
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