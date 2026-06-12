import { Form, useNavigation, useActionData, redirect, Link } from 'react-router-dom';
import axios from 'axios';
import { loginAdmin, salvarSessao } from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

export async function loginAdminAction({ request }: { request: Request }) {
  const formData = await request.formData();
  const dadosLogin = Object.fromEntries(formData);

  try {
    const auth = await loginAdmin(dadosLogin as { email: FormDataEntryValue; senha: FormDataEntryValue }, request.signal);

    salvarSessao(auth);

    return redirect('/admin/agendamentos');
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

export default function LoginAdmin() {
  const navigation = useNavigation();
  const actionData = useActionData() as { error?: string } | undefined;

  const isSubmitting = navigation.state === 'submitting';

  return (
    <main className="login-admin-page">
      <div className="login-admin-overlay">
        <section className="login-admin-card">
          <div className="login-admin-logo-area">
            <img src={logoAzul} alt="Logo BabelUp" className="login-admin-logo" />
          </div>

          <h1>Portal do Administrativo</h1>
          <p> </p>

          {actionData?.error && (
            <p className="login-admin-error">{actionData.error}</p>
          )}

          <Form method="post" className="login-admin-form">
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

          <a href="#" className="login-admin-forgot-password">
            Esqueceu sua senha?
          </a>

          
        </section>

        <Link to="/login-professor" className="login-admin-back-button">
          Voltar
        </Link>
      </div>
    </main>
  );
}
