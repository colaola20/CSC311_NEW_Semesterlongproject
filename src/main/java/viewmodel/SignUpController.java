package viewmodel;

import dao.DbConnectivityClass;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import model.Person;

import java.sql.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SignUpController {

    final static String DB_NAME="CSC311_Week10_HW";

    final static String SQL_SERVER_URL = "jdbc:mysql://csc311sorychserver.mysql.database.azure.com";//update this server name
    final static String DB_URL = "jdbc:mysql://csc311sorychserver.mysql.database.azure.com/"+DB_NAME;//update this database name
    final static String USERNAME = "csc311admin";// update this username
    final static String PASSWORD = "MvT$!qp9c26ZY!V";// update this password

    @FXML
    private TextField email, f_name, l_name, password;

    @FXML
    private Button newAccountBtn;

    @FXML
    private Label errorMsg;

    private DbConnectivityClass dbConnection;

    private Person newp;


    /**
     * Set up the initial state of the GUI, add listeners to input fields, and disable the register button until all fields are valid.
     */
    @FXML
    public void initialize() {
        // Add listeners to input fields
        f_name.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        l_name.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        email.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        password.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());

        // Initially disable the register button
        newAccountBtn.setDisable(true);

    }


    private void validateInputs() {
        String firstName = f_name.getText();
        String lastName = l_name.getText();
        String emailText = email.getText();
        String passwordText = password.getText();

        if (!firstName.isEmpty() && (!isNameValid(firstName))) {
            errorMsg.setText("Invalid first name.");
            return;
        }
        else {
            errorMsg.setText("");
        }

        if (!lastName.isEmpty() && (!isNameValid(lastName))) {
            errorMsg.setText("Invalid last name.");
            return;
        }
        else {
            errorMsg.setText("");
        }

        if (!emailText.isEmpty() && (!isEmailValid(emailText))) {
            errorMsg.setText("Invalid email.");
            return;
        }
        else {
            errorMsg.setText("");
        }

        if (!passwordText.isEmpty() && (!isPasswordValid(passwordText))) {
            errorMsg.setText("Invalid password.");
            return;
        }
        else {
            errorMsg.setText("");
        }


        boolean isValid = isNameValid(firstName) &&
                isNameValid(lastName) &&
                isEmailValid(emailText) &&
                isPasswordValid(passwordText);

        newAccountBtn.setDisable(!isValid);
    }

    private boolean isPasswordValid(String passwordText) {
        final String regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{4,}$";
        return passwordText.matches(regex);
    }


    /**
     * Check if the email is valid (should be in the format <word>@farmingdale.edu)
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        final String regex = "(\\w+)@([a-zA-Z]+).([a-zA-Z]{3})";
        return email.matches(regex);
    }

    /**
     * Check if the name is valid (should be between 2 and 25 characters long)
     * @param name
     * @return
     */
    private boolean isNameValid(String name) {
        final String regex = "([a-zA-Z]{2,25})";
        return name.matches(regex);
    }

    public void createNewAccount(ActionEvent actionEvent) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(SQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+DB_NAME+"");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS clients ("
                    + "first_name VARCHAR(200) NOT NULL," + "last_name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL PRIMARY KEY,"
                    + "password VARCHAR(200))";
            statement.executeUpdate(sql);

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO clients (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, f_name.getText());
            preparedStatement.setString(2, l_name.getText());
            preparedStatement.setString(3, email.getText());
            preparedStatement.setString(4, password.getText());
            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("A new user was inserted successfully.");
            }
            preparedStatement.close();
            conn.close();

            Parent root = FXMLLoader.load(getClass().getResource("/view/db_interface_gui.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }





    public void goBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
