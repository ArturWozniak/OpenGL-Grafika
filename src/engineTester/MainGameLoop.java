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

/**
 * Created by AW on 2017-08-29.
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


        //ladowanie
        Player player = new Player(playerTexturedModel,new Vector3f(100,0,-50),0,0,0,0.4f);
        Ghost ghost = new Ghost(ghostTexturedModel,new Vector3f(100,-0.5f,-50),0,0,0,0.5f);

        Entity floor[] = new Entity[iterator];
        Entity walls[] = new Entity[iterator];
        for (int i = 0; i < iterator; i++) {
            floor[i] = new Entity(floorTexturedModel,new Vector3f(100,-0.4f,zStart),0,0,0,1);
            walls[i] = new Entity(wallTexturedModel,new Vector3f(100,-0.4f,zStart),0,0,0,1);
            zStart += 15;
        }
        Entity stone = new Entity(stoneTexturedModel,new Vector3f(102,-1,-5),0,90f,0,1.25f);
        Entity spikes = new Entity(spikesTexturedModel,new Vector3f(100,-1,5),0,90f,0,1);
        Entity barrier = new Entity(wallTexturedModel,new Vector3f(100,-1,-50),0,90f,0,1);
        Entity lava = new Entity(lavaTexturedModel,new Vector3f(100,-0.2f,-15),180f,0,0,1.7f);

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
            if (!(Math.abs(player.getPosition().z - ghost.getPosition().z) >= 14))
                shader.loadLight(ghostsLight);
            shader.loadViewMatrix(camera);
            entityRenderer.render(player,shader);
            entityRenderer.render(ghost,shader);
            if (Keyboard.isKeyDown(Keyboard.KEY_O) && Keyboard.isKeyDown(Keyboard.KEY_P)){
                barrier.switchDoRender(false);
                player.deleteFromRestrictedZ(-46); //pierwsza przeszkoda
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_O) && Keyboard.isKeyDown(Keyboard.KEY_B)){
                lava.switchDoRender(false);
                player.deleteFromRestrictedZ(-20); //pierwsza przeszkoda
            }
            if (player.getPosition().z > 6){
                if (reSet) { //do it once
                    reSet = false;
                    lava.switchDoRender(true);
                    lava.setPosition(new Vector3f(90f, -0.2f, 6));
                    lava.increaseRotation(0, 90f, 0);
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
            for (int i = 0; i < iterator; i++) {
                entityRenderer.render(walls[i],shader);
                entityRenderer.render(floor[i],shader);
            }
            entityRenderer.render(barrier,shader);
            shader.stop();
            System.out.println(player.getPosition());
            DisplayManager.updateDisplay();
        }
        shader.cleanUp();
        loader.cleanUp();
    DisplayManager.closeDisplay();
    }
}
