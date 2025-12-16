package it.unicam.cs.mpgc.jtime126225.controller;

import it.unicam.cs.mpgc.jtime126225.model.*;
import it.unicam.cs.mpgc.jtime126225.persistency.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskController {
    private Manager manager;
    private Project project;
    private Task task;

    @FXML
    private Label nameIdLabel;
    @FXML
    private Label descLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button completeButton;
    @FXML
    private Button projectButton;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label estimatedLabel;
    @FXML
    private Label realLabel;


    private void initialize(){
        manager=Manager.getManager();

        nameIdLabel.setText(this.task.getName()+" (#"+this.task.getId()+")");
        descLabel.setText((task.getDescription().isEmpty())?"Nessuna descrizione":task.getDescription());
        startLabel.setText(task.getStartTime().toString());
        endLabel.setText((task.isComplete())?task.getEndTime().toString():"-");
        estimatedLabel.setText((task.getEstimatedDuration()==0)?"-":String.valueOf(task.getEstimatedDuration())+" h");
        realLabel.setText((task.isComplete())?String.valueOf(task.getRealDuration())+" h":"-");

        if(task.isComplete()){
            statusLabel.setText("La task è stata già completata");
            statusLabel.setStyle("-fx-text-fill: green;");
            completeButton.setVisible(false);
            completeButton.setManaged(false);
        }else{
            statusLabel.setText("La task non è stata completata");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void completeTask() {
        this.task.completeTask();
        manager.completeTaskInProject(this.task.hashCode(), this.project.hashCode());

        statusLabel.setText("La task è stata completata con successo!");
        statusLabel.setStyle("-fx-text-fill: green;");
        completeButton.setVisible(false);
        completeButton.setManaged(false);
        endLabel.setText(this.task.getEndTime().toString());
        realLabel.setText(String.valueOf(this.task.getRealDuration())+" h");
    }

    @FXML
    public void goToProject() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project.fxml"));

            Parent root = loader.load();

            ProjectController projectController = loader.getController();
            projectController.setProject(this.project);

            Stage stage=(Stage) completeButton.getScene().getWindow();
            Scene scene=new Scene(root);
            stage.setScene(scene);
            stage.setTitle(this.project.getName());
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setTaskProject(Task task, Project project) {
        this.task = task;
        this.project = project;
        initialize();
    }
}
