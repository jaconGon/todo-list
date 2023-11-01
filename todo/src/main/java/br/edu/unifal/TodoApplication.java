package br.edu.unifal;

import br.edu.unifal.repository.ChoreRepository;
import br.edu.unifal.repository.impl.FileChoreRepository;
import br.edu.unifal.repository.impl.MySQLChoreRepository;
import br.edu.unifal.service.ChoreService;

import java.time.LocalDate;

public class TodoApplication {
    public static void main(String[] args) {
        ChoreRepository repository = new MySQLChoreRepository();
        ChoreService service = new ChoreService(repository);
        service.loadChores();
        service.addChore("Teste bd",LocalDate.now());
        System.out.println("tamanho da lista de chores: " + service.getChores().size());
    }
}
