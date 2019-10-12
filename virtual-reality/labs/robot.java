package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.Node;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    protected Node neck;
    protected Node LAJ;
    protected Node LFAJ;
    protected Node RAJ;
    protected Node RFAJ;
    protected Node LLJ;
    protected Node LFLJ;
    protected Node RLJ;
    protected Node RFLJ;
    
    
    @Override
    public void simpleInitApp() {
        Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Material mat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.DarkGray);
        mat2.setColor("Color", ColorRGBA.Blue);
        mat3.setColor("Color", ColorRGBA.White);
        
        
        Box B = new Box(1, 1.8f, 1);
        Geometry Body = new Geometry("Body", B);
        Body.setMaterial(mat1);
        
        Box H =new Box(0.6f,0.5f,1f);
        Geometry Head=new Geometry("Head",H);
        Head.setMaterial(mat2);
        
        neck=new Node("Neck");
        neck.attachChild(Head);
        neck.move(0, 1.8f, 0);
        Head.move(0, 0.5f, 0);
        
        // Left arm
        Box LA = new Box(0.5f, 1.2f, 0.5f);
        Geometry LeftArm = new Geometry("LeftArm", LA);
        LeftArm.setMaterial(mat2);
        LAJ =new Node("LeftArmJoint");
        LAJ.attachChild(LeftArm);
        LAJ.move(-1.5f, 1.2f, 0);
        LeftArm.move(0f,-0.6f,0f);
               
        Box LFA = new Box(0.4f, 1.0f, 0.4f);
        Geometry LeftFArm = new Geometry("LeftFArm", LFA);
        LeftFArm.setMaterial(mat3);
        LFAJ =new Node("LeftFArmJoint");
        LFAJ.attachChild(LeftFArm);
        LFAJ.move(0f, -1.7f, 0);
        LeftFArm.move(0f,-1.0f,0f);
        
        Sphere SLFA =new Sphere(30, 30, 0.4f);
        Geometry SphereLFA =new Geometry("SphereLA",SLFA);
        SphereLFA.setMaterial(mat3);
        LFAJ.attachChild(SphereLFA);
        
        // Right arm
        Box RA = new Box(0.5f, 1.2f, 0.5f);
        Geometry RightArm = new Geometry("RightArm", RA);
        RightArm.setMaterial(mat2);  
        RAJ =new Node("RightArmJoint");
        RAJ.attachChild(RightArm);
        RAJ.move(1.5f, 1.2f, 0);
        RightArm.move(0f,-0.6f,0f);
        
        Box RFA = new Box(0.4f, 1.0f, 0.4f);
        Geometry RightFArm = new Geometry("RighttFArm", RFA);
        RightFArm.setMaterial(mat3);
        RFAJ =new Node("RightFArmJoint");
        RFAJ.attachChild(RightFArm);
        RFAJ.move(0f, -1.7f, 0);
        RightFArm.move(0f,-1.0f,0f);
        
        Sphere SRFA =new Sphere(30, 30, 0.5f);
        Geometry SphereRFA =new Geometry("SphereLA",SRFA);
        SphereRFA.setMaterial(mat3);
        RFAJ.attachChild(SphereRFA);
        
        // Left leg
        Box LL = new Box(0.5f, 1.2f, 0.5f);
        Geometry LeftLeg = new Geometry("LeftLeg", LL);
        LeftLeg.setMaterial(mat2);  
        LLJ =new Node("LeftLegJoint");
        LLJ.attachChild(LeftLeg);
        LLJ.move(-0.6f, -1.8f, 0);
        LeftLeg.move(0f,-1.2f,0f);
        
        Box LFL = new Box(0.4f, 1.0f, 0.4f);
        Geometry LeftFLeg = new Geometry("LeftFLeg", LFL);
        LeftFLeg.setMaterial(mat3);  
        LFLJ =new Node("LeftFLegJoint");
        LFLJ.attachChild(LeftFLeg);
        LFLJ.move(0, -2.2f, 0);
        LeftFLeg.move(0f,-1.0f,0f);
        
        Sphere SLFL =new Sphere(30, 30, 0.5f);
        Geometry SphereLFL =new Geometry("SphereLA",SLFL);
        SphereLFL.setMaterial(mat3);
        LFLJ.attachChild(SphereLFL);
        
        // Right leg
        Box RL = new Box(0.5f, 1.2f, 0.5f);
        Geometry RightLeg = new Geometry("RightLeg", RL);
        RightLeg.setMaterial(mat2);  
        RLJ =new Node("RightLegJoint");
        RLJ.attachChild(RightLeg);
        RLJ.move(0.6f, -1.8f, 0);
        RightLeg.move(0f,-1.2f,0f);
        
        Box RFL = new Box(0.4f, 1.0f, 0.5f);
        Geometry RightFLeg = new Geometry("RightFLeg", RFL);
        RightFLeg.setMaterial(mat3);  
        RFLJ =new Node("RightFLegJoint");
        RFLJ.attachChild(RightFLeg);
        RFLJ.move(0, -2.2f, 0);
        RightFLeg.move(0f,-1.0f,0f);
        
        Sphere SRFL =new Sphere(30, 30, 0.5f);
        Geometry SphereRFL =new Geometry("SphereLA",SRFL);
        SphereRFL.setMaterial(mat3);
        RFLJ.attachChild(SphereRFL);
        
        
        // ----------------------------------
        rootNode.attachChild(Body);
        rootNode.attachChild(neck);
        
        rootNode.attachChild(LAJ);
        LAJ.attachChild(LFAJ);
        
        rootNode.attachChild(RAJ);
        RAJ.attachChild(RFAJ);
        
        rootNode.attachChild(LLJ);
        LLJ.attachChild(LFLJ);
        
        rootNode.attachChild(RLJ);
        RLJ.attachChild(RFLJ);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        LAJ.rotate(0.01f, 0, 0);
//        RAJ.rotate(0.01f, 0, 0);
//        LFAJ.rotate(0, 0, 0.003f);
//        RFAJ.rotate(0, 0, -0.003f);
//        LLJ.rotate(0, 0, -0.01f);
//        RLJ.rotate(0, 0, 0.01f);
//        LFLJ.rotate(0.001f, 0, 0);
//        RFLJ.rotate(0.001f, 0, 0);
        
        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
 
