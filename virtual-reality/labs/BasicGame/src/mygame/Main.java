package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingBox;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterBoxShape;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private Node robotRootNode;
    private Node robotNeck;
    
    private Node leftArm1;
    private Node leftArm2;
    
    private Node rightArm1;
    private Node rightArm2;
    
    private Node leftLeg1;
    private Node leftLeg2;
    
    private Node rightLeg1;
    private Node rightLeg2;
    
    public static void main(String[] args) 
    {
        // Set settings
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayMode mode = device.getDisplayModes()[0];
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1152, 648);
        settings.setFrequency(mode.getRefreshRate());
        //settings.setFrameRate(mode.getRefreshRate());
        settings.setVSync(true);
        settings.setBitsPerPixel(mode.getBitDepth());

        Main app = new Main();
        app.setSettings(settings);
        app.setShowSettings(false);
        app.start();
    }

    private Geometry addMeshWireFrame(String name, Mesh shape, ColorRGBA color) 
    {
        Geometry g = new Geometry(name, shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);
        g.setMaterial(mat);
        rootNode.attachChild(g);
        return g;
    }
    
    private Geometry addMesh(Node root, String name, Mesh shape, ColorRGBA color) 
    {
        Geometry g = new Geometry(name, shape);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        g.setMaterial(mat);
        root.attachChild(g);
        return g;
    }

    private void attachCoordinateAxes(Vector3f pos, float width) 
    {
        Arrow arrow = new Arrow(new Vector3f(width, 0, 0));
        arrow.setLineWidth(3);
        addMeshWireFrame("coordinate axis X", arrow, ColorRGBA.Red).setLocalTranslation(pos);

        arrow = new Arrow(new Vector3f(0, width, 0));
        arrow.setLineWidth(3);
        addMeshWireFrame("coordinate axis Y", arrow, ColorRGBA.Green).setLocalTranslation(pos);

        arrow = new Arrow(new Vector3f(0, 0, width));
        arrow.setLineWidth(3);
        addMeshWireFrame("coordinate axis Z", arrow, ColorRGBA.Blue).setLocalTranslation(pos);
    }

    private void attachCoordinateAxes(Vector3f pos) 
    {
        attachCoordinateAxes(pos, 1);
    }

    private Geometry attachGrid(Vector3f pos, int size, ColorRGBA color) 
    {
        Geometry g = addMeshWireFrame("wireframe grid", new Grid(size, size, 0.2f), color);
        g.center().move(pos);
        return g;
    }

    public Geometry attachWireBox(Vector3f pos, float size, ColorRGBA color) {
        Geometry g = addMeshWireFrame("wireframe cube", new WireBox(size, size, size), color); 
        g.setLocalTranslation(pos);
        return g;
    }

    public void positionRelative(Spatial object, Spatial attachTo, String axis, boolean isNegative)
    {
        BoundingBox boxAttachTo = (BoundingBox)attachTo.getWorldBound();
        BoundingBox boxObject = (BoundingBox)object.getWorldBound();
        int sign = (isNegative ? -1 : 1);
        
        if (axis.equals("x"))
        {
            object.move(
                (boxAttachTo.getXExtent() + (sign * attachTo.getLocalTranslation().x) + boxObject.getXExtent()) * sign,
                0,
                0
            );
        }
        else if (axis.equals("y"))
        {
            object.move(
                0, 
                (boxAttachTo.getYExtent() + (sign * attachTo.getLocalTranslation().y)  + boxObject.getYExtent()) * sign,
                0
            );
        }
        else if (axis.equals("z"))
        {
            object.move(
                0, 0,
                (boxAttachTo.getZExtent() + (sign * attachTo.getLocalTranslation().z) + boxObject.getZExtent()) * sign
            );
        }
        else
        {
            throw new RuntimeException(String.format("Axis '%s' is invalid", axis));
        }

    }
    
    protected float alpha = 0.0f;
    protected float leftAngle = 0.0f;
    protected float rightAngle = 0.0f;
    protected float leftDir = -1.0f;
    protected float rightDir = 1.0f;
    
    long prevTime = -1;
    
    @Override
    public void simpleInitApp() 
    {  
        // Root node for the robot
        Vector3f robotStart = new Vector3f(0, 1.5f, 0);
        robotRootNode = new Node("root");
        robotRootNode.setLocalTranslation(robotStart);
                
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(0, -2f, 0).normalizeLocal());
        rootNode.addLight(sun);
        
        // Head/neck and body
        robotNeck = new Node("neck");
        Geometry robotHead = addMesh(robotNeck, "head", new Box(0.1f, 0.1f, 0.1f), ColorRGBA.Yellow);
        Geometry robotBody = addMesh(robotRootNode, "body", new Box(0.3f, 0.3f, 0.18f), ColorRGBA.Red);
        BoundingBox boxBody = (BoundingBox)robotBody.getWorldBound();
        
//        System.out.println(robotBody.getWorldBound());
//        System.out.println(robotBody.getLocalTranslation());

        positionRelative(robotBody, robotNeck, "y", true);
        
        // Hat
        Spatial hat = assetManager.loadModel("Models/christmas_hat.obj");
        hat.move(0, 0.1f, 0);
        robotNeck.attachChild(hat);
        
        //----------------------------------------------------------------------
        Box armFirst = new Box(0.06f, 0.3f, 0.06f);
        Box armSecond = new Box(0.03f, 0.1f, 0.03f);
        Sphere armSphere = new Sphere(60, 60, 0.07f);
        float topArms = -0.2f;
        
        // Left Arm
        leftArm1 = new Node("leftArmJoint1");
        leftArm2 = new Node("leftArmJoint2");
        leftArm1.attachChild(leftArm2);

        Geometry leftArm1Geometry = addMesh(leftArm1, "leftArm1", armFirst.clone(), ColorRGBA.Blue);
        Geometry leftArm2Geometry = addMesh(leftArm2, "leftArm2", armSecond.clone(), ColorRGBA.Green);
        addMesh(leftArm2, "leftArmSphere2", armSphere, ColorRGBA.Gray);
        
        positionRelative(leftArm1Geometry, robotBody, "x", true); // relative to body
        leftArm1Geometry.move(0, -armFirst.yExtent, 0); // Move pivot to top   
        leftArm1.move(0, topArms, 0);
        
        leftArm2Geometry.move(0, -armSecond.yExtent, 0); // Move Pivot to top
        leftArm2.setLocalTranslation(leftArm1Geometry.getLocalTranslation()); // same place as arm 1
        leftArm2.move(0, -armFirst.yExtent , 0); // move to bottom of arm 1
        
        // Right Arm
        rightArm1 = new Node("rightArmJoint1");
        rightArm2 = new Node("rightArmJoint2");
        rightArm1.attachChild(rightArm2);
        
        Geometry rightArm1Geometry = addMesh(rightArm1, "rightArm1", armFirst.clone(), ColorRGBA.Blue);
        Geometry rightArm2Geometry = addMesh(rightArm2, "rightArm2", armSecond.clone(), ColorRGBA.Green);
        addMesh(rightArm2, "rightArmSphere2", armSphere, ColorRGBA.Gray);
        
        positionRelative(rightArm1Geometry, robotBody, "x", false); // relative to body
        rightArm1Geometry.move(0, -armFirst.yExtent, 0); // Move pivot to top   
        rightArm1.move(0, topArms, 0);
        
        rightArm2Geometry.move(0, -armSecond.yExtent, 0); // Move Pivot to top
        rightArm2.setLocalTranslation(rightArm1Geometry.getLocalTranslation()); // same place as arm 1
        rightArm2.move(0, -armFirst.yExtent , 0); // move to bottom of arm 1
        
        //----------------------------------------------------------------------
        Box legFirst = new Box(0.08f, 0.25f, 0.06f);
        Box legSecond = new Box(0.03f, 0.20f, 0.05f);
        Sphere legSphere = new Sphere(60, 60, 0.08f);
        
        // Left leg
        leftLeg1 = new Node("leftLegJoint1");
        leftLeg2 = new Node("leftLegJoint2");
        leftLeg1.attachChild(leftLeg2);
        
        Geometry leftLeg1Geometry = addMesh(leftLeg1, "leftLeg1", legFirst.clone(), ColorRGBA.Blue);
        Geometry leftLeg2Geometry = addMesh(leftLeg2, "leftLeg2", legSecond.clone(), ColorRGBA.Green);
        addMesh(leftLeg2, "leftLegSphere2", legSphere, ColorRGBA.Gray);
        
        positionRelative(leftLeg1, robotBody, "y", true); // relative to body
        leftLeg1Geometry.move(0, -legFirst.yExtent, 0); // Move pivot to top  
        leftLeg1.move(-boxBody.getXExtent() / 2, legFirst.yExtent, 0);

        leftLeg2Geometry.move(0, -legSecond.yExtent, 0); // Move pivot to top
        leftLeg2.setLocalTranslation(leftLeg1Geometry.getLocalTranslation()); // same place as leg 1
        leftLeg2.move(0, -legFirst.yExtent, 0); // move to bottom of leg 1
        
        // Right leg
        rightLeg1 = new Node("rightLegJoint1");
        rightLeg2 = new Node("rightLegJoint2");
        rightLeg1.attachChild(rightLeg2);
        
        Geometry rightLeg1Geometry = addMesh(rightLeg1, "rightLeg1", legFirst.clone(), ColorRGBA.Blue);
        Geometry rightLeg2Geometry = addMesh(rightLeg2, "rightLeg2", legSecond.clone(), ColorRGBA.Green);
        addMesh(rightLeg2, "rightLegSphere2", legSphere, ColorRGBA.Gray);
        
        positionRelative(rightLeg1, robotBody, "y", true); // relative to body
        rightLeg1Geometry.move(0, -legFirst.yExtent, 0); // Move pivot to top  
        rightLeg1.move(boxBody.getXExtent() / 2, legFirst.yExtent, 0);
        
        rightLeg2Geometry.move(0, -legSecond.yExtent, 0); // Move pivot to top
        rightLeg2.setLocalTranslation(rightLeg1Geometry.getLocalTranslation()); // same place as leg 1
        rightLeg2.move(0, -legFirst.yExtent, 0); // move to bottom of leg 1
        
        // ---------------------------------------------------------------------
        // Move Camera to new default position
        cam.setLocation(new Vector3f(0, 2, 5));
        cam.lookAt(robotStart, Vector3f.UNIT_Y);

        // Display grid and coordinate axes
        attachGrid(Vector3f.ZERO, 100, ColorRGBA.Yellow);
        attachCoordinateAxes(Vector3f.ZERO, 3);
        
        robotRootNode.attachChild(robotNeck);
        robotRootNode.attachChild(leftArm1);
        robotRootNode.attachChild(rightArm1);
        robotRootNode.attachChild(leftLeg1);
        robotRootNode.attachChild(rightLeg1);
        rootNode.attachChild(robotRootNode);
        
        // Snow
        ParticleEmitter snow = new ParticleEmitter("Emitter", Type.Triangle, 10000);
        snow.setShape(new EmitterBoxShape(new Vector3f(-2f, 2f, -2f), new Vector3f(2f, 2f, 2f)));
        Material snow_mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        snow_mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
        snow.setMaterial(snow_mat);
        
        //snow.setSelectRandomImage(true);
        snow.setImagesX(2); snow.setImagesY(2); // 2x2 texture animation
        snow.setParticlesPerSec(500);
        snow.setEndColor(ColorRGBA.White);
        snow.setStartColor(ColorRGBA.White);
        snow.getParticleInfluencer().setInitialVelocity(new Vector3f(0, -0.5f, 0));
        snow.setStartSize(0.01f);
        snow.setEndSize(0.02f);
        snow.setGravity(0, -0.1f, 0);
        snow.setLowLife(8f);
        snow.setHighLife(10f);
        snow.getParticleInfluencer().setVelocityVariation(-0.4f);
        snow.setLocalTranslation(0, 0f, 0);
        robotRootNode.attachChild(snow);
        
        // Sound
        AudioNode music = new AudioNode(assetManager, "Sounds/song.wav", true);
        music.setVolume(100);
        music.setRefDistance(2f);
        music.setMaxDistance(10f);
        music.setPositional(false);
        robotRootNode.attachChild(music);
        music.play();
    }

    public static final float maxAngle = 30.0f/180.0f * FastMath.PI;
    
    @Override
    public void simpleUpdate(float tpf) {
        if(prevTime < 0) {
            prevTime = System.currentTimeMillis();
            return;
        }
        
        long time = System.currentTimeMillis();
        
        // rotate around in a circle
        float dt = tpf;
        float radius = 2f;
        float w = 1f;
        float v = radius * w;
        float leg = 1.0f;
        alpha += w * dt;
        robotRootNode.setLocalTranslation(radius * (float)Math.cos(alpha), 1.5f, radius * (float)Math.sin(alpha));
        robotRootNode.rotate(0, -w * dt, 0);
        
        // reverse directions if not in range
        if (rightAngle <= -maxAngle || rightAngle >= maxAngle) {
            rightDir *= -1;
        }
        if (leftAngle <= -maxAngle || leftAngle >= maxAngle) {
            leftDir *= -1;
        }
        
        float kl = leftDir * 1.5f * v/ leg * dt;
        float kr = rightDir * 1.5f * v/ leg * dt;
        
        // stop seizures
        if (leftAngle + kl < -maxAngle) {
            kl = -maxAngle - leftAngle;
        }
        if (leftAngle + kl > maxAngle) {
            kl = maxAngle - leftAngle;
        }
        if (rightAngle + kr < -maxAngle) {
            kr = -maxAngle - rightAngle;
        }
        if (rightAngle + kr > maxAngle) {
            kr = maxAngle - rightAngle;
        }
        
        leftAngle += kl;
        rightAngle += kr;
        
        robotNeck.rotate(0, kl, 0);
        
        leftLeg1.rotate(kl / 2.0f, 0, 0);
        leftLeg2.rotate(kl, 0, 0);
        leftArm1.rotate(kl, 0, 0);
        leftArm2.rotate(kl, 0, 0);
        
        rightArm1.rotate(kr, 0, 0);
        rightArm2.rotate(kr, 0, 0);
        rightLeg1.rotate(kr / 2.0f, 0, 0);
        rightLeg2.rotate(kr, 0, 0);  
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
