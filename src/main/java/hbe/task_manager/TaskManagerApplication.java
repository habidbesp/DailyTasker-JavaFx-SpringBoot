package hbe.task_manager;

import hbe.task_manager.presentation.TaskSystemFx;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(TaskManagerApplication.class, args);
		Application.launch(TaskSystemFx.class, args);
	}

}
