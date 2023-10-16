package br.com.leovieira.todolist.user;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data; //Importação da Lib Lombok
import java.time.LocalDateTime; //Importação da Lib LocalDateTime

@Data //O comando @Data, da lib Lombok, cria os métodos getters e setters
@Entity(name = "tb_Users") // Criação da entidade no BD
public class UserModel {

    //Atributos da classe UserModel
    
    @Id //Indica que o atributo "id" é a PK
    @GeneratedValue(generator = "UUID") //Spring data JPA fica responsável pela geração da PK
    private UUID id;
    //@Column(name="nome_de_usuario") => Esta anotação serviria para atribuir um nome específico para a coluna username no BD, tendo nomes diferentes na aplicação e no BD. Não será utilizada nesta aplicação.
    @Column(unique = true) //Validação de dados => Coluna "username" aceita apenas valores ainda não presentes no BD(únicos/unique = true)
    private String username;
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt; //Atributo que indica quando o registro foi adicionado ao BD

}
