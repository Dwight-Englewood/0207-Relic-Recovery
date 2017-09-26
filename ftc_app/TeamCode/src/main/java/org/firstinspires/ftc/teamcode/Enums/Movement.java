package org.firstinspires.ftc.teamcode.Enums;

/**
 * Created by weznon on 9/22/17.
 */

//Reference: https://softwareengineering.stackexchange.com/questions/159804/how-do-you-encode-algebraic-data-types-in-a-c-or-java-like-language


public class Movement {

public static final class MovementAngle extends Movement {
        private int test;
        private MovementEnum name;

        public MovementAngle(int a) {
            this.name = MovementEnum.ANGLE;
            this.test = a;
        }
    }

    public static final class MovementOther extends Movement {
        private MovementEnum name;

        public MovementOther(MovementEnum move) {
            if (move == MovementEnum.ANGLE) {
                assert(false); 
                //TODO: Figure out a better thing to put here. Maybe make it default to straight, and log an error?
            } else {
                this.name = move;
            }

        }
    }
    
    /* Example usage of the Movement object
    
     */
    private static int merp(Movement a) {
        if (a.getClass() == MovementAngle.class) {
            return 1;
        } else if (a.getClass() == MovementOther.class) {
            return 0;
        } else {
            return 0;
        }
    }

    private static void derp () {
        MovementOther asd = new MovementOther(MovementEnum.BACKWARD);
        System.out.println(merp(asd));
        MovementAngle dsa = new MovementAngle(5);
        System.out.println(merp(dsa));

    }
}
