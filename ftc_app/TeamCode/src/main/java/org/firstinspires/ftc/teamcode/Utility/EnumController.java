package org.firstinspires.ftc.teamcode.Utility;

import java.util.ArrayList;

/**
 * The EnumController is an object which allows for easier handling of state machines(?)
 * Created by weznon on 11/27/17.
 */

public class EnumController<T> {

    private T defaultVal;
    private T currentVal;
    private ArrayList<T> instruction;
    private ArrayList<Integer> priorities;

    /**
     * Constructor
     * Takes a defaultVal, which is what EnumController#process will return if nothing is added to its internal lists
     */
    public EnumController(T defaultVal) {
        this.defaultVal = defaultVal;
        this.instruction = new ArrayList<T>();
        this.priorities = new ArrayList<Integer>();
    }

    /**
     * Handles the addition of values and the associated priority
     * in EnumController#process, it is assumed the lists are the same length. Therefore, we only have one add method, which forces this to be true
     */
    public void addInstruction(T newVal, int priority) {
        this.instruction.add(newVal);
        this.priorities.add(priority);
    }

    /** 
     * This function will iterate over the lists and take the object with the highest priority
     * The exception is when the priority is negative, which will break the loop.
     * This has applications in instances if we must be sure that something exits immedediately,
     * as we an unsure what the largest priority passed in will be
     */
    public T process() {
        if (this.instruction.size() == 0) {
            return this.defaultVal;
        }
        Integer merp = 0;

        for (int i = 0; i < this.instruction.size(); i++) {
            T tempV = this.instruction.get(i);
            Integer tempI = this.priorities.get(i);
            if (tempI < 0) {
                return tempV;
            }
            if (tempI > merp) {
                merp = tempI;
                this.currentVal = tempV;
            }
        }

        return (this.currentVal);
    }
    
    /**
     * empties the lists, to avoid conflicts with future instances of process
     */
    public void reset() {
        this.currentVal = defaultVal;
        this.instruction.clear();
        this.priorities.clear();
    }

}

