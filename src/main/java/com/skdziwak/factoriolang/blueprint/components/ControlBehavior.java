package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ControlBehavior {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Filter> filters = new ArrayList<>();

    @JsonProperty("decider_conditions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DeciderConditions deciderConditions;
}
