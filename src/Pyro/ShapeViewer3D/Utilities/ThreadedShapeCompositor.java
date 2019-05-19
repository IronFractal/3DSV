package Pyro.ShapeViewer3D.Utilities;

import processing.core.PApplet;
import processing.core.PShape;

public class ThreadedShapeCompositor implements Runnable
{

  private boolean complete = false;
  
  private PShape shape;
  private PApplet applet;
  
  private boolean blockMap[][][] = new boolean[500][256][500];
  
  public ThreadedShapeCompositor(PApplet applet)
  {
    
    this.applet = applet;
    
  }
  
  @Override
  public void run()
  {

    shape = this.applet.createShape(PApplet.GROUP);
    
    for (int x = 0; x < 420; x++)
    {
      
      for (int z = 0; z < 500; z++)
      {
        
        addBlock(x, 0, z);
        
        addBlock(z, 0, x);
        
        if (z < 256)
        {
          
          addBlock(0, z, 0);
          
        }
        
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
  
  private void addBlock(int x, int y, int z)
  {
    
    if (this.blockMap[x][y][z])
    {
      
      return;
      
    }
    
    PShape box;
    
    box = getBox(x, y, z);
    
    box.translate((x * 16) - 3992, (y * -16) - 8, (z * 16) - 3992);
    
    shape.addChild(box);
    
    this.blockMap[x][y][z] = true;
    
  }
  
  private PShape getBox(int x, int y, int z)
  {
    
    PShape box = applet.createShape();
    
    box.beginShape(PApplet.QUADS);
    box.noFill();
    box.strokeWeight(1.6f);
    
    if (z == 0 || this.blockMap[x][y][z - 1] == false)
    {
      
      //box.stroke(255, 0, 0);
    
      box.vertex(-8, -8, -8);
      box.vertex( 8, -8, -8);
      box.vertex( 8,  8, -8);
      box.vertex(-8,  8, -8);
      
    }

    if (x == 0 || this.blockMap[x - 1][y][z] == false)
    {
      
      //box.stroke(0, 255, 0);
    
      box.vertex(-8, -8, -8);
      box.vertex(-8,  8, -8);
      box.vertex(-8,  8,  8);
      box.vertex(-8, -8,  8);
      
    }
    
    if (x == 499 || this.blockMap[x + 1][y][z] == false)
    {
      
      //box.stroke(0, 0, 255);
    
      box.vertex( 8, -8, -8);
      box.vertex( 8, -8,  8);
      box.vertex( 8,  8,  8);
      box.vertex( 8,  8, -8);
      
    }
    
    if (z == 499 || this.blockMap[x][y][z + 1] == false)
    {
      
      //box.stroke(0, 0, 0);
    
      box.vertex(-8, -8,  8);
      box.vertex(-8,  8,  8);
      box.vertex( 8,  8,  8);
      box.vertex( 8, -8,  8);
      
    }
    
    box.endShape();
    
    return box;
    
  }

}
