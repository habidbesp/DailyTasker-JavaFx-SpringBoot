package hbe.task_manager.repository;

import hbe.task_manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository< Task, Integer > {
}
