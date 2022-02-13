package com.skdziwak.factoriolang.blueprint.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConnectionPort {
    private final List<ConnectionEntity> green = new ArrayList<>();
    private final List<ConnectionEntity> red = new ArrayList<>();
}
