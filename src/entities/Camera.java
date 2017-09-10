package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by AW on 2017-08-30.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private float distanceFromPlayer = 5;
    private float angleAroundPlayer = 0;

    private Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public void move(){
        calcAngleAroundPlayer();
        calcPitch();
        float horiDistance = calcHorizDistance();
        float vertDistance = calcVertDistance();
        calcCameraPos(horiDistance,vertDistance);
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);

        //reset kamery
        if (Keyboard.isKeyDown(Keyboard.KEY_R)){
            angleAroundPlayer = 0;
            pitch = 20;
            yaw = 0;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calcCameraPos(float horiz, float vertic){
        float alpha = player.getRotY() + angleAroundPlayer;
        float offsetX = (float)(horiz * Math.sin(Math.toRadians(alpha)));
        float offsetZ =  (float)(horiz * Math.cos(Math.toRadians(alpha)));
        position.x = player.getPosition().x - offsetX;
        position.z = player.getPosition().z - offsetZ;
        position.y = player.getPosition().y + vertic;
    }

    private float calcHorizDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }
    private float calcVertDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }


    private void calcPitch(){
        float pitchChange = 0.1f;
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
            if ((pitch > 0) && (pitch < 90))
            pitch += pitchChange;
            else if (pitch < 1)
                    pitch = 1;
                    else
                        pitch = 89;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
            if ((pitch > 0) && (pitch < 90))
            pitch -= pitchChange;
            else if (pitch < 1)
                pitch = 1;
            else
                pitch = 89;
        }
    }

    private void calcAngleAroundPlayer(){
        float angleChange = 0.2f;
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            if ((angleAroundPlayer >= -30) && (angleAroundPlayer <= 30))
            angleAroundPlayer -= angleChange;
            else if (angleAroundPlayer <= -30)
                angleAroundPlayer = -29;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            if ((angleAroundPlayer >= -30) && (angleAroundPlayer <= 30))
            angleAroundPlayer += angleChange;
            else if (angleAroundPlayer >= 30)
                angleAroundPlayer = 29;
        }
    }

}
