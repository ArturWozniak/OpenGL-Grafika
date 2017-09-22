package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.util.HashSet;
import java.util.Random;

/**
 * Created by AW on 2017-09-08.
 */
public class Ghost extends Entity {

    private int[] xToChoose = {103,101,99,97,60,65,70,75};
    private int from;
    private int to;

    private int mode = 1;

    private static final float RUN_SPEED = 3;



    public Ghost(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale,int from, int to) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.from = from;
        this.to = to;

    }

    public void setFromTo(int from, int to){
        this.from = from;
        this.to = to;
    }

    private int pickRandomXfromArray(){
        if (mode == 1)
        return xToChoose[new Random().nextInt(4)];
        else  return xToChoose[new Random().nextInt(4)+4];
    }



    public void move() {
        if (mode == 1) {
            if (getPosition().z < to) {
                setZ(from);
                setX(pickRandomXfromArray());
            }
            float distance = RUN_SPEED * DisplayManager.getFrameTimeSeconds();
            // duch rusza sie w dol (-z)
            float dz = (distance * -1);
            super.increasePosition(0, 0, dz);
        } else {
            if (getPosition().z < to) {
                setZ(from);
                setX(pickRandomXfromArray());
            }
            float distance = RUN_SPEED * DisplayManager.getFrameTimeSeconds();
            // duch rusza sie w dol (-z)
            float dz = (distance * -1);
            super.increasePosition(0, 0, dz);
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}
