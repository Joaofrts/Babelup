import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login, { loginAction } from './pages/Login';
import ErroGlobal from './pages/ErroGlobal';
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
        path: '/login',
        element: <Login />,
        action: loginAction,
        errorElement: <ErroGlobal />,
      },
      {
        path: '/dashboard',
        element: <DashboardAluno />,
        loader: dashboardAlunoLoader,
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