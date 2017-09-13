package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.util.HashSet;


/**
 * Created by AW on 2017-09-05.
 */
public class Player extends Entity {

    private HashSet<Integer> restrictedZ = new HashSet<>();

    private boolean alreadyJumped = false;
    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 140;

    private static final float TERRAIN_H = 0;

    private static final float GRAVITY = -40;
    private static final float JUMP_POWER = 15;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
        //zabronione ztki
        restrictedZ.add(-44);
    }

    public void move(Vector3f ghostsPositon){
        //kolizja 105 i 95 na x
            if ((getPosition().x >= 104)){
                setPosition(new Vector3f(103.96f,getPosition().y,getPosition().z));
            } else if ((getPosition().x <= 96)){
                setPosition(new Vector3f(96.04f,getPosition().y,getPosition().z));
            }
            float tempZ = getPosition().z;
            if (restrictedZ.contains(Math.round(tempZ)) || restrictedZ.contains(Math.round(tempZ+1))){
                setPosition(new Vector3f(getPosition().x,getPosition().y,tempZ - 0.51f));
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

            Vector3f tempPosition =  ghostsPositon;
            if ((((int)tempPosition.x == (int)getPosition().x) && ((int)tempPosition.z == (int)getPosition().z)) || (((int)tempPosition.x == ((int)getPosition().x) + 1) && ((int)tempPosition.z == (int)getPosition().z))){
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

    public void deleteFromRestrictedZ(int delete){
        restrictedZ.remove(delete);
    }
}
