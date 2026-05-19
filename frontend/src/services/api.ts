import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios';
import globalRouter from './globalRouter.ts';

export const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
});

// Variáveis de controle para o sistema de Fila (Concurrency)
let isRefreshing = false;
let failedQueue: Array<{ resolve: (value?: unknown) => void, reject: (reason?: any) => void }> = [];

// Função que processa a fila de requisições que ficaram pausadas
const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

// INTERCEPTOR DE REQUISIÇÃO (IDA)
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// INTERCEPTOR DE RESPOSTA (VOLTA)
API.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    // Pega as configurações da requisição original que falhou
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

    // Se o erro for 401 (Não Autorizado) e ainda não tentamos refazer a chamada (_retry)
    if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
      originalRequest._retry = true; // Previne loops infinitos (a chamada só pode ser refeita 1 vez) [5, 6]

      // Se já existe um processo de refresh acontecendo, coloca esta requisição na fila
      if (isRefreshing) {
        return new Promise(function(resolve, reject) {
          failedQueue.push({ resolve, reject });
        }).then(token => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return API(originalRequest);
        }).catch(err => Promise.reject(err));
      }

      // Inicia o bloqueio para o refresh
      isRefreshing = true;
      const refreshToken = localStorage.getItem('refreshToken');

      try {
        // ATENÇÃO: Usamos o axios "puro" aqui e não a nossa instância "API" 
        // para evitar que o interceptor entre em loop interceptando a si mesmo!
        const response = await axios.post('http://localhost:8080/api/autenticacao/refresh', { 
            refreshToken: refreshToken 
        });

        const newToken = response.data.token;
        const newRefreshToken = response.data.refreshToken;

        // Salva os novos tokens na memória do navegador
        localStorage.setItem('token', newToken);
        localStorage.setItem('refreshToken', newRefreshToken);

        // Atualiza a requisição original com o novo token
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        
        // Libera a fila avisando que deu certo e passando o token novo [6]
        processQueue(null, newToken);

        // Refaz a requisição original que tinha falhado [3, 4]
        return API(originalRequest);

      } catch (refreshError) {
        // Se a tentativa de usar o Refresh Token falhar (ex: ele também expirou ou foi revogado)
        processQueue(refreshError, null);
        
        // O caso de falha definitiva: limpa a sessão e desloga o aluno [7, 8]
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        if (globalRouter.navigate) {
          globalRouter.navigate('/');
        }
        return Promise.reject(refreshError);
      } finally {
        // Libera a trava de refresh independentemente de sucesso ou falha
        isRefreshing = false;
      }
    }

    // Se o erro não for 401, apenas repassa o erro para o componente lidar (ex: ErrorBoundary)
    return Promise.reject(error);
  }
);