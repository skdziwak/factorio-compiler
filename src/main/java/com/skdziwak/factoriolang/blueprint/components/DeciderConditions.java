package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeciderConditions {
    @JsonProperty("first_signal")
    private Signal firstSignal;
    private Integer constant;
    private String comparator;
    @JsonProperty("output_signal")
    private Signal outputSignal;
    @JsonProperty("copy_count_from_input")
    private Boolean copyCountFromInput;
}
