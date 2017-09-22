package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aburur on 8/6/17.
 */

//TODO: Create bot class
public class Bot
{
    /**
     * Constructor
     */
    public Bot()
    {

    }

    /**
     * Motor Declarations
     */
    DcMotor FR, FL, BR, BL;

    /**
     * Servo Declarations
     */

    /**
     * Sensor Declarations
     */
    BNO055IMU imu;

    /**
     * Variable Declarations
     */

    /**
     * Other Declarations
     */

    /**
     * Initialization Function
     */
    public void init(HardwareMap hardwareMap, Telemetry telemetry) {
        this.initialize(hardwareMap, telemetry);
    }

    private void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        /*Motor Related Stuff
        Currently set all to forward, if we use mecanum it'll need to be changed
        */
        FL = hardwareMap.dcMotor.get("FL");
        FR = hardwareMap.dcMotor.get("FR");
        BL = hardwareMap.dcMotor.get("BL");
        BR = hardwareMap.dcMotor.get("BR");

        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Drive
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);


    }

    /**
     * MovementEnum Functions
     */

    public void drive (MovementEnum direction) {
        if (direction == MovementEnum.ANGLE) {

        }
    }
    /**
     * Action Functions
     */

    /**
     * Sensor-Related Functions
     */

    /**
     * Getter Functions
     */

    /**
     * Helper Functions
     */
}
