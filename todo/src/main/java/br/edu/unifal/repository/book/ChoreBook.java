package br.edu.unifal.repository.book;

public class ChoreBook {

    public static final String FIND_ALL_CHORES = "SELECT * FROM db2022108040.chore";
    public static final String INSERT_CHORE = "INSERT INTO db2022108040.chore (`description`,`isCompleted`, `deadline`) VALUES (?,?,?)";
}
