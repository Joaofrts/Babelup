import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import logoBranca from '../../assets/LogoBranca.png';
import { API } from '../../services/api';
import './style.css';

interface CursoCatalogo {
  id: string;
  titulo: string;
  descricao: string;
  precoMensal: number;
}

export default function Cursos() {
  const [cursos, setCursos] = useState<CursoCatalogo[]>([]);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    async function buscarCursos() {
      try {
        setCarregando(true);
        setErro('');

        const response = await API.get('/catalogo/cursos');

        setCursos(response.data);
      } catch (error) {
        console.error('Erro ao buscar cursos:', error);
        setErro('Não foi possível carregar os cursos cadastrados.');
      } finally {
        setCarregando(false);
      }
    }

    buscarCursos();
  }, []);

  function formatarPreco(valor: number) {
    return valor.toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
  }

  return (
    <main className="catalog-page">
      <header className="catalog-navbar">
        <div className="catalog-navbar-content">
          <Link to="/" className="catalog-logo-area">
            <img src={logoBranca} alt="Logo BabelUp" className="catalog-logo" />
          </Link>

          <nav className="catalog-menu">
            <Link to="/">Home</Link>
            <a href="#suporte">Suporte</a>
            <a href="#cursos">Cursos</a>
          </nav>

          <Link to="/" className="catalog-login-button">
            Login
          </Link>
        </div>
      </header>

      <section className="catalog-hero" id="cursos">
        <h1>Nossos cursos</h1>
        <p>Escolha o curso perfeito para sua jornada de aprendizado de idiomas.</p>
      </section>

      <section className="catalog-content">
        {carregando && (
          <p className="catalog-status">Carregando cursos...</p>
        )}

        {erro && !carregando && (
          <p className="catalog-status catalog-error">{erro}</p>
        )}

        {!carregando && !erro && cursos.length === 0 && (
          <p className="catalog-status">Nenhum curso cadastrado no momento.</p>
        )}

        {!carregando && !erro && cursos.length > 0 && (
          <div className="courses-grid">
            {cursos.map((curso) => (
              <article className="course-card" key={curso.id}>
                <div className="course-card-header">
                  <div className="course-card-top">
                    <span>Curso</span>
                  </div>

                  <h2>{curso.titulo}</h2>
                </div>

                <div className="course-card-body">
                  <p>{curso.descricao}</p>

                  <strong className="course-price">
                    {formatarPreco(Number(curso.precoMensal))}
                  </strong>

                  <button type="button">Inscreva-se agora</button>
                </div>
              </article>
            ))}
          </div>
        )}

        <section className="level-test-box">
          <h2>Não tem certeza de qual nível é o ideal para você?</h2>
          <p>
            Faça nosso teste de nível gratuito e encontre o curso perfeito para suas habilidades atuais.
          </p>

          <button type="button">Agende seu teste</button>
        </section>
      </section>

      <footer className="catalog-footer" id="suporte">
        <div className="footer-content">
          <div className="footer-brand">
            <img src={logoBranca} alt="Logo BabelUp" />
            <p>
              Sua plataforma para aprender idiomas online com qualidade e flexibilidade.
            </p>
          </div>

          <div className="footer-column">
            <h3>Links</h3>
            <a href="#cursos">Courses</a>
            <a href="#suporte">Support</a>
            <Link to="/">Terms</Link>
            <Link to="/">Privacy</Link>
          </div>

          <div className="footer-column">
            <h3>Contatos</h3>
            <p>contato@babelup.com</p>
            <p>+55 11 1234-5678</p>
          </div>

          <div className="footer-social">
            <h3>Follow us</h3>

            <div>
              <a href="#">f</a>
              <a href="#">◎</a>
              <a href="#">𝕏</a>
              <a href="#">▶</a>
            </div>
          </div>
        </div>

        <p className="footer-copy">© 2026 BabelUp. All rights reserved.</p>
      </footer>
    </main>
  );
}