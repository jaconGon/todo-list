package br.edu.unifal.service;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.enumerator.ChoreFilter;
import br.edu.unifal.excepition.*;
import br.edu.unifal.repository.ChoreRepository;
import br.edu.unifal.repository.impl.FileChoreRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChoreServiceTest {

    @InjectMocks
    private  ChoreService service;

    @Mock
    private ChoreRepository repository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

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

        assertDoesNotThrow(()-> service.toggleChore("Chore 1", LocalDate.now()));

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

    @Test
    @DisplayName("#filterChores > When the filter is ALL > When the list is empty > Return all chores")
    void filterChoresWhenTheFilterIsAllWhenTheListIsEmptyReturnAllChores(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.ALL);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#filterChores > When the filter is ALL > When the list is not empty > Return all chores")
    void filterChoresWhenTheFilterIsAllWhenTheListIsNotEmptyReturnAllChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore 1", Boolean.FALSE , LocalDate.now()));
        service.getChores().add(new Chore("Chore 2", Boolean.TRUE , LocalDate.now().plusDays(1)));
        List<Chore> response = service.filterChores(ChoreFilter.ALL);
        assertAll(
                ()->assertEquals(2, response.size()),
                ()-> assertEquals("Chore 1", response.get(0).getDescription()),
                ()-> assertEquals("Chore 2", response.get(1).getDescription())
        );
    }

    @Test
    @DisplayName("#filterChores > When the filter is COMPLETED > When the list is empty > Return an empty list")
    void filterChoreWhenTheFilterIsCompletedWhenTheListIsEmptyReturnAnEmptyList(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.COMPLETED);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#filterChores > When the filter is COMPLETED > When the list is not empty > Return the filtered chores")
    void filterChoreWhenTheFilterIsCompletedWhenTheListIsNotEmptyReturnTheFilteredChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore 1", Boolean.FALSE , LocalDate.now()));
        service.getChores().add(new Chore("Chore 2", Boolean.TRUE , LocalDate.now().plusDays(1)));
        List<Chore> response = service.filterChores(ChoreFilter.COMPLETED);
        assertAll(
                ()-> assertEquals(1, response.size()),
                ()-> assertEquals("Chore 2", response.get(0).getDescription()),
                ()-> assertEquals(Boolean.TRUE , response.get(0).getIsCompleted())
        );
    }

    @Test
    @DisplayName("#filterChores > When the filter is UNCOMPLETED > When the list is empty > Return an empty list")
    void filterChoreWhenTheFilterIsUncompletedWhenTheListIsEmptyReturnAnEmptyList(){
        ChoreService service = new ChoreService();
        List<Chore> response = service.filterChores(ChoreFilter.UNCOMPLETED);
        assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#filterChores > When the filter is UNCOMPLETED > When the list is not empty > Return the filtered chores")
    void filterChoreWhenTheFilterIsUncompletedWhenTheListIsNotEmptyReturnTheFilteredChores(){
        ChoreService service = new ChoreService();
        service.getChores().add(new Chore("Chore 1", Boolean.FALSE , LocalDate.now()));
        service.getChores().add(new Chore("Chore 2", Boolean.TRUE , LocalDate.now().plusDays(1)));
        List<Chore> response = service.filterChores(ChoreFilter.UNCOMPLETED);
        assertAll(
                ()-> assertEquals(1, response.size()),
                ()-> assertEquals("Chore 1", response.get(0).getDescription()),
                ()-> assertEquals(Boolean.FALSE , response.get(0).getIsCompleted())
        );
    }

    @Test
    @DisplayName("#printChores > When the list is empty > throw exception")
    void printChoresWhenTheListIsEmptyThrowAnException(){
        ChoreService service = new ChoreService();
        assertThrows(EmptyChoreListException.class, ()-> service.printChores());
    }

    @Test
    @DisplayName("#printChores > When the list is not empty > Print the descriptions")
    void printChoresWhenTheListIsNotEmptyPrintTheDescriptions(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 1",LocalDate.now().plusDays(3));
        service.addChore("Chore 2",LocalDate.now().plusDays(2));
        service.addChore("Chore 3",LocalDate.now().plusDays(1));
        assertEquals("Description: Chore 1 - Deadline: "+ LocalDate.now().plusDays(3) +" - Status: Incompleta\n" +
                     "Description: Chore 2 - Deadline: "+ LocalDate.now().plusDays(2) +" - Status: Incompleta\n" +
                     "Description: Chore 3 - Deadline: "+ LocalDate.now().plusDays(1) +" - Status: Incompleta\n",
                      service.printChores());
    }

    @Test
    @DisplayName("#editChores > When the chore doesn't exist > Throw an exception")
    void editChoresWhenTheChoreDoesnotExistThrowAnexception(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 2",LocalDate.now().plusDays(5));
        assertThrows(ChoreNotFoundException.class,()-> service.editChore("Chore 1",LocalDate.now(),"Lavar louça",LocalDate.now().plusDays(1)));
    }

    @Test
    @DisplayName("#editChores > When the chore exists > Update the chore")
    void editChoresWhenTheChoreExistsUpdateTheChore(){
        ChoreService service = new ChoreService();
        service.addChore("Chore 1",LocalDate.now().plusDays(3));
        List<Chore> response = service.editChore("Chore 1", LocalDate.now().plusDays(3), "Lavar louca",LocalDate.now().plusDays(1));
        assertAll(
                ()-> assertEquals("Lavar louca", response.get(0).getDescription()),
                ()-> assertEquals(LocalDate.now().plusDays(1) , response.get(0).getDeadline())
        );
    }


    @Test
    @DisplayName("#loadChores > When read the file > Upload the chores")
    void loadChoresAndUploadChores(){
        Mockito.when(repository.load()).thenReturn(new ArrayList<>() {{
            add(new Chore("Chore 1",Boolean.FALSE, LocalDate.now()));
            add(new Chore("Chore 2",Boolean.FALSE, LocalDate.now().plusDays(3)));
        }});
        service.loadChores();
        //int size = service.getChores().size();
        //assertEquals(2,size);
        List<Chore> loadedChores = service.getChores();
        assertAll(
                ()-> assertEquals(2,loadedChores.size()),
                ()-> assertEquals("Chore 1",loadedChores.get(0).getDescription()),
                ()-> assertEquals(Boolean.FALSE,loadedChores.get(0).getIsCompleted()),
                ()-> assertEquals(LocalDate.now(),loadedChores.get(0).getDeadline()),
                ()-> assertEquals("Chore 2",loadedChores.get(1).getDescription()),
                ()-> assertEquals(Boolean.FALSE,loadedChores.get(1).getIsCompleted()),
                ()-> assertEquals(LocalDate.now().plusDays(3),loadedChores.get(1).getDeadline())
        );
    }

    @Test
    @DisplayName("#loadChores > When no chores are loaded > update the chore list")
    void loadChoreWhenNoChoresAreloadedupdateThechoreList(){
        Mockito.when(repository.load()).thenReturn(new ArrayList<>());
        service.loadChores();
        List<Chore> loadChores = service.getChores();
        assertTrue(loadChores.isEmpty());
    }

}


