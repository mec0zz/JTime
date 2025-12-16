package it.unicam.cs.mpgc.jtime126225.controller;

import it.unicam.cs.mpgc.jtime126225.model.Manager;
import it.unicam.cs.mpgc.jtime126225.model.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController {
    private Manager manager;

    @FXML
    private TableView<Project> listProjects;
    @FXML
    private TableColumn<Project, String> idColumn;
    @FXML
    private TableColumn<Project,String> nameColumn;
    @FXML
    private TableColumn<Project,String> statusColumn;
    @FXML
    private TableColumn<Project,String> descColumn;
    @FXML
    private Button createProjButton;
    @FXML
    private Label completedProjects;
    @FXML
    private Label notCompletedProjects;
    @FXML
    private Label totalProjects;

    @FXML
    private void initialize(){
        manager=Manager.getManager();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        listProjects.getItems().setAll(manager.getProjects());

        completedProjects.setText(completedProjects.getText()+" "+
                manager.completedProjects()+"/"+manager.totalProjects());

        notCompletedProjects.setText(notCompletedProjects.getText()+" "+
                manager.notCompletedProjects()+"/"+manager.totalProjects());

        totalProjects.setText(totalProjects.getText()+" "+manager.totalProjects());

        //TODO implementare doppio click item
        listProjects.setRowFactory(tv -> {
            TableRow<Project> row = new TableRow<Project>();

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Project selectedProject = row.getItem();
                    goToProject(selectedProject);
                }
            });

            return row;
        });
    }


    @FXML
    public void goToAdder() {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/screensFXML/adderProject.fxml"));
            Stage stage = (Stage)listProjects.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Aggiungi transazione");
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void goToProject(Project selectedProject){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/screensFXML/project.fxml"));

            Parent root = loader.load();

            ProjectController controller= loader.getController();
            controller.setProject(selectedProject);

            Stage stage = (Stage)listProjects.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(selectedProject.getName());
            stage.centerOnScreen();
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
