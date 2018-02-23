package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import objects.Note;
import connection.ConnectionDb;
import start.Main;


import java.net.URL;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    private String sqlCommand;

    private ConnectionDb connection = ConnectionDb.getInstance();

    private Note note;

    @FXML
    private Label dateLabel;
    @FXML
    private TextArea textArea;

    @FXML
    private Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //ограничение до 100 символов
        initTextArea();
        //текущее время в отдельной нити
        initTimeLabel();
    }
    private void initTextArea() {
        //ограничение на ввод символов ( до 100 символов )
        TextFormatter<String> textFormatter = new TextFormatter<>(c -> c
                .getControlNewText().length() > 99 ? null : c);
        textArea.setTextFormatter(textFormatter);

        //разрешить перенос строк
        textArea.setWrapText(true);
    }

    private void initTimeLabel() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        java.util.Date date = new java.util.Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                dateLabel.setText(sdf.format(date));
                            }
                        });
                        Thread.sleep(1000);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void actionSave(ActionEvent event) {
        String type = sqlCommand.split(" ")[0];
        switch (type){
            case "INSERT" : insert(event); break;
            case "UPDATE" : update(event); break;
            case "DELETE" : delete(event); break;
        }
    }

    public void actionClose(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.hide();
    }

    private void insert(ActionEvent event){
        try (
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(Main.INSERT_INTO)
        ){
            java.util.Date date = new java.util.Date();
            java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

            String text = textArea.getText();

            preparedStatement.setTimestamp(1,new java.sql.Timestamp(sqlDate.getTime()));
            preparedStatement.setString(2, text);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            note.setDate(sdf.format(sqlDate));
            note.setText(text);

            preparedStatement.execute();

            actionClose(event);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update(ActionEvent event){
        delete(event);
        insert(event);
    }

    private void delete(ActionEvent event){
        try (
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(Main.DELETE)
        ) {
            preparedStatement.setString(1, note.getText());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNote(Note note) {
        if (note == null) return;

        this.note = note;
        dateLabel.setText(note.getDate());
        textArea.setText(note.getText());
    }

    public Note getNote() {
        return note;
    }

    public void setSqlCommand(String sqlCommand) {
        this.sqlCommand = sqlCommand;
    }

    public String getSqlCommand() {
        return sqlCommand;
    }
}
