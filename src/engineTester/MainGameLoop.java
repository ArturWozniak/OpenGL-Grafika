package engineTester;

import entities.*;
import models.TexturedModel;
import objLoader.ModelData;
import objLoader.OBJFileLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.concurrent.TimeUnit;

/**
 * Created by AW on 2017-08-29.
 *
 * VM OPTION! .>> -Djava.library.path=libs\
 */
public class MainGameLoop {
    public static void main(String[] args) {
        final int iterator = 5;
        int zStart = -50;
        boolean reSet = true;


        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //shadery renderery
        StaticShader shader = new StaticShader();
        EntityRenderer entityRenderer = new EntityRenderer(shader);

        //gracz
        ModelData playerData = OBJFileLoader.loadOBJ("Gracz\\gracz");
        RawModel playerModel = loader.loadToVAO(playerData.getVertices(),playerData.getTextureCoords(),playerData.getNormals(),playerData.getIndices());
        TexturedModel playerTexturedModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("Gracz\\gracz")));

        //duch
        ModelData ghostData = OBJFileLoader.loadOBJ("Duch\\duch");
        RawModel ghostModel = loader.loadToVAO(ghostData.getVertices(),ghostData.getTextureCoords(),ghostData.getNormals(),ghostData.getIndices());
        TexturedModel ghostTexturedModel = new TexturedModel(ghostModel,new ModelTexture(loader.loadTexture("Duch\\duch")));


        //sciany
        ModelData wallData = OBJFileLoader.loadOBJ("Sciana\\sciana");
        RawModel wallModel = loader.loadToVAO(wallData.getVertices(),wallData.getTextureCoords(),wallData.getNormals(),wallData.getIndices());
        TexturedModel wallTexturedModel = new TexturedModel(wallModel,new ModelTexture(loader.loadTexture("Sciana\\stoneFloor")));

        //podloga
        ModelData floorData = OBJFileLoader.loadOBJ("Podloga\\podloga");
        RawModel floorModel = loader.loadToVAO(floorData.getVertices(),floorData.getTextureCoords(),floorData.getNormals(),floorData.getIndices());
        TexturedModel floorTexturedModel = new TexturedModel(floorModel,new ModelTexture(loader.loadTexture("Podloga\\dirtFloor")));

        //lawa
        ModelData lavaData = OBJFileLoader.loadOBJ("Lawa\\lawa");
        RawModel lavaModel = loader.loadToVAO(lavaData.getVertices(),lavaData.getTextureCoords(),lavaData.getNormals(),lavaData.getIndices());
        TexturedModel lavaTexturedModel = new TexturedModel(lavaModel,new ModelTexture(loader.loadTexture("Lawa\\lawa")));

        //kolce
        ModelData spikesData = OBJFileLoader.loadOBJ("Kolce\\kolce");
        RawModel spikesModel = loader.loadToVAO(spikesData.getVertices(),spikesData.getTextureCoords(),spikesData.getNormals(),spikesData.getIndices());
        ModelTexture spikesModelTexture = new ModelTexture(loader.loadTexture("Kolce\\silver"));
        spikesModelTexture.setReflectivity(1);
        spikesModelTexture.setShineDamper(0.5f);
        TexturedModel spikesTexturedModel = new TexturedModel(spikesModel,spikesModelTexture);
        //
        ModelData stoneData = OBJFileLoader.loadOBJ("Kamien\\rock");
        RawModel stoneModel = loader.loadToVAO(stoneData.getVertices(),stoneData.getTextureCoords(),stoneData.getNormals(),stoneData.getIndices());
        TexturedModel stoneTexturedModel = new TexturedModel(stoneModel,new ModelTexture(loader.loadTexture("Kamien\\rock")));

        //swiatlo
        Light playersLight = new Light(new Vector3f(10,10,10),new Vector3f(1,1,1));
        Light ghostsLight = new Light(new Vector3f(10,10,10),new Vector3f(0.7f,0.3f,0.3f));

        //napisy
        ModelData startData = OBJFileLoader.loadOBJ("Start\\start");
        RawModel startModel = loader.loadToVAO(startData.getVertices(),startData.getTextureCoords(),startData.getNormals(),startData.getIndices());
        TexturedModel startTexturedModel = new TexturedModel(startModel,new ModelTexture(loader.loadTexture("Start\\start")));
        ModelData obData = OBJFileLoader.loadOBJ("Ob\\ob");
        RawModel ObModel = loader.loadToVAO(obData.getVertices(),obData.getTextureCoords(),obData.getNormals(),obData.getIndices());
        TexturedModel obTexturedModel = new TexturedModel(ObModel,new ModelTexture(loader.loadTexture("Ob\\ob")));
        ModelData strzalkaData = OBJFileLoader.loadOBJ("Strzalka\\strzalka");
        RawModel strzalkaModel = loader.loadToVAO(strzalkaData.getVertices(),strzalkaData.getTextureCoords(),strzalkaData.getNormals(),strzalkaData.getIndices());
        TexturedModel strzalkaTexturedModel = new TexturedModel(strzalkaModel,new ModelTexture(loader.loadTexture("Strzalka\\strzalka")));
        ModelData wygranaData = OBJFileLoader.loadOBJ("Wygrana\\wygrana");
        RawModel wygranaModel = loader.loadToVAO(wygranaData.getVertices(),wygranaData.getTextureCoords(),wygranaData.getNormals(),wygranaData.getIndices());
        TexturedModel wygranaTexturedModel = new TexturedModel(wygranaModel,new ModelTexture(loader.loadTexture("Wygrana\\wygrana")));

        //ladowanie
        Player player = new Player(playerTexturedModel,new Vector3f(100,0,-54),0,0,0,0.4f);
        Ghost ghost = new Ghost(ghostTexturedModel,new Vector3f(100,-0.5f,-50),0,0,0,0.5f,-26,-39);
        Ghost ghost2 = new Ghost(ghostTexturedModel,new Vector3f(0,0,0),0,0,0,0.6f,10,1);
        ghost2.switchDoRender(false);

        Entity start = new Entity(startTexturedModel,new Vector3f(100,1,-45),0,90f,0,1);
        Entity ob = new Entity(obTexturedModel,new Vector3f(100,3.5f,-20),-30f,90f,0,0.5f);
        Entity strzalka = new Entity(strzalkaTexturedModel,new Vector3f(102,2,10.5f),0,90f,0,0.9f);
        strzalka.switchDoRender(false);
        Entity wygrana = new Entity(wygranaTexturedModel,new Vector3f(40,1,6),0,0,0,1f);

        Entity floor[] = new Entity[iterator];
        Entity walls[] = new Entity[iterator];
        for (int i = 0; i < iterator; i++) {
            floor[i] = new Entity(floorTexturedModel,new Vector3f(100,-0.4f,zStart),0,0,0,1);
            walls[i] = new Entity(wallTexturedModel,new Vector3f(100,-0.4f,zStart),0,0,0,1);
            zStart += 15;
        }
        Entity stone = new Entity(stoneTexturedModel,new Vector3f(102,-1,-5),0,90f,0,1.25f);
        Entity spikes = new Entity(spikesTexturedModel,new Vector3f(100,-1,4),0,90f,0,1);
        Entity lava = new Entity(lavaTexturedModel,new Vector3f(100,-0.2f,-18.3f),95f,0,0,3f);

        //kamera
        Camera camera = new Camera(player);


        while(!Display.isCloseRequested()){
            Vector3f ghostPos = ghost.getPosition();
            playersLight.setPosition(camera.getPosition()); //latarka gracza
            ghostsLight.setPosition(ghostPos); //swiatlo czerwone ducha
            camera.move();
            player.move(ghostPos);
            ghost.move();
            entityRenderer.prepare();
            shader.start();
            shader.loadSkyColour();
            shader.loadLight(playersLight);
            //duch swieci jak w odlegosci 14 max
            if (ghost.getMode() == 1) {
                if (!(Math.abs(player.getPosition().z - ghost.getPosition().z) >= 12))
                    shader.loadLight(ghostsLight);
            }
            else{
                if (!(Math.abs(player.getPosition().x - ghost.getPosition().x) >= 10))
                    shader.loadLight(ghostsLight);
            }
            shader.loadViewMatrix(camera);
            entityRenderer.render(player,shader);
            entityRenderer.render(ghost,shader);
            entityRenderer.render(ghost2,shader);
            ghost2.move();
            entityRenderer.render(start,shader);
            entityRenderer.render(ob,shader);
            if (Keyboard.isKeyDown(Keyboard.KEY_O) && Keyboard.isKeyDown(Keyboard.KEY_P)){
                start.switchDoRender(false);
                player.deleteFromRestrictedZ(-46); //pierwsza przeszkoda
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_O) && Keyboard.isKeyDown(Keyboard.KEY_B)){
                lava.switchDoRender(false);
                ob.switchDoRender(false);
                player.deleteFromRestrictedZ(-20); //pierwsza przeszkoda
            }
            if (player.getPosition().z > 6){
                if (reSet) { //do it once
                    ghost2.switchDoRender(true);
                    ghost2.setMode(2);
                    reSet = false;
                    strzalka.switchDoRender(true);
                    ghost.setFromTo(10,1);
                    ghost.setMode(2);
                    player.setGoingRight(true);
                    lava.switchDoRender(true);
                    lava.setPosition(new Vector3f(90f, -0.2f, 6));
                    lava.increaseRotation(85f, 90f, 0);
                    lava.setScale(1.7f);
                    spikes.switchDoRender(false);
                    stone.switchDoRender(false);
                    int xStart = 105;
                    for (int i = 0; i < iterator; i++) {
                        floor[i] = new Entity(floorTexturedModel, new Vector3f(xStart, -0.4f, 6), 0, 90, 0, 1);
                        walls[i] = new Entity(wallTexturedModel, new Vector3f(xStart, -0.4f, 6), 0, 90, 0, 1);
                        xStart -= 15;
                    }
                }
            }
            entityRenderer.render(lava,shader);
            entityRenderer.render(spikes,shader);
            entityRenderer.render(stone,shader);
            entityRenderer.render(strzalka,shader);
            for (int i = 0; i < iterator; i++) {
                entityRenderer.render(walls[i],shader);
                entityRenderer.render(floor[i],shader);
            }
            if (player.getPosition().x < 51)
                camera.firstPlayerLook();
            if (player.getPosition().x < 50) {
                entityRenderer.render(wygrana,shader);
                shader.loadViewMatrix(camera);
                try {
                    DisplayManager.updateDisplay();
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            shader.stop();
            System.out.println(player.getPosition());
            DisplayManager.updateDisplay();

        }
        shader.cleanUp();
        loader.cleanUp();
    DisplayManager.closeDisplay();
    }
}
