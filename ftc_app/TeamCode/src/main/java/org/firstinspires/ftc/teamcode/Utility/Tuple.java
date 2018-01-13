package org.firstinspires.ftc.teamcode.Utility;

//im a little tuple, short and stout
public class Tuple<T> {
    public T value;
    public T value2;
    public org.firstinspires.ftc.teamcode.Utility.Flag flag;

    public Tuple(T value, org.firstinspires.ftc.teamcode.Utility.Flag flag) {
        this.value = value;
        this.flag = flag;
    }
    public Tuple(T value, T value2) {
        this.value = value;
        this.value2 = value2;
    }

}
