import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login, { loginAction } from './pages/Login';
import ErroGlobal from './pages/ErroGlobal'; 
import DashboardAluno, { dashboardAlunoLoader } from './pages/DashboardAluno';
import LayoutGeral from './components/LayoutGeral';

const router = createBrowserRouter([
    { element: <LayoutGeral />,
        errorElement: <ErroGlobal />, 
        children: [
          {
            path: '/',
              element: <Login />,
            action: loginAction,
            errorElement: <ErroGlobal />, 
          },
          {
            path: '/dashboard',
            element: <DashboardAluno />,
            loader: dashboardAlunoLoader,
            errorElement: <ErroGlobal />,
          }
        ]
    }
]);

export default function App() {
  return <RouterProvider router={router} />;
}