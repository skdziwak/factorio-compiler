package com.skdziwak.factoriolang.simulator;

import com.skdziwak.factoriolang.HardwareConstants;
import com.skdziwak.factoriolang.compilation.Instruction;

import java.util.LinkedList;
import java.util.List;

public class HardwareSimulator {
    private final int[] registers = new int[HardwareConstants.REGISTERS_COUNT];
    private final LinkedList<Integer> stack = new LinkedList<>();
    private final int[] ram = new int[HardwareConstants.RAM_SIZE];
    private int currentIndex = 0;

    public void simulate(List<Instruction> instructionList) {
        int len = instructionList.size();
        while (currentIndex < len) {
            simulate(instructionList.get(currentIndex));
            currentIndex++;
        }
    }

    private void simulate(Instruction instruction) {
        int a = instruction.getSignalA();
        int b = instruction.getSignalB();
        int c = instruction.getSignalC();

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
        this.registers[index] = value;
    }

    private int getRegister(int index) {
        validateRegisterIndex(index);
        return this.registers[index];
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
