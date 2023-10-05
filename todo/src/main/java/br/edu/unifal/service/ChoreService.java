package br.edu.unifal.service;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.enumerator.ChoreFilter;
import br.edu.unifal.excepition.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChoreService {

    private List<Chore> chores;

    public ChoreService(){
        chores = new ArrayList<>();
    }

    public Chore addChore(String description, LocalDate deadline){
        if (Objects.isNull(description) || description.isEmpty()){
            throw new InvalidDescriptionException("The description cannot be null or empty");
        }
        if(Objects.isNull(deadline) || deadline.isBefore(LocalDate.now())){
            throw new InvalidDeadlineException("The deadline cannot be null or before the current date");
        }

//        boolean choreExists = chores.stream().anyMatch(chore -> chore.getDescription().equals(description)
//        && chore.getDeadline().isEqual(deadline));
//
//        if(choreExists){
//            throw new DuplicatedChoreException("The given chore already exists.");
//        }

        for(Chore chore : chores){
            if (chore.getDescription().equals(description) && chore.getDeadline().isEqual(deadline)) {
                throw new DuplicatedChoreException("The given chore already exists.");
            }
        }

//        Chore chore = new Chore();
//        chore.setDescription(description);
//        chore.setDeadline(deadline);
//        chore.setIsCompleted(Boolean.FALSE);

        Chore chore = new Chore(description, Boolean.FALSE, deadline);
        chores.add(chore);

        // TODO: CASO DE TESTE EM QUE O CHORE SEJA ADICIONADO
        return chore;
    }


    public List<Chore> getChores(){
        return this.chores;
    }

    /**
     *
     * @param description -the description of the chore
     * @param deadline -
     */
    public void deleteChore(String description, LocalDate deadline){
        if(isChoreListEmpty.test(this.chores)){
            throw new EmptyChoreListException("Unable to remove a chore from an empty list");
        }
        boolean choreExist = this.chores.stream().anyMatch((chore -> chore.getDescription().equals(description)
                && chore.getDeadline().isEqual(deadline)));
        if(!choreExist){
            throw new ChoreNotFoundException("the given chore doesn't exist");
        }

        this.chores = this.chores.stream().filter(chore -> !chore.getDescription().equals(description)
                && !chore.getDeadline().isEqual(deadline)).collect(Collectors.toList());
    }

    public void toggleChore (String description, LocalDate deadline){
        boolean ischoreExist = this.chores.stream().anyMatch((chore) ->
                        chore.getDescription().equals(description) &&
                        chore.getDeadline().isEqual(deadline)
                );
        if(!ischoreExist){
            throw new ChoreNotFoundException("Chore not found,. Impossible to toggle");
        }

        this.chores.stream().map(chore -> {
         if(!chore.getDescription().equals(description) && !chore.getDeadline().isEqual(deadline)){
            return chore;
         }
         if (chore.getDeadline().isBefore(LocalDate.now()) && chore.getIsCompleted()){
             throw new TogglechoreWithInvalidedeadlineexception("Unable to toggle a complet chore");
         }
         chore.setIsCompleted(!chore.getIsCompleted());
         return  chore;
         }).collect((Collectors.toList()));
    }

    public List<Chore> filterChores(ChoreFilter filter){
        switch(filter){
            case COMPLETED:
                return this.chores.stream().filter(Chore::getIsCompleted).collect(Collectors.toList());
            case UNCOMPLETED:
                return this.chores.stream().filter(chore -> !chore.getIsCompleted()).collect(Collectors.toList());
            case ALL:
            default:
                return this.chores;
        }
    }

    public String printChores(){
        if(isChoreListEmpty.test(this.chores)){
            throw new EmptyChoreListException("Unable to remove a chore from an empty list");
        }
        StringBuilder retorno = new StringBuilder();
        for(Chore chore : chores){
            if(chore.getIsCompleted()) {
                retorno.append("Description: ").append(chore.getDescription()).append(" - Deadline: ").append(chore.getDeadline()).append(" - Status: Completa\n");
            }else {
                retorno.append("Description: ").append(chore.getDescription()).append(" - Deadline: ").append(chore.getDeadline()).append(" - Status: Incompleta\n");
            }
        }
        return retorno.toString();
    }

    public List<Chore> editChore(String description, LocalDate deadline, String newDescription, LocalDate newDeadline){
        boolean ischoreExist = this.chores.stream().anyMatch((chore) ->
                chore.getDescription().equals(description) &&
                        chore.getDeadline().isEqual(deadline)
        );
        if(!ischoreExist){
            throw new ChoreNotFoundException("Chore not found,. Impossible to toggle");
        }
        for(Chore chore : chores){
            if(chore.getDescription().equals(description) && chore.getDeadline().equals(deadline)) {
                chore.setDescription(newDescription);
                chore.setDeadline(newDeadline);
            }
        }
        return this.chores;
    }

    private final Predicate<List<Chore>> isChoreListEmpty = chorelist -> chorelist.isEmpty();
}
