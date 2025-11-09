package org.example.batodolist.repo;

import org.example.batodolist.model.TaskCheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCheckListRepository extends JpaRepository<TaskCheckList, Long> {
}
