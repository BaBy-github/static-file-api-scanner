package com.example.babyapiscanner.repository;

import com.example.babyapiscanner.entity.Lib;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibRepo extends JpaRepository<Lib, String> {
    public Lib findByGroupIdAndArtifactIdAndVersion(String groupId, String artifactId, String version);
}
