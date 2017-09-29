package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;

/**
 * Created by aburur on 8/6/17.
 */

//TODO: Create bot class
public class Bot
{
    /**
     * Constructor
     * Empty, since initializing the instance fields doesn't occur on object instantiation.
     * That occurs during init phase, with the .init(HardwareMap hwm, Telemetry telem)
     */
    public Bot()
    {

    }

    /**
     * Motor Declarations
     * FR, FL, BR, BL are drive train motors
     */
    DcMotor FR, FL, BR, BL;

    /**
     * Servo Declarations
     */

    /**
     * Sensor Declarations
     * BNO055IMU is the builtin gyro on the REV Module
     */
    BNO055IMU imu;

    /**
     * Variable Declarations
     */

    /**
     * Other Declarations
     * Orientation angles is used for the REv Module's gyro, to store the headings
     */
    private Orientation angles;


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

        //BNO055IMU related initialization code
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        //getting the motors from the hardware map
        FL = hardwareMap.get(DcMotor.class, "FL");
        FR = hardwareMap.get(DcMotor.class, "FR");
        BL = hardwareMap.get(DcMotor.class, "BL");
        BR = hardwareMap.get(DcMotor.class, "BR");

        //setting renmode
        FL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //setting directions for drive
        FL.setDirection(DcMotorSimple.Direction.FORWARD);
        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BL.setDirection(DcMotorSimple.Direction.FORWARD);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);

        // TODO: Test different zeropower behaviors (BRAKE, FLOAT, etc)

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);


    }

    /**
     * MovementEnum Functions
     */

    public void drive (int power) {
        FL.setPower(power);
        FR.setPower(power);
        BL.setPower(power);
        BR.setPower(power);
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
