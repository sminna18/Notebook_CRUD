package org.example.dao;

import org.example.models.Person;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
//    private static int PEOPLE_COUNT;

    public final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    private static final String URL = "jdbc:postgresql://localhost:5432/Db";
//    private static final String USERNAME = "postgres";
//    private static final String PASSWORD = "postgres";
//    private static Connection connection;
//
//    static {
//        try {
//            Class.forName("org.postgresql.Driver");
//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }



//    private List<Person> people;
//    {
//        people = new ArrayList<>();
//
//        people.add(new Person(++PEOPLE_COUNT, "Tom", 23, "tom32@mail.ru"));
//        people.add(new Person(++PEOPLE_COUNT, "Bob", 21, "bobsmobs@google.com"));
//        people.add(new Person(++PEOPLE_COUNT, "Mike", 31, "mi63535@mail.ru"));
//        people.add(new Person(++PEOPLE_COUNT, "Katy", 15, "Kitimyau@gmail.ru"));
//
//    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
//        List<Person> people = new ArrayList<>();
//
//         try {
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery("SELECT * FROM Person");
//
//             while (resultSet.next()) {
//                 Person person = new Person();
//
//                 person.setId(resultSet.getInt("id"));
//                 person.setName(resultSet.getString("name"));
//                 person.setEmail(resultSet.getString("email"));
//                 person.setAge(resultSet.getInt("age"));
//
//                 people.add(person);
//
//             }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//         return people;
    }

    public Optional<Person> show(String email) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE email=?", new Object[]{email},
                new PersonMapper()).stream().findFirst();
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id=?", new Object[]{id},
                new PersonMapper()).stream().findFirst().orElse(null);


//        Person person = null;
//
//        try {
//
//            PreparedStatement preparedStatement = connection.
//                    prepareStatement("SELECT * FROM Person WHERE id=?");
//            preparedStatement.setInt(1, id);
//            System.out.println(222);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            resultSet.next();
//            person = new Person();
//            System.out.println(333);
//            person.setId(resultSet.getInt("id"));
//            person.setName(resultSet.getString("name"));
//            person.setEmail(resultSet.getString("email"));
//            person.setAge(resultSet.getInt("age"));
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return person;
    }

    public void save(Person person) {

        jdbcTemplate.update("insert into Person(name, age, email) values(?, ?, ?)",
                person.getName(), person.getAge(), person.getEmail());

//        try {
//            PreparedStatement preparedStatement = connection.
//                    prepareStatement("INSERT INTO Person VALUES (1, ?, ?, ?)");
//            preparedStatement.setString(1, person.getName());
//            preparedStatement.setInt(2,person.getAge());
//            preparedStatement.setString(3, person.getEmail());
//
//            preparedStatement.executeUpdate();
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }

    public void update(int id, Person updatePerson) {

        jdbcTemplate.update("UPDATE Person SET name=?, age=?, email=? WHERE id=?",
                updatePerson.getName(), updatePerson.getAge(), updatePerson.getEmail(), id);

//        try {
//            PreparedStatement preparedStatement = connection.
//                    prepareStatement("UPDATE Person SET name=?, age=?, email=? WHERE id=?");
//            preparedStatement.setString(1, updatePerson.getName());
//            preparedStatement.setInt(2,updatePerson.getAge());
//            preparedStatement.setString(3, updatePerson.getEmail());
//            preparedStatement.setInt(4, id);
//
//            preparedStatement.executeUpdate();
//
////            Statement statement = connection.createStatement();
////            statement.executeUpdate("INSERT INTO Person VALUES(" + 1 + ",'" +
////                    person.getName() + "'," +
////                    person.getAge() + ",'" + person.getEmail() + "')");
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void delete(int id) {

            jdbcTemplate.update("DELETE FROM Person WHERE id=?", id);

//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(
//                    "DELETE FROM Person WHERE id=?");
//
//            preparedStatement.setInt(1, id);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//        e.printStackTrace();
//    }
    }

    ///////////////////
    // Batch tasting //
    ///////////////////

    public void testMultipleUpdate() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            people.add(new Person(i, "Name" + i, 30,
                    "test-mail" + i + "@mail.ru"));

        long berfore = System.currentTimeMillis();

        for (Person person : people)

            jdbcTemplate.update("INSERT INTO Person VALUES (?, ?, ?, ?)",
                    person.getId(), person.getName(), person.getAge(), person.getEmail());

        long after = System.currentTimeMillis();

        System.out.println("1)Time: " + (after - berfore));
    }

    public void testBatchUpdate() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            people.add(new Person(i, "Name" + i, 30,
                    "test-mail" + i + "@mail.ru"));

        long berfore = System.currentTimeMillis();

            jdbcTemplate.batchUpdate("INSERT INTO Person VALUES (?, ?, ?, ?)",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                            preparedStatement.setInt(1, people.get(i).getId());
                            preparedStatement.setString(2, people.get(i).getName());
                            preparedStatement.setInt(3, people.get(i).getAge());
                            preparedStatement.setString(4, people.get(i).getEmail());
                        }

                        @Override
                        public int getBatchSize() {
                            return 100 ;
                        }
                    });

        long after = System.currentTimeMillis();

        System.out.println("2)Time: " + (after - berfore));
    }

}
