package br.com.leovieira.todolist.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;   //Biblioteca de criptografia de dados

@RestController //@RestController => Retorna o objeto e os dados do objeto são gravados diretamente na resposta HTTP como JSON ou XML.
@RequestMapping("/user") //@RequestMapping => Anotação de nível de classe que define o prefixo de URL para todas as rotas de um controller.
public class UserController {
    @Autowired //@Autowired => Faz com que o Spring gerencie o ciclo de vida da interface IUserRespository
    private IUserRepository userRepository; //Chamada de interface

    @PostMapping("/") // Requisição HTTP POST
    public ResponseEntity create(@RequestBody UserModel userModel) {

        System.out.println("Rota UserControler");

        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) { //Se houver já houver algum usuário cadastrado com o mesmo username
            //Mensagem de erro
            //Status code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado");
            
        }

        var passordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());  //Criptografa a senha do usuário

        userModel.setPassword(passordHashed);              //Atribui a senha criptografada ao usuário
        var userCreated = this.userRepository.save(userModel); //Salva os dados do usuário no BD
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated); //Retorna o status da requisição HTTP
    }
}
