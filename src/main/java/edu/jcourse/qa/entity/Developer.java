package edu.jcourse.qa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "developer")
public class Developer {
    @Id
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String speciality;
    private Status status;
}