/**
 NyARToolkit for proce55ing/1.2.0
 (c)2008-2012 nyatla
 airmail(at)ebony.plala.or.jp
 
 マーカファイルの変わりにPNGを使います。
 PNGは任意解像度の正方形である必要があります。
 エッジ部分のパターンは含めてください。
 
 This sample program uses a PNG image instead of the patt file.
 The PNG image must be square form that includes edge.
 */
import processing.video.*;
import jp.nyatla.nyar4psg.*;
PImage tex;
float a;
String textsource = "";
Capture cam;
MultiMarker nya;

void setup() {
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

void draw()
{
  if (cam.available() !=true) {
    return;
  }
  cam.read();
  nya.detect(cam);
  background(0);
  nya.drawBackground(cam);//frustumを考慮した背景描画

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
    rotateX(a*0.03);
    rotateY(a*0.05);
    scale(25);
    TexturedCube(tex);
    a++;
    nya.endTransform();
  }
}

