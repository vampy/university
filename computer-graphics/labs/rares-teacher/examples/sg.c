#include <stdio.h>
#include <string.h>
#include <math.h>
#include <GL/glut.h>

GLfloat cameraPosition[3] = {0, 0, 10};
GLfloat angle = 0;
int pause = 0;

/* Mapping matrix. OpenGL stores transform matrixes in one-dimension arrays.
   This matrix specifies for each element of the 4x4 matrix the corresponding
   index in the OpenGL array */
int mxm[4][4] = {{ 0,  1,  2,  3},
                 { 4,  5,  6,  7},
                 { 8,  9, 10, 11},
                 {12, 13, 14, 15}};

GLfloat rotation[] = {0, 0, 0};

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

  /* Transpose rotation sub-matrix. The transpose ofa rotation matrix is also
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
void idle(void) {
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
    case ' ':
      pause = (pause+1) % 2;
      break;
  }
}

/* ------------------------------------------------------------------------- */
void drawText(char *s) {
  int i;
  GLfloat k = 0.001;

  glPushMatrix();
  glLineWidth(7);
  glScalef(k, k, k);
  for (i=0; i<(int)strlen(s); i++) {
    glutStrokeCharacter(GLUT_STROKE_ROMAN, s[i]);
  }
  glPopMatrix();
}

/* ------------------------------------------------------------------------- */
void drawAxes(char* name, GLfloat size) {
  GLfloat k = size;

  glLineWidth(7);

  /* X */
  glColor3f(1, 0, 0);
  glBegin(GL_LINES);
  glVertex3f(0, 0, 0);
  glVertex3f(k, 0, 0);
  glEnd();

  glPushMatrix();
  glTranslatef(k, 0, 0);
  drawText(name);
  glPopMatrix();

  /* Y */
  glColor3f(0, 1, 0);
  glBegin(GL_LINES);
  glVertex3f(0, 0, 0);
  glVertex3f(0, k, 0);
  glEnd();

  glPushMatrix();
  glTranslatef(0, k, 0);
  drawText(name);
  glPopMatrix();

  /* Z */
  glColor3f(0, 0, 1);
  glBegin(GL_LINES);
  glVertex3f(0, 0, 0);
  glVertex3f(0, 0, k);
  glEnd();

  glPushMatrix();
  glTranslatef(0, 0, k);
  drawText(name);
  glPopMatrix();

  glColor3f(1, 1, 1);
  drawText(name);
}

/* ------------------------------------------------------------------------- */
void drawLine(GLdouble v0[3], GLdouble v1[3],
              GLdouble r, GLdouble g, GLdouble b) {
  glLineWidth(7);
  glLineStipple(1, 0xE0E0);
  glColor3f(r, g, b);
  glBegin(GL_LINES);
  glVertex3dv(v0);
  glVertex3dv(v1);
  glEnd();
  glLineStipple(0, 0xFFFF);
}

/* ------------------------------------------------------------------------- */
void display(void) {
  GLdouble v0[3];
  GLdouble v1[3];
  GLdouble m0[16];
  GLdouble m1[16];
  GLdouble m2[16];
  GLdouble inv[16];

  if(pause) {
    return;
  }

  rotation[0] += 0.02f;
  rotation[1] += 0.04f;
  rotation[2] += 0.06f;

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  setCamera();

  /* Draw the world reference frame */
  drawAxes("", 0.3);

  /* Rotate the current reference frame and draw its axes */
  glRotatef(rotation[0], 1, 0, 0);
  glRotatef(rotation[1], 0, 1, 0);
  glRotatef(rotation[2], 0, 0, 1);
  drawAxes("0", 1);

  /* Save the matrix of the current reference frame */
  glGetDoublev(GL_MODELVIEW_MATRIX, m0);

  /* Apply more rotations and translations, then draw the axes */
  glTranslatef(1, 1, 1);
  glRotatef(rotation[0], 1, 0, 0);
  glRotatef(rotation[1], 0, 1, 0);
  glRotatef(rotation[2], 0, 0, 1);
  drawAxes("1", 1);

  /* Save the matrix of the new reference frame */
  glGetDoublev(GL_MODELVIEW_MATRIX, m1);

  /* Calculate the end-points of a segment in the current reference frame
     that goes from the current origin to the parent frame origin.
     v1 - is simply (0, 0, 0) as that is the current frame origin position
     v0 - needs to be transformed so that it becomes the position of the parent
          frame origin, represented in the current frame */
  setv(v1, 0, 0, 0);

  setv(v0, 0, 0, 0);
  mulv(m0, v0, v0);
  invm(m1, inv);
  mulv(inv, v0, v0);

  /* Actually draw the segment */
  drawLine(v0, v1, 1, 1, 0);

  /* Repeat the above for another reference frame (i.e. draw lines from its origin to
     the other frames' origins */
  glRotatef(rotation[0], 1, 0, 0);
  glRotatef(rotation[1], 0, 1, 0);
  glRotatef(rotation[2], 0, 0, 1);
  glTranslatef(1, 1, 1);
  drawAxes("2", 1);

  glGetDoublev(GL_MODELVIEW_MATRIX, m2);

  /* Draw line to frame "0" */
  setv(v1, 0, 0, 0);
  setv(v0, 0, 0, 0);
  mulv(m0, v0, v0);
  invm(m2, inv);
  mulv(inv, v0, v0);

  drawLine(v0, v1, 1, 0, 1);

  /* Draw line to frame "1" */
  setv(v1, 0, 0, 0);
  setv(v0, 0, 0, 0);
  mulv(m1, v0, v0);
  invm(m2, inv);
  mulv(inv, v0, v0);

  drawLine(v0, v1, 0, 1, 1);

  glutSwapBuffers();
}

/* ------------------------------------------------------------------------- */
void reshape(int width, int height) {
  glViewport(0, 0, width, height);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  gluPerspective(45, (GLfloat)width/(GLfloat)height, 0.1, 800);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();
}

/* ------------------------------------------------------------------------- */
void init() {
  glEnable(GL_DEPTH_TEST);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
  glEnable(GL_BLEND);
  glEnable(GL_LINE_SMOOTH);
  glEnable(GL_LINE_STIPPLE);

  glMatrixMode(GL_PROJECTION);  
  gluPerspective(40, 1, 0.2, 100);
  setCamera();
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

  init();
  glutMainLoop();
  return 0;
}
