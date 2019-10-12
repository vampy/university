#include <stdio.h>
#include <math.h>
#include <windows.h>
#include <GL/glut.h>

#define N 7

#define POS      0
#define VEL      1
#define ACC      2
#define MAXACC   3
#define MAXDEC   4
#define MAXVEL   5
#define LEN      6
#define RED      7
#define GREEN    8
#define BLUE     9
#define STOPD   10
#define WORSTD  11

#define PI 3.141592653

GLfloat light_position[] = {0.0, 0.0, 1.0, 0.0};
GLfloat light_diffuse[] =  {1.0, 1.0, 1.0, 1.0};
GLfloat light_specular[] = {1.0, 1.0, 1.0, 1.0};

GLdouble cameraPosition[3] = {0.0f, 0.0f, 180.0f};
GLdouble angle = 0.0f;
GLdouble roadRadius = 70.0;
GLdouble roadAngle = 0.0;
GLdouble reactionTime = 0.675;
GLdouble prevTime;
double maxdt = 0.0;


FILE* flog = NULL;

GLdouble car[N][12] = {
  /* Pos  Vel  Acc  MaxAcc  MaxDec  MaxVel   Len    R    G    B  STOPD WORSTD*/
  {  0.0, 0.0, 0.0,    3.0,   -4.5,   27.0,  2.0, 1.0, 1.0, 1.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,    6.0,   -9.0,   30.0,  2.5, 1.0, 0.0, 0.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,    8.0,  -12.0,   35.0,  4.0, 0.0, 1.0, 0.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,    4.0,   -6.0,   28.0,  4.0, 0.0, 0.0, 1.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,    8.0,  -12.0,   30.0,  6.0, 1.0, 0.0, 1.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,    2.0,   -3.0,   26.0,  4.0, 1.0, 1.0, 0.0,   0.0,   0.0},
  {  0.0, 0.0, 0.0,   10.0,  -15.0,   40.0,  4.0, 0.0, 1.0, 1.0,   0.0,   0.0}};

int pause = 1;

/* ------------------------------------------------------------------------- */
void setMaterial(GLdouble r, GLdouble g, GLdouble b) {
  GLfloat mat[3];

  mat[0] = r;
  mat[1] = g;
  mat[2] = b;

  glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, mat);
  glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, mat);
  glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, mat);

  glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 1);
}

/* ------------------------------------------------------------------------- */
void idle(void) {
  glutPostRedisplay();
}

/* ------------------------------------------------------------------------- */
void drawCar(int n) {
  GLdouble x = (360.0*car[n][POS])/(2.0*PI*roadRadius);

  glPushMatrix();
  glRotated(x, 0.0, 0.0, 1.0);
  glTranslated(roadRadius, 0.0, 0.0);

  setMaterial(car[n][RED], car[n][GREEN], car[n][BLUE]);
  glScaled(2.0, car[n][LEN], 1.0);
  glutSolidCube(1.0);
  glPopMatrix();
}

/* ------------------------------------------------------------------------- */
void drawText(char *s) {
  int i;
  GLfloat k = 0.1;

  glPushMatrix();
  glLineWidth(5.0);
  glScalef(k, k, k);
  setMaterial(1.0, 1.0, 1.0);
  glColor3d(1.0, 1.0, 1.0);

  glDisable(GL_LIGHTING);
  for (i=0; i<(int)strlen(s); i++) {
    glutStrokeCharacter(GLUT_STROKE_ROMAN, s[i]);
  }
  glEnable(GL_LIGHTING);

  glPopMatrix();
}

/* ------------------------------------------------------------------------- */
void setCamera() {
  GLdouble dir[3] = {0, 0, -1};
  GLdouble up[3] =  {0, 1,  0};
  GLdouble v[3];
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
    case '0':
      car[0][VEL] = 0;
      car[0][ACC] = 0;
      break;
    case '1':
    case '2':
    case '3':
    case '4':
    case '5':
    case '6':
    case '7':
    case '8':
    case '9':
      car[0][ACC] = (car[0][MAXACC] - car[0][MAXDEC])*(key - '1')/8+ car[0][MAXDEC];
      break;
    case 'a':
      roadAngle += 10.0;
      break;
    case 'z':
      roadAngle -= 10.0;
      break;
    case ' ':
      pause = (pause+1) % 2;
      prevTime = (GLdouble)GetTickCount()/1000.0;
      break;
  }
}

/* ------------------------------------------------------------------------- */
GLdouble dist(int current, int front) {
  GLdouble d = car[front][POS] - car[current][POS];
  while(d < 0) {
    d += 2*PI*roadRadius;
  }
  return d;
}

/* ------------------------------------------------------------------------- */
GLdouble stoppingDist(int n) {
  GLdouble reactVel;
  GLdouble reactDist;
  GLdouble stopTime;
  GLdouble stopDist;

  /* Distance and velocity change until the driver reacts */
  reactDist = reactionTime*car[n][VEL];
  if(car[n][ACC] > 0) {
	reactDist += 0.5*car[n][ACC]*reactionTime*reactionTime;
  }
  reactVel = car[n][VEL];
  if(car[n][ACC] > 0) {
    reactVel += car[n][ACC]*reactionTime;
  }

  /* Stopping time */
  stopTime = -reactVel/car[n][MAXDEC];
  stopTime = maxdt*ceil(stopTime/maxdt);

  /* Stopping distance */
  stopDist = abs(stopTime*reactVel + 0.5*car[n][MAXDEC]*stopTime*stopTime);
  if(stopDist < 0) {
    stopDist = 0;
  }
  stopDist += reactDist;

  fprintf(flog, "STOPD % 3d -> % 7.2lf  % 7.2lf | % 7.2lf  % 7.2lf\n", n, reactDist, reactVel, stopTime, stopDist);

  return stopDist;
}

/* ------------------------------------------------------------------------- */
GLdouble worstDist(int n) {
  GLdouble stopTime;
  GLdouble stopDist;
  GLdouble dec;

  /* Stopping time */
  stopTime = -car[n][VEL]/car[n][MAXDEC];
  stopTime = maxdt*ceil(stopTime/maxdt);

  /* Stopping distance */
  dec = -10;
  stopDist = stopTime*car[n][VEL] + 0.5*dec*stopTime*stopTime;
  if(stopDist < 0) {
    stopDist = 0;
  }

  fprintf(flog, "WORSTD % 3d -> % 7.2lf  % 7.2lf\n", n, stopTime, stopDist);

  return stopDist;
}

/* ------------------------------------------------------------------------- */
GLdouble safeDist(int current, int front) {
  GLdouble currentStopDist;
  GLdouble frontStopDist;
  GLdouble d;

  /* Stopping distance */
  currentStopDist = car[current][STOPD];
  frontStopDist = car[front][WORSTD];

  d = 0.5*(car[front][LEN] + car[current][LEN]) + 1;
  if(currentStopDist > frontStopDist) {
    d += currentStopDist - frontStopDist;
  }

  return d;
}

/* ------------------------------------------------------------------------- */
int carInFront(int n) {
  int i;
  int k;
  GLdouble d;
  GLdouble x;
  int first;

  first = 1;
  for(i=0; i<N; i++) {
    if(i == n) {
      continue;
    }

    x = dist(n, i);
    if(first) {
      d = x;
      k = i;
      first = 0;
      continue;
    }

    if(d > x) {
      d = x;
      k = i;
    }
  }

  return k;
}

/* ------------------------------------------------------------------------- */
void updateCars() {
  int i;
  int k;
  int front;
  int slowest;
  GLdouble acc[N];
  GLdouble x;
  GLdouble err;
  GLdouble sd;
  GLdouble gain;


  GLdouble t = (GLdouble)GetTickCount()/1000.0;
  GLdouble dt = t - prevTime;
  prevTime = t;

  if(dt > maxdt) {
    maxdt = dt;
  }

  /* Update car velocities and positions based on the time elapsed and
     previous values*/
  for(i=0; i<N; i++) {
    x = car[i][POS] + car[i][VEL]*dt + 0.5*car[i][ACC]*dt*dt;
    if(car[i][POS] < x) {
      car[i][POS] = x;
    }
    car[i][POS] = 
      car[i][POS] - 
      2.0*PI*roadRadius*floor(car[i][POS]/(2.0*PI*roadRadius));

    car[i][VEL] = car[i][VEL] + car[i][ACC]*dt;
    if(car[i][VEL] < 0) {
      car[i][VEL] = 0;
    }
    if(car[i][VEL] > car[i][MAXVEL]) {
      car[i][VEL] = car[i][MAXVEL];
    }
  }

  /* Find the car with the lowest velocity */
  for(i=0; i<N; i++) {
    if(i == 0) {
      slowest = 0;
      continue;
    }

    if(car[i][VEL] < car[slowest][VEL]) {
      slowest = i;
    }
  }

  /* Calculate stopping distances */
  for(i=0; i<N; i++) {
    car[i][STOPD] = stoppingDist(i);
  }
  fprintf(flog, "\n");

  /* Calculate worst distances */
  for(i=0; i<N; i++) {
    car[i][WORSTD] = worstDist(i);
  }
  fprintf(flog, "\n");

  /* Update car accelerations in order to adapt to the traffic, starting 
     from the slowest car */
  for(i=0; i<N; i++) {
    k = (slowest+i) % N;

    front = carInFront(k);
	sd = safeDist(k, front);
    err = dist(k, front) - sd;

    if(err > 0) {
	  if(err >= sd) {
	    gain = 1.0;
	  }
	  else {
	    gain = err/sd;
	  }
      acc[k] = car[k][MAXACC]*gain;
    }
    else if (err < 0) {
      acc[k] = car[k][MAXDEC];
    }
    else {
      acc[k] = 0;
    }

	acc[0] = car[0][ACC];
    fprintf(flog, "% 9.3lf % 9.3lf % 3d % 3d | % 7.2lf % 7.2lf | % 7.2lf % 7.2lf % 7.2lf | % 7.2lf\n", t, dt, front, k, dist(k, front), sd, car[k][POS], car[k][VEL], acc[k], err);
  }
  fprintf(flog, "\n");

  for(i=1; i<N; i++) {
    car[i][ACC] = acc[i];
  }
}

/* ------------------------------------------------------------------------- */
void display(void) {
  int i;
  char s[32];

  if(!pause) {
    updateCars();
  }

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  setCamera();

  sprintf(s, "% 3.0lf", ceil(car[0][VEL]*3.6));
  drawText(s);

  glRotated(roadAngle, 1.0, 0.0, 0.0);
  setMaterial(0.4, 0.4, 0.4);
  glutSolidTorus(0.2, roadRadius, 100, 100);

  for(i=0; i<N; i++) {
    drawCar(i);
  }

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
int main(int argc, char **argv) {
  int i;

  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
  glutInitWindowSize(500, 500);
  glutCreateWindow("Demo");
  glutDisplayFunc(display);
  glutIdleFunc(idle);
  glutKeyboardFunc(keyboard);
  glutReshapeFunc(reshape);

  init();

  prevTime = (GLdouble)GetTickCount()/1000.0;
  for(i=0; i<N; i++) {
    car[i][POS] = (N-i-1)*2*PI*roadRadius/(2*N);
  }

  flog = fopen("cars.log", "wt");
  glutMainLoop();
  fclose(flog);
  return 0;
}
