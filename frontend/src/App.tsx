import React from 'react';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login, { loginAction } from './pages/Login';
import ErroGlobal from './pages/ErroGlobal'; // Importamos a nossa tela de erro
// Toda a inteligência de qual tela carrega qual dado ou ação fica aqui [1, 21]
const router = createBrowserRouter([
  {
    path: '/',
    element: <Login />,
    action: loginAction,
    errorElement: <ErroGlobal />, // Atrela o formulário da tela com a lógica de requisição [15]
  },
  {
    path: '/dashboard',
    element: <h2>Bem-vindo ao Dashboard! (Tela protegida no futuro)</h2>,
    errorElement: <ErroGlobal />,
  }
]);

export default function App() {
  return <RouterProvider router={router} />;
}