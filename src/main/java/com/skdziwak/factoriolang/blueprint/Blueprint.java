package com.skdziwak.factoriolang.blueprint;

import com.skdziwak.factoriolang.blueprint.components.Entity;
import com.skdziwak.factoriolang.blueprint.components.Icon;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Blueprint {
    private List<Icon> icons = new ArrayList<>();
    private List<Entity> entities = new ArrayList<>();
    private final String item = "blueprint";
    private final Long version = 281479275151360L;
    private String label = "Blueprint";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Integer nextId = 1;

    public Integer nextId() {
        return nextId++;
    }
}
