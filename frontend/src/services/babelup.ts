import { API, API_PUBLIC } from './api';

export type PerfilUsuario = 'ALUNO' | 'PROFESSOR' | 'ADMIN';

export interface LoginPayload {
  email: FormDataEntryValue;
  senha: FormDataEntryValue;
}

export interface AuthResponse {
  mensagem: string;
  token: string;
  refreshToken: string;
  perfil: PerfilUsuario;
}

export interface UsuarioDTO {
  id?: string;
  nome?: string;
  email?: string;
  perfil?: string;
  curso?: string | null;
}

export interface PerfilAlunoDTO {
  nome?: string;
  email?: string;
  nivelAtual?: string;
  progressoGeral?: number;
  nivelId?: string | null;
  curso?: string | null;
}

export interface CursoCatalogoDTO {
  id: string;
  titulo: string;
  descricao: string;
  precoMensal: number;
}

export interface NivelDTO {
  id: string;
  idioma?: string;
  nome?: string;
  cargaHorariaEstimada?: number;
  descricao?: string;
  ordem?: number;
  modulos?: ModuloDTO[];
}

export interface ModuloDTO {
  id: string;
  titulo?: string;
  descricao?: string;
  videoaulas?: VideoAulaDTO[];
  url_pdf?: MaterialApoioDTO[];
  nivel_id?: string;
  nivelId?: string;
  carga_horaria_minima?: number;
  cargaHorariaMinima?: number;
  ordem?: number;
}

export interface VideoAulaDTO {
  id: string;
  titulo: string;
  url: string;
  duracao?: number;
  tipo?: 'GRAVADO' | 'AO_VIVO' | string;
  modulo_id?: string;
}

export interface MaterialApoioDTO {
  id: string;
  titulo: string;
  url_pdf: string;
  modulo_id?: string;
}

export interface ExercicioDTO {
  id: string;
  enunciado: string;
  alternativas: string;
  videoaula_id?: string;
}

export interface SessaoDTO {
  id: string;
  data_hora?: string;
  professor_id?: string;
  modulo_id?: string | null;
  aluno_ids?: string[];
  qtd_alunos?: number;
  max_alunos?: number;
  tipo_sessao?: string;
  modalidade?: string;
  status?: string;
  gravacao_url?: string | null;
}

export interface RankingDTO {
  id: string;
  aluno_id: string;
  aluno: string;
  mes: number;
  ano: number;
  posicao: number;
  pontuacao: number;
}

export interface CriarProfessorPayload {
  nome: string;
  email: string;
  senha: string;
  telefone?: string | null;
}

export interface CriarAlunoPayload extends CriarProfessorPayload {
  aceiteTermos: boolean;
  menorIdade: boolean;
  dadosResponsaveis?: string | null;
}

export interface CriarNivelPayload {
  idioma: string;
  nome: string;
  ordem: number;
  carga_horaria: number;
  descricao: string;
  preco_mensal: number;
}

export interface CriarModuloPayload {
  nivel_id: string;
  titulo: string;
  descricao?: string;
  ordem?: number;
  carga_horaria_minima?: number;
}

export interface AtualizarNivelPayload {
  nome:string;
  descricao: string;
  carga_horaria: number;
}

export interface AtualizarUsuarioPayload{
  nome: string;
  email: string;
}

export interface AtualizarModuloPayload {
  titulo: string;
  descricao?: string;
  ordem?: number;
  carga_horaria_minima?: number;
}

export interface CriarVideoAulaPayload {
  modulo_id: string;
  titulo: string;
  url: string;
  duracao: number;
  tipo: 'GRAVADO' | 'AO_VIVO';
  data_transmissao?: string | null;
}

export interface CriarMaterialPayload {
  modulo_id: string;
  titulo: string;
  url_pdf: string;
}

export interface CriarExercicioPayload {
  videoaula_id: string;
  enunciado: string;
  alternativas: string[];
  resposta_correta: string;
}

export interface CriarAgendamentoPayload {
  professor_id: string;
  modulo_id?: string | null;
  tipo_sessao: 'PRATICA_MODULO' | 'NIVELAMENTO_INICIAL' | 'AVALIACAO_FINAL_NIVEL';
  modalidade: 'INDIVIDUAL' | 'GRUPO';
  data_hora: string;
  max_alunos: number;
}

export function normalizarLista<T>(data: unknown): T[] {
  if (Array.isArray(data)) return data as T[];

  if (
    data &&
    typeof data === 'object' &&
    'content' in data &&
    Array.isArray((data as { content: unknown }).content)
  ) {
    return (data as { content: T[] }).content;
  }

  if (
    data &&
    typeof data === 'object' &&
    'data' in data &&
    Array.isArray((data as { data: unknown }).data)
  ) {
    return (data as { data: T[] }).data;
  }

  return [];
}


export function salvarSessao(auth: AuthResponse) {
  localStorage.setItem('token', auth.token);
  localStorage.setItem('refreshToken', auth.refreshToken);
  localStorage.setItem('perfil', auth.perfil);
}

export function limparSessao() {
  localStorage.removeItem('token');
  localStorage.removeItem('refreshToken');
  localStorage.removeItem('perfil');
  localStorage.removeItem('usuarioLogado');
}

export function pegarTokenPayload() {
  const token = localStorage.getItem('token');
  if (!token) return null;

  try {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(payloadJson) as { sub?: string; email?: string; nome?: string; perfil?: string };
  } catch {
    return null;
  }
}

export async function loginAluno(payload: LoginPayload, signal?: AbortSignal) {
  const response = await API_PUBLIC.post<AuthResponse>('/autenticacao/login/aluno', payload, { signal });
  return response.data;
}

export async function loginProfessor(payload: LoginPayload, signal?: AbortSignal) {
  const response = await API_PUBLIC.post<AuthResponse>('/autenticacao/login/professor', payload, { signal });
  return response.data;
}

export async function loginAdmin(payload: LoginPayload, signal?: AbortSignal) {
  const response = await API_PUBLIC.post<AuthResponse>('/autenticacao/login/adm', payload, { signal });
  return response.data;
}

export async function buscarPerfilAluno(signal?: AbortSignal) {
  const response = await API.get<PerfilAlunoDTO>('/alunos/meu-perfil', { signal });
  return response.data;
}

export async function listarCursosCatalogo(signal?: AbortSignal) {
  const response = await API_PUBLIC.get<CursoCatalogoDTO[]>('/catalogo/cursos', { signal });
  return normalizarLista<CursoCatalogoDTO>(response.data);
}

export async function listarUsuarios(signal?: AbortSignal) {
  const response = await API.get<UsuarioDTO[]>('/admin/listarUsuarios', { signal });
  return normalizarLista<UsuarioDTO>(response.data);
}

export async function cadastrarProfessor(payload: CriarProfessorPayload) {
  const response = await API_PUBLIC.post('/admin/cadastroProfessor', payload);
  return response.data;
}

export async function cadastrarAdmin(payload: CriarProfessorPayload) {
  const response = await API_PUBLIC.post('/admin/cadastro', payload);
  return response.data;
}

export async function cadastrarAluno(payload: CriarAlunoPayload) {
  const response = await API_PUBLIC.post('/alunos/cadastro', {
    ...payload,
    perfil: 'ALUNO',
  });
  return response.data;
}

export async function listarNiveis(signal?: AbortSignal) {
  const response = await API.get<NivelDTO[]>('/niveis', { signal });
  return normalizarLista<NivelDTO>(response.data);
}

export async function obterNivel(id: string, signal?: AbortSignal) {
  const response = await API.get<NivelDTO>(`/niveis/${id}`, { signal });
  return response.data;
}

export async function criarNivel(payload: CriarNivelPayload) {
  const response = await API.post<NivelDTO>('/admin/niveis/criar', payload);
  return response.data;
}

export async function atualizarNivel(id: string, payload: AtualizarNivelPayload) {
  const response = await API.put<NivelDTO>(`/admin/niveis/${id}`, payload);
  return response.data;
}

export async function atualizarUsuario(id: string, payload: AtualizarUsuarioPayload){
  const response = await API.put<UsuarioDTO>(`/admin/usuario/${id}`,payload);
  return response.data;
}

export async function deletarNivel(id: string) {
  await API.delete(`/admin/niveis/${id}`);
}

export async function listarModulos(signal?: AbortSignal) {
  const response = await API.get<ModuloDTO[]>('/modulos/listar', { signal });
  return normalizarLista<ModuloDTO>(response.data);
}

export async function criarModulo(payload: CriarModuloPayload) {
  const response = await API.post<ModuloDTO>('/admin/modulo/criar', payload);
  return response.data;
}

export async function listarModulosPorNivel(nivelId: string, signal?: AbortSignal) {
  const response = await API.get<ModuloDTO[]>(`/modulos/nivel/${nivelId}`, { signal });
  return normalizarLista<ModuloDTO>(response.data);
}

export async function atualizarModulo(id: string, payload: AtualizarModuloPayload) {
  const response = await API.put<ModuloDTO>(`/admin/modulo/${id}`, payload);
  return response.data;
}

export async function deletarModulo(id: string) {
  await API.delete(`/admin/modulo/${id}`);
}

export async function listarVideoAulasPorModulo(moduloId: string, signal?: AbortSignal) {
  const response = await API.get<VideoAulaDTO[]>(`/conteudos/modulos/${moduloId}/videoaulas`, { signal });
  return normalizarLista<VideoAulaDTO>(response.data);
}

export async function criarVideoAula(payload: CriarVideoAulaPayload) {
  const response = await API.post<VideoAulaDTO>('/conteudos/videoaulas', payload);
  return response.data;
}

export async function listarMateriaisPorModulo(moduloId: string, signal?: AbortSignal) {
  const response = await API.get<MaterialApoioDTO[]>(`/conteudos/modulos/${moduloId}/materiais`, { signal });
  return normalizarLista<MaterialApoioDTO>(response.data);
}

export async function criarMaterialApoio(payload: CriarMaterialPayload) {
  const response = await API.post<MaterialApoioDTO>('/conteudos/materiais', payload);
  return response.data;
}

export async function listarExerciciosPorVideoAula(videoAulaId: string, signal?: AbortSignal) {
  const response = await API.get<ExercicioDTO[]>(`/conteudos/videoaulas/${videoAulaId}/exercicios`, { signal });
  return normalizarLista<ExercicioDTO>(response.data);
}

export async function criarExercicio(payload: CriarExercicioPayload) {
  const response = await API.post<ExercicioDTO>('/conteudos/exercicios', payload);
  return response.data;
}

export async function listarAgendamentos(signal?: AbortSignal) {
  const response = await API.get<SessaoDTO[]>('/agendamentos', { signal });
  return normalizarLista<SessaoDTO>(response.data);
}

export async function criarAgendamento(payload: CriarAgendamentoPayload) {
  const response = await API.post<SessaoDTO>('/agendamentos', payload);
  return response.data;
}

export async function atualizarAgendamentoData(id: string, novaDataHora: string) {
  const response = await API.put<SessaoDTO>(`/agendamentos/${id}`, null, {
    params: { novaDataHora },
  });
  return response.data;
}

export async function finalizarAgendamento(id: string, gravacaoUrl: string) {
  const response = await API.post<SessaoDTO>(`/agendamentos/${id}/finalizar`, {
    gravacao_url: gravacaoUrl,
  });
  return response.data;
}

export async function deletarAgendamento(id: string) {
  await API.delete(`/agendamentos/${id}`);
}

export async function inscreverAlunoAgendamento(id: string, alunoId: string) {
  const response = await API.post<SessaoDTO>(`/agendamentos/${id}/alunos/${alunoId}`);
  return response.data;
}

export async function removerAlunoAgendamento(id: string, alunoId: string) {
  await API.delete(`/agendamentos/${id}/aluno/${alunoId}`);
}

export async function listarRanking(mes: number, ano: number, signal?: AbortSignal) {
  const response = await API.get<RankingDTO[]>('/gamificacao/ranking', {
    params: { mes, ano },
    signal,
  });
  return normalizarLista<RankingDTO>(response.data);
}

