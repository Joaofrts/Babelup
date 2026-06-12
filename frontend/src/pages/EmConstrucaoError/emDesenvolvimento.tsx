import { useNavigate } from 'react-router-dom';
import './EmDesenvolvimento.css';

export default function EmDesenvolvimento() {
  const navigate = useNavigate();

  return (
    <div className="wip-container">
      <div className="wip-card">
        <div className="wip-icon">🚧</div>
        <h2>Em Desenvolvimento</h2>
        <p>
          Esta funcionalidade ainda está sendo construída pela nossa equipe. 
          Em breve, ela estará disponível com muitas novidades para você!
        </p>
        
        {/* Usamos navigate(-1) para voltar exatamente para a tela de onde o usuário veio */}
        <button type="button" className="wip-back-button" onClick={() => navigate(-1)}>
          Voltar para a página anterior
        </button>
      </div>

      
    </div>
  );
}