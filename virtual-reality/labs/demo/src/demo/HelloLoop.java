package demo;
 
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.Node;

/** Sample 4 - how to trigger repeating actions from the main update loop.
 * In this example, we make the player character rotate. */
public class HelloLoop extends SimpleApplication {
    long prevTime = -1;
 
    public static void main(String[] args){
        HelloLoop app = new HelloLoop();
        app.start();
    }
    
    public static final float maxAngle = 30.0f/180.0f*3.141592653f;
 
    protected Node body;
    protected Node left;
    protected Node right;
    protected Node leftHip;
    protected Node rightHip;
    protected float alpha = 0.0f;
    protected float leftAngle = 0.0f;
    protected float rightAngle = 0.0f;
    protected float leftDir = -1.0f;
    protected float rightDir = 1.0f;
 
    @Override
    public void simpleInitApp() {
        Box b;
        Geometry g;
        Material mat;
        
        body  = new Node("body");
        left  = new Node("left");
        right  = new Node("right");
        leftHip  = new Node("leftHip");
        rightHip  = new Node("rightHip");
 
        b = new Box(Vector3f.ZERO, 1.0f, 0.1f, 0.2f);
        g = new Geometry("body geom", b);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        g.setMaterial(mat);
        body.attachChild(g);
        
        b = new Box(Vector3f.ZERO, 0.1f, 1.0f, 0.1f);
        g = new Geometry("left geom", b);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        g.setMaterial(mat);
        left.attachChild(g);
        
        b = new Box(Vector3f.ZERO, 0.1f, 1.0f, 0.1f);
        g = new Geometry("right geom", b);
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        g.setMaterial(mat);
        right.attachChild(g);
        
        rightHip.attachChild(right);
        leftHip.attachChild(left);
        
        body.attachChild(rightHip);
        body.attachChild(leftHip);
        rootNode.attachChild(body);
        
        left.move(-1f, -1f, 0);
        right.move(1f, -1f, 0);
    }
 
    /* This is the update loop */
    @Override
    public void simpleUpdate(float tpf) {
        if(prevTime < 0) {
            prevTime = System.currentTimeMillis();
            return;
        }
        
        long time = System.currentTimeMillis();
        float dt = (float)(time - prevTime)/1000.0f;
        float r = 2.0f;
        float w = 1.0f;
        float v = r*w;
        float leg = 1.0f;
        
        alpha += w*dt;
        
        body.setLocalTranslation(r*(float)Math.cos(alpha), 0, r*(float)Math.sin(alpha));
        body.rotate(0, -w*dt, 0);
        
        if(rightAngle <= -maxAngle || rightAngle >= maxAngle) {
            rightDir *= -1;
        }
        if(leftAngle <= -maxAngle || leftAngle >= maxAngle) {
            leftDir *= -1;
        }
        
        float kl = leftDir*2*v/leg*dt;
        float kr = rightDir*2*v/leg*dt;
        
        if(leftAngle+kl < -maxAngle) {
            kl = -maxAngle - leftAngle;
        }
        if(leftAngle+kl > maxAngle) {
            kl = maxAngle - leftAngle;
        }
        if(rightAngle+kr < -maxAngle) {
            kr = -maxAngle - rightAngle;
        }
        if(rightAngle+kr > maxAngle) {
            kr = maxAngle - rightAngle;
        }
        
        leftAngle += kl;
        rightAngle += kr;

        leftHip.rotate(kl, 0, 0);
        rightHip.rotate(kr, 0, 0);
        
        prevTime = time;
    }
}