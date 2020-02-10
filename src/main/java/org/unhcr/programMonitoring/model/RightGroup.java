package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "right_groups")
public class RightGroup extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description", nullable = false, unique = true, columnDefinition = "text")
    private String description;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany( mappedBy = "rightGroup",fetch = FetchType.LAZY)
    private Set<Objetive> objetives= new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    private void addObjetive(Objetive objetive){
        if(!this.objetives.add(objetive)){
            this.objetives.remove(objetive);
            this.objetives.add(objetive);
        }
    }

    private void removeObjetive(Objetive objetive){
        this.objetives.remove(objetive);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Objetive> getObjetives() {
        return objetives;
    }

    public void setObjetives(Set<Objetive> objetives) {
        this.objetives = objetives;
    }

    @Override
    public String toString() {
        return "RightGroup{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}