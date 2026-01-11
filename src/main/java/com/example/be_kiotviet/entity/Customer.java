package com.example.be_kiotviet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    private String name; // "Công ty Cà Phê ABC"
    private String email;
    private String phone;
    private String taxCode;
    private String packageType; // ENTERPRISE cho chuỗi

    @OneToMany(mappedBy = "customer")
    private List<Shops> shops = new ArrayList<>();
}
