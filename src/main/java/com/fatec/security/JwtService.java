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
				.setSigningKey(getSignKey()) // Define a chave usada para verificar a assinatura do token
				.build()
				.parseClaimsJws(token) // Faz o parsing do token JWT
				.getBody(); // Retorna o corpo do token, que contém as claims
	}

	/**
	 * Método genérico para extrair uma claim específica do token
	 * - Usa uma função (Function<Claims, T>) para aplicar uma extração personalizada
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token); // Obtém todas as claims do token
		return claimsResolver.apply(claims); // Aplica a função para extrair a claim específica desejada
	}

	/**
	 * Método para extrair o nome de usuário (subject) do token JWT
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject); // Obtém a claim "subject" (nome do usuário)
	}

	/**
	 * Método para extrair a data de expiração do token JWT
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration); // Obtém a claim "exp" (data de expiração)
	}

	/**
	 * Método privado para verificar se o token está expirado
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date()); // Verifica se a expiração é anterior à data atual
	}

	/**
	 * Método para validar um token JWT
	 * - Compara o nome de usuário extraído com o nome do usuário fornecido
	 * - Verifica se o token ainda é válido (não expirado)
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token); // Obtém o nome de usuário do token
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Retorna true se for válido
	}

	/**
	 * Método privado para criar um token JWT com base em claims e no nome de usuário
	 */
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims) // Adiciona as claims ao token
				.setSubject(userName) // Define o usuário como "subject" no token
				.setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Expira em 1 hora
				.signWith(getSignKey(), SignatureAlgorithm.HS256) // Assina o token usando a chave secreta e algoritmo HS256
				.compact(); // Gera o token JWT final
	}

	/**
	 * Método público para gerar um token JWT para um usuário
	 */
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>(); // Cria um mapa para armazenar claims adicionais
		return createToken(claims, userName); // Cria e retorna o token
	}
}
