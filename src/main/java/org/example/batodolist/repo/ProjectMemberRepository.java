package org.example.batodolist.repo;

import org.example.batodolist.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember findProjectMemberByProject_IdAndUser_Id(Long projectId, Long userId);

    ProjectMember findProjectMemberByUser_Username(String userName);
}
