package com.project.schoolmanagment.entity.concretes.buisnes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lessonName;

    private Integer creditScore;

    private  Boolean isCompulsory;

    //TODO lessonProgram will be added.
    @JsonIgnore
    @ManyToMany(mappedBy = "lessons", cascade = CascadeType.REMOVE)
    private Set<LessonProgram> lessonPrograms; // (H)get-> null
}
