/*
 * This file is part of the Soapbox Race World core source code.
 * If you use any of this code for third-party purposes, please provide attribution.
 * Copyright (c) 2020.
 */

package com.soapboxrace.core.jpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "VINYLSTORE")
@NamedQueries({ 
        
})
public class VinylStoreEntity {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = PersonaEntity.class, cascade = CascadeType.DETACH, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "personaId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_CAR_PERSONA_personaId"))
    private PersonaEntity persona;

    private String code;

    @OneToMany(mappedBy = "vinylstore", targetEntity = VinylStoreDataEntity.class, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(FetchMode.SUBSELECT)
    private Set<VinylStoreDataEntity> vinyl_data;
    private LocalDateTime created;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }   

    public PersonaEntity getPersona() { return persona; }
    public void setPersona(PersonaEntity personaEntity) { this.persona = personaEntity; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Set<VinylStoreDataEntity> getData() { return vinyl_data; }
    public void setData(Set<VinylStoreDataEntity> vinyl_data) { this.vinyl_data = vinyl_data; }   

    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }
}
