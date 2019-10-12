import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Self-contained example (within a single class only to keep it simple)
 * displaying a rotating quad
 */
public class Main implements GLEventListener, KeyListener
{
    private float rotateX = 0, rotateY = 0, rotateZ = 0;   // rotation amounts about axes, controlled by keyboard
    private float moveX = 0, moveY = 0, moveZ = 0;
    private int fov = 100;
    private int texture;
    private GL2 gl;
    private GLU glu;

    private static boolean ENABLE_COLOR = false;
    private static boolean ENABLE_LIGHT = true;
    private static boolean ENABLE_DEPTH = true;
    private static boolean ENABLE_TEXTURE = true;

    public void bindCheckTexture(GL2 gl)
    {
        //genereaza un sir de octeti cu valorile pentru textura
        //la utilizare se poate folosi id-ul texturii, dat de functie
        int i, j;
        int n = 32; //64;
        int IDTextura[] = new int[1];
        byte T[] = new byte[n * n * 3];  //textura
        int c;
        int k = 16; //32; R G B A R G B A R.....

        //se construieste vectorul T, cu culorile pixelilor,
        //linie dupa linie ("tabla de sah" cu 2*2 zone)
        //pentru fiecare pixel se rezerva un octet
        for (i = 0; i < n; i++)
        {
            for (j = 0; j < n; j++)
            {
                c = (i / k) + (j / k);
                if (c == 2) c = 0;
                c = c * 255;   // => c=0 or c=255
                T[(j * n + i) * 3] = (byte) c;  // red
                T[(j * n + i) * 3 + 1] = (byte) c;  // green
                T[(j * n + i) * 3 + 2] = (byte) c;  // blue
            }
        }
        // Create texture
        gl.glGenTextures(1, IDTextura, 0);

        // Activate texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, IDTextura[0]);

        //pregateste sirul T ca osuccesiune de octeti
        //(tip de data cerut la generarea texturii)
        ByteBuffer image = ByteBuffer.wrap(T);

        // Give the image to OpenGL
        gl.glTexImage2D(GL2.GL_TEXTURE_2D,//Textura2D
            0,                     //Niv.de detaliu
            GL2.GL_RGB,             //Formatul intern
            n, n,                   //Dim.texturii
            0,                     // Marginea: 0 sau 1, legacy stuff
            GL2.GL_RGB,             //Formatul culorii
            GL2.GL_UNSIGNED_BYTE,   //Tipul datelor pentru pixeli
            image                    //Adresa tabelului de pixeli
        );

        texture = IDTextura[0];
    }

    public void bindFileTexture(GL2 gl, String filename)
    {
        try
        {
            File im = new File(filename);
            Texture t = TextureIO.newTexture(im, true);
            texture = t.getTextureObject(gl);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        float amountRotate = 10;
        float amountMove = 0.5f;

        int key = keyEvent.getKeyCode();

        // Rotate keys
        if (key == KeyEvent.VK_LEFT)
            rotateY -= amountRotate;
        else if (key == KeyEvent.VK_RIGHT)
            rotateY += amountRotate;
        else if (key == KeyEvent.VK_DOWN)
            rotateX += amountRotate;
        else if (key == KeyEvent.VK_UP)
            rotateX -= amountRotate;
        else if (key == KeyEvent.VK_X)
            rotateZ += amountRotate;
        else if (key == KeyEvent.VK_Z)
            rotateZ -= amountRotate;

        // Move keys
        else if (key == KeyEvent.VK_O)
            moveX -= amountMove;
        else if (key == KeyEvent.VK_P)
            moveX += amountMove;
        else if (key == KeyEvent.VK_K)
            moveY -= amountMove;
        else if (key == KeyEvent.VK_L)
            moveY += amountMove;
        else if (key == KeyEvent.VK_N)
            moveZ -= amountMove;
        else if (key == KeyEvent.VK_M)
            moveZ += amountMove;

        // Toggle keys
        else if (key == KeyEvent.VK_C)
            ENABLE_COLOR = !ENABLE_COLOR;
        else if (key == KeyEvent.VK_E)
            ENABLE_LIGHT = !ENABLE_LIGHT;
        else if (key == KeyEvent.VK_D)
            ENABLE_DEPTH = !ENABLE_DEPTH;
        else if (key == KeyEvent.VK_T)
            ENABLE_TEXTURE = !ENABLE_TEXTURE;

        // Zoom
        else if (key == KeyEvent.VK_ADD)
            fov -= 5;
        else if (key == KeyEvent.VK_SUBTRACT)
            fov += 5;

        // Reset
        else if (key == KeyEvent.VK_HOME)
            rotateX = rotateY = rotateZ = moveX = moveY = moveZ = 0;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
    }

    @Override
    public void init(GLAutoDrawable glDrawable)
    {
        gl = glDrawable.getGL().getGL2();
        glu = GLU.createGLU(gl);

        // Set up Projection, https://www.opengl.org/archives/resources/faq/technical/viewing.htm
        gl.glMatrixMode(GL2.GL_PROJECTION);  // Set up the projection.
        gl.glLoadIdentity();
        glu.gluPerspective(fov, 1280f / 720f, 1f, 100.0f);


        // Enable extensions, Z-Buffer
        gl.glEnable(GL2.GL_DEPTH_TEST);


        // Textures
        gl.glEnable(GL2.GL_TEXTURE_2D);
//        bindCheckTexture(gl);
        bindFileTexture(gl, "box.jpg");
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);


        // Lightening
        float light_position[] = {2.0f, 2.0f, 2.0f, 0.0f};
        float light_ambient[] = { 0.1f, 0.1f, 0.1f, 1.0f };
        float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular, 0);

        float mat_amb_diff[] = {0.1f, 0.5f, 0.8f, 1.0f};
        float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
        float mat_shininess = 50.0f;
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, mat_amb_diff, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess);

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
//        gl.glEnable(GL2.GL_NORMALIZE);


        // weak RED ambient
        //float[] ambientLight = { 0.1f, 0.f, 0.f,0f };
        //gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

        // multicolor diffuse
//        float[] diffuseLight = { 1f,2f,1f,0f };
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);

//        GL2 gl = glDrawable.getGL().getGL2();
//        gl.glShadeModel(GLLightingFunc.GL_SMOOTH);
//        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        gl.glClearDepth(1.0f);
//        gl.glEnable(GL.GL_DEPTH_TEST);
//        gl.glDepthFunc(GL.GL_LEQUAL);
//        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
    }

    @Override
    public void display(GLAutoDrawable gLDrawable)
    {
        //GL2 gl = gLDrawable.getGL().getGL2();
        // Set up Projection, https://www.opengl.org/archives/resources/faq/technical/viewing.htm
        gl.glMatrixMode(GL2.GL_PROJECTION);  // Set up the projection.
        gl.glLoadIdentity();
        glu.gluPerspective(fov, 1280f / 720f, 1f, 100.0f);

        // Clear screen
        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_COLOR_BUFFER_BIT);

        // Toggle light
        if (ENABLE_LIGHT)
            gl.glEnable(GL2.GL_LIGHTING);
        else
            gl.glDisable(GL2.GL_LIGHTING);

        // Toggle z-test
        if (ENABLE_DEPTH)
            gl.glEnable(GL2.GL_DEPTH_TEST);
        else
            gl.glDisable(GL2.GL_DEPTH_TEST);

        // Toggle textures
        if (ENABLE_TEXTURE)
            gl.glEnable(GL2.GL_TEXTURE_2D);
        else
            gl.glDisable(GL2.GL_TEXTURE_2D);

        // Toggle color
        if (!ENABLE_COLOR) gl.glColor4f(1, 1, 1, 1);

        // Translate and set camera
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt( // https://www.opengl.org/sdk/docs/man2/xhtml/gluLookAt.xml
            0, 1, 3, // eye
            0, 0, 0, // center
            0, 1, 0 // up
        );

        // Draw cube
        gl.glPushMatrix();

        // Animation from the user
        gl.glRotatef(rotateZ, 0, 0, 1);
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glTranslatef(moveX, moveY, moveZ);

        gl.glBegin(GL2.GL_QUADS);

        // Top face
        if (ENABLE_COLOR) gl.glColor3f(0.0f, 1.0f, 0.0f);       // Green
        gl.glNormal3f(0, 1.0f, 0);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);     // Top-right of top face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);    // Top-left of top face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);     // Bottom-left of top face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, 1.0f);      // Bottom-right of top face

        // Bottom face
        if (ENABLE_COLOR) gl.glColor3f(1.0f, 0.5f, 0.0f);     // Orange
        gl.glNormal3f(0, -1.0f, 0);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);  // Top-right of bottom face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Top-left of bottom face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom-left of bottom face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom-right of bottom face

        // Front face
        if (ENABLE_COLOR) gl.glColor3f(1.0f, 0.0f, 0.0f);     // Red
        gl.glNormal3f(0, 0, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, 1.0f, 1.0f);    // Top-Right of front face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top-left of front face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom-left of front face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom-right of front face

        // Back face
        if (ENABLE_COLOR) gl.glColor3f(1.0f, 1.0f, 0.0f);     // Yellow
        gl.glNormal3f(0, 0, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom-Left of back face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom-Right of back face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top-Right of back face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top-Left of back face

        // Left face
        if (ENABLE_COLOR) gl.glColor3f(0.0f, 0.0f, 1.0f);     // Blue
        gl.glNormal3f(-1.0f, 0, 0);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);   // Top-Right of left face
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);  // Top-Left of left face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom-Left of left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);  // Bottom-Right of left face

        // Right face
        if (ENABLE_COLOR) gl.glColor3f(1.0f, 0.0f, 1.0f);     // Violet
        gl.glNormal3f(1.0f, 0, 0);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, 1.0f);    // Top-Right of left face
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(1.0f, 1.0f, -1.0f);   // Top-Left of left face
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, -1.0f);  // Bottom-Left of left face
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(1.0f, -1.0f, 1.0f);   // Bottom-Right of left face

        gl.glEnd();
        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height)
    {
//        GL2 gl = gLDrawable.getGL().getGL2();
//        final float aspect = (float) width / (float) height;
//        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
//        gl.glLoadIdentity();
//        final float fh = 0.5f;
//        final float fw = fh * aspect;
//        gl.glFrustumf(-fw, fw, -fh, fh, 1.0f, 1000.0f);
//        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
//        gl.glLoadIdentity();
    }

    @Override
    public void dispose(GLAutoDrawable gLDrawable)
    {
    }

    public static void main(String[] args)
    {
        final GLCapabilities capabilities = new GLCapabilities(null);
        final GLCanvas canvas = new GLCanvas(capabilities);
        final Frame frame = new Frame("Jogl Quad drawing");
        final FPSAnimator animator = new FPSAnimator(canvas, 60);
        Main program = new Main();

        canvas.addGLEventListener(program);
        canvas.addKeyListener(program);

        frame.add(canvas);
        frame.setSize(1280, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                animator.stop();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();
    }
}
