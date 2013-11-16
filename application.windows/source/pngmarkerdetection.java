import processing.core.*; 
import processing.xml.*; 

import processing.video.*; 
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

public class pngmarkerdetection extends PApplet {

/**
 NyARToolkit for proce55ing/1.2.0
 (c)2008-2012 nyatla
 airmail(at)ebony.plala.or.jp
 
 \u30de\u30fc\u30ab\u30d5\u30a1\u30a4\u30eb\u306e\u5909\u308f\u308a\u306bPNG\u3092\u4f7f\u3044\u307e\u3059\u3002
 PNG\u306f\u4efb\u610f\u89e3\u50cf\u5ea6\u306e\u6b63\u65b9\u5f62\u3067\u3042\u308b\u5fc5\u8981\u304c\u3042\u308a\u307e\u3059\u3002
 \u30a8\u30c3\u30b8\u90e8\u5206\u306e\u30d1\u30bf\u30fc\u30f3\u306f\u542b\u3081\u3066\u304f\u3060\u3055\u3044\u3002
 
 This sample program uses a PNG image instead of the patt file.
 The PNG image must be square form that includes edge.
 */


PImage tex;
float a;
String textsource = "";
Capture cam;
MultiMarker nya;

public void setup() {
  size(640, 480, P3D);
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
}

public void draw()
{
  if (cam.available() !=true) {
    return;
  }
  cam.read();
  nya.detect(cam);
  background(0);
  nya.drawBackground(cam);//frustum\u3092\u8003\u616e\u3057\u305f\u80cc\u666f\u63cf\u753b

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
    }else if(i == 4){
      textsource = "kanji.png";  
    }

    tex = loadImage(textsource);
    translate(0, 0, 20);
    rotateX(a*0.03f);
    rotateY(a*0.05f);
    scale(25);
    TexturedCube(tex);
    a++;
    nya.endTransform();
  }
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
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "pngmarkerdetection" });
  }
}
