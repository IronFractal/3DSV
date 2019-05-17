package Pyro.ShapeViewer3D.Utilities;

import processing.core.PApplet;
import processing.core.PShape;

public class ThreadedShapeCompositor implements Runnable
{

  private boolean complete = false;
  
  private PShape shape;
  private PApplet applet;
  
  public ThreadedShapeCompositor(PApplet applet)
  {
    
    this.applet = applet;
    
  }
  
  @Override
  public void run()
  {

    PShape box;
    
    shape = this.applet.createShape(PApplet.GROUP);
    
    box = applet.createShape(PApplet.BOX, 16);
    
    box.noFill();
    
    shape.noFill();
    shape.setStroke(0);
    box.translate(0, -8, 0);
    shape.addChild(box);
    
    box = applet.createShape(PApplet.BOX, 16);
    box.translate(0, -32, 0);
    shape.addChild(box);
    
    for (int x = 0; x < 200; x++)
    {
      
      for (int z = 0; z < 200; z++)
      {
        
        box = applet.createShape(PApplet.BOX, 16);
        box.noFill();
        box.noStroke();
        box.translate(x * 16, -8, z * 16);
        shape.addChild(box);
        
      }
      
    }
    
    complete = true;
    
  }
  
  public PShape getShape()
  {
    
    return this.shape;
    
  }
  
  public boolean isComplete()
  {
    
    return this.complete;
    
  }

}
