import axios from 'axios';

export const API = axios.create({
  // URL do seu backend Spring Boot
  baseURL: 'http://localhost:8080/api', 
  timeout: 10000, // Evita que a requisição trave a tela infinitamente [11]
});

// 1. INTERCEPTOR DE REQUISIÇÃO: Anexa o token antes de enviar para o Java
API.interceptors.request.use(
  (config) => {
    // Pega o token que foi salvo no momento do login
    const token = localStorage.getItem('token');
    
    // Se existir um token, cria o cabeçalho 'Authorization' com 'Bearer '
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para injetar o token no futuro e tratar Erro 401 [10, 12]
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      console.error("Usuário não autorizado. Redirecionar para login.");
      // Lógica de redirecionamento global irá aqui futuramente [13, 14]
    }
    return Promise.reject(error);
  }
);