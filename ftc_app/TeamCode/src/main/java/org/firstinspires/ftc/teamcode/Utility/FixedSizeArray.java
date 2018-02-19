package org.firstinspires.ftc.teamcode.Utility;

import java.util.ArrayList;

/**
 * Created by weznon on 2/19/18.
 */

public class FixedSizeArray<T> extends ArrayList {

    public FixedSizeArray (int i) {

    }
    @Deprecated
    @Override
    public boolean add(Object object) {
        return false;
    }

    @Deprecated
    @Override
    public void add(int index, Object object) {

    }

    @Deprecated
    @Override
    public Object remove(int index) {
        return new Object();
        //lol
    }


}
