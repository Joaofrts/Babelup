import { Form, useNavigation, useActionData, redirect, Link } from 'react-router-dom';
import axios from 'axios';
import { loginAluno, salvarSessao } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

// 1. A Ação: Roda nos bastidores quando o formulário é enviado
export async function loginAction({ request }: { request: Request }) {
  // Pega os dados do formulário nativo do HTML
  const formData = await request.formData();
  const dadosLogin = Object.fromEntries(formData);

  try {
    // Envia os dados para o backend
    const auth = await loginAluno(dadosLogin as { email: FormDataEntryValue; senha: FormDataEntryValue }, request.signal);

    // Sucesso: salva os tokens e redireciona
    salvarSessao(auth);

    return redirect('/dashboard-aluno');
  } catch (error: unknown) {
    // Verifica se o erro foi gerado pelo Axios
    if (axios.isAxiosError(error)) {
      if (error.response) {
        // Erro de login inválido
        if (error.response.status === 401 || error.response.status === 403) {
          return { error: 'E-mail ou senha incorretos.' };
        }

        // Mensagem customizada vinda do backend
        return {
          error: error.response.data?.message || 'Falha na requisição.',
        };
      }

      if (error.request) {
        // Backend indisponível
        throw new Response('Backend indisponível', { status: 503 });
      }
    }

    // Erro genérico
    return { error: 'Erro interno no sistema.' };
  }
}

// 2. Tela de Login
export default function Login() {
  const navigation = useNavigation();
  const actionData = useActionData() as { error?: string } | undefined;

  const isSubmitting = navigation.state === 'submitting';

  return (
    <main className="login-page">
      <div className="login-overlay">
        <section className="login-card">
          <div className="login-logo-area">
            <img src={logoAzul} alt="Logo BabelUp" className="login-logo" />

          </div>

          <h1>Portal do Aluno</h1>
          <p> </p>

          {actionData?.error && (
            <p className="login-error">{actionData.error}</p>
          )}

          <Form method="post" className="login-form">
            <input
              name="email"
              type="email"
              placeholder="E-mail"
              required
            />

            <input
              name="senha"
              type="password"
              placeholder="Senha"
              required
            />

            <button type="submit" disabled={isSubmitting}>
              {isSubmitting ? 'Verificando...' : 'Acessar'}
            </button>
          </Form>

          <a href="#" className="forgot-password">
            Esqueceu sua senha?
          </a>

          <Link to="/cadastro-aluno" className="forgot-password">
            Criar conta de aluno
          </Link>
        </section>
        <Link to="/" className="login-back-button">
          Voltar
        </Link>
      </div>
    </main>
  );
}
