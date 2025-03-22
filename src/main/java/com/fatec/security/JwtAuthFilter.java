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

@Component // Marca a classe como um componente gerenciado pelo Spring, ou seja, será tratada como um Bean
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService; // Serviço de JWT, responsável por extrair e validar o token

	@Autowired
	private UserDetailsServiceImpl userDetailsService; // Serviço para carregar os detalhes do usuário a partir do banco

	// Método que executa a filtragem da requisição HTTP
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization"); // Obtém o cabeçalho Authorization da requisição
		String token = null;
		String username = null;

		try {
			// Verifica se o cabeçalho Authorization existe e começa com "Bearer "
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7); // Extrai o token JWT do cabeçalho
				username = jwtService.extractUsername(token); // Extrai o nome de usuário do token
			}

			// Verifica se o nome de usuário não é nulo e se o contexto de autenticação do Spring Security está vazio
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// Carrega os detalhes do usuário (UserDetails) usando o nome de usuário
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				// Verifica se o token é válido para o usuário
				if (jwtService.validateToken(token, userDetails)) {
					// Cria um token de autenticação com os detalhes do usuário
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					// Adiciona detalhes adicionais à autenticação
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// Define o token de autenticação no contexto de segurança do Spring
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

			// Continua o filtro da requisição (passa para o próximo filtro na cadeia)
			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
				 | ResponseStatusException e) {
			// Se ocorrer uma exceção relacionada ao token (expirado, malformado, não suportado ou assinatura inválida)
			response.setStatus(HttpStatus.FORBIDDEN.value()); // Retorna um status HTTP 403 (Proibido)
			return; // Interrompe o processamento da requisição
		}
	}
}
