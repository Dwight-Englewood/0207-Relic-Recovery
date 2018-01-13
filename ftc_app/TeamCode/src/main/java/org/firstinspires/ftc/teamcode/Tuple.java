package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Enums.EnumController.fl;

//im a little tuple, short and stout
public class Tuple<T> {
    public T value;
    public T value2;
    public org.firstinspires.ftc.teamcode.Enums.flag flag;

    public Tuple(T value, flag flag) {
        this.value = value;
        this.flag = flag;
    }
    public Tuple(T value, T value2) {
        this.value = value;
        this.value2 = value2;
    }

}
