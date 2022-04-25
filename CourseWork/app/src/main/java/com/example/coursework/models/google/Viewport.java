package com.example.coursework.models.google;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "northeast",
        "southwest"
})
public class Viewport {

    @JsonProperty("northeast")
    private Northeast_ northeast;
    @JsonProperty("southwest")
    private Southwest_ southwest;

    @JsonProperty("northeast")
    public Northeast_ getNortheast() {
        return northeast;
    }

    @JsonProperty("northeast")
    public void setNortheast(Northeast_ northeast) {
        this.northeast = northeast;
    }

    @JsonProperty("southwest")
    public Southwest_ getSouthwest() {
        return southwest;
    }

    @JsonProperty("southwest")
    public void setSouthwest(Southwest_ southwest) {
        this.southwest = southwest;
    }

}