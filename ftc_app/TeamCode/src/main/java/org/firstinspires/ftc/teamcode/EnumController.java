package org.firstinspires.ftc.teamcode;

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

    public void addInstruction(T newVal, flag flag) {
        this.instructionList.add(new Tuple<T>(newVal, flag));
    }

    public T processAndGetCurrentVal() {
        if (this.instructionList.size() == 0) {
            return this.defaultVal;
        }

        for (int i = 0; i < this.instructionList.size(); i++) {
            //MODIFY means it cannot be overriden afterwards, except by OVERRIDE
            if (this.instructionList.get(i).flag == flag.MODIFY || this.instructionList.get(i).flag == flag.m) {
                if (abnormalFlag) {
                    ;
                } else {
                    abnormalFlag = true;
                    this.currentVal = this.instructionList.get(i).value;
                }
                //NORMAL means it can be overriden by anything, including other normals
            } else if (this.instructionList.get(i).flag == flag.NORMAL || this.instructionList.get(i).flag == flag.n) {
                if (abnormalFlag) {
                    ;
                } else {
                    this.currentVal = this.instructionList.get(i).value;
                }
                //OVERRIDE will break the loop - so using this means no matter what other stuff was, do this
            } else if (this.instructionList.get(i).flag == flag.OVERRIDE || this.instructionList.get(i).flag == flag.o) {
                this.currentVal = this.instructionList.get(i).value;
                break;
            }
        }
        return (this.currentVal);
    }

    /*
    public void modifyVal(T newVal, flag flag) {

        if (flag == flag.MODIFY) {
            if (abnormalFlag) {
                return;
            } else {
                abnormalFlag = true;
                this.currentVal = newVal;
            }
        } else if (flag == flag.NORMAL) {
            if (abnormalFlag) {
                return;
            } else {
                this.currentVal = newVal;
            }
        } else if (flag == flag.OVERRIDE) {
            this.currentVal = newVal;
        }
    }
    */

    public void reset() {
        this.abnormalFlag = false;
        this.currentVal = defaultVal;
    }

}

enum flag {
    OVERRIDE,
    o,
    MODIFY,
    m,
    NORMAL,
    n
}
