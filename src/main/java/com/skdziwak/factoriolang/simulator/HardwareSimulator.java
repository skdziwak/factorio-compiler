package com.skdziwak.factoriolang.simulator;

import com.skdziwak.factoriolang.AssemblyHumanizer;
import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.Instruction;
import com.skdziwak.factoriolang.constants.InstructionType;
import com.skdziwak.factoriolang.constants.MathOperator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HardwareSimulator {
    private final int[] registers = new int[HardwareConstants.REGISTERS_COUNT];
    private final LinkedList<Integer> stack = new LinkedList<>();
    private final int[] ram = new int[HardwareConstants.RAM_SIZE];
    private int currentIndex = 0;

    public String simulate(List<Instruction> instructionList, int limit) {
        StringBuilder stringBuilder = new StringBuilder();
        int len = instructionList.size();
        int i = 0;
        while (currentIndex < len) {
            if (i == limit) {
                stringBuilder.append("Simulation length limit exceeded.");
                return stringBuilder.toString();
            }
            int displayedIndex = currentIndex + 1;
            stringBuilder.append("\t[").append(displayedIndex).append("] ");
            if (displayedIndex < 100) stringBuilder.append(" ");
            if (displayedIndex < 10) stringBuilder.append(" ");
            stringBuilder.append(AssemblyHumanizer.humanizeState(instructionList.get(currentIndex), displayedIndex))
                    .append("\n");
            simulate(instructionList.get(currentIndex));
            currentIndex++;
            i++;
        }
        if (!this.stack.isEmpty()) {
            stringBuilder.append("\nStack values: ");
            stringBuilder.append(this.stack.stream().map(String::valueOf).collect(Collectors.joining(", "))).append("\n");
        }
        stringBuilder.append("\nRegister values: \n");
        arrayToString(stringBuilder, registers);
        stringBuilder.append("\nRAM values: \n");
        arrayToString(stringBuilder, ram);
        return stringBuilder.toString();
    }

    private void arrayToString(StringBuilder stringBuilder, int[] arr) {
        for (int i = 0 ; i < arr.length ; i++) {
            stringBuilder.append("\t").append(i + 1).append(": ").append(arr[i]).append("\n");
        }
    }

    private void simulate(Instruction instruction) {
        InstructionType instructionType = InstructionType.bySignal(instruction.getSignalA());
        int b = instruction.getSignalB();
        int c = instruction.getSignalC();

        switch (instructionType) {
            case COPY_REG_TO_REG -> setRegister(c, getRegister(b));
            case MATH -> setRegister(1, simulateMath(MathOperator.bySignal(b)));
            case PUSH_REG_TO_STACK -> pushStack(getRegister(b));
            case POP_STACK_TO_REG -> setRegister(b, popStack());
            case COPY_REG_TO_RAM -> setRAM(c, getRegister(b));
            case COPY_RAM_TO_REG -> setRegister(c, getRAM(b));
            case JUMP_CONSTANT_OFFSET -> this.currentIndex += b;
            case CONDITIONAL_JUMP_CONSTANT_OFFSET -> {
                if(getRegister(1) == 0) {
                    this.currentIndex += b;
                }
            }
            case JUMP_DYNAMIC_OFFSET -> this.currentIndex += getRegister(8);
            case CONDITIONAL_JUMP_DYNAMIC_OFFSET -> {
                if(getRegister(1) == 0) {
                    this.currentIndex += getRegister(8);
                }
            }
            case SET_REGISTER -> setRegister(b, c);
        }
    }
    private int simulateMath(MathOperator operator) {
        return switch (operator) {
            case ADD -> getRegister(1) + getRegister(2);
            case SUBTRACT -> getRegister(1) - getRegister(2);
            case MULTIPLY -> getRegister(1) * getRegister(2);
            case DIVIDE -> getRegister(1) / getRegister(2);
            case MODULO -> getRegister(1) % getRegister(2);
            case EQUALS -> getRegister(1) == getRegister(2) ? 1 : 0;
            case NOT_EQUALS -> getRegister(1) != getRegister(2) ? 1 : 0;
            case GREATER -> getRegister(1) > getRegister(2) ? 1 : 0;
            case LOWER -> getRegister(1) < getRegister(2) ? 1 : 0;
            case GREATER_EQUAL -> getRegister(1) >= getRegister(2) ? 1 : 0;
            case LOWER_EQUAL -> getRegister(1) <= getRegister(2) ? 1 : 0;
            case SHIFT_LEFT -> getRegister(1) << getRegister(2);
            case SHIFT_RIGHT -> getRegister(1) >> getRegister(2);
            case AND -> getRegister(1) & getRegister(2);
            case OR -> getRegister(1) | getRegister(2);
        };
    }

    private void validateRamIndex(int index) {
        if (index < 1) {
            throw new SimulationException("Unable to access RAM at address lower than 1");
        } else if (index > HardwareConstants.RAM_SIZE) {
            throw new SimulationException("Unable to access RAM at address greater than " + HardwareConstants.RAM_SIZE);
        }
    }

    private void validateRegisterIndex(int index) {
        if (index < 1) {
            throw new SimulationException("Unable to access register at address lower than 1");
        } else if (index > HardwareConstants.RAM_SIZE) {
            throw new SimulationException("Unable to access register at address greater than " + HardwareConstants.REGISTERS_COUNT);
        }
    }

    private int getRAM(int index) {
        validateRamIndex(index);
        return this.ram[index - 1];
    }

    private void setRAM(int index, int value) {
        validateRamIndex(index);
        this.ram[index - 1] = value;
    }

    private void setRegister(int index, int value) {
        validateRegisterIndex(index);
        this.registers[index - 1] = value;
    }

    private int getRegister(int index) {
        validateRegisterIndex(index);
        return this.registers[index - 1];
    }

    private int popStack() {
        if (this.stack.size() == 0) {
            throw new SimulationException("Stack is empty");
        }
        return this.stack.pop();
    }

    private void pushStack(int value) {
        if (this.stack.size() == HardwareConstants.STACK_SIZE) {
            throw new SimulationException("Stack overflow");
        }
        this.stack.push(value);
    }
}
