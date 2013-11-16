/**
 nyARToolkit + neurosky mindwave
 */
import processing.video.*;
import neurosky.*;
import org.json.*;
import jp.nyatla.nyar4psg.*;
ThinkGearSocket neuroSocket;
PFont font;
int attention=10;
int meditation=10;
PImage tex;
float a;
float mindvalue;
String textsource = "";
Capture cam;
MultiMarker nya;

void setup() {
  size(640, 480, P3D);
  ThinkGearSocket neuroSocket = new ThinkGearSocket(this);
  try {
    neuroSocket.start();
  } 
  catch (ConnectException e) {
    //println("Is ThinkGear running??");
  }
  colorMode(RGB, 100);
  println(MultiMarker.VERSION);
  cam=new Capture(this, 640, 480);
  nya=new MultiMarker(this, width, height, "camera_para.dat", NyAR4PsgConfig.CONFIG_PSG);
  nya.addARMarker(loadImage("input.png"), 16, 25, 80); //id0
  nya.addARMarker(loadImage("hiro.png"), 16, 25, 80); //id1
  nya.addARMarker(loadImage("input2.png"), 16, 25, 80); //id2
  nya.addARMarker(loadImage("input3.png"), 16, 25, 80); //id3
  nya.addARMarker(loadImage("kanji.png"), 16, 25, 80); //id4
  textureMode(NORMALIZED);
  fill(255);
  stroke(color(44, 48, 32));
  font = createFont("FFScala", 32);
  textFont(font);
}

void draw()
{  
  stroke(255);


  if (cam.available() !=true) {
    return;
  }

  cam.read();
  nya.detect(cam);
  background(0);
  nya.drawBackground(cam);//frustumを考慮した背景描画
  //fill(0,255,255);
  //rect(0,0,width,height);
  for (int i=0;i<5;i++) { 
    if ((!nya.isExistMarker(i))) {
      continue;
    }

    nya.beginTransform(i);
    if (i == 0) {
      textsource = "input.png";
    } 
    else if (i == 1) {
      textsource = "hiro.png";
    } 
    else if (i == 2) {
      textsource = "input2.png";
    } 
    else if (i == 3) {
      textsource = "input3.png";
    }
    else if (i == 4) {
      textsource = "kanji.png";
    }

    tex = loadImage(textsource);

    if (attention < 45) {
      mindvalue = 20;
      translate(0, 0, lerp(mindvalue,20,.03));
    } 
    else {
      mindvalue = lerp(mindvalue, attention, .03);
      translate(0, 0, map(mindvalue,0,127,0,180));
      rotateX(a*0.03);
      rotateY(a*0.05);
      
    }
    
    scale(25);
    TexturedCube(tex);
    a++;
    nya.endTransform();
  }
}







