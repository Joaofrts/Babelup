import React from 'react';
import { useLoaderData, useNavigate } from 'react-router-dom';
import { API } from '../../services/api';
import globalRouter from '../../services/globalRouter';

// 1. Tipagem: O "espelho" dos dados que o Java vai nos devolver
interface DadosAluno {
  nome: string;
  email: string;
  nivelAtual: string;
  progressoGeral: number; // Para atender ao RF-011
}

// 2. O Loader: Executa ANTES da tela renderizar para buscar os dados
export async function dashboardAlunoLoader({ request }: { request: Request }) {
  // Passamos o 'request.signal' para o Axios. Se o aluno clicar em "Voltar" 
  // antes da requisição terminar, o Axios cancela a chamada para poupar internet [5, 6].
  const [progressoResponse, niveisResponse, modulosResponse] = await Promise.all([
    API.get('/alunos/meu-perfil', {signal: request.signal}),
    API.get('/niveis/listar', {signal: request.signal}),
    API.get('modulos/nivel/1', {signal: request.signal})
  ])
  
  return {
    perfil: progressoResponse.data,
    niveis: niveisResponse,
    modulos: modulosResponse 
}}

// 3. O Componente Visual
export default function DashboardAluno() {
  // O React Router nos entrega os dados que o Loader buscou já tipados! [3]
  const dados = useLoaderData() as any;
  const navigate = useNavigate();

  const lidarComLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    navigate('/');
  };

  return (
    <div style={{ padding: '40px', fontFamily: 'sans-serif', maxWidth: '800px', margin: '0 auto' }}>
      
      {/* Cabeçalho */}
      <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid #eee', paddingBottom: '20px' }}>
        <h1 style={{ color: '#007bff' }}>BabelUp</h1>
        <div>
          <span style={{ marginRight: '15px', fontWeight: 'bold' }}>Olá, {dados.nome}</span>
          <button onClick={lidarComLogout} style={{ padding: '8px 15px', background: '#ff4d4f', color: 'white', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>
            Sair
          </button>
        </div>
      </header>

      {/* Conteúdo Principal */}
      <main style={{ marginTop: '40px' }}>
        <h2>Resumo do seu aprendizado</h2>
        
        {/* Card de Progresso (RF-011) */}
        <div style={{ background: '#f9f9f9', padding: '25px', borderRadius: '10px', marginTop: '20px', boxShadow: '0 4px 6px rgba(0,0,0,0.05)' }}>
          <h3 style={{ margin: '0 0 10px 0' }}>Trilha: Nível {dados.perfil.nivelAtual}</h3>
          
          {/* Barra de Progresso Visual */}
          <div style={{ width: '100%', background: '#e0e0e0', height: '24px', borderRadius: '12px', overflow: 'hidden', marginTop: '15px' }}>
            <div style={{ 
              width: `${dados.progressoGeral}%`, 
              background: '#4caf50', 
              height: '100%', 
              transition: 'width 0.5s ease-in-out' 
            }} />
          </div>
          <p style={{ marginTop: '10px', color: '#555', fontWeight: 'bold' }}>{dados.progressoGeral}% concluído</p>
        </div>

        <button style={{ marginTop: '30px', padding: '15px 30px', background: '#007bff', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '16px', width: '100%' }}>
          Continuar Estudos (Módulo Seguinte)
        </button>
      </main>
    </div>
  );
}