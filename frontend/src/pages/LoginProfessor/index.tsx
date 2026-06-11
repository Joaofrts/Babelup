import { Form, useNavigation, useActionData, redirect, Link } from 'react-router-dom';
import axios from 'axios';
import { loginProfessor, salvarSessao } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

export async function loginProfessorAction({ request }: { request: Request }) {
  const formData = await request.formData();
  const dadosLogin = Object.fromEntries(formData);

  try {
    const auth = await loginProfessor(dadosLogin as { email: FormDataEntryValue; senha: FormDataEntryValue }, request.signal);

    salvarSessao(auth);

    return redirect('/dashboard-professor');
  } catch (error: unknown) {
    if (axios.isAxiosError(error)) {
      if (error.response) {
        if (error.response.status === 401 || error.response.status === 403) {
          return { error: 'Usuário ou senha incorretos.' };
        }

        return {
          error: error.response.data?.message || 'Falha na requisição.',
        };
      }

      if (error.request) {
        throw new Response('Backend indisponível', { status: 503 });
      }
    }

    return { error: 'Erro interno no sistema.' };
  }
}

export default function LoginProfessor() {
  const navigation = useNavigation();
  const actionData = useActionData() as { error?: string } | undefined;

  const isSubmitting = navigation.state === 'submitting';

  return (
    <main className="login-page login-professor-page">
      <div className="login-overlay">
        <section className="login-card">
          <div className="login-logo-area">
            <img src={logoAzul} alt="Logo BabelUp" className="login-logo" />
          </div>

          <h1>Portal do Professor</h1>
          <p> </p>

          {actionData?.error && (
            <p className="login-error">{actionData.error}</p>
          )}

          <Form method="post" className="login-form">
            <input
              name="email"
              type="email"
              placeholder="Usuário"
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

          <Link to="/cadastro-professor" className="forgot-password">
            Criar conta de professor
          </Link>
        </section>

        <Link to="/login-admin" className="login-admin-button">
          Administrativo
        </Link>

        <Link to="/" className="login-back-button">
          Voltar
        </Link>
      </div>
    </main>
  );
}
