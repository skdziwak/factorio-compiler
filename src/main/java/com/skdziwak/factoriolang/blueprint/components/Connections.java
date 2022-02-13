package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skdziwak.factoriolang.blueprint.components.filters.ConnectionPortFilter;
import lombok.Data;

@Data
public class Connections {

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ConnectionPortFilter.class)
    @JsonProperty("1")
    private ConnectionPort port1 = new ConnectionPort();

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ConnectionPortFilter.class)
    @JsonProperty("2")
    private ConnectionPort port2 = new ConnectionPort();

    public enum Port {
        PORT_1,
        PORT_2
    }

    public enum Cable {
        GREEN,
        RED
    }

    public void connect(Port port, Cable cable, Entity entity) {
        ConnectionPort connectionPort = switch (port) {
            case PORT_1 -> this.port1;
            case PORT_2 -> this.port2;
        };
        switch (cable) {
            case RED -> connectionPort.getRed().add(ConnectionEntity.builder().entityId(entity.getId()).build());
            case GREEN -> connectionPort.getGreen().add(ConnectionEntity.builder().entityId(entity.getId()).build());
        }
    }

    public void connect(Port port, Cable cable, Entity entity, Integer circuitId) {
        ConnectionPort connectionPort = switch (port) {
            case PORT_1 -> this.port1;
            case PORT_2 -> this.port2;
        };
        switch (cable) {
            case RED -> connectionPort.getRed().add(ConnectionEntity.builder().entityId(entity.getId()).circuitId(circuitId).build());
            case GREEN -> connectionPort.getGreen().add(ConnectionEntity.builder().entityId(entity.getId()).circuitId(circuitId).build());
        }
    }
}
