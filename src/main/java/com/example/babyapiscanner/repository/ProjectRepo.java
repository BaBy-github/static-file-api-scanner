package com.example.babyapiscanner.repository;

import com.example.babyapiscanner.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends JpaRepository<Project, String> {
}
