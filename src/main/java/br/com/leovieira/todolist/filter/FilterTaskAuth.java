package br.com.leovieira.todolist.filter;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.leovieira.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Esta classe filtra os usuários não cadastrados no banco de dados da aplicação.

@Component // Faz com que o Spring gerencie a classe
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/tasks/")) { //Se o path da requisição começar com "/tasks/"", então é direcionado para a rota de autenticação de usuário

            System.out.println("Rota de Autenticação"); //Linha de teste de rota

            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim(); // Cria uma variável com a senha do usuário (Codificada). O método substring retira uma parte de uma string maior. O método trim() retira todos os espaços da string
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded); //Cria um Array de bytes com a decodificação da senha do usuário
            var authString = new String(authDecoded);
            String[] authCredentials = authString.split(":"); 
            var userName = authCredentials[0];
            var password = authCredentials[1];

            // Validar usuário
            var user = this.userRepository.findByUsername(userName);

            if (user == null) {                   // Se não existir este usuário no banco de dados
                response.sendError(401);          //Retorna o erro 401
            } 
            else {                                   //Se existir
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {       // Se senha correta(True)
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }
                else {                               // Se senha incorreta (False)
                    response.sendError(401);
                }
            }

        } else {                                     //Continua a execução da aplicação
            filterChain.doFilter(request, response);
        }
    }
}
