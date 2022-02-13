package com.skdziwak.factoriolang.blueprint.components.filters;

import com.skdziwak.factoriolang.blueprint.components.ConnectionPort;

public class ConnectionPortFilter {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConnectionPort port) {
            return port.getRed().isEmpty() && port.getGreen().isEmpty();
        }
        return true;
    }
}
