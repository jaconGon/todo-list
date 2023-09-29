package br.edu.unifal.service;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.excepition.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ChoreServiceTest {

    @Test
    void addChoreWhenTheDescriptionIsInvalidThrowAnException(){
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertThrows(InvalidDescriptionException.class, // garante que chama a exceção
                        () -> service.addChore(null, null)),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", null)),
                () -> assertThrows(InvalidDescriptionException.class, // garante que chama a exceção
                        () -> service.addChore(null, LocalDate.now().plusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", LocalDate.now().plusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class, // garante que chama a exceção
                        () -> service.addChore(null, LocalDate.now().minusDays(1))),
                () -> assertThrows(InvalidDescriptionException.class,
                        () -> service.addChore("", LocalDate.now().minusDays(1)))
        );
    }

    @Test
    @DisplayName("#addChore > When the deadline is invalid > Throw an exception")
    void addChoreWhenTheDeadlineIsInvalidThrowAnException(){
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertThrows(InvalidDeadlineException.class,
                        () -> service.addChore("Description", null)),
                () -> assertThrows(InvalidDeadlineException.class,
                        () -> service.addChore("Description", LocalDate.now().minusDays(1)))
        );
    }

    @Test
    @DisplayName("#addChore > When the chore already exists > Throw an exception")
    void addChoreWhenTheChoreAlreadyExistsThrowAnException(){
        ChoreService service = new ChoreService();
        service.addChore("Description", LocalDate.now());
        assertThrows(DuplicatedChoreException.class,
                () -> service.addChore("Description", LocalDate.now()));
    }

    @Test
    @DisplayName("#addChore > Adding just one valid chore > Do not throw any exception")
    public void addJustOneValidChoreWithoutExceptions() {
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertDoesNotThrow(() -> service.addChore("Description 101", LocalDate.now())),
                () -> assertDoesNotThrow(() -> service.addChore("Description 102", LocalDate.now()))
        );
    }

    @Test
    @DisplayName("#addChore > When adding more than one valid chore without repetition > Do not throw any exception")
    public void addMoreThanOneValidChoreWithoutRepetition() {
        ChoreService service = new ChoreService();
        assertAll(
                () -> assertDoesNotThrow(() -> service.addChore("Description 101", LocalDate.now())),
                () -> assertDoesNotThrow(() -> service.addChore("Description 102", LocalDate.now())),
                () -> assertDoesNotThrow(() -> service.addChore("Description 103", LocalDate.now())),
                () -> assertDoesNotThrow(() -> service.addChore("Description 104", LocalDate.now().plusDays(1))),
                () -> assertDoesNotThrow(() -> service.addChore("Description 105", LocalDate.now().plusDays(2))),
                () -> assertDoesNotThrow(() -> service.addChore("Description 106", LocalDate.now().plusDays(3)))
        );
    }

    @Test
    @DisplayName("#deleteChore > When the list is empty > Throw an exception")
    void deleteChoreWhenTheListIsEmptyThrownAnException(){
        ChoreService service = new ChoreService();
        assertThrows(EmptyChoreListException.class, () ->{
            service.deleteChore("Lavar a roupa",LocalDate.now());
        });
    }

    @Test
    @DisplayName("#deleteChore > When the list is not empty > When the chore doesn't exist > Throw an exception")
    void deleteChoreWhenTheListIsNotEmptyWhenTheChoreDoesNotExistThrowAnException(){
       ChoreService service = new ChoreService();
       service.addChore("Description", LocalDate.now());
       assertThrows(ChoreNotFoundException.class, ()->{
           service.deleteChore("Chore to be deleted", LocalDate.now().plusDays(2));
       });

    }

    @Test
    @DisplayName("#deleteChore > When the list is not empty > When the chore exists > Delete the chore")
    void deleteChoreWhenTheListIsnotEmptyWhenTheChoreExistsDeleteTheChore(){
        ChoreService service = new ChoreService();

        service.addChore("Chore: #01", LocalDate.now().plusDays(1));
        assertEquals(1, service.getChores().size());

        assertDoesNotThrow(()-> service.deleteChore("Chore: #01", LocalDate.now().plusDays(1)));
        assertEquals(0, service.getChores().size());


    }

    @Test
    @DisplayName("#toggleChore > When the deadline is valid > Toggle chore")
    void toggleChoreWhenTheDeadlineIsValidtoggleChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 1", LocalDate.now());
        assertFalse(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(()-> service.toggleChore("chore 1", LocalDate.now()));

        assertTrue(service.getChores().get(0).getIsCompleted());


    }

    @Test
    @DisplayName("#toggleChore > When the chore doesn't exist > Throw an exception")
    void toggleChoreWhenTheChoreDoesNotExistThrowAnException(){
        ChoreService service = new ChoreService();
        assertThrows(ChoreNotFoundException.class, ()-> service.toggleChore("Chore 1",LocalDate.now()));

    }

    @Test
    @DisplayName("#toggleChore > When the deadline is invalid > When the status is uncompleted > Toggle the chore")
    void toggleChoreWhenThedeadlineIsInvalidWhenTheStatusIsUncompletedToggleTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 1", LocalDate.now());
        service.getChores().get(0).setDeadline(LocalDate.now().minusDays(1));
        assertFalse(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(()-> service.toggleChore("Chore 1", LocalDate.now().minusDays(1)));

        assertTrue(service.getChores().get(0).getIsCompleted());
    }

    @Test
    @DisplayName("#toggleChore > When the deadline is valid > When toggle the chore twice > Toggle the chore")
    void toggleChoreWhenThedeadlineIsvalidWhenToggleTheChoretwiceToggleTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 1", LocalDate.now());
        assertFalse(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(()-> service.toggleChore("Chore 1", LocalDate.now()));

        assertTrue(service.getChores().get(0).getIsCompleted());

        assertDoesNotThrow(()-> service.toggleChore("Chore 1", LocalDate.now()));

        assertFalse(service.getChores().get(0).getIsCompleted());
    }

    @Test
    @DisplayName("#toggleChore > When the deadline is invalid > When the status is Completed > Throw exception")
    void toggleChoreWhenThedeadlineIsInvalidWhenStatusIsCompletedThrowException(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore 1", Boolean.TRUE ,LocalDate.now().minusDays(1)));
        assertThrows(TogglechoreWithInvalidedeadlineexception.class, ()->
                service.toggleChore("Chore 1",LocalDate.now().minusDays(1))
        );
    }

}
