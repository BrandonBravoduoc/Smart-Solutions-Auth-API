package com.smart_solutions_auth.API.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "communes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commune {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "commune_name", nullable = false, unique = true)
    private String communeName;

    @OneToMany(mappedBy = "street")
    @JsonIgnore
    private List<UserAddress> userAddress;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;
}
