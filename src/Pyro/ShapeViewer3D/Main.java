package Pyro.ShapeViewer3D;

import Pyro.ShapeViewer3D.Utilities.Box3D;
import Pyro.ShapeViewer3D.Utilities.MouseHandler;
import Pyro.ShapeViewer3D.Utilities.QuesyCam;
import Pyro.ShapeViewer3D.Utilities.ThreadTest;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.event.KeyEvent;

public class Main extends PApplet
{
	
  private QuesyCam cam;
  
  public final int WIDTH = 1600;
  public final int HEIGHT = 900;
  
  private PImage img;
  
  private PGraphics pg;
  
  public static void main(String[] args)
  {
    
    PApplet.main("Pyro.ShapeViewer3D.Main");
    
  }
  
  public void settings()
  {
    
    size(WIDTH, HEIGHT, P3D);
    
  }
  
  public void setup()
  {
    
    this.frame.setLocation(0, 0);
    
    cam = new QuesyCam(this, 0.01f, 9000, new Box3D(-3990, -10, -3990, 7980, -4086, 7980));
    cam.speed = 5;
    cam.sensitivity = 0.5f;
    this.frameRate(60);
    
    this.registerMethod("keyEvent", this);
    
    MouseHandler.setupGLWIN(this);
    
    img = this.loadImage("ground.png");
    pg = this.createGraphics(this.WIDTH, this.HEIGHT, P3D);
    
    for (int i = 0; i < 500; i++)
    {
      
      pg.beginDraw();
      pg.push();
      pg.noFill();
      pg.stroke(0);
      pg.translate(0, -140, 0);
      pg.box(100);
      pg.pop();
      pg.endDraw();
      
    }
    
  }
  
  public void draw()
  {
    
    background(255);
    stroke(0);
    noFill();
    
    this.fill(100);
    
    this.beginShape();
      texture(img);
      vertex(-4000, 0, -4000, 0, 0);
      vertex(-4000, 0, 4000, img.width, 0);
      vertex(4000, 0, 4000, img.width, img.height);
      vertex(4000, 0, -4000, 0, img.height);
    this.endShape(CLOSE);
    
    new Thread(new ThreadTest(this, (applet) -> 
    {
      
      applet.push();
      applet.fill(255, 0, 0);
      applet.translate(0, -140, 0);
      applet.box(100);
      applet.pop();
      
    }));
    
    try
    {
      
      pg.camera(cam.position.x, cam.position.y, cam.position.z, cam.getCenter().x, cam.getCenter().y, cam.getCenter().z, cam.getUp().x, cam.getUp().y, cam.getUp().z);
      
    }
    catch (NullPointerException e)
    {
      
      
      
    }
    
    this.image(pg, 0, 0);
    
    drawHUD( () -> 
    {
      
      stroke(0);
      fill(0);
      text(frameRate, 10, 10 + textAscent());
 
      text(this.cam.position.toString(), 10, 34);
      
      fill(0, 0, 0, 100);
      rect(0, 0, 120, 80);
      
      if (!cam.controllable)
      {
        
        cursor();
        
        rect(800 - (200 / 2), 450 - (80 / 2), 200, 80);
        
        
      }
      else
      {
        
        noCursor();
        
        //-- Reticle
        noStroke();
        fill(0);
        rect((WIDTH / 2) - (22 / 2), (HEIGHT / 2) - (4 / 2), 22, 4);
        rect((WIDTH / 2) - (4 / 2), (HEIGHT / 2) - (22 / 2), 4, 22);
        fill(255);
        rect((WIDTH / 2) - (20 / 2), (HEIGHT / 2) - (2 / 2), 20, 2);
        rect((WIDTH / 2) - (2 / 2), (HEIGHT / 2) - (20 / 2), 2, 20);
        //-- Reticle
        
      }
      
    });
    
  }
  
  public void keyEvent(KeyEvent event)
  {

    if (event.getKey() == 'e')
    {
      
      cam.toggleControllable();
      
    }
    
  }
  
  interface HUDScript
  {
    
    void draw();
    
  }
  
  private void drawHUD(HUDScript hud)
  {
    
    this.push();
    this.hint(DISABLE_DEPTH_TEST);
    this.noLights();
    this.textMode(MODEL);
    camera();
    hud.draw();
    this.hint(ENABLE_DEPTH_TEST);
    this.pop();
    
  }

}
