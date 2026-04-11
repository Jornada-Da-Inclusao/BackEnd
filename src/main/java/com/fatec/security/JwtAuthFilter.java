package com.fatec.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService; // Serviço para gerar e validar o JWT

	@Autowired
	private UserDetailsServiceImpl userDetailsService; // Serviço para buscar os detalhes do usuário

	// Método para filtrar as requisições e adicionar o token JWT ao contexto de segurança
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization"); // Obtém o cabeçalho de autorização
		String token = null;
		String username = null;

		try {
			// Verifica se o cabeçalho Authorization contém um token válido
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7); // Extrai o token JWT do cabeçalho
				username = jwtService.extractUserId(token); // Extrai o nome de usuário do token
			}

			// Verifica se o token e o nome de usuário são válidos
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// Valida o token para o usuário
				if (jwtService.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken); // Define a autenticação no contexto de segurança
				}
			}

			// Continua a execução da cadeia de filtros
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
			// Captura erros relacionados ao token JWT e retorna uma resposta 403 (Proibido)
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().write("Token JWT inválido ou expirado.");
			return;
		} catch (ResponseStatusException e) {
			// Caso uma exceção do tipo ResponseStatusException ocorra
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.getWriter().write("Erro de autenticação: " + e.getMessage());
			return;
		}
	}
}