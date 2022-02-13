package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionEntity{
    @JsonProperty("entity_id") Integer entityId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("circuit_id") Integer circuitId;
}
