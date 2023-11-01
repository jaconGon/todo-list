package br.edu.unifal.repository;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.repository.impl.FileChoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileChoreRepositoryTest {

    @InjectMocks
    private FileChoreRepository repository;

    @Mock
    private ObjectMapper mapper;

    @BeforeEach
    public void input() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#loadChores > when the file is found > when the content is empty > Return empty list")
    void loadChoresWhenTheFileIsFoundWhenTheContentIsEmptyReturnEmptyList() throws IOException {
        Mockito.when(
                this.mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenReturn(new Chore[0]);
        List<Chore> response = repository.load();

        assertTrue(response.isEmpty());

    }

    @Test
    @DisplayName("#loadChores > when the file is not found > Or path is invalid > Throws exception")
    void loadChoresWhenTheFileIsNotFoundOrPathIsInvalidThrowsException() throws IOException {
        Mockito.when(
                this.mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenThrow(FileNotFoundException.class);
        List<Chore> response = repository.load();

        assertTrue(response.isEmpty());

    }

    @Test
    @DisplayName("#loadChores > When the file is loaded > Return a chore list")
    void loadChoresWhenTheFileIsLoadedReturnAChoreList() throws  IOException{
        Mockito.when(
                this.mapper.readValue(new File("chores.json"), Chore[].class)
        ).thenReturn(new Chore[]{
                new Chore("Chore 1",Boolean.FALSE, LocalDate.now()),
                new Chore("Chore 2",Boolean.FALSE, LocalDate.now().plusDays(2))
        });

        List<Chore> chores = repository.load();
        assertAll(
                ()-> assertEquals(2,chores.size()),
                ()-> assertEquals("Chore 1",chores.get(0).getDescription()),
                ()-> assertEquals(LocalDate.now(),chores.get(0).getDeadline())
        );

    }

    @Test
    @DisplayName("#save > When unable to write the chores on the file > Return false")
    void saveWhenUnableToWriteTheChoresOnTheFileReturnFalse() throws IOException {
        Mockito.doThrow(IOException.class)
                .when(mapper).writeValue(Mockito.any(File.class), Mockito.any());

        List<Chore> chores = new ArrayList<>();
        boolean response = repository.save(chores);

        assertFalse(response);
    }

    @Test
    @DisplayName("#save > When able to write the chores on the file > Return true")
    void saveWhenAbleToWriteTheChoresOnTheFileReturnTrue() throws IOException {
        Mockito.doNothing().when(mapper).writeValue(Mockito.any(File.class), Mockito.any());

        List<Chore> chores = new ArrayList<>();
        boolean response = repository.save(chores);

        assertTrue(response);
    }

}