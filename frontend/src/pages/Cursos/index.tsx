import { Link } from 'react-router-dom';
import logoAzul from '../../assets/LogoAzul.png';
import './style.css';

interface Curso {
  etiqueta: string;
  titulo: string;
  descricao: string;
  tempo: string;
  aulas: string;
  exercicios: string;
  preco: string;
  nota: string;
}

const cursos: Curso[] = [
  {
    etiqueta: 'Iniciante',
    titulo: 'Inglês - Iniciante',
    descricao: 'Comece sua jornada no inglês com vocabulário e gramática básicos.',
    tempo: '3 meses',
    aulas: '120 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 299/mês',
    nota: '4.8',
  },
  {
    etiqueta: 'Básico',
    titulo: 'Inglês - Básico',
    descricao: 'Desenvolva a confiança em conversas do dia a dia e escrita básica.',
    tempo: '4 meses',
    aulas: '160 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 349/mês',
    nota: '4.7',
  },
  {
    etiqueta: 'Intermediário',
    titulo: 'Inglês - Intermediário',
    descricao: 'Desenvolva fluência em tópicos complexos e contextos profissionais.',
    tempo: '6 meses',
    aulas: '180 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 399/mês',
    nota: '4.9',
  },
  {
    etiqueta: 'Avançado',
    titulo: 'Inglês - Avançado',
    descricao: 'Domine gramática avançada, expressões idiomáticas e inglês para negócios.',
    tempo: '6 meses',
    aulas: '250 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 449/mês',
    nota: '4.9',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Espanhol - Iniciante',
    descricao: 'Aprenda o básico de espanhol com aulas voltadas a viagens e rotina cultural.',
    tempo: '3 meses',
    aulas: '100 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 299/mês',
    nota: '4.8',
  },
  {
    etiqueta: 'Intermediário',
    titulo: 'Espanhol - Intermediário',
    descricao: 'Aprimore seu espanhol com situações cotidianas e conversas do dia a dia.',
    tempo: '5 meses',
    aulas: '150 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 379/mês',
    nota: '4.7',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Francês - Iniciante',
    descricao: 'Desenvolva o início da comunicação francesa de forma clara e prática.',
    tempo: '6 meses',
    aulas: '120 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 329/mês',
    nota: '4.6',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Alemão - Iniciante',
    descricao: 'Comece a aprender alemão com aulas estruturadas e práticas.',
    tempo: '4 meses',
    aulas: '140 aulas',
    exercicios: 'Vídeo aulas + exercícios',
    preco: 'R$ 329/mês',
    nota: '4.7',
  },
];

export default function Cursos() {
  return (
    <main className="catalog-page">
      <header className="catalog-navbar">
        <div className="catalog-navbar-content">
          <Link to="/" className="catalog-logo-area">
            <img src={logoAzul} alt="Logo BabelUp" className="catalog-logo" />
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
        <div className="courses-grid">
          {cursos.map((curso) => (
            <article className="course-card" key={curso.titulo}>
              <div className="course-card-header">
                <span>{curso.etiqueta}</span>

                <div className="course-rating">
                  <span>⭐</span>
                  <strong>{curso.nota}</strong>
                </div>
              </div>

              <div className="course-card-body">
                <h2>{curso.titulo}</h2>
                <p>{curso.descricao}</p>

                <ul>
                  <li>🕒 {curso.tempo}</li>
                  <li>📚 {curso.aulas}</li>
                  <li>🖥️ {curso.exercicios}</li>
                </ul>

                <strong className="course-price">{curso.preco}</strong>

                <button type="button">Inscreva-se agora</button>
              </div>
            </article>
          ))}
        </div>

        <section className="level-test-box">
          <h2>Não tem certeza de qual nível é o ideal para você?</h2>
          <p>
            Faça nosso teste de nível gratuito e encontre o curso perfeito para suas habilidades atuais.
          </p>

          <button type="button">Faça o teste de nível</button>
        </section>
      </section>

      <footer className="catalog-footer" id="suporte">
        <div className="footer-content">
          <div className="footer-brand">
            <img src={logoAzul} alt="Logo BabelUp" />
            <p>
              Sua plataforma para aprender idiomas online com qualidade e flexibilidade.
            </p>
          </div>

          <div className="footer-column">
            <h3>Links</h3>
            <a href="#cursos">Cursos</a>
            <a href="#suporte">Suporte</a>
            <Link to="/">Home</Link>
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
              <a href="#">in</a>
            </div>
          </div>
        </div>

        <p className="footer-copy">© 2026 BabelUp. All rights reserved.</p>
      </footer>
    </main>
  );
}