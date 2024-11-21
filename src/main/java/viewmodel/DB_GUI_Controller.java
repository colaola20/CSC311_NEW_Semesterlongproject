package viewmodel;

import com.azure.storage.blob.BlobClient;
import dao.DbConnectivityClass;
import dao.StorageUploader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Person;
import service.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.control.ProgressBar;

public class DB_GUI_Controller implements Initializable {
    StorageUploader store = new StorageUploader();

    @FXML
    TextField first_name, last_name, department, major, email, imageURL;
    @FXML
    ImageView img_view;

    @FXML
    private ProgressBar progressBar;

    @FXML
    MenuBar menuBar;

    @FXML
    private MenuItem ChangePic, ClearItem, CopyItem, deleteItem, editItem, logOut, newItem;

    @FXML
    private TableView<Person> tv;
    @FXML
    private Button deleteBtn, editBtn, addBtn, clearBtn;
    @FXML
    private TableColumn<Person, Integer> tv_id;

    @FXML
    private ChoiceBox<String> choiceBox;


    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_major, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));

            tv.setItems(data);

            // Disable the delete and edit buttons until a record is selected
            deleteBtn.setDisable(true);
            editBtn.setDisable(true);
            clearBtn.setDisable(true);
            // Disable the add button until all fields are filled with valid data
            addBtn.setDisable(true);

            editItem.setDisable(true);
            deleteItem.setDisable(true);
            ClearItem.setDisable(true);

            // Disable the major field, so that the user can only select from the dropdown. After selecting a value from the dropdown, the major field will be automatically populated with the selected value.
            major.setDisable(true);

            // Add listeners to the first_name text field to enable the edit, delete, and add buttons when the field is filled with valid data
            first_name.textProperty().addListener((observable, oldValue, newValue) -> {
                validationForAddBtn();
                boolean isInputValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() && !email.getText().isEmpty() && !department.getText().isEmpty() && !major.getText().isEmpty();
                deleteBtn.setDisable(!isInputValid);
                editBtn.setDisable(!isInputValid);
                clearBtn.setDisable(!isInputValid);
                ClearItem.setDisable(!isInputValid);
                editItem.setDisable(!isInputValid);
                deleteItem.setDisable(!isInputValid);
            });

            // Add listeners to the last_name text field to enable the edit, delete, and add buttons when the field is filled with valid data
            last_name.textProperty().addListener((observable, oldValue, newValue) -> {
                validationForAddBtn();
                boolean isInputValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() && !email.getText().isEmpty() && !department.getText().isEmpty() && !major.getText().isEmpty();
                deleteBtn.setDisable(!isInputValid);
                editBtn.setDisable(!isInputValid);
                clearBtn.setDisable(!isInputValid);
                ClearItem.setDisable(!isInputValid);
                editItem.setDisable(!isInputValid);
                deleteItem.setDisable(!isInputValid);
            });

            // Add listeners to the department text field to enable the dit, delete, and add buttons when the field is filled with valid data
            department.textProperty().addListener((observable, oldValue, newValue) -> {
                validationForAddBtn();
                boolean isInputValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() && !email.getText().isEmpty() && !department.getText().isEmpty() && !major.getText().isEmpty();
                deleteBtn.setDisable(!isInputValid);
                editBtn.setDisable(!isInputValid);
                clearBtn.setDisable(!isInputValid);
                ClearItem.setDisable(!isInputValid);
                editItem.setDisable(!isInputValid);
                deleteItem.setDisable(!isInputValid);
            });

            // Add listeners to the email text field to enable the edit, delete, and add buttons when the field is filled with valid data
            email.textProperty().addListener((observable, oldValue, newValue) -> {
                validationForAddBtn();
                boolean isInputValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() && !email.getText().isEmpty() && !department.getText().isEmpty() && !major.getText().isEmpty();
                deleteBtn.setDisable(!isInputValid);
                editBtn.setDisable(!isInputValid);
                clearBtn.setDisable(!isInputValid);
                ClearItem.setDisable(!isInputValid);
                editItem.setDisable(!isInputValid);
                deleteItem.setDisable(!isInputValid);
            });

            major.textProperty().addListener((observable, oldValue, newValue) -> {
                validationForAddBtn();
                boolean isInputValid = !first_name.getText().isEmpty() && !last_name.getText().isEmpty() && !email.getText().isEmpty() && !department.getText().isEmpty() && !major.getText().isEmpty();
                deleteBtn.setDisable(!isInputValid);
                editBtn.setDisable(!isInputValid);
                clearBtn.setDisable(!isInputValid);
                ClearItem.setDisable(!isInputValid);
                editItem.setDisable(!isInputValid);
                deleteItem.setDisable(!isInputValid);
            });



            // Add listeners to the choice box to enable getting the selected value and populating the major field with the selected value
            choiceBox.setItems(FXCollections.observableArrayList(
                    Stream.of(Major.values())
                            .map(Enum::name)
                            .collect(Collectors.toList())
            ));

            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                major.setText(newValue);
                System.out.println("Selected value: " + newValue); // Debugging
            });


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validate the fields for the add button
     */
    public void validationForAddBtn(){
        if (isNameValid(first_name.getText()) && isNameValid(last_name.getText()) && isEmailValid(email.getText()) && isDepartmentValid(department.getText()) && isMajorValid(major)) {
            addBtn.setDisable(false);
        } else {
            addBtn.setDisable(true);
        }
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

    /**
     * Check if the department is valid (should be between 2 and 15 characters long)
     * @param department
     * @return
     */
    private boolean isDepartmentValid(String department) {
        final String regex = "([a-zA-Z]{2,15})";
        return department.matches(regex);
    }

    private boolean isMajorValid(TextField major) {
        System.out.println("Major: " + major.getText());
        System.out.println(!major.getText().isEmpty());
        return !major.getText().isEmpty();
    }

    /**
     * Handle the clear menu item pressed event
     * @param event
     */
    @FXML
    void clearItemPressed(ActionEvent event) {
        clearForm();
    }

    @FXML
    void copyItemPressed(ActionEvent event) {

    }

    /**
     * Handle the delete menu item pressed event
     * @param event
     */
    @FXML
    void deleteItemPressed(ActionEvent event) {
        deleteRecord();
    }

    /**
     * Handle the edit menu item pressed event
     * @param event
     */
    @FXML
    void editItemPressed(ActionEvent event) {
        editRecord();
    }


    @FXML
    protected void addNewRecord() {

            Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                    major.getText(), email.getText(), imageURL.getText());
            cnUtil.insertUser(p);
            cnUtil.retrieveId(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            clearForm();

    }

    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
        major.setText("");
        email.setText("");
        imageURL.setText("");
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").getFile());
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editRecord() {

        if (first_name.getText() != null && last_name.getText() != null && department.getText() != null
                && major.getText() != null && email.getText() != null) {
            editBtn.setDisable(false);
        }

        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        Person p2 = new Person(index + 1, first_name.getText(), last_name.getText(), department.getText(),
                major.getText(), email.getText(),  imageURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        cnUtil.deleteRecord(p);
        data.remove(index);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
            Task<Void> uploadTask = createUploadTask(file, progressBar);
            progressBar.progressProperty().bind(uploadTask.progressProperty());
            new Thread(uploadTask).start();
        }
    }

    private Task<Void> createUploadTask(File file, ProgressBar progressBar) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                BlobClient blobClient = store.getContainerClient().getBlobClient(file.getName());
                long fileSize = Files.size(file.toPath());
                long uploadedBytes = 0;

                try (FileInputStream fileInputStream = new FileInputStream(file);
                     OutputStream blobOutputStream = blobClient.getBlockBlobClient().getBlobOutputStream()) {

                    byte[] buffer = new byte[1024 * 1024]; // 1 MB buffer size
                    int bytesRead;

                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        blobOutputStream.write(buffer, 0, bytesRead);
                        uploadedBytes += bytesRead;

                        // Calculate and update progress as a percentage
                        int progress = (int) ((double) uploadedBytes / fileSize * 100);
                        updateProgress(progress, 100);
                    }
                }

                return null;
            }
        };
    }

    @FXML
    protected void addRecord() {
        showSomeone();
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        if (p==null) return;

        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        major.setText(p.getMajor());
        email.setText(p.getEmail());
        imageURL.setText(p.getImageURL());
    }

    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.getScene().getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            System.out.println("light " + scene.getStylesheets());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options =
                FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2,textField3, comboBox));
        Platform.runLater(textField1::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(),
                        textField2.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(
                    results.fname + " " + results.lname + " " + results.major);
        });
    }

    public static enum Major {Business, CSC, CPIS, English, Mathematics, Nursing, SecuritySystems, SportManagement}

    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }

}