package org.firstinspires.ftc.teamcode.Enums;

import org.firstinspires.ftc.teamcode.Tuple;

import java.util.ArrayList;

/**
 * Created by weznon on 11/27/17.
 */

public class EnumController<T> {

    public boolean abnormalFlag;
    public final T defaultVal;
    public T currentVal;
    public ArrayList<Tuple<T>> instructionList;
    public String log;

    public EnumController(T defaultVal) {
        this.abnormalFlag = false;
        this.defaultVal = defaultVal;
        this.instructionList = new ArrayList<Tuple<T>>();
    }

    public void addInstruction(T newVal, Flag Flag) {
        this.instructionList.add(new Tuple<T>(newVal, Flag));
    }

    public T processAndGetCurrentVal() {
        if (this.instructionList.size() == 0) {
            return this.defaultVal;
        }

        for (int i = 0; i < this.instructionList.size(); i++) {
            //MODIFY means it cannot be overriden afterwards, except by OVERRIDE
            if (this.instructionList.get(i).flag == Flag.MODIFY || this.instructionList.get(i).flag == Flag.m) {
                if (abnormalFlag) {
                    ;
                } else {
                    abnormalFlag = true;
                    this.currentVal = this.instructionList.get(i).value;
                }
                //NORMAL means it can be overriden by anything, including other normals
            } else if (this.instructionList.get(i).flag == Flag.NORMAL || this.instructionList.get(i).flag == Flag.n) {
                if (abnormalFlag) {
                    ;
                } else {
                    this.currentVal = this.instructionList.get(i).value;
                }
                //OVERRIDE will break the loop - so using this means no matter what other stuff was, do this
            } else if (this.instructionList.get(i).flag == Flag.OVERRIDE || this.instructionList.get(i).flag == Flag.o) {
                this.currentVal = this.instructionList.get(i).value;
                break;
            }
        }
        return (this.currentVal);
    }

    /*
    public void modifyVal(T newVal, Flag Flag) {

        if (Flag == Flag.MODIFY) {
            if (abnormalFlag) {
                return;
            } else {
                abnormalFlag = true;
                this.currentVal = newVal;
            }
        } else if (Flag == Flag.NORMAL) {
            if (abnormalFlag) {
                return;
            } else {
                this.currentVal = newVal;
            }
        } else if (Flag == Flag.OVERRIDE) {
            this.currentVal = newVal;
        }
    }
    */

    public void reset() {
        this.abnormalFlag = false;
        this.currentVal = defaultVal;
    }

}

