package org.unhcr.programMonitoring.model;

import com.sagatechs.generics.persistence.model.BaseEntity;
import com.sagatechs.generics.persistence.model.State;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "program_monitoring", name = "quarter")
public class Quarter extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quarter_number", nullable = false)
    private QuarterNumber quarterNumber;

    @Column(name = "commentary", nullable = true, columnDefinition = "text")
    private String commentary;

    @OneToMany(mappedBy = "quarter", fetch = FetchType.LAZY)
    private Set<IndicatorValue> indicatorValues = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "indicatorExecution_id", foreignKey = @ForeignKey(name = "fk_quarter_indicator_execution"))
    private IndicatorExecution indicatorExecution;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuarterNumber getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(QuarterNumber quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Set<IndicatorValue> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(Set<IndicatorValue> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }

    public IndicatorExecution getIndicatorExecution() {
        return indicatorExecution;
    }

    public void setIndicatorExecution(IndicatorExecution indicatorExecution) {
        this.indicatorExecution = indicatorExecution;
    }

    public void addIndicatorValue(IndicatorValue indicatorValue) {
        indicatorValue.setQuarter(this);
        if(!this.indicatorValues.add(indicatorValue)){
            this.indicatorValues.add(indicatorValue);
            this.indicatorValues.add(indicatorValue);
        }

    }


}