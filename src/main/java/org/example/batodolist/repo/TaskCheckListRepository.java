package org.example.batodolist.repo;

import org.example.batodolist.model.TaskCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCheckListRepository extends JpaRepository<TaskCheckList, Long> {
}
