package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

/**
 * Created by weznon on 11/27/17.
 */

public class EunmController<T> {

    public boolean abnormalFlag;
    public final T defaultVal;
    public T currentVal;
    public String log;

    public EunmController(T defaultVal) {
        this.abnormalFlag = false;
        this.defaultVal = defaultVal;
        this.currentVal = defaultVal;
    }

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

    public void reset() {
        this.abnormalFlag = false;
        this.currentVal = defaultVal;
    }

}

enum flag {
    OVERRIDE,
    MODIFY,
    NORMAL
}
