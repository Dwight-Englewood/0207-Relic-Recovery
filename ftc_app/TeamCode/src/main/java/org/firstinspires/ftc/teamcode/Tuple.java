package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Enums.EnumController;

//im a little tuple, short and stout
public class Tuple<T> {
    public T value;
    public T value2;
    public org.firstinspires.ftc.teamcode.Enums.Flag flag;

    public Tuple(T value, org.firstinspires.ftc.teamcode.Enums.Flag flag) {
        this.value = value;
        this.flag = flag;
    }
    public Tuple(T value, T value2) {
        this.value = value;
        this.value2 = value2;
    }

}
