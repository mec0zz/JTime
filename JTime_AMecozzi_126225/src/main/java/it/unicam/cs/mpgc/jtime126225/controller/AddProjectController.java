package it.unicam.cs.mpgc.jtime126225.controller;

import it.unicam.cs.mpgc.jtime126225.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddProjectController {
    private Manager manager;

    @FXML
    private TextField nameField;
    @FXML
    private TextArea descArea;
    @FXML
    private Button createButton;
    @FXML
    private Button homeButton;
    @FXML
    Label warningLabel;
    @FXML
    TextField idField;

    public void initialize(){
        manager=Manager.getManager();
        warningLabel.setOpacity(0);
    }

    @FXML
    public void controlKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            createProject();
        }
    }

    @FXML
    public void createProject() {
        if(nameField.getText().isEmpty()||idField.getText().isEmpty()){
            warningLabel.setText("Nome ed ID sono obbligatori");
            warningLabel.setOpacity(1);
        }else if(!idField.getText().matches("[0-9]+")){ //Controllo che sia un numero
            warningLabel.setText("L'ID deve essere un numero (>0)");
            warningLabel.setOpacity(1);
        }else{
            int id=Integer.parseInt(idField.getText());
            if(manager.idIsIn(id)){
                warningLabel.setText("ID già presente");
                warningLabel.setOpacity(1);
            }else{
                Project createdProject=new Project(id,nameField.getText(),descArea.getText());
                manager.addProject(createdProject);
                goToHome();
            }
        }
        //TODO implementare
    }

    @FXML
    public void goToHome() {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/screensFXML/home.fxml"));
            Stage stage= (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Home");
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
