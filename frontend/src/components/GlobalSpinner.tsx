import { useNavigation } from 'react-router-dom';

export default function GlobalSpinner() {
  const navigation = useNavigation();
  const isCarregando = navigation.state === 'loading';

  if (!isCarregando) {
    return null; // Se não estiver carregando, não renderiza nada na tela
  }

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      backgroundColor: 'rgba(255, 255, 255, 0.8)',
      display: 'flex', flexDirection: 'column', 
      justifyContent: 'center', alignItems: 'center', zIndex: 9999
    }}>
      <div style={{ 
        border: '4px solid #f3f3f3', 
        borderTop: '4px solid #007bff', 
        borderRadius: '50%', 
        width: '40px', 
        height: '40px', 
        animation: 'spin 1s linear infinite' 
      }} />
      <h3 style={{ marginTop: '15px', color: '#007bff' }}>Carregando dados...</h3>
      
      <style>
        {`@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }`}
      </style>
    </div>
  );
}