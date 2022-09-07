package com.example.babyapiscanner.entity;

import cn.hutool.core.date.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "scan_project")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    private String id;

    private String gitUrl;

    private Timestamp createTime;

    @OneToMany(mappedBy = "project")
    private List<ProjectApi> projectApis;

    @ManyToMany(cascade = {CascadeType.REFRESH},targetEntity = Lib.class, fetch = FetchType.EAGER)
    @JoinTable(name = "m_project_lib",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "lib_id")})
    private List<Lib> libs;
}
