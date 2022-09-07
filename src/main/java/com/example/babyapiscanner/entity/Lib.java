package com.example.babyapiscanner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scan_lib")
@AllArgsConstructor
@NoArgsConstructor
public class Lib {
    @Id
    private String id;

    @Column(nullable = false)
    private String groupId;

    @Column(nullable = false)
    private String artifactId;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private Boolean isManagement;

    @OneToMany(mappedBy = "lib")
    private List<LibApi> libApis;

    @ManyToMany(targetEntity = Project.class, mappedBy = "libs")
    private List<Project> projects;
}
