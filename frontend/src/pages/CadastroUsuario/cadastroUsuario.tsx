import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import type { FormEvent } from 'react';
import axios from 'axios';
import {
  cadastrarAdmin,
  cadastrarAluno,
  cadastrarProfessor,
  type PerfilUsuario,
} from '../../services/babelup';
import logoAzul from '../../assets/LogoAzul.png';
import '../LoginAluno/style.css';

interface CadastroUsuarioProps {
  perfil: PerfilUsuario;
}

const perfilConfig = {
  ALUNO: {
    titulo: 'Cadastro de Aluno',
    loginPath: '/login-aluno',
  },
  PROFESSOR: {
    titulo: 'Cadastro de Professor',
    loginPath: '/login-professor',
  },
  ADMIN: {
    titulo: 'Cadastro de Administrador',
    loginPath: '/login-admin',
  },
};

export default function CadastroUsuario({ perfil }: CadastroUsuarioProps) {
  const navigate = useNavigate();
  const config = perfilConfig[perfil];
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmarSenha, setConfirmarSenha] = useState('');
  const [telefone, setTelefone] = useState('');
  const [menorIdade, setMenorIdade] = useState(false);
  const [dadosResponsaveis, setDadosResponsaveis] = useState('');
  const [aceiteTermos, setAceiteTermos] = useState(false);
  const [erro, setErro] = useState('');
  const [salvando, setSalvando] = useState(false);

  async function cadastrar(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setErro('');

    if (!nome.trim() || !email.trim() || !senha.trim()) {
      setErro('Informe nome, e-mail e senha.');
      return;
    }

    if (senha !== confirmarSenha) {
      setErro('As senhas nao conferem.');
      return;
    }

    if (perfil === 'ALUNO' && !aceiteTermos) {
      setErro('Aceite os termos para concluir o cadastro.');
      return;
    }

    try {
      setSalvando(true);

      if (perfil === 'ALUNO') {
        await cadastrarAluno({
          nome: nome.trim(),
          email: email.trim(),
          senha,
          telefone: telefone.trim() || null,
          aceiteTermos,
          menorIdade,
          dadosResponsaveis: dadosResponsaveis.trim() || null,
        });
      } else if (perfil === 'PROFESSOR') {
        await cadastrarProfessor({
          nome: nome.trim(),
          email: email.trim(),
          senha,
          telefone: telefone.trim() || null,
        });
      } else {
        await cadastrarAdmin({
          nome: nome.trim(),
          email: email.trim(),
          senha,
          telefone: telefone.trim() || null,
        });
      }

      navigate(config.loginPath);
    } catch (error: unknown) {
      if (axios.isAxiosError(error)) {
        setErro(
          typeof error.response?.data === 'string'
            ? error.response.data
            : 'Nao foi possivel concluir o cadastro.'
        );
        return;
      }

      setErro('Erro interno no cadastro.');
    } finally {
      setSalvando(false);
    }
  }

  return (
    <main className="login-page">
      <div className="login-overlay">
        <section className="login-card">
          <div className="login-logo-area">
            <img src={logoAzul} alt="Logo BabelUp" className="login-logo" />
          </div>

          <h1>{config.titulo}</h1>

          {erro && <p className="login-error">{erro}</p>}

          <form className="login-form" onSubmit={cadastrar}>
            <input
              type="text"
              placeholder="Nome"
              value={nome}
              onChange={(event) => setNome(event.target.value)}
              required
            />

            <input
              type="email"
              placeholder="E-mail"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />

            <input
              type="text"
              placeholder="Telefone"
              value={telefone}
              onChange={(event) => setTelefone(event.target.value)}
            />

            <input
              type="password"
              placeholder="Senha"
              value={senha}
              onChange={(event) => setSenha(event.target.value)}
              required
            />

            <input
              type="password"
              placeholder="Confirmar senha"
              value={confirmarSenha}
              onChange={(event) => setConfirmarSenha(event.target.value)}
              required
            />

            {perfil === 'ALUNO' && (
              <>
                <label className="forgot-password">
                  <input
                    type="checkbox"
                    checked={menorIdade}
                    onChange={(event) => setMenorIdade(event.target.checked)}
                  />
                  Menor de idade
                </label>

                {menorIdade && (
                  <input
                    type="text"
                    placeholder="Dados do responsavel"
                    value={dadosResponsaveis}
                    onChange={(event) => setDadosResponsaveis(event.target.value)}
                  />
                )}

                <label className="forgot-password">
                  <input
                    type="checkbox"
                    checked={aceiteTermos}
                    onChange={(event) => setAceiteTermos(event.target.checked)}
                  />
                  Aceito os termos
                </label>
              </>
            )}

            <button type="submit" disabled={salvando}>
              {salvando ? 'Cadastrando...' : 'Cadastrar'}
            </button>
          </form>
        </section>

        <Link to={config.loginPath} className="login-back-button">
          Voltar
        </Link>
      </div>
    </main>
  );
}
