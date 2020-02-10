package com.sagatechs.generics.service.smsService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class SmsMessageWeb {

    public SmsMessageWeb(String toNumber, String content) {
        this.toNumber = toNumber;
        this.content = content;
    }

    public SmsMessageWeb() {


    }

    @JsonProperty("to_number")
    private String toNumber;

    @JsonProperty("content")
    private String content;

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
