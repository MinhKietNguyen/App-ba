package org.example.batodolist.repo;

import org.example.batodolist.model.Project;
import org.example.batodolist.model.ProjectMember;
import org.example.batodolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember findProjectMemberByProject_IdAndUser_Id(Long projectId, Long userId);

    Optional<ProjectMember> findProjectMemberByUserAndProject(User user, Project project);
}
