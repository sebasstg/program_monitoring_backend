package org.unhcr.programMonitoring.webServices.model;

import org.unhcr.programMonitoring.model.QuarterNumber;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class QuarterValuesWeb extends QuarterWeb{

    public QuarterValuesWeb() {
    }


    public QuarterValuesWeb(Long id, QuarterNumber quarterNumber, String commentary, List<IndicatorValueWeb> indicatorValues) {
        super(id, quarterNumber, commentary);
        this.indicatorValues = indicatorValues;
    }

    private List<IndicatorValueWeb> indicatorValues= new ArrayList<>();

    public List<IndicatorValueWeb> getIndicatorValues() {
        return indicatorValues;
    }

    public void setIndicatorValues(List<IndicatorValueWeb> indicatorValues) {
        this.indicatorValues = indicatorValues;
    }
}
