package com.skdziwak.factoriolang.blueprint.components;

import lombok.Data;

@Data
public class Filter {
    private final Signal signal;
    private final Integer count;
    private final Integer index;
}
