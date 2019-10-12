#include <stdio.h>
#include <math.h>
#include <windows.h>
#include <GL/glut.h>

/* Mapping matrix. OpenGL stores transform matrixes in one-dimension arrays.
   This matrix specifies for each element of the 4x4 matrix the corresponding
   index in the OpenGL array */
int mxm[4][4] = {{ 0,  1,  2,  3},
                 { 4,  5,  6,  7},
                 { 8,  9, 10, 11},
                 {12, 13, 14, 15}};

#define PI 3.141592653

GLfloat cameraPosition[3] = {0.0f, 3.0f, 10.0f};
GLfloat angle = 0.0f;

GLfloat cubeAngle[3] = {0.0f, 0.0f, 0.0f};

GLfloat light_position[] = {0.0, 0.0, 1.0, 0.0};
GLfloat light_diffuse[] =  {1.0, 1.0, 1.0, 1.0};
GLfloat light_specular[] = {1.0, 1.0, 1.0, 1.0};

long prevTime = 0;

GLfloat rx = 0;
GLfloat rv = 1;
GLfloat ra = 0;

int pause = 0;

GLfloat colors[7][4] =
  {{1.0, 0.0, 0.0, 0.1},
   {0.0, 1.0, 0.0, 0.6},
   {0.0, 0.0, 1.0, 0.1},
   {1.0, 1.0, 0.0, 0.6},
   {0.0, 1.0, 1.0, 0.6},
   {1.0, 0.0, 1.0, 0.6},
   {1.0, 1.0, 1.0, 0.6}};

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
/* Set vector values */
void setv(GLdouble v[3], GLdouble x, GLdouble y, GLdouble z) {
  v[0] = x;
  v[1] = y;
  v[2] = z;
}

/* ------------------------------------------------------------------------- */
/* Print matrix to STDOUT */
void printm(GLdouble m[16], char* name) {
  int i, j;

  printf("[%s]\n", name);
  printf("+-----------------------------+\n");
  for(i=0; i<4; i++) {
    printf("| ");
    for(j=0; j<4; j++) {
      printf("% 6.2lf ", m[mxm[i][j]]);
    }
    printf("|\n");
  }
  printf("+-----------------------------+\n");
}

/* ------------------------------------------------------------------------- */
/* Print vector to STDOUT */
void printv(GLdouble v[3], char* name) {
  int i;

  printf("[%s]\n", name);
  printf("+---------------------+\n");
  printf("|");
  for(i=0; i<3; i++) {
    printf("% 6.2lf ", v[i]);
  }
  printf("|\n");
  printf("+---------------------+\n");
}

/* ------------------------------------------------------------------------- */
/* Multiply transform matrixes: a * b -> dst */
void mulm(GLdouble a[16], GLdouble b[16], GLdouble dst[16]) {
  int i, j, k;
  GLdouble res[16] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  for(i=0; i<4; i++) {
    for(j=0; j<4; j++) {
      for(k=0; k<4; k++) {
        res[mxm[i][j]] += a[mxm[i][k]]*b[mxm[k][j]];
      }
    }
  }

  for(i=0; i<16; i++) {
    dst[i] = res[i];
  }
}

/* ------------------------------------------------------------------------- */
/* Multiply vector by transform matrix: vin * m -> dst */
void mulv(GLdouble m[16], GLdouble vin[3], GLdouble dst[3]) {
  int i, j;
  GLdouble v[4] = {0, 0, 0, 1};
  GLdouble res[4] = {0, 0, 0, 0};

  v[0] = vin[0];
  v[1] = vin[1];
  v[2] = vin[2];

  for(i=0; i<4; i++) {
    for(j=0; j<4; j++) {
      res[i] += m[mxm[j][i]]*v[j];
    }
  }

  for(i=0; i<3; i++) {
    dst[i] = res[i];
  }
}

/* ------------------------------------------------------------------------- */
/* Invert transform matrix */
void invm(GLdouble src[16], GLdouble dst[16]) {
  int i, j;
  GLdouble res[16] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  GLdouble v[3];

  /* Transpose rotation sub-matrix. The transpose of a rotation matrix is also
     its inverse */
  for(i=0; i<3; i++) {
    for(j=0; j<3; j++) {
      res[mxm[i][j]] = src[mxm[j][i]];
    }
  }

  /* Populate the last column with the usual values */
  res[mxm[0][3]] = 0;
  res[mxm[1][3]] = 0;
  res[mxm[2][3]] = 0;
  res[mxm[3][3]] = 1;

  /* Invert the position vector of the matrix */
  v[0] = -src[mxm[3][0]];
  v[1] = -src[mxm[3][1]];
  v[2] = -src[mxm[3][2]];

  mulv(res, v, v);

  res[mxm[3][0]] = v[0];
  res[mxm[3][1]] = v[1];
  res[mxm[3][2]] = v[2];

  /* Copy the result to destination */
  for(i=0; i<16; i++) {
    dst[i] = res[i];
  }
}

/* ------------------------------------------------------------------------- */
GLdouble dot(GLdouble v0[3], GLdouble v1[3]) {
  return v0[0]*v1[0]+v0[1]*v1[1]+v0[2]*v1[2];
}

/* ------------------------------------------------------------------------- */
void cross(GLdouble v0[3], GLdouble v1[3], GLdouble r[3]) {
  r[0] = v0[1]*v1[2]-v0[2]*v1[1];
  r[1] = v0[2]*v1[0]-v0[0]*v1[2];
  r[2] = v0[0]*v1[1]-v0[1]*v1[0];
}

/* ------------------------------------------------------------------------- */
void normalize(GLdouble v[3]) {
  GLdouble norm = sqrt(dot(v, v));
  if(norm > 0.0) {
    GLdouble inv = 1.0/norm;
    v[0] *= inv;
    v[1] *= inv;
    v[2] *= inv;
  }                
}

/* ------------------------------------------------------------------------- */
void drawLine(GLdouble v0[3], GLdouble v1[3]) {
  glLineWidth(3);
  glLineStipple(1, 0xE0E0);
  glBegin(GL_LINES);
  glVertex3dv(v0);
  glVertex3dv(v1);
  glEnd();
  glLineStipple(0, 0xFFFF);
}

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

  dir[0] = sin(PI*angle/180.0);
  dir[2] = -cos(PI*angle/180.0);

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
    case 'w':
      ra += 0.1;
      break;
    case 's':
      ra -= 0.1;
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
  GLfloat a;
  GLfloat r;
  long t;
  GLfloat delta;
  GLfloat crx;
  GLfloat crv;
  GLdouble ml[16];
  GLdouble mr[16];
  GLdouble inv[16];
  GLdouble vl[3];
  GLdouble vr[3];
  GLdouble vx[3];
  GLdouble vy[3];
  GLdouble vz[3];
  GLdouble vd[3];
  GLdouble xa;
  GLdouble za;
  GLdouble len;

  t = GetTickCount();
  delta = (t - prevTime)/1000.0f;
  prevTime = t;

  crv = rv + ra*delta;
  crx = rx + rv*delta + 0.5*ra*delta*delta;
  
  rv = crv;
  rx = crx;

  crx = crx/PI*180.0;

  if(pause) {
    return;
  }

  cubeAngle[0] += 0.01f*rand()/RAND_MAX;
  cubeAngle[1] += 0.02f*rand()/RAND_MAX;
  cubeAngle[2] += 0.03f*rand()/RAND_MAX;

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  setCamera();

  r = 5.0f;
  a = 2*PI/7.0f;

  glMatrixMode(GL_MODELVIEW);

  // Left bottom
  glPushMatrix();
  glTranslatef(-1.0f, 0.0f, 0);
  glRotatef(sin(cubeAngle[0])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[1])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(0);
  glPopMatrix();

  // Left middle
  glTranslatef(0.0f, 1.0f, 0);
  glRotatef(sin(cubeAngle[0])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[1])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(1);
  glPopMatrix();

  // Left top
  glTranslatef(0.0f, 1.0f, 0);
  glRotatef(sin(cubeAngle[0])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[1])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(2);
  glPopMatrix();
  glGetDoublev(GL_MODELVIEW_MATRIX, ml);
  glPopMatrix();

  // Right bottom
  glPushMatrix();
  glTranslatef(1.0f, 0.0f, 0);
  glRotatef(sin(cubeAngle[1])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[0])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(3);
  glPopMatrix();

  // Right middle
  glTranslatef(0.0f, 1.0f, 0);
  glRotatef(sin(cubeAngle[1])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[0])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(4);
  glPopMatrix();

  // Right top
  glTranslatef(0.0f, 1.0f, 0);
  glRotatef(sin(cubeAngle[1])*10, 1, 0, 0);
  glRotatef(sin(cubeAngle[2])*10, 0, 1, 0);
  glRotatef(sin(cubeAngle[0])*10, 0, 0, 1);
  glTranslatef(0.0f, 1.0f, 0);
  glPushMatrix();
  glScalef(0.1f, 1.0f, 0.1f);
  drawCube(5);
  glPopMatrix();

  // Bar
  glTranslatef(0.0f, 1.0f, 0);
  glGetDoublev(GL_MODELVIEW_MATRIX, mr);

  setv(vl, 0, 1, 0);
  setv(vr, 0, 0, 0);
  mulv(ml, vl, vl);
  invm(mr, inv);
  mulv(inv, vl, vl);
  drawLine(vl, vr);

  setv(vx, 1, 0, 0);
  setv(vy, 0, 1, 0);
  setv(vz, 0, 0, 1);

  cross(vl, vy, vr);
  len = sqrt(dot(vl, vl));

  normalize(vl);
  xa = -180*acos(dot(vl, vy))/PI;

  glRotatef(xa, vr[0], vr[1], vr[2]);

  glTranslatef(0.0f, 0.5*len, 0);
  glPushMatrix();
  glScalef(0.1f, 0.5*len, 0.1f);
  drawCube(6);
  glPopMatrix();

  glPushMatrix();
  glScalef(0.3f, 0.75f, 0.3f);
  drawCube(0);
  glPopMatrix();

  glPopMatrix();

  glutSwapBuffers();
}

/* ------------------------------------------------------------------------- */
void reshape(int width, int height) {
  glViewport(0, 0, width, height);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(45.0f, (GLfloat)width/(GLfloat)height, 0.1f, 100.0f);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
}

/* ------------------------------------------------------------------------- */
void init() {
  //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  //glEnable(GL_BLEND);

  glLightfv(GL_LIGHT0, GL_DIFFUSE,  light_diffuse);
  glLightfv(GL_LIGHT0, GL_SPECULAR, light_specular);
  glLightfv(GL_LIGHT0, GL_POSITION, light_position);
  glEnable(GL_LIGHT0);
  glEnable(GL_LIGHTING);

  glEnable(GL_DEPTH_TEST);

  glEnable(GL_NORMALIZE);

  glMatrixMode(GL_PROJECTION);  
  gluPerspective(45, 1, 0.2f, 100.0f);
  setCamera();
}

/* ------------------------------------------------------------------------- */
void joystick(unsigned int buttons, int x, int y, int z) {
  printf("%d %d %d %d\n", buttons, x, y, z);
  cameraPosition[0] += x/50;
  cameraPosition[1] += y/50;
  cameraPosition[2] += z/50;
}

/* ------------------------------------------------------------------------- */
int main(int argc, char **argv) {
  srand(GetTickCount());
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
