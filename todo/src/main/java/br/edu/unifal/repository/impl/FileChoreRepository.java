package br.edu.unifal.repository.impl;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.repository.ChoreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileChoreRepository implements ChoreRepository {

    private ObjectMapper mapper;

    public FileChoreRepository(){
        mapper = new ObjectMapper().findAndRegisterModules();
    }

    @Override
    public List<Chore> load() {
        try {
            return new ArrayList<>(
                    Arrays.asList(this.mapper.readValue(new File("chores.json"), Chore[].class))
            );
        }catch(MismatchedInputException exception){
            System.out.println("Unable to convert the content of the file into Chores");
//            return new ArrayList<>();
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to open file.");
//            return new ArrayList<>();
        }
        return new ArrayList<>();

    }

    @Override
    public boolean saveAll(List<Chore> chores){
        try {
            mapper.writeValue(new File("chores.json"),chores);
            return true;
        } catch (IOException exception) {
            System.out.println("ERROR: unable to write the chores on the file.");
        }
        return false;
    }

    @Override
    public boolean save(Chore chore){
        return false;
    }
}
