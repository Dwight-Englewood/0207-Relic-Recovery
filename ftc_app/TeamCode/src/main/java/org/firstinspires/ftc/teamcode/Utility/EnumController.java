package org.firstinspires.ftc.teamcode.Utility;

import java.util.ArrayList;

/**
 * Created by weznon on 11/27/17.
 */

public class EnumController<T> {

    public final T defaultVal;
    public T currentVal;
    public ArrayList<T> instruction;
    public ArrayList<Integer> priorities;


    public EnumController(T defaultVal) {
        this.defaultVal = defaultVal;
        this.instruction = new ArrayList<>();
        this.priorities = new ArrayList<>();
    }

    public void addInstruction(T newVal, int Flag) {
        this.instruction.add(newVal);
        this.priorities.add(Flag);
    }

    public T process() {
        if (this.instruction.size() == 0) {
            return this.defaultVal;
        }

        T thonking = defaultVal;
        Integer merp = 0;

        for (int i = 0; i < this.instruction.size(); i++) {
            T tempV = this.instruction.get(i);
            Integer tempI = this.priorities.get(i);
            if (tempI > merp) {
                merp = tempI;
                thonking = tempV;
            }
        }

        return (this.currentVal);
    }

    public void reset() {
        this.currentVal = defaultVal;
    }

}

