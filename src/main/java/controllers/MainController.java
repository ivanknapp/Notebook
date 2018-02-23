package controllers;

import connection.ConnectionDb;
import interfaces.impls.CollectionNoteBook;
import javafx.collections.ListChangeListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import objects.Note;
import start.Main;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private CollectionNoteBook data = new CollectionNoteBook();

    private static Connection connection = ConnectionDb.getInstance().getConnection();
    private Stage editDialogStage;
    private FXMLLoader fxmlLoader = new FXMLLoader();
    private EditController editController;
    private Parent fxmlEdit;
    private Stage mainStage;


    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label countNotesLabel;
    @FXML
    private TableView<Note> tableNotes;
    @FXML
    private TableColumn<Note, String> date;
    @FXML
    private TableColumn<Note, String> text;

    //private ObservableList<Note> data;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLoader();
        initListeners();
        loadData();
    }

    public void addNote(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            EditController noteController = (EditController) loader.getController();

            Pane root = loader.load(getClass().getResource("../fxml/note.fxml").openStream());

            Stage addWindow = new Stage();
            addWindow.initModality(Modality.APPLICATION_MODAL);
            addWindow.setTitle("Add new note");
            addWindow.setScene(new Scene(root));
            addWindow.setResizable(false);
            addWindow.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end addNote method

    //load data from table
    public void loadData() {
        try {
            ResultSet res = connection.createStatement().executeQuery(Main.GET_ALL);

            while (res.next()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date sqlDate = res.getTimestamp("date");

                String date = sdf.format(sqlDate);
                String text = res.getString("text");

                Note note = new Note(date, text);
                data.add(note);
            }

            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            text.setCellValueFactory(new PropertyValueFactory<>("text"));

            //tableNotes.setItems(null);
            tableNotes.setItems(data.getNotes());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditWindow() {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setTitle("Edit");
            editDialogStage.setMinHeight(150);
            editDialogStage.setMinWidth(300);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            editDialogStage.initModality(Modality.WINDOW_MODAL);
            editDialogStage.initOwner(mainStage);
        }

        editDialogStage.showAndWait(); // для ожидания закрытия окна

    }

    public void actionButtonPressed(ActionEvent event) {
        Object source = event.getSource();
        if (!(source instanceof Button)) {
            return;
        }

        //Note selectedPerson = (Note) tableNotes.getSelectionModel().getSelectedItem();

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "addButton":
                editController.setNote(new Note());
                showEditWindow();
                if (!editController.getNote().getText().isEmpty())
                    data.add(editController.getNote());
                break;

            case "editButton":
                //заглушка для изменения записей
                break;

            case "deleteButton":
                //заглушка для удаления записей

        }
    }

    private void initListeners() {
        // слушает изменения в коллекции для обновления надписи "Кол-во записей"
        data.getNotes().addListener(new ListChangeListener<Note>() {
            @Override
            public void onChanged(Change<? extends Note> c) {
                updateCountLabel();
            }
        });
    }

    private void updateCountLabel() {
        countNotesLabel.setText("Total notes : " + data.getNotes().size());
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource(Main.FXML_EDIT));
            fxmlEdit = fxmlLoader.load();
            editController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
