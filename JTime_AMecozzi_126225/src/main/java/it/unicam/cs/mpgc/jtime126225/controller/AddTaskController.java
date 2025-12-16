package it.unicam.cs.mpgc.jtime126225.controller;

import it.unicam.cs.mpgc.jtime126225.model.*;
import it.unicam.cs.mpgc.jtime126225.persistency.*;
import it.unicam.cs.mpgc.jtime126225.model.Manager;
import it.unicam.cs.mpgc.jtime126225.model.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class AddTaskController {
    private Persistency persistency;
    private Manager manager;
    private Project project;

    @FXML
    private TextArea descArea;
    @FXML
    private TextField nameField;
    @FXML
    private TextField durationField;
    @FXML
    private TextField idField;
    @FXML
    private Button createButton;
    @FXML
    private Button projectButton;
    @FXML
    Label warningLabel;

    private void initialize(){
        this.manager=Manager.getManager();
        warningLabel.setStyle("-fx-text-fill: red;");
        warningLabel.setText("");
    }

    @FXML
    public void controlKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            createTask();
        }
    }

    @FXML
    public void createTask() {
        if(idField.getText().isEmpty()||nameField.getText().isEmpty()){
            warningLabel.setText("Nome ed ID sono obbligatori");
        }else if(!idField.getText().matches("[0-9]+")){
            warningLabel.setText("L'ID deve essere un numero (>0)");
        }else{
            int id=Integer.parseInt(idField.getText());
            if(project.idIsIn(id)){
                warningLabel.setText("ID già presente nel progetto");
            }else{
                long estimated=0;
                if(!durationField.getText().isEmpty()){
                    if(!durationField.getText().matches("[0-9]+")){
                        warningLabel.setText("La durata deve essere un numero (>0)");
                    }else{
                        estimated=Long.parseLong(durationField.getText());
                        Task createdTask=new Task(id, nameField.getText(), descArea.getText(), estimated);
                        manager.addTaskInProject(createdTask, this.project.hashCode());
                        goToProject();
                    }
                }else{
                    Task createdTask=new Task(id, nameField.getText(), descArea.getText(), estimated);
                    manager.addTaskInProject(createdTask, this.project.hashCode());
                    goToProject();
                }
            }
        }
    }

    @FXML
    public void goToProject() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project.fxml"));

            Parent root = loader.load();

            ProjectController controller= loader.getController();
            controller.setProject(this.project);

            Stage stage = (Stage)projectButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(this.project.getName());
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setProject(Project project){
        this.project = project;
        initialize();
    }
}
