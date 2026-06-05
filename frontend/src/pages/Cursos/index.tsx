import { Link } from 'react-router-dom';
import logoBranca from '../../assets/LogoBranca.png';
import './style.css';

interface Curso {
  etiqueta: string;
  titulo: string;
  descricao: string;
  duracao: string;
  alunos: string;
  conteudo: string;
  preco: string;
  nota: string;
}

const cursos: Curso[] = [
  {
    etiqueta: 'Iniciante',
    titulo: 'Inglês - Iniciante',
    descricao: 'Comece sua jornada no inglês com vocabulário e gramática básicos.',
    duracao: '3 meses',
    alunos: '1250 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 299/mês',
    nota: '4.8',
  },
  {
    etiqueta: 'Básico',
    titulo: 'Inglês - Básico',
    descricao: 'Desenvolva a confiança em conversas do dia a dia e na escrita básica.',
    duracao: '4 meses',
    alunos: '980 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 349/mês',
    nota: '4.7',
  },
  {
    etiqueta: 'Intermediário',
    titulo: 'Inglês - Intermediário',
    descricao: 'Desenvolver fluência em tópicos complexos e contextos profissionais.',
    duracao: '6 meses',
    alunos: '1500 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 399/mês',
    nota: '4.9',
  },
  {
    etiqueta: 'Avançado',
    titulo: 'Inglês - Avançado',
    descricao: 'Domine gramática avançada, expressões idiomáticas e inglês para negócios.',
    duracao: '6 meses',
    alunos: '750 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 449/mês',
    nota: '4.9',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Espanhol - Iniciante',
    descricao: 'Aprenda o básico de espanhol com falantes nativos e vivencie uma imersão cultural.',
    duracao: '3 meses',
    alunos: '890 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 299/mês',
    nota: '4.8',
  },
  {
    etiqueta: 'Intermediário',
    titulo: 'Espanhol - Intermediário',
    descricao: 'Aprimore seu espanhol com situações e conversas do dia a dia.',
    duracao: '5 meses',
    alunos: '650 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 379/mês',
    nota: '4.7',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Francês - Iniciante',
    descricao: 'Descubra a língua e a cultura francesa do zero.',
    duracao: '4 meses',
    alunos: '520 alunos',
    conteudo: 'Vídeo aulas + exercícios',
    preco: 'R$ 329/mês',
    nota: '4.6',
  },
  {
    etiqueta: 'Iniciante',
    titulo: 'Alemão - Iniciante',
    descricao: 'Comece a aprender alemão com aulas estruturadas e prática.',
    duracao: '4 meses',
    alunos: '430 alunos',
    conteudo: 'Vídeo aulas + exercícios',
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
        <div className="courses-grid">
          {cursos.map((curso) => (
            <article className="course-card" key={curso.titulo}>
              <div className="course-card-header">
                <div className="course-card-top">
                  <span>{curso.etiqueta}</span>

                  <div className="course-rating">
                    <span>⭐</span>
                    <strong>{curso.nota}</strong>
                  </div>
                </div>

                <h2>{curso.titulo}</h2>
              </div>

              <div className="course-card-body">
                <p>{curso.descricao}</p>

                <ul>
                  <li>🕒 {curso.duracao}</li>
                  <li>👥 {curso.alunos}</li>
                  <li>📖 {curso.conteudo}</li>
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