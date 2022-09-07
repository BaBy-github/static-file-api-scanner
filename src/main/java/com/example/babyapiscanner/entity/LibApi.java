package com.example.babyapiscanner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "scan_lib_api")
@AllArgsConstructor
@NoArgsConstructor
public class LibApi {
    @Id
    private String id;

    @Column(nullable = false)
    private String api;

    @Column(nullable = false)
    private String filePath;

    private String method;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lib lib;
}
