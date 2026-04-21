package com.fatec.security;

// Importação das classes necessárias
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
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

// Define a classe como um "componente" do Spring, permitindo que seja injetada em outras partes do código
@Component
public class JwtService {

	// Chave secreta para assinar o token JWT, gerada dinamicamente para maior segurança
	private static final String SECRET = generateSecretKey();

	/**
	 * Método para gerar uma chave secreta segura
	 * - Gera 32 bytes (256 bits) aleatórios para garantir segurança
	 * - Converte para Base64 para compatibilidade com JWT
	 */
	private static String generateSecretKey() {
		byte[] keyBytes = new byte[32]; // Define um array de 32 bytes para a chave
		new SecureRandom().nextBytes(keyBytes); // Preenche o array com valores aleatórios criptograficamente seguros
		return Base64.getEncoder().encodeToString(keyBytes); // Retorna a chave codificada em Base64
	}

	/**
	 * Método para obter a chave de assinatura do JWT
	 * - Decodifica a chave Base64 para bytes
	 * - Retorna uma chave HMAC-SHA256 compatível com JWT
	 */
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET); // Decodifica a chave secreta armazenada
		return Keys.hmacShaKeyFor(keyBytes); // Cria uma chave HMAC-SHA256 usando os bytes gerados
	}

	/**
	 * Método para extrair todas as "claims" (informações contidas no token JWT)
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Método genérico para extrair uma claim específica do token
	 * - Usa uma função (Function<Claims, T>) para aplicar uma extração personalizada
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}


	public Long extractUserId(String token) {
		return Long.parseLong(extractClaim(token, Claims::getSubject));
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}


	public Boolean validateToken(String token, UserDetails userDetails) {
		Long userIdFromToken = extractUserId(token);

		Long userIdFromUser = ((UserDetailsImpl) userDetails).getId();

		return userIdFromToken.equals(userIdFromUser) && !isTokenExpired(token);
	}


	private String createToken(Map<String, Object> claims, Long idUser) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(String.valueOf(idUser))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateToken(Long idUser) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, idUser);
	}
}
