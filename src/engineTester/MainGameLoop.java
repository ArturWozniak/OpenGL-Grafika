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
import renderEngine.TerrainRenderer;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;

/**
 * Created by AW on 2017-08-29.
 */
public class MainGameLoop {
    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        //shadery
        StaticShader shader = new StaticShader();

        //renderery
        EntityRenderer entityRenderer = new EntityRenderer(shader);

        //gracz
        ModelData data = OBJFileLoader.loadOBJ("creeperGlowa");
        RawModel graczModel = loader.loadToVAO(data.getVertices(),data.getTextureCoords(),data.getNormals(),data.getIndices());
        TexturedModel graczTexturedModel = new TexturedModel(graczModel,new ModelTexture(loader.loadTexture("mapkaGlowa")));
        //duch

        //sciany
        ModelData data2 = OBJFileLoader.loadOBJ("scianaisufit");
        RawModel scianaModel = loader.loadToVAO(data2.getVertices(),data2.getTextureCoords(),data2.getNormals(),data2.getIndices());
        TexturedModel scianaTexturedModel = new TexturedModel(scianaModel,new ModelTexture(loader.loadTexture("stoneFloor")));

        //swiatlo
        Light light = new Light(new Vector3f(10,10,10),new Vector3f(1,1,1));

        Player player = new Player(graczTexturedModel,new Vector3f(100,0,-50),0,0,0,0.5f);
        Entity sciana = new Entity(scianaTexturedModel,new Vector3f(100,-1,-50),0,0,0,1);
        Entity sciana2 = new Entity(scianaTexturedModel,new Vector3f(100,-1,-50),0,90f,0,1);

        Ghost ghost = new Ghost(graczTexturedModel,new Vector3f(100,0,-50),0,0,0,0.5f);

        Camera camera = new Camera(player);
        Vector3f lightAbove;


        while(!Display.isCloseRequested()){

            lightAbove = camera.getPosition();
            light.setPosition(lightAbove);
            camera.move();
            player.move(ghost.getPosition());
            ghost.move();
            entityRenderer.prepare();
            shader.start();
            shader.loadSkyColour();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            entityRenderer.render(player,shader);
            entityRenderer.render(sciana,shader);
            entityRenderer.render(ghost,shader);
            if (Keyboard.isKeyDown(Keyboard.KEY_O) && Keyboard.isKeyDown(Keyboard.KEY_P)){
                sciana2.setDoRender(false);
                player.deleteFromRestrictedZ(-44);
            }
            entityRenderer.render(sciana2,shader);
            shader.stop();
            System.out.println(player.getPosition());

            DisplayManager.updateDisplay();
        }
        shader.cleanUp();
        loader.cleanUp();
    DisplayManager.closeDisplay();
    }
}
