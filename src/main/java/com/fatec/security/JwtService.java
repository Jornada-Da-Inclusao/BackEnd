package com.fatec.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component // Anotação que marca a classe como um componente Spring, permitindo que ela seja gerenciada pelo contexto de aplicação do Spring
public class JwtService {

	// Chave secreta para assinar os tokens JWT (em ambiente real, isso deve ser mantido seguro)
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

	// Método privado para obter a chave de assinatura
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET); // Decodifica a chave secreta em base64 para obter os bytes
		return Keys.hmacShaKeyFor(keyBytes); // Cria uma chave HMAC SHA para assinar o token
	}

	// Método privado para extrair todas as claims (informações) do token JWT
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder() // Inicia o parser JWT
				.setSigningKey(getSignKey()) // Define a chave de assinatura
				.build() // Constrói o parser
				.parseClaimsJws(token) // Analisa o token JWT e extrai as claims
				.getBody(); // Retorna as claims do corpo do JWT
	}

	// Método genérico para extrair uma claim específica do token JWT, dado um resolver de claims
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token); // Extrai todas as claims do token
		return claimsResolver.apply(claims); // Aplica o resolver à claim extraída e retorna o resultado
	}

	// Método para extrair o nome de usuário (subject) do token JWT
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject); // Retorna o nome de usuário (subject) da claim
	}

	// Método para extrair a data de expiração do token JWT
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration); // Retorna a data de expiração da claim
	}

	// Método privado para verificar se o token está expirado
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); // Verifica se a data de expiração é antes da data atual
	}

	// Método para validar o token JWT
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token); // Extrai o nome de usuário do token
		// Verifica se o nome de usuário no token é igual ao nome de usuário no UserDetails e se o token não está expirado
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// Método privado para criar um token JWT com base em uma lista de claims e nome de usuário
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder() // Cria um builder para o token JWT
				.setClaims(claims) // Define as claims do token
				.setSubject(userName) // Define o nome de usuário (subject) do token
				.setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Define a data de expiração (1 hora a partir da emissão)
				.signWith(getSignKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta e o algoritmo HS256
				.compact(); // Compacta o token em formato JWT
	}

	// Método público para gerar um token JWT para um usuário
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>(); // Cria um mapa para armazenar as claims (informações) do token
		return createToken(claims, userName); // Cria o token com as claims e o nome de usuário
	}
}
