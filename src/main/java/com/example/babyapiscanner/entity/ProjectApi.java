package com.example.babyapiscanner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "scan_project_api")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectApi {
    @Id
    private String id;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String api;

    @Column(nullable = false)
    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}

