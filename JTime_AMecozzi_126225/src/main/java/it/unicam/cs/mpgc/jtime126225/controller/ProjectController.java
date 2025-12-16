package it.unicam.cs.mpgc.jtime126225.controller;

import it.unicam.cs.mpgc.jtime126225.model.*;
import it.unicam.cs.mpgc.jtime126225.persistency.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProjectController {
    private Persistency persistency;
    private Manager manager;
    private Project project;

    @FXML
    private TableView<Task> listTasks;
    @FXML
    private TableColumn<Task, String> idColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> statusColumn;
    @FXML
    private TableColumn<Task, String> startColumn;
    @FXML
    private TableColumn<Task, String> endColumn;
    @FXML
    private TableColumn<Task, String> estimatedColumn;
    @FXML
    private Button homeButton;
    @FXML
    private Button addTaskButton;
    @FXML
    private Label completedLabel;
    @FXML
    private Label notCompletedLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button completeButton;
    @FXML
    private Label estimatedLabel;
    @FXML
    private Label realLabel;
    @FXML
    private Label warningLabel;

    private void initialize(){
        this.manager=Manager.getManager();

        warningLabel.setText("");

        if(project.isComplete()){
            addTaskButton.setVisible(false);
            addTaskButton.setManaged(false);
            completeButton.setVisible(false);
            completeButton.setManaged(false);
            warningLabel.setStyle("-fx-text-fill: green;");
            warningLabel.setText("Progetto completato");
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        estimatedColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDuration"));

        listTasks.getItems().setAll(project.getTasks());
        listTasks.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Task selectedTask = row.getItem();
                    goToTask(selectedTask, this.project);
                }
            });

            return row;
        });

        completedLabel.setText(completedLabel.getText()+" "+project.completedTasks()+
                "/"+project.totalTasks());
        notCompletedLabel.setText(notCompletedLabel.getText()+" "+project.notCompletedTasks()+
                "/"+project.totalTasks());
        totalLabel.setText(totalLabel.getText()+" "+project.totalTasks());
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

    @FXML
    public void completeProject() {
        if(manager.closeProject(project.hashCode())){
            warningLabel.setStyle("-fx-text-fill: green;");
            warningLabel.setText("Progetto completato con successo!");
            completeButton.setVisible(false);
            completeButton.setManaged(false);
            addTaskButton.setVisible(false);
            addTaskButton.setManaged(false);
        }else{
            warningLabel.setStyle("-fx-text-fill: red;");
            warningLabel.setText("Non è possibile completare il progetto");
        }
    }

    @FXML
    public void goToAdderTask() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screensFXML/adderTask.fxml"));

            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setProject(this.project);

            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Aggiungi task");
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void goToTask(Task task, Project project){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screensFXML/task.fxml"));

            Parent root=loader.load();

            TaskController controller = loader.getController();
            controller.setTaskProject(task, project);

            Stage stage = (Stage) homeButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(task.getName()+" (#"+task.getId()+")");
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setProject(Project selectedProject) {
        this.project=selectedProject;
        initialize();
    }
}
