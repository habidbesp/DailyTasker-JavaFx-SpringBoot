package hbe.task_manager.controller;

import hbe.task_manager.model.Task;
import hbe.task_manager.service.TaskService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;

@Component
public class IndexController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private TaskService taskService;

    @FXML
    private TableView<Task> taskView;

    @FXML
    private TableColumn<Task, Integer> idTaskColumn;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> ownerColumn;

    @FXML
    private TableColumn<Task, String> statusColumn;

    private final ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    private TextField taskNameText;

    @FXML
    private TextField ownerText;

    @FXML
    private TextField statusText;

    private Integer idTaskIntern;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configureColumns();
        listTasks();
    }

    private void configureColumns() {
        idTaskColumn.setCellValueFactory(new PropertyValueFactory<>("idTask"));
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void listTasks() {
        logger.info("Executing listTasks()");

        taskList.clear();
        taskList.addAll(taskService.listTasks());
        taskView.setItems(taskList);
    }

    public void addTask(){
        if(idTaskIntern != null)
            return;
        if(taskNameText.getText().isEmpty()){
            showMessage("Error validation", "Provide a task");
            taskNameText.requestFocus();
            return;
        }else{
            var task = new Task();
            collectFormData(task);
            task.setIdTask(null);
            taskService.saveTask(task);
            showMessage("Information", "Task was added!");
            clearForm();
            listTasks();
        }
    }

    public void modifyTask() {
        if(idTaskIntern == null){
            showMessage("Information", "Select a Task");
            return;
        }

        if(taskNameText.getText().isEmpty()){
            showMessage("Error validation", "Provide a task");
        }

        var task = new Task();
        collectFormData(task);
        taskService.saveTask(task);
        showMessage("Information", "Task was modified!");
        clearForm();
        listTasks();
    }

    public void clearFormTask() {
        clearForm();
    }

    public void deleteTask() {
        var task = taskView.getSelectionModel().getSelectedItem();
        if(task != null) {
            logger.info("Task to Delete: " + task.toString());
            taskService.deleteTask(task);
            showMessage("Information", "Task was deleted: " + task.getIdTask());
            clearForm();
            listTasks();
        }
        else {
            showMessage("Error", "No task was selected");
        }
    }

    public void loadTaskForm(){
        var task = taskView.getSelectionModel().getSelectedItem();
        if(task != null){
            idTaskIntern = task.getIdTask();
            taskNameText.setText(task.getTaskName());
            ownerText.setText(task.getOwner());
            statusText.setText(task.getStatus());
        }

    }

    private void collectFormData(Task task){
        if(idTaskIntern != null){
            task.setIdTask(idTaskIntern);
        }
        task.setTaskName(taskNameText.getText());
        task.setOwner(ownerText.getText());
        task.setStatus(statusText.getText());
    }

    private void clearForm() {
        idTaskIntern = null;
        taskNameText.clear();
        ownerText.clear();
        statusText.clear();
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
