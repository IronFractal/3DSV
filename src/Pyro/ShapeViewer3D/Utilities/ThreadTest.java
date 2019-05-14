package Pyro.ShapeViewer3D.Utilities;

import processing.core.PApplet;

public class ThreadTest implements Runnable
{

  private PApplet applet;
  private ThreadedDraw lam;
  
  public ThreadTest(PApplet applet, ThreadedDraw lam)
  {
    
    this.applet = applet;
    this.lam = lam;
    
  }
  
  @Override
  public void run()
  {

    lam.draw(this.applet);
    
  }
  
  public static interface ThreadedDraw
  {
    
    public void draw(PApplet applet);
    
  }

}
