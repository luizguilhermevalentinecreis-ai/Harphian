package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Atirador {

    private DcMotor rodaTiro;
    private Servo travaObjeto;

    public Atirador(HardwareMap hardwareMap) {
        rodaTiro    = hardwareMap.get(DcMotor.class, "shootwheel");
        travaObjeto = hardwareMap.get(Servo.class, "artifactstopper");
        configurar();
    }

    private void configurar() {
        travaObjeto.setPosition(0.2);
        rodaTiro.setPower(0.0);
    }

    public void controlar(boolean atirar) {
        if (atirar) {
            travaObjeto.setPosition(0.0);
            rodaTiro.setPower(0.8);
        } else {
            travaObjeto.setPosition(0.2);
            rodaTiro.setPower(0.0);
        }
    }
}
