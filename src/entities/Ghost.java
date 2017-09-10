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

    private int[] xToChoose = {103,100,97};


    private static final float RUN_SPEED = 3;



    public Ghost(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);

    }

    private int pickRandomXfromArray(){
        return xToChoose[new Random().nextInt(3)];
    }


    public void move(){
        if (getPosition().z < -55){
            setZ(-25);
            setX(pickRandomXfromArray());
        }
        float distance = RUN_SPEED * DisplayManager.getFrameTimeSeconds();
        // duch rusza sie w dol (-z)
        float dz = (distance * -1);
        super.increasePosition(0, 0, dz);
        }


}
