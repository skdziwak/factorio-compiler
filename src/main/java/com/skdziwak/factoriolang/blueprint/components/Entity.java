package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Entity {
    @JsonProperty("entity_number")
    private final Integer id;
    private final String name;
    private Position position = new Position(0.0, 0.0);
    @JsonProperty("control_behavior")
    private final ControlBehavior controlBehavior = new ControlBehavior();
    private final Connections connections = new Connections();
}
