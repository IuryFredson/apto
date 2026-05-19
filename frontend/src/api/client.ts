const BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

export class ApiError extends Error {
  status: number;
  body: unknown;
  serverMessage: string;

  constructor(
    status: number,
    message: string,
    serverMessage: string,
    body: unknown,
  ) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.serverMessage = serverMessage;
    this.body = body;
  }
}

/** Mensagem genérica e segura para exibir ao usuário, derivada do status HTTP. */
function mensagemGenerica(status: number): string {
  if (status === 401)
    return "Sua sessão expirou ou é inválida. Faça login novamente.";
  if (status === 403) return "Você não tem permissão para realizar esta ação.";
  if (status === 404) return "Não encontramos o que você procurava.";
  if (status === 409)
    return "A operação conflita com o estado atual. Atualize a página e tente novamente.";
  if (status === 429)
    return "Muitas tentativas em pouco tempo. Aguarde um instante e tente novamente.";
  if (status >= 400 && status < 500)
    return "Não foi possível concluir a operação. Verifique os dados e tente novamente.";
  if (status >= 500)
    return "Ocorreu um erro no servidor. Tente novamente mais tarde.";
  return "Ocorreu um erro inesperado. Tente novamente.";
}

type Query = Record<string, string | number | boolean | null | undefined>;

function buildUrl(path: string, query?: Query): string {
  const url = new URL(path.startsWith("http") ? path : BASE_URL + path);
  if (query) {
    for (const [k, v] of Object.entries(query)) {
      if (v === undefined || v === null || v === "") continue;
      url.searchParams.set(k, String(v));
    }
  }
  return url.toString();
}

async function parseError(res: Response): Promise<ApiError> {
  let body: unknown = null;
  let serverMessage = `HTTP ${res.status}`;
  try {
    const text = await res.text();
    if (text) {
      try {
        body = JSON.parse(text);
        const obj = body as {
          erro?: string;
          mensagem?: string;
          message?: string;
          error?: string;
        };
        serverMessage =
          obj.erro ?? obj.mensagem ?? obj.message ?? obj.error ?? serverMessage;
      } catch {
        body = text;
        serverMessage = text || serverMessage;
      }
    }
  } catch {
    // ignore
  }
  return new ApiError(
    res.status,
    mensagemGenerica(res.status),
    serverMessage,
    body,
  );
}

interface RequestOptions {
  query?: Query;
  body?: unknown;
  signal?: AbortSignal;
}

async function request<T>(
  method: string,
  path: string,
  opts: RequestOptions = {},
): Promise<T> {
  const init: RequestInit = {
    method,
    headers: { "Content-Type": "application/json" },
    signal: opts.signal,
  };
  if (opts.body !== undefined) {
    init.body = JSON.stringify(opts.body);
  }

  const res = await fetch(buildUrl(path, opts.query), init);
  if (!res.ok) {
    throw await parseError(res);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  const text = await res.text();
  if (!text) return undefined as T;
  return JSON.parse(text) as T;
}

export const api = {
  get: <T>(path: string, query?: Query, signal?: AbortSignal) =>
    request<T>("GET", path, { query, signal }),
  post: <T>(path: string, body?: unknown, query?: Query) =>
    request<T>("POST", path, { body, query }),
  put: <T>(path: string, body?: unknown, query?: Query) =>
    request<T>("PUT", path, { body, query }),
  patch: <T>(path: string, body?: unknown, query?: Query) =>
    request<T>("PATCH", path, { body, query }),
  delete: <T = void>(path: string, query?: Query) =>
    request<T>("DELETE", path, { query }),
};
