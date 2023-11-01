package br.edu.unifal.repository.impl;

import br.edu.unifal.domain.Chore;
import br.edu.unifal.repository.ChoreRepository;
import br.edu.unifal.repository.book.ChoreBook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySQLChoreRepository implements ChoreRepository {

    //Estabelecer uma conexão com o banco de dados
    private Connection connection;

    //Realizar operações no banco de dados
    //Executam em tempo de compilação (consulta estatica)
    //Usar quando for consulta unica
    private Statement statement;

    //Realizar operações no banco de dados
    //Executa queries dinâmicas em tempo de execução
    //Ideal para quando vamos executar a query multiplas vezes
    //melhor perfomance que o statement
    private PreparedStatement preparedStatement;

    //Utilizado para capturar o retorno de uma consulta
    private ResultSet resultSet;

    @Override
    public List<Chore> load() {
        if(!connectToMySQL()){
            return new ArrayList<>();
        }
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(ChoreBook.FIND_ALL_CHORES);

            List<Chore> chores = new ArrayList<>();
            while(resultSet.next()){
                Chore chore = Chore.builder()
                        .description(resultSet.getString("description"))
                        .isCompleted(resultSet.getBoolean("iscompleted"))
                        .deadline(resultSet.getDate("deadline").toLocalDate())
                        .build();
                chores.add(chore);
            }
            return  chores;
        } catch (SQLException e) {
            System.out.println("Error when connecting to database");
        }finally {
            closeConnections();
        }
        return null;
    }

    @Override
    public boolean saveAll(List<Chore> chores) {
        return false;
    }

    private boolean connectToMySQL(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                    .getConnection("jdbc:mysql://192.168.1.254:3306/db2022108040?"
                    + "user=user2022108040&password=2022108040");
            return Boolean.TRUE;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error when connecting to database. Try again later");
        }
        return Boolean.FALSE;
    }

    private void closeConnections(){
        try{
            if(Objects.nonNull(connection) && !connection.isClosed()){
                connection.close();
            }
            if(Objects.nonNull(statement) && !statement.isClosed()){
                statement.close();
            }
            if (Objects.nonNull(preparedStatement) && !preparedStatement.isClosed()){
                preparedStatement.close();
            }
            if (Objects.nonNull(resultSet) && !resultSet.isClosed()) {
                resultSet.close();
            }
        }catch(SQLException exception){
            System.out.println("Error when closing database connections");
        }
    }
    @Override
    public boolean save(Chore chore){
        if(!connectToMySQL()){
            return Boolean.FALSE;
        }
        try{
            preparedStatement = connection.prepareStatement(
                    ChoreBook.INSERT_CHORE);
            preparedStatement.setString(1, chore.getDescription());
            preparedStatement.setBoolean(2, chore.getIsCompleted());
            preparedStatement.setDate(3, Date.valueOf(chore.getDeadline()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }catch(SQLException exception){
            System.out.println("ERROR when inserting a new chore on database");
        } finally {
            closeConnections();
        }
        return false;
    }
}
