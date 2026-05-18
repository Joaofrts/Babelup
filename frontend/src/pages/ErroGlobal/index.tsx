import React from 'react';
import { useRouteError, isRouteErrorResponse, Link } from 'react-router-dom';

export default function ErroGlobal() {
  // O hook useRouteError captura automaticamente o erro que fez a tela falhar [8, 9]
  const erro = useRouteError();
  let mensagemErro = "Ocorreu um erro inesperado.";

  // Verifica se é um erro HTTP conhecido (ex: 404 Not Found ou 500 Server Error) [10]
  if (isRouteErrorResponse(erro)) {
    if (erro.status === 404) {
      mensagemErro = "A página que você está procurando não existe.";
    } else if (erro.status === 500) {
      mensagemErro = "Nosso servidor está passando por instabilidades. Tente novamente mais tarde.";
    } else if (erro.status === 503) {
      mensagemErro = "Serviço indisponível no momento.";
    }
  } else if (erro instanceof Error) {
    // Captura erros de código (falhas do próprio React) [8]
    mensagemErro = erro.message;
  }

  return (
    <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'sans-serif' }}>
      <h1 style={{ fontSize: '3rem', color: '#ff4d4f' }}>Oops!</h1>
      <h2>Algo deu errado no BabelUp.</h2>
      <p style={{ color: '#666', margin: '20px 0' }}>{mensagemErro}</p>
      
      <Link 
        to="/" 
        style={{ padding: '10px 20px', background: '#007bff', color: 'white', textDecoration: 'none', borderRadius: '5px' }}
      >
        Voltar para o Início
      </Link>
    </div>
  );
}