import { Form, useNavigation, useActionData, redirect } from 'react-router-dom';
import { API } from '../../services/api';
import axios from 'axios';

// 1. A Ação: Roda nos bastidores quando o formulário é enviado [15]
export async function loginAction({ request }: { request: Request }) {
  // Pega os dados do formulário nativo do HTML [16]
  const formData = await request.formData();
  const dadosLogin = Object.fromEntries(formData);

  try {
    // Enviamos para o Java passando o "signal" para cancelamento automático [5, 6]
    const response = await API.post('/autenticacao/login', dadosLogin, { 
      signal: request.signal 
    });
    
    // Sucesso: Salva o token e redireciona (o React Router gerencia isso) [13]
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('refreshToken', response.data.refreshToken);
    return redirect('/dashboard');  

  }catch (error: unknown) {
    // Verifica se o erro foi gerado pelo Axios [11, 12]
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // O servidor Java respondeu com erro (ex: 401 Email ou senha incorretos) [11, 13]
        if (error.response.status === 401 || error.response.status === 403) {
          return { error: "E-mail ou senha incorretos." };
        }
        // Se o Java mandar uma mensagem de validação customizada, pegamos ela [13]
        return { error: error.response.data?.message || "Falha na requisição." };
      } else if (error.request) {
        // A requisição foi feita, mas o Java está desligado (Erro de Rede / CORS) [11, 13]
        // Se o Java estiver desligado, isso fará a Tela de Erro Global assumir o controle! [5]
        throw new Response("Backend indisponível", { status: 503 }); 
      }
    }
    
    // Erros genéricos ou cancelamentos de requisição [11]
    return { error: "Erro interno no sistema." };
  }
}

// 2. A Tela (Componente Visual)
export default function Login() {
  const navigation = useNavigation();
  const actionData = useActionData() as { error?: string };
  
  // O React Router nos diz se a requisição está acontecendo (submitting) [18, 19]
  const isSubmitting = navigation.state === 'submitting';

  return (
    <div style={{ padding: '50px', maxWidth: '400px', margin: '0 auto' }}>
      <h2>Login - BabelUp</h2>
      
      {/* Exibe o erro devolvido pela Action, se houver */}
      {actionData?.error && <p style={{ color: 'red' }}>{actionData.error}</p>}

      {/* O componente Form substitui o form tradicional do HTML e o onSubmit [4] */}
      <Form method="post">
        
        <div style={{ marginBottom: '15px' }}>
          <label>E-mail:</label> <br />
          {/* Note que o atributo 'name' dita a chave enviada no JSON [4] */}
          <input name="email" type="email" required style={{ width: '100%', padding: '8px' }} />
        </div>

        <div style={{ marginBottom: '15px' }}>
          <label>Senha:</label> <br />
          <input name="senha" type="password" required style={{ width: '100%', padding: '8px' }} />
        </div>

        {/* Desabilita o botão enquanto aguarda o servidor (Feedback visual) [20] */}
        <button type="submit" disabled={isSubmitting} style={{ width: '100%', padding: '10px' }}>
          {isSubmitting ? 'Verificando...' : 'Entrar'}
        </button>
      </Form>
    </div>
  );
}