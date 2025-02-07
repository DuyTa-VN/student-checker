package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Student {
    @jakarta.persistence.Id
    private String Id;

    @Column
    @Size(max = 50)
    private String name;

    @Column(unique = true)
    @Email
    @Size(max = 50)
    private String email;

    @Size(max = 50)
    private String sex;

    @Column
    private String fingerId;

    @Column
    private String phone;

    @OneToOne
    private Account account;

    @Size(max = 100)
    private String className;
}
