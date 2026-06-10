import { Link } from 'react-router-dom';
import babelLogo from '../../assets/Babel.png';
import caixaDialog from '../../assets/caixaDialog.png';
import telaInicio from '../../assets/TelaInicio.png';
import logoBranca from '../../assets/LogoBranca.png';
import './style.css';

export default function Inicio() {
  return (
    <main className="inicio-page">
      {/* NAVBAR */}
      <header className="inicio-navbar">
        <div className="inicio-navbar-content">
          <Link to="/" className="inicio-logo-area">
            <img src={logoBranca} alt="BabelUp" className="inicio-logo" />
          </Link>

          <Link to="/login-professor" className="inicio-nav-button">
            Professor
          </Link>

          <nav className="inicio-menu">
            <Link to="/">Home</Link>
            <Link to="/suporte">Suporte</Link>
            <Link to="/cursos">Cursos</Link>
          </nav>

          <Link to="/login-aluno" className="inicio-nav-button">
            Aluno
          </Link>
        </div>
      </header>

      {/* HERO */}
      <section className="inicio-hero">
        <div className="inicio-hero-content">
          <div className="inicio-left">
            <div className="inicio-title-row">
              <h1>
                SUBA O
                <br />
                NÍVEL DO
                <br />
                SEU IDIOMA
              </h1>

              <div className="inicio-dialog-box">
                <img
                  src={caixaDialog}
                  alt="Caixa de diálogo"
                  className="inicio-dialog"
                />

                <img
                  src={babelLogo}
                  alt="BabelUp"
                  className="inicio-babel-text"
                />
              </div>
            </div>

            <div className="inicio-line" />

            <p>
              Aprenda idiomas online com aulas interativas,
              <br />
              conversas ao vivo e conteúdo personalizado.
            </p>

            <Link to="/cursos" className="inicio-main-button">
              Inscreva-se
            </Link>
          </div>

          <div className="inicio-right">
            <img
              src={telaInicio}
              alt="Aluna estudando"
              className="inicio-main-image"
            />
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="inicio-cta-section">
        <div className="inicio-cta-box">
          <h2>Pronto para começar sua jornada?</h2>
          <p>
            Junte-se a milhares de estudantes aprendendo idiomas com o BabelUp.
          </p>

          <Link to="/cursos" className="inicio-cta-button">
            Ver cursos
          </Link>
        </div>
      </section>

      {/* BENEFITS */}
      <section className="inicio-benefits-section">
        <div className="inicio-benefits-box">
          <h2>Por que escolher o BabelUp?</h2>
          <p>Plataforma completa para sua jornada de aprendizado de idiomas</p>

          <div className="inicio-benefits-grid">
            <article className="inicio-benefit-card">
              <div className="inicio-benefit-icon">📖</div>
              <h3>Aulas interativas</h3>
              <p>Vídeoaulas com legendas e exercícios práticos.</p>
            </article>

            <article className="inicio-benefit-card">
              <div className="inicio-benefit-icon">👥</div>
              <h3>Conversas ao vivo</h3>
              <p>Pratique com professores nativos e outros alunos.</p>
            </article>

            <article className="inicio-benefit-card">
              <div className="inicio-benefit-icon">🏆</div>
              <h3>Gamificação</h3>
              <p>Ganhe pontos, medalhas e dispute rankings.</p>
            </article>

            <article className="inicio-benefit-card">
              <div className="inicio-benefit-icon">💬</div>
              <h3>Comunidade</h3>
              <p>Chat, fórum e rede de suporte.</p>
            </article>
          </div>
        </div>
      </section>

      {/* FOOTER */}
      <footer className="inicio-footer">
        <div className="inicio-footer-content">
          <div className="inicio-footer-brand">
            <img src={logoBranca} alt="BabelUp" />
            <p>
              Sua plataforma para aprender idiomas online com qualidade e
              flexibilidade.
            </p>
          </div>

          <div className="inicio-footer-column">
            <h3>Links</h3>
            <Link to="/cursos">Courses</Link>
            <Link to="/suporte">Support</Link>
            <Link to="/termos">Terms</Link>
            <Link to="/privacidade">Privacy</Link>
          </div>

          <div className="inicio-footer-column">
            <h3>Contatos</h3>
            <p>contato@babelup.com</p>
            <p>+55 11 1234-5678</p>
          </div>

          <div className="inicio-footer-social">
            <h3>Follow us</h3>

            <div>
              <a href="#" aria-label="Facebook">
                f
              </a>
              <a href="#" aria-label="Instagram">
                ◎
              </a>
              <a href="#" aria-label="X">
                𝕏
              </a>
              <a href="#" aria-label="YouTube">
                ▶
              </a>
            </div>
          </div>
        </div>

        <div className="inicio-footer-copy">
          © 2026 BabelUp. All rights reserved.
        </div>
      </footer>
    </main>
  );
}