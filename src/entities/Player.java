package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.util.ArrayList;
import java.util.HashSet;


/**
 * Created by AW on 2017-09-05.
 */
public class Player extends Entity {

    private ArrayList<Integer> restrictedX = new ArrayList<>();
    private ArrayList<Integer> restrictedZ = new ArrayList<>();

    private boolean alreadyJumped = false;
    private static final float RUN_SPEED = 12;
    private static final float TURN_SPEED = 130;

    private static final float TERRAIN_H = 0;

    private static final float GRAVITY = -40;
    private static final float JUMP_POWER = 15;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    private int NoOfBarriersPassed = 0;
    private int NoOfBarriersAtX = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
        //zabronione ztki
        restrictedZ.add(-46);
        restrictedZ.add(-20);
        restrictedZ.add(-10);
        restrictedZ.add(4);
        restrictedZ.add(10);
        restrictedX.add(94);
        restrictedX.add(60);
            }

    public void move(Vector3f ghostsPosition){
        //kolizja 105 i 95 na x wchodzenie na sciany
        if (getPosition().z < 1.49f) {
            if ((getPosition().x >= 104)) {
                setPosition(new Vector3f(103.96f, getPosition().y, getPosition().z));
            } else if ((getPosition().x <= 96)) {
                setPosition(new Vector3f(96.04f, getPosition().y, getPosition().z));
            }
        }
        else {  //tutaj wchodzi dopiero jak zmienia sie w prawo droga
            if (restrictedX.size() > 0) {
                float tempX = restrictedX.get(NoOfBarriersAtX);
                if (tempX >= 94 && getPosition().x < 95) {
                    if (alreadyJumped){
                        //do nothing to skip else
                    }
                    else{
                        //jak przeskoczyl to super
                        if (getPosition().x > 90 && getPosition().x < 94){
                            //jak nie to na poczatek
                            setPosition(new Vector3f(100,getPosition().y,6f));
                        }
                        else {
                            NoOfBarriersAtX++;
                        }
                    }
                }
            }
            if (getPosition().z < 1.5){
                setPosition(new Vector3f(getPosition().x, getPosition().y, 1.55f));
            }
        }

        // blokowanie z (przeszkoda)
            if (restrictedZ.size() > 0) {
                float tempZ = restrictedZ.get(NoOfBarriersPassed);
                if (tempZ == -10){
                    if (getPosition().x < 97) {
                        if (getPosition().z > -4){
                            NoOfBarriersPassed++;
                        }
                    }
                    else if (tempZ < getPosition().z) {
                        setPosition(new Vector3f(getPosition().x, getPosition().y, tempZ));
                    }
                }
                else if (tempZ == 4 || tempZ == -20){
                    if (alreadyJumped && tempZ == 4){
                        NoOfBarriersPassed++;
                    }
                    else if (tempZ < getPosition().z) {
                        setPosition(new Vector3f(getPosition().x, getPosition().y, -50));
                    }
                }
                else {
                    if (tempZ < getPosition().z) {
                        setPosition(new Vector3f(getPosition().x, getPosition().y, tempZ));
                    }
                }
            }
            checkInputs();
            super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
            float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
            float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
            super.increasePosition(dx, 0, dz);
            upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
            super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
            if (super.getPosition().y < TERRAIN_H) {
                upwardSpeed = 0;
                alreadyJumped = false;
                super.getPosition().y = TERRAIN_H;
            }

            //jak wejdzie na ducha
            if ((((int)ghostsPosition.x == (int)getPosition().x) && ((int)ghostsPosition.z == (int)getPosition().z)) || (((int)ghostsPosition.x == ((int)getPosition().x) + 1) && ((int)ghostsPosition.z == (int)getPosition().z))){
                setPosition(new Vector3f(100f,0f,-50f));
            }

    }

    private void jump(){
        if (!alreadyJumped) {
            this.upwardSpeed = JUMP_POWER;
            alreadyJumped = true;
        }
    }

    private void checkInputs(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            this.currentSpeed = RUN_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            this.currentSpeed = -RUN_SPEED;
        }
        else this.currentSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            this.currentTurnSpeed = -TURN_SPEED;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_A)){
            this.currentTurnSpeed = TURN_SPEED;
        }else this.currentTurnSpeed = 0;

        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            jump();
    }

    public void deleteFromRestrictedZ(Integer delete){
        restrictedZ.remove(delete);
    }
    public void increaseNoOfBarriersPassed(){
        NoOfBarriersPassed++;
    }
}
