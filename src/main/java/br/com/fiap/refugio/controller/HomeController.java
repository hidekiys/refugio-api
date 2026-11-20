package br.com.fiap.refugio.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Página inicial de status da API e health check.
 *
 * <p>Não dependem do banco de dados — servem para confirmar que o deploy
 * está no ar (ex.: na URL pública do render.com) mesmo que a conexão com o
 * Oracle ainda não esteja configurada.</p>
 */
@RestController
public class HomeController {

    /** Página de status (HTML) na raiz. */
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String home() {
        return """
            <!doctype html>
            <html lang="pt-br">
            <head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>Refúgio API</title>
              <style>
                :root { color-scheme: light dark; }
                body { font-family: system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
                       max-width: 680px; margin: 48px auto; padding: 0 16px; line-height: 1.5; }
                h1 { margin-bottom: 4px; }
                .tag { display:inline-block; background:#e8f5e9; color:#1b5e20;
                       border-radius:12px; padding:2px 10px; font-size:.85rem; font-weight:600; }
                code { background: rgba(127,127,127,.15); padding: 1px 6px; border-radius: 4px; }
                ul { padding-left: 20px; }
                footer { margin-top: 32px; font-size: .85rem; opacity: .7; }
              </style>
            </head>
            <body>
              <h1>Refúgio API</h1>
              <p><span class="tag">● no ar</span></p>
              <p>API REST do <strong>Refúgio</strong> — sistema de alerta de calor por bairro
                 (CitizenTech · Global Solution FIAP 2026.2). Spring Boot + JDBC + Oracle.</p>

              <h2>Endpoints</h2>
              <ul>
                <li><code>GET /health</code> — status da aplicação</li>
                <li><code>GET /api/cidadaos</code> — cidadãos (CRUD)</li>
                <li><code>GET /api/pontos-refresco</code> — pontos de refresco (CRUD)</li>
                <li><code>GET /api/alertas</code> — alertas de calor (CRUD)</li>
                <li><code>GET /api/checkins</code> — check-ins "Tô bem" (CRUD + escalada)</li>
              </ul>

              <footer>
                Os endpoints <code>/api/**</code> exigem conexão com o Oracle
                (variáveis <code>DB_URL</code>, <code>DB_USERNAME</code>, <code>DB_PASSWORD</code>).
              </footer>
            </body>
            </html>
            """;
    }

    /** Health check simples (JSON), não acessa o banco. */
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "app", "refugio-api",
            "solucao", "Refúgio - alerta de calor (GS FIAP 2026.2)"
        );
    }
}
