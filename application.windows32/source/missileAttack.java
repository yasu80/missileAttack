import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class missileAttack extends PApplet {

//**************************The_Source_was_written_by_Tokuyasu_Hajime**************************
//**************************__________Source_Built:20160613__________**************************
//TODO.get fps
//obj
Missile_op[] missile_op = new Missile_op[40];
EnemyAero[] enemyaero = new EnemyAero[30];  
//inherit_num
float ROCKET_SIZE = 10;
float SPD_TERM = 30;
boolean breakFlag;
boolean missileFlag;
boolean hitFlag;
boolean titleFlag = true;
boolean endFlag = false;
int bombCount = 0;
int bullet = 0;
int memo_bullet;
int Title = 4;
int loopCount = 0;
int memo_loopCount;

PImage bgr,hoke;
float targetX, targetY;

//CallBacks Start
public void mousePressed() {
  if(breakFlag == false){
    missileFlag = true;
  }
}
public void mouseReleased() {
  breakFlag = true;
}
//CallBacks Over

//Controller Start
public void command_enemy(){
  int i,
  j = 0;
   for(i = 0; i < enemyaero.length; i ++){
   enemyaero[i].update();
   enemyaero[i].display();
   if (enemyaero[i].checkdown()) j++ ;
   }
   if (j == enemyaero.length){
     if (endFlag ==false){
      memo_bullet = bullet;
      memo_loopCount = loopCount;
     }
     endFlag = true;
     text("You_survived_with\n"+Integer.toString(memo_bullet)+"_Shot\n" +Integer.toString(enemyaero.length) + "_Kill\n" + Integer.toString(memo_loopCount*100) + "_Casuality",100,100);
   }  
}
public void command_myMis(){
  if (missileFlag) {
    if (!breakFlag) {
      update();
      drawMissile(pointX, pointY);
    } else {
      if (++bombCount < 25) {
        bomb();
      } else {
        missileFlag = false;
        bullet++;
      }
    }
  } else {
    init_missile();
  } 
}

public void mainLoop(){
    noCursor();
    background(0xffFFFFFF);
    loopCount++;
    drawBG();
    sight();
    ifTerm();
    command_enemy();
    command_myMis();
    drawEnemyAero(targetX,targetY,30);
    drawTruck(mouseX,height-50);
}

//Controller Over

//Judger Start
public boolean judgeHit(float bx, float by, float tgtX, float tgtY) {
  if (breakFlag = true) {
    if ((bombCount + bombCount) > ( dist(bx,by,tgtX,tgtY) ) )  {
      return true;
    } else return false;
  } else return false;
}
public void ifTerm(){
  if (endFlag == true){
  drawBG();
  text("You_survived_with\n"+Integer.toString(memo_bullet)+"_Shot\n" +Integer.toString(enemyaero.length) + "_Kill\n" + Integer.toString(memo_loopCount*100) + "_Casuality\n__GAME_OVER__",100,100);
  
  println("You_survived_with\n"+Integer.toString(memo_bullet)+"_Shot\n" +Integer.toString(enemyaero.length) + "_Kill\n" + Integer.toString(memo_loopCount*100) + "_Casuality");
  println("GAME OVER");
  noLoop();
  }
}
//Judger Over  

//Animetor Start
public void sight(){
  fill(0xffFFFFFF);
  float x = mouseX;
  float y = mouseY;
  float reticle = 40;
  if (missileFlag == false){
     stroke(0xff0DFF47);
  } else {
    stroke(0xffFA0303);
    text("MSL:"+Float.toString(pointX)+"."+Float.toString(height - pointY),x+20,y-20);
    if ( ((x - reticle) < pointX) && ( pointX <(x + reticle)) && ((y - reticle) < pointY) && ( pointY <(y + reticle)) ){//aiming
    fill(0xffF8FC19 );
    text("VALID AIM",x,y+50);  
    }
  }
  ellipse(x,y,reticle,reticle);
  rect(x-reticle/4,y-reticle/4,reticle/2,reticle/2);
  line(x-reticle/3,y,x+reticle/3,y);
  line(x,y-reticle/3,x,y+reticle/3);
  fill(0xff0DFF47);
  textSize(16);     
  text("TGT:"+Float.toString(x)+"."+Float.toString(height - y),x+20,y+20);

  //line();
}

public void drawBG() {
  noStroke();
  image(bgr,0,0);
  float shoreSide =  width/8;
  float citySide = width*6/8;
  float zero_alt = height -  height/20;
  float building = 200;
  //buiding
  for (int i = 0; i < 4; i++) {
    fill(0xffB4AE9B);
    rect(citySide, height - shoreSide, building/4, building);
    fill(0xffD7FC08);
    rect(citySide + building/16, height - shoreSide*6/7, building/16, building/16);
    citySide += building /2;
  }
  //land
  fill(0xffFCBB05);  
  triangle(0, height, shoreSide, zero_alt, shoreSide, height);
  rect(shoreSide, zero_alt, width - shoreSide, height - zero_alt);
}
public void drawTruck(float arpha, float beta) {
  float center = arpha + 10;
  float truck_SIZE = 10;
  float car = 20;
  //    float len_msl = 50;
  float len_msl = truck_SIZE*5;
  //truck
  stroke(5);
  fill(0xff584BF7);
  rect (arpha,beta,car * 4,car);
  float radius = 20; 
  float offset = 20;
  ellipse(arpha +offset, beta + offset, radius, radius );
  ellipse(arpha + 3* offset , beta + offset, radius, radius );
  rect (arpha + car/2, beta - car, car*3 , car);
  //missile
  noStroke();
  fill(0xff000000);
  for (int i = 0; i < 3; i ++){ 
    rect(arpha,beta - len_msl,truck_SIZE,5*truck_SIZE);
    triangle(arpha+truck_SIZE/2,beta - len_msl+truck_SIZE*3.5f,arpha+truck_SIZE*3/2,beta - len_msl+truck_SIZE*5,arpha-truck_SIZE/2,beta - len_msl+truck_SIZE*5);
    triangle(arpha+truck_SIZE/2,beta - len_msl-truck_SIZE/4,arpha-truck_SIZE*3/4,beta - len_msl+truck_SIZE/2,arpha+truck_SIZE*7/4,beta - len_msl+truck_SIZE/2);
    ellipse(arpha+truck_SIZE/2,beta - len_msl,truck_SIZE,truck_SIZE);
    arpha = arpha + 20;
  }
}
public void drawTarget() {
  fill(0xff000000);
  rect(targetX-15, targetY-15, 30, 30);
} 
public void setTarget() {
  for(int i = 0; i < enemyaero.length; i ++){
    enemyaero[i] = new EnemyAero(random(10,width),random(30,height-100));
    }      
}
public void bomb() {
  float bombX = pointX;
  float bombY = pointY;
  fill(0xffFF1212);
  ellipse(bombX, bombY, bombCount, bombCount);
  for(int i = 0; i < enemyaero.length; i++){
    hitFlag = judgeHit(bombX, bombY, enemyaero[i].x, enemyaero[i].y);
    if (true == hitFlag){
      if (enemyaero[i].downFlag == false){
      enemyaero[i].downFlag = true;
      println("EnemyNo:"+Integer.toString( i )+"Shot_down");
      }
    }
    }
}

public void message() {
  if (hitFlag) {
    text("TargetDown!!!", width/2, height/2);
    text("Your Kill Shot is_"+Integer.toString(bullet), width/2, height/2 -20);      
    if (bullet ==1 )text("\nGoodShot!\n", width/2, height/2 -70);
  }
}

//Title Start
public void effect_title(){
  for(int i = 0; i < 10; i++){
    fill(0xffFF1212);
    delay(100);
    fill(0xff000000);
  }
}
class Missile_op {
  float xMis, yMis, xSpeed, ySpeed,
  RKT_SIZE = 40;  
  Missile_op (float _x, float _y, float _ySpeed) {
    xMis = _x;
    yMis = _y;
    ySpeed = _ySpeed;
  }
  public void update() {
    yMis += ySpeed;

    if (yMis > height) {
      yMis = 0;
    }
  }
  public void display(){
    noStroke();
    fill(0xffFFFFFF);
    rect(xMis,yMis,RKT_SIZE,5*RKT_SIZE);
    triangle(xMis+RKT_SIZE/2,yMis+RKT_SIZE*3.5f,xMis+RKT_SIZE*3/2,yMis+RKT_SIZE*5,xMis-RKT_SIZE/2,yMis+RKT_SIZE*5);
    triangle(xMis+RKT_SIZE/2,yMis-RKT_SIZE/4,xMis-RKT_SIZE*3/4,yMis+RKT_SIZE/2,xMis+RKT_SIZE*7/4,yMis+RKT_SIZE/2);
    ellipse(xMis+RKT_SIZE/2,yMis,RKT_SIZE,RKT_SIZE);
  }  
}
public void drawTitle(){
  int i;
//  int word = 4;
  if ( 0 == phase_title){
    background(0xff000000);  
//    else  phase_title = 1;
    fill(0xffFF080C);
    textSize(Title/4);
    text("MISSILE\nATTACK",100,200);
  fill(0xffFFFFFF);
  for(i = 0; i < missile_op.length; i ++){
   missile_op[i].update();
   missile_op[i].display();
    }
  }
  if (1 == phase_title){
    background(0xff000000);
    fill(0xffFF080C);
    textSize(Title/4);
    text("MISSILE\nATTACK",100,200);
    textSize(Title/8);
    text("PRESS_ANY_KEY",100,550);
  }
}

public void processTitleState(){
  if (2 == phase_title) {
    titleFlag = false;
    println("Incoming " + Integer.toString(enemyaero.length) + " Attackers!!"); 
  } else if (1 == phase_title) {
  phase_title = 2;
  background(0xff000000);
  textSize(28);
  text("Click to Launch\nRelease to Fire.\n...Kill off Invadors!",50,100);
  delay(200);
  println("Click to Launch\nRelease to Fire.\n...Kill off Invadors!");
  image(hoke,width/3, height/3 );
  } else {
  phase_title = 1;
  Title = 400;
  background(0xffFF1212);
  delay(300);//Hey!
  }
}
public void setOpMissiles(){
  for(int i = 0; i < missile_op.length; i ++){
    missile_op[i] = new Missile_op(random(width), height,  random(-8.0f, -2.0f));
    }    
}
//Title Over


//Updators Start 
public void drawEnemyAero(float pX, float pY,float size){
  float arpha = size;
  float beta = size/4;
  fill(0xff8ECCF2);
  ellipse(pX + beta /2, pY - beta/2,beta+ beta,beta);
  noStroke();
  fill(0xffCE1313);
  ellipse(pX,pY,arpha,beta);
  rect(pX,pY - beta/2,arpha,beta);
  triangle(pX + arpha, pY - beta/2, pX + arpha, pY + beta/2 ,pX + arpha + beta /4, pY + beta/2);
  triangle(pX,pY + beta/2, pX + arpha,pY + beta/2,pX + arpha/2 + beta,pY + beta);
  triangle(pX + arpha/2, pY - beta/2, pX + arpha, pY - beta/2 ,pX + arpha , pY - beta - beta);
}

public void drawMissile(float startX, float startY) {
  fill(0xff000000);
  rect(startX, startY, ROCKET_SIZE, 5*ROCKET_SIZE);
  triangle(startX+ROCKET_SIZE/2, startY+ROCKET_SIZE*3.5f, startX+ROCKET_SIZE*3/2, startY+ROCKET_SIZE*5, startX-ROCKET_SIZE/2, startY+ROCKET_SIZE*5);
  triangle(startX+ROCKET_SIZE/2, startY-ROCKET_SIZE/4, startX-ROCKET_SIZE*3/4, startY+ROCKET_SIZE/2, startX+ROCKET_SIZE*7/4, startY+ROCKET_SIZE/2);
  ellipse(startX+ROCKET_SIZE/2, startY, ROCKET_SIZE, ROCKET_SIZE);
}

public void accelMe() {
  spd += random(1, 2);  
  pointY  -= spd;
}

public void enplace_myMis() {
  if (pointY < 50) breakFlag = true;
  if (pointY > height) pointY = 0;
  if (pointX < 0) pointX = width;
  if (pointX > width) pointX = 0;
}

public void speedLimit() {
  if (spd > SPD_TERM) spd = SPD_TERM;
}

public void update() {
  accelMe();
  enplace_myMis();
  speedLimit();
}

public void init_missile() {
  point_zero = mouseX;
  pointX = point_zero;
  pointY = height - 50;
  bombCount = 0;
  breakFlag = false;
}
//Updators Start
class Missile {
  float xMis, yMis, xSpeed, ySpeed; 
   float RKT_SIZE = 50;
  Missile (float _x, float _y, float _ySpeed) {
    xMis = _x;
    yMis = _y;
    ySpeed = _ySpeed;
  }
  public void update() {
    yMis += ySpeed;
    if (yMis > height) {
      yMis = 0;
    }
  }
  public void display(){
    noStroke();
    fill(0xffFFFFFF);
    rect(xMis,yMis,RKT_SIZE,5*RKT_SIZE);
    triangle(xMis+RKT_SIZE/2,yMis+RKT_SIZE*3.5f,xMis+RKT_SIZE*3/2,yMis+RKT_SIZE*5,xMis-RKT_SIZE/2,yMis+RKT_SIZE*5);
    triangle(xMis+RKT_SIZE/2,yMis-RKT_SIZE/4,xMis-RKT_SIZE*3/4,yMis+RKT_SIZE/2,xMis+RKT_SIZE*7/4,yMis+RKT_SIZE/2);
    ellipse(xMis+RKT_SIZE/2,yMis,RKT_SIZE,RKT_SIZE);
  }  
}
class EnemyAero {
 float x, y;
 float size = 50;
 boolean downFlag = false;
 EnemyAero (float X, float Y){
   x = X;
   y = Y;
 }
 public void display(){
  drawEnemyAero(x,y,size);   
 }
 public void update(){
   x += random(-10,-6);
   y += random(-2,2);
   if(y < 100 ) y = 101;
   if(y > height - 200) y = height -210;
   if(x < 0) x = width -5;
 }
 public boolean checkdown(){
  if (true == downFlag){
    size = 1;
    return true;
  }
  else return false;
 }
}
class EnemyMis {
 float x, y;
 float size = 50;
 EnemyMis (float X, float Y){
   x = X;
   y = Y;
 }
 public void display(float x, float y, float size){
  float arpha = size;
  float beta = size/4;
  fill(0xff8ECCF2);
  ellipse(x + beta /2, y - beta/2,beta+ beta,beta);
  noStroke();
  fill(0xffCE1313);
  ellipse(x,y,arpha,beta);
  rect(x,y - beta/2,arpha,beta);
  triangle(x + arpha, y - beta/2, x + arpha, y + beta/2 ,x + arpha + beta /4, y + beta/2);
  triangle(x,y + beta/2, x + arpha,y + beta/2,x + arpha/2 + beta,y + beta);
  triangle(x + arpha/2, y - beta/2, x + arpha, y - beta/2 ,x + arpha , y - beta - beta);
 }
}
//Animetors Over

//Val
int phase_title = 0;
float point_zero;
float spd = 0;  
float turn = 0;
float pointX;
float pointY;
//Val_End

public void setup() {
  textSize(16);
  
  setTarget();
  setOpMissiles();
  bgr = loadImage("background.jpg");
  hoke = loadImage("hoke.jpg");
}

public void draw() {
  if (titleFlag == true){
    drawTitle();
    if (keyPressed){
    processTitleState();
    }
    if(Title < 400)Title++;
    if (Title >= 400 && phase_title == 0) phase_title = 1;
  }
  else {
    mainLoop();
  }
}
  public void settings() {  size (1000, 700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "missileAttack" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
