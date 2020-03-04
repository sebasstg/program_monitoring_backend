package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "objetives")
public class Objetive extends BaseEntity<Long> {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "description", nullable = false, unique = true, columnDefinition = "text")
    private String description;

    @Column(name = "state", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "rigth_group_id",foreignKey = @ForeignKey(name = "fk_objetives_right_groups"))
    private RightGroup rightGroup;

    @OneToMany( mappedBy = "objetive",fetch = FetchType.LAZY)
    Set<Output> outputs= new HashSet<>();

    private void addOutput(Output output){
        if(!this.outputs.add(output)){
            this.outputs.remove(output);
            this.outputs.add(output);
        }
    }

    private void removeOutput(Output output){
        this.outputs.remove(output);
    }

    @Override
    public Long getId() {
        return id;
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

    public RightGroup getRightGroup() {
        return rightGroup;
    }

    public void setRightGroup(RightGroup rightGroup) {
        this.rightGroup = rightGroup;
    }

    public Set<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(Set<Output> outputs) {
        this.outputs = outputs;
    }

    @Override
    public String toString() {
        return "Objetive{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}