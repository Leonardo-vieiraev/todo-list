package br.com.leovieira.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.leovieira.todolist.utils.Utils;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

@RestController 
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("Rota task controller");
        var idUser = request.getAttribute("idUser"); //Armazena o atributo "id" do usuário que veio da requisição(request)
        taskModel.setIdUser((UUID) idUser); // Seta o id do usuário automaticamente na requisição
        var currentDate = LocalDateTime.now(); // Data atual

        /*
        var title = taskModel.getTitle();

        if (title.length() > 50) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Título maior do que o permitido");
        }
        */

        if (currentDate.isAfter(taskModel.getStartAt())) { // Se a data atual for maior do que a data de início da tarefa.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início da tarefa deve ser maior ou igual a data atual");
        }

        if (taskModel.getEndAt().isBefore((taskModel.getStartAt()))) { // Se a data de final da tarefa for menor do que a data de início da tarefa.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de fim da tarefa deve ser maior ou igual a data de início");
        }

        var task = this.taskRepository.save(taskModel);
        //return task;
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        
        var task = this.taskRepository.findById(id).orElse(null); //Variável com a task que tem o id buscado
        
        if (task == null) { //Se a tarefa não for encontrada

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser"); //Obtém o id do usuário que está fazendo a requisição

        if (!task.getIdUser().equals(idUser)) { //Se o id do usuário que criou a tarefa não for igual ao id do usuário que está fazendo a requisição

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão para alterar a tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task); //Mescla o objeto do banco de dados e o objeto após a alteração(Possivelmente com propriedades nulas)
        var taskUdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(this.taskRepository.save(taskUdated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(HttpServletRequest request, @PathVariable UUID id) {
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null) { //Se a tarefa não for encontrada

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) { //Se o id do usuário que criou a tarefa não for igual ao id do usuário que está fazendo a requisição

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário sem permissão para alterar a tarefa");
        }

        this.taskRepository.delete(task);
        return ResponseEntity.ok("Tarefa excluída");
    }

}