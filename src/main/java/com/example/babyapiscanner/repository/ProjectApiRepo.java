package com.example.babyapiscanner.repository;

import com.example.babyapiscanner.entity.ProjectApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;

@Repository
public interface ProjectApiRepo extends JpaRepository<ProjectApi, String> {
    public ProjectApi findByFilePathAndApiAndMethod(String filePath, String api, String method);
}
