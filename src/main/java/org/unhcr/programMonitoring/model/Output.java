package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "outputs")
public class Output extends BaseEntity<Long> {

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

    @OneToMany(mappedBy = "output", fetch = FetchType.LAZY)
    Set<PerformanceIndicator> performanceIndicators = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "objetive_id", foreignKey = @ForeignKey(name = "fk_outputs_objetives"))
    private Objetive objetive;

    private void addPerformanceIndicators(PerformanceIndicator performanceIndicator) {
        if (!this.performanceIndicators.add(performanceIndicator)) {
            this.performanceIndicators.remove(performanceIndicator);
            this.performanceIndicators.add(performanceIndicator);
        }
    }

    private void removePerformanceIndicators(PerformanceIndicator performanceIndicator) {
        this.performanceIndicators.remove(performanceIndicator);
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

    public Set<PerformanceIndicator> getPerformanceIndicators() {
        return performanceIndicators;
    }

    public void setPerformanceIndicators(Set<PerformanceIndicator> performanceIndicators) {
        this.performanceIndicators = performanceIndicators;
    }

    public Objetive getObjetive() {
        return objetive;
    }

    public void setObjetive(Objetive objetive) {
        this.objetive = objetive;
    }

    @Override
    public String toString() {
        return "Output{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                '}';
    }
}