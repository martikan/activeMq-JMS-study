package org.martikan.jms.hm.model;

import java.io.Serial;
import java.io.Serializable;

public class Patient implements Serializable {

    @Serial
    private static final long serialVersionUID = 2046157073850141759L;

    private Integer id;

    private String name;

    private String insuranceProvider;

    private Double copay;

    private Double amountToBePayed;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsuranceProvider() {
        return insuranceProvider;
    }

    public void setInsuranceProvider(String insuranceProvider) {
        this.insuranceProvider = insuranceProvider;
    }

    public Double getCopay() {
        return copay;
    }

    public void setCopay(Double copay) {
        this.copay = copay;
    }

    public Double getAmountToBePayed() {
        return amountToBePayed;
    }

    public void setAmountToBePayed(Double amountToBePayed) {
        this.amountToBePayed = amountToBePayed;
    }
}
