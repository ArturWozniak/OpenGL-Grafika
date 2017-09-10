package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;
import toolbox.Maths;

import static renderEngine.EntityRenderer.BLUE;
import static renderEngine.EntityRenderer.GREEN;
import static renderEngine.EntityRenderer.RED;

/**
 * Created by AW on 2017-09-06.
 */
public class TerrainShader extends  ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_skyColour;

    public TerrainShader() {
        super(VERTEX_FILE,  FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocaton("transformationMatrix");
        location_projectionMatrix = super.getUniformLocaton("projectionMatrix");
        location_viewMatrix = super.getUniformLocaton("viewMatrix");
        location_lightPosition = super.getUniformLocaton("lightPosition");
        location_lightColour = super.getUniformLocaton("lightColour");
        location_shineDamper = super.getUniformLocaton("shineDamper");
        location_reflectivity = super.getUniformLocaton("reflectivity");
        location_skyColour = super.getUniformLocaton("skyColour");
    }

    public void loadSkyColour(){
        super.loadVector(location_skyColour,new Vector3f(RED,GREEN,BLUE));
    }

    public void loadShineVariable(float damper,float reflectivity){
        super.loadFloat(location_shineDamper,damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    protected void bindAttributes(){
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"normal");
    }
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix,projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition,light.getPosition());
        super.loadVector(location_lightColour,light.getColour());
    }
}
