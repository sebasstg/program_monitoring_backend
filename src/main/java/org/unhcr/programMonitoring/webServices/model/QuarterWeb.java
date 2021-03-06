package org.unhcr.programMonitoring.webServices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sagatechs.generics.persistence.model.State;
import org.unhcr.programMonitoring.model.QuarterNumber;

import java.math.BigDecimal;

public class QuarterWeb {

    public QuarterWeb() {
    }

    public QuarterWeb(Long id, QuarterNumber quarterNumber, String commentary) {
        this.id = id;
        this.quarterNumber = quarterNumber;
        this.commentary = commentary;
    }

    private Long id;
    private QuarterNumber quarterNumber;
    private String commentary;
    private BigDecimal totalExecution=BigDecimal.ZERO;

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

    public BigDecimal getTotalExecution() {
        return totalExecution;
    }

    public void setTotalExecution(BigDecimal totalExecution) {
        this.totalExecution = totalExecution;
    }


}
