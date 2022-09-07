package com.example.babyapiscanner.repository;

import com.example.babyapiscanner.entity.LibApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibApiRepo extends JpaRepository<LibApi, String> {
}
