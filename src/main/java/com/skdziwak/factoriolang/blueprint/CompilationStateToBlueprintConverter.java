package com.skdziwak.factoriolang.blueprint;

import com.skdziwak.factoriolang.blueprint.components.*;
import com.skdziwak.factoriolang.compilation.CompilationState;
import com.skdziwak.factoriolang.compilation.Instruction;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class CompilationStateToBlueprintConverter {

    public static Blueprint convert(CompilationState compilationState) {
        Blueprint blueprint = new Blueprint();
        blueprint.setLabel("Factorio Lang Program");
        blueprint.setIcons(List.of(new Icon(new Signal("item", "constant-combinator"), 1)));

        int x = 1;
        Block lastBlock = null;
        for (Instruction instruction : compilationState.getInstructions()) {
            Block block = new Block(blueprint, instruction, x++);
            block.addToBlueprint(blueprint);

            if (lastBlock != null) {
                block.connect(lastBlock);
            }

            lastBlock = block;
        }

        return blueprint;
    }

    private static class Block {
        @Getter
        private final Entity constantCombinator;
        @Getter
        private final Entity deciderCombinator;
        @Getter
        private final Entity lamp;

        Block(Blueprint blueprint, Instruction instruction, int x) {
            Map<String, Integer> signals = instruction.toMap();
            constantCombinator = new Entity(blueprint.nextId(), "constant-combinator");
            constantCombinator.setPosition(new Position((double) x, 1.5));

            ControlBehavior controlBehavior = constantCombinator.getControlBehavior();
            List<Filter> filters = controlBehavior.getFilters();
            int filterIndex = 1;
            for (String signalName : signals.keySet()) {
                Integer count = signals.get(signalName);
                Signal signal = new Signal("virtual", signalName);
                Filter filter = new Filter(signal, count, filterIndex++);
                filters.add(filter);
            }

            deciderCombinator = new Entity(blueprint.nextId(), "decider-combinator");
            deciderCombinator.setPosition(new Position((double) x, 0.0));
            controlBehavior = deciderCombinator.getControlBehavior();
            controlBehavior.setDeciderConditions(DeciderConditions.builder()
                    .firstSignal(new Signal("virtual", "signal-E"))
                    .comparator("=")
                    .constant(x)
                    .outputSignal(new Signal("virtual", "signal-everything"))
                    .copyCountFromInput(true)
                    .build());

            deciderCombinator.getConnections().connect(Connections.Port.PORT_1, Connections.Cable.GREEN, constantCombinator);
            constantCombinator.getConnections().connect(Connections.Port.PORT_1, Connections.Cable.GREEN, deciderCombinator, 1);

            lamp = new Entity(blueprint.nextId(), "small-lamp");
            lamp.setPosition(new Position((double) x, -1.5));
            controlBehavior = lamp.getControlBehavior();
            controlBehavior.setCircuitCondition(DeciderConditions.builder()
                    .firstSignal(new Signal("virtual", "signal-E"))
                    .comparator("=")
                    .constant(x)
                    .outputSignal(null)
                    .copyCountFromInput(null)
                    .build());

            lamp.getConnections().connect(Connections.Port.PORT_1, Connections.Cable.RED, deciderCombinator, 1);
            deciderCombinator.getConnections().connect(Connections.Port.PORT_1, Connections.Cable.RED, lamp);
        }

        void addToBlueprint(Blueprint blueprint) {
            blueprint.getEntities().add(constantCombinator);
            blueprint.getEntities().add(deciderCombinator);
            blueprint.getEntities().add(lamp);
        }

        void connect(Block block) {
            block.getDeciderCombinator().getConnections().connect(Connections.Port.PORT_1, Connections.Cable.RED, this.getDeciderCombinator(), 1);
            block.getDeciderCombinator().getConnections().connect(Connections.Port.PORT_2, Connections.Cable.GREEN, this.getDeciderCombinator(), 2);

            this.getDeciderCombinator().getConnections().connect(Connections.Port.PORT_1, Connections.Cable.RED, block.getDeciderCombinator(), 1);
            this.getDeciderCombinator().getConnections().connect(Connections.Port.PORT_2, Connections.Cable.GREEN, block.getDeciderCombinator(), 2);
        }
    }
}
