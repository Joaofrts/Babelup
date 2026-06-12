import { useRouteError, isRouteErrorResponse, Link } from 'react-router-dom';
import './style.css';

export default function ErroGlobal() {
  const erro = useRouteError();
  let mensagemErro = "Ocorreu um erro inesperado.";
  let codigoErro = "Oops!";

  if (isRouteErrorResponse(erro)) {
    codigoErro = String(erro.status);
    if (erro.status === 404) {
      mensagemErro = "A página que você está procurando não existe ou foi movida.";
    } else if (erro.status === 500) {
      mensagemErro = "Nosso servidor está passando por instabilidades. Tente novamente mais tarde.";
    } else if (erro.status === 503) {
      mensagemErro = "Serviço indisponível no momento.";
    }
  } else if (erro instanceof Error) {
    mensagemErro = erro.message;
    codigoErro = "Erro interno";
  }

  return (
    <div className="error-page-container">
      <div className="error-card">
        <div className="error-icon">⚠️</div>
        <h1 className="error-title">{codigoErro}</h1>
        <h2>Algo deu errado no BabelUp.</h2>
        
        <div className="error-message-box">
          <p>{mensagemErro}</p>
        </div>
        
        <Link to="/" className="error-back-button">
          Voltar para o Início
        </Link>
      </div>

      
    </div>
  );
}