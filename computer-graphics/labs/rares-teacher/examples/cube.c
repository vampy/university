#include <stdio.h>
#include <math.h>
#include <GL/glut.h>

GLfloat cameraPosition[3] = {0.0f, 0.0f, 5.0f};
GLfloat angle = 0.0f;

GLfloat cubeAngle[3] = {0.0f, 0.0f, 0.0f};

GLfloat light_position[] = {0.0, 0.0, 1.0, 0.0};
GLfloat light_diffuse[] =  {1.0, 1.0, 1.0, 1.0};
GLfloat light_specular[] = {1.0, 1.0, 1.0, 1.0};

int pause = 0;

GLfloat colors[7][4] =
  {{1.0, 0.0, 0.0, 1.0},
   {0.0, 1.0, 0.0, 1.0},
   {0.0, 0.0, 1.0, 1.0},
   {1.0, 1.0, 0.0, 1.0},
   {0.0, 1.0, 1.0, 1.0},
   {1.0, 0.0, 1.0, 1.0},
   {1.0, 1.0, 1.0, 1.0}};

/*        3 +------------------+ 2  */
/*         /|                 /|    */
/*        / |                / |    */
/*       /  |               /  |    */
/*    0 +------------------+ 1 |    */
/*      |   |              |   |    */
/*      |   |              |   |    */
/*      |   |              |   |    */
/*      | 7 +--------------|---+ 6  */
/*      |  /               |  /     */
/*      | /                | /      */
/*      |/                 |/       */
/*    4 +------------------+ 5      */

GLfloat v[8][3] = 
  {{-1.0,  1.0,  1.0},
   { 1.0,  1.0,  1.0},
   { 1.0,  1.0, -1.0},
   {-1.0,  1.0, -1.0},
   {-1.0, -1.0,  1.0},
   { 1.0, -1.0,  1.0},
   { 1.0, -1.0, -1.0},
   {-1.0, -1.0, -1.0}};

GLint faces[6][4] =
  {{0, 1, 2, 3}, 
   {3, 2, 6, 7}, 
   {7, 6, 5, 4},
   {4, 5, 1, 0}, 
   {5, 6, 2, 1}, 
   {7, 4, 0, 3}};

GLfloat n[6][3] =
  {{ 0.0,  1.0,  0.0}, 
   { 0.0,  0.0, -1.0},
   { 0.0, -1.0,  0.0},
   { 0.0,  0.0,  1.0},
   { 1.0,  0.0,  0.0},
   {-1.0,  0.0,  0.0}};

/* ------------------------------------------------------------------------- */
void idle() {
  glutPostRedisplay();
}

/* ------------------------------------------------------------------------- */
void setCamera() {
  GLfloat dir[3] = {0, 0, -1};
  GLfloat up[3] =  {0, 1,  0};
  GLfloat v[3];
  int i;

  dir[0] = sin(3.141592653*angle/180.0);
  dir[2] = -cos(3.141592653*angle/180.0);

  for(i=0; i<3; i++) {
    v[i] = cameraPosition[i] + dir[i];
  }

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
  gluLookAt(cameraPosition[0], cameraPosition[1], cameraPosition[2],
            v[0],              v[1],              v[2],
            up[0],             up[1],             up[2]);
}

/* ------------------------------------------------------------------------- */
void motion(int x, int y) {
  int w = glutGet(GLUT_WINDOW_WIDTH);
  int h = glutGet(GLUT_WINDOW_HEIGHT);

  if(y > h/2) {
    cameraPosition[2] += 2;
  }
  else {
    cameraPosition[2] -= 2;
  }
}

/* ------------------------------------------------------------------------- */
void mouse(int button, int state, int x, int y) {
  int w = glutGet(GLUT_WINDOW_WIDTH);
  int h = glutGet(GLUT_WINDOW_HEIGHT);

  if(button == GLUT_LEFT_BUTTON) {
  }
}
/* ------------------------------------------------------------------------- */
void keyboard(unsigned char key, int x, int y) {
  switch(key) {
    case 'q':
      exit(0);
    case 'a':
      cameraPosition[2] -= 2;
      break;
    case 'z':
      cameraPosition[2] += 2;
      break;
    case 'o':
      angle += 1;
      break;
    case 'p':
      angle -= 1;
      break;
    case ' ':
      pause = (pause+1) % 2;
      break;
  }
}

/* ------------------------------------------------------------------------- */
void drawCube(int k) {
  int i;

  for (i = 0; i < 6; i++) {
    glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, colors[k]);
    glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, colors[k]);
    glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, colors[k]);
    glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 128);

    //glutSolidCube(1);

    glBegin(GL_QUADS);

    glNormal3fv(n[i]);
    glVertex3fv(v[faces[i][0]]);
    glVertex3fv(v[faces[i][1]]);
    glVertex3fv(v[faces[i][2]]);
    glVertex3fv(v[faces[i][3]]);

    glEnd();
  }
}

/* ------------------------------------------------------------------------- */
void display() {
  int i;

  if(pause) {
    return;
  }

  cubeAngle[0] += 0.1f;
  cubeAngle[1] += 0.2f;
  cubeAngle[2] += 0.3f;

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  setCamera();

  glMatrixMode(GL_MODELVIEW);

  glRotatef(cubeAngle[0], 1.0, 0.0, 0.0);
  glRotatef(cubeAngle[1], 0.0, 1.0, 0.0);
  glRotatef(cubeAngle[2], 0.0, 0.0, 1.0);
  drawCube(0);

  glPushMatrix();
  glRotatef(cubeAngle[0], 1.0, 0.0, 0.0);
  glRotatef(cubeAngle[1], 0.0, 1.0, 0.0);
  glRotatef(cubeAngle[2], 0.0, 0.0, 1.0);
  glTranslatef(0.0f, 5.5f, 0.0f);
  drawCube(1);
  glPopMatrix();

  glPushMatrix();
  glTranslatef(0.0f, -5.5f, 0.0f);
  glRotatef(cubeAngle[0], 1.0, 0.0, 0.0);
  glRotatef(cubeAngle[1], 0.0, 1.0, 0.0);
  glRotatef(cubeAngle[2], 0.0, 0.0, 1.0);
  drawCube(2);
  glPopMatrix();

  glutSwapBuffers();
}

/* ------------------------------------------------------------------------- */
void reshape(int width, int height) {
  glViewport(0, 0, width, height);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(45.0f, (GLfloat)width/(GLfloat)height, 0.1f, 800.0f);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
}

/* ------------------------------------------------------------------------- */
void init() {
  glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
  glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
  glLightfv(GL_LIGHT0, GL_POSITION, light_position);
  glEnable(GL_LIGHT0);
  glEnable(GL_LIGHTING);

  glEnable(GL_DEPTH_TEST);

  glMatrixMode(GL_PROJECTION);  
  gluPerspective(40, 1, 0.2, 100);
  setCamera();
}

/* ------------------------------------------------------------------------- */
void joystick(unsigned int buttons, int x, int y, int z) {
  cameraPosition[0] += x/50;
  cameraPosition[1] += y/50;
  cameraPosition[2] += z/50;
}

/* ------------------------------------------------------------------------- */
int main(int argc, char **argv) {
  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
  glutInitWindowSize(500, 500);
  glutCreateWindow("Demo");
  glutDisplayFunc(display);
  glutIdleFunc(idle);
  glutKeyboardFunc(keyboard);
  glutReshapeFunc(reshape);
  glutMouseFunc(mouse);
  glutMotionFunc(motion);
  glutJoystickFunc(joystick, 100);

  init();
  glutMainLoop();
  return 0;
}
