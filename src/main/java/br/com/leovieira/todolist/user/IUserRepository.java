package br.com.leovieira.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

//Interface com métodos padrão da classe JpaRepository, que recebe como parâmetros a Classe Usermodel e o tipo de ID desta classe(immutable universally unique identifier (UUID))

public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);
  
}
