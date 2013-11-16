import processing.core.*; 
import processing.xml.*; 

import processing.video.*; 
import neurosky.*; 
import org.json.*; 
import jp.nyatla.nyar4psg.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class armindwave extends PApplet {

/**
 nyARToolkit + neurosky mindwave
 */




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

public void setup() {
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

public void draw()
{  
  stroke(255);


  if (cam.available() !=true) {
    return;
  }

  cam.read();
  nya.detect(cam);
  background(0);
  nya.drawBackground(cam);//frustum\u3092\u8003\u616e\u3057\u305f\u80cc\u666f\u63cf\u753b
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
      translate(0, 0, lerp(mindvalue,20,.03f));
    } 
    else {
      mindvalue = lerp(mindvalue, attention, .03f);
      translate(0, 0, map(mindvalue,0,127,0,180));
      rotateX(a*0.03f);
      rotateY(a*0.05f);
      
    }
    
    scale(25);
    TexturedCube(tex);
    a++;
    nya.endTransform();
  }
}







public void poorSignalEvent(int sig) {
  println("SignalEvent "+sig);
}

public void attentionEvent(int attentionLevel) {
  println("Attention Level: " + attentionLevel);
  attention = attentionLevel;
}


public void meditationEvent(int meditationLevel) {
  println("Meditation Level: " + meditationLevel);
  meditation = meditationLevel;
}

public void blinkEvent(int blinkStrength) {
  println("blinkStrength: " + blinkStrength);
}

/*public void eegEvent(int delta, int theta, int low_alpha, int high_alpha, int low_beta, int high_beta, int low_gamma, int mid_gamma) {
  println("delta Level: " + delta);
  println("theta Level: " + theta);
  println("low_alpha Level: " + low_alpha);
  println("high_alpha Level: " + high_alpha);
  println("low_beta Level: " + low_beta);
  println("high_beta Level: " + high_beta);
  println("low_gamma Level: " + low_gamma);
  println("mid_gamma Level: " + mid_gamma);
}*/

/*void rawEvent(int[] raw) {
  //println("rawEvent Level: " + raw);
}*/	

public void stop() {
  neuroSocket.stop();
  super.stop();
}
public void TexturedCube(PImage tex) {
  beginShape(QUADS);
  texture(tex);

  // Given one texture and six faces, we can easily set up the uv coordinates
  // such that four of the faces tile "perfectly" along either u or v, but the other
  // two faces cannot be so aligned.  This code tiles "along" u, "around" the X/Z faces
  // and fudges the Y faces - the Y faces are arbitrarily aligned such that a
  // rotation along the X axis will put the "top" of either texture at the "top"
  // of the screen, but is not otherwised aligned with the X/Z faces. (This
  // just affects what type of symmetry is required if you need seamless
  // tiling all the way around the cube)
  
  // +Z "front" face
  vertex(-1, -1,  1, 0, 0);
  vertex( 1, -1,  1, 1, 0);
  vertex( 1,  1,  1, 1, 1);
  vertex(-1,  1,  1, 0, 1);

  // -Z "back" face
  vertex( 1, -1, -1, 0, 0);
  vertex(-1, -1, -1, 1, 0);
  vertex(-1,  1, -1, 1, 1);
  vertex( 1,  1, -1, 0, 1);

  // +Y "bottom" face
  vertex(-1,  1,  1, 0, 0);
  vertex( 1,  1,  1, 1, 0);
  vertex( 1,  1, -1, 1, 1);
  vertex(-1,  1, -1, 0, 1);

  // -Y "top" face
  vertex(-1, -1, -1, 0, 0);
  vertex( 1, -1, -1, 1, 0);
  vertex( 1, -1,  1, 1, 1);
  vertex(-1, -1,  1, 0, 1);

  // +X "right" face
  vertex( 1, -1,  1, 0, 0);
  vertex( 1, -1, -1, 1, 0);
  vertex( 1,  1, -1, 1, 1);
  vertex( 1,  1,  1, 0, 1);

  // -X "left" face
  vertex(-1, -1, -1, 0, 0);
  vertex(-1, -1,  1, 1, 0);
  vertex(-1,  1,  1, 1, 1);
  vertex(-1,  1, -1, 0, 1);

  endShape();
}

  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "armindwave" });
  }
}
