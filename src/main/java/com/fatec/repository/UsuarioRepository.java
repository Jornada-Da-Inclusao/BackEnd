package com.fatec.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fatec.model.Usuario;

// A interface UsuarioRepository estende JpaRepository, fornecendo métodos prontos para interação com o banco de dados.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	// Método para buscar um usuário pelo nome de usuário
	// O Spring Data JPA cria a implementação automaticamente com base no nome do método
	// Retorna um Optional<Usuario> que pode ou não conter o usuário encontrado
	public Optional<Usuario> findByUsuario(String usuario);

}
