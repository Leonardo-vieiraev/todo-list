package br.com.leovieira.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data                                  //@Data => Cria Getters e Setters
@Entity(name="tb_tasks")               //Cria uma entidade/tabela no BD chamada "tb_tasks"
public class TaskModel {

    @Id                                 //id vira a PK da tabela
    @GeneratedValue(generator = "UUID") //Gera a PK automaticamente
    private UUID id;
    private UUID idUser;
    private String description;
    @Column(length = 50)                //Limita o tamanho de 50 caracteres para o atributo title
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    @CreationTimestamp                  //Criação da tarefa no BD
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {
        if (title.length() >50) {
            throw new Exception("O campo Title deve conter no máximo 50 caracteres");
        }   
        this.title = title;
    }
}
