package Pyro.ShapeViewer3D.Utilities;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;

import com.jogamp.newt.opengl.GLWindow;

import processing.core.PApplet;

public class MouseHandler
{
  
  private static Point prevMouse;
  
  public static Point getMouseDiff(PApplet app)
  {
    
    Point mouse = new Point(app.mouseX, app.mouseY);
    
    if (prevMouse == null) prevMouse = mouse;
    
    Point diff = subP(mouse, prevMouse);
    
    prevMouse = mouse;
    
    return diff;
    
  }
  
  public static void wrapMouse(PApplet app, Point box)
  {
    
    Point center = new Point(app.width / 2, app.height / 2);
    
    Point newMouse = new Point();
    
    newMouse.x = app.mouseX < (center.x - (box.x / 2)) ? (center.x + (box.x / 2) - 1) : app.mouseX > (center.x + (box.x / 2)) ? (center.x - (box.x / 2) + 1) : app.mouseX;
    newMouse.y = app.mouseY < (center.y - (box.y / 2)) ? (center.y + (box.y / 2) - 1) : app.mouseY > (center.y + (box.x / 2)) ? (center.y - (box.x / 2) + 1) : app.mouseY;
    
    Point windLoc = new Point(((GLWindow) app.getSurface().getNative()).getX(), ((GLWindow) app.getSurface().getNative()).getY());
    
    try
    {
      
      new Robot().mouseMove((int)(windLoc.getX() + newMouse.getX()), (int)(windLoc.getY() + newMouse.getY()));
      prevMouse = newMouse;
    
    }
    catch (AWTException e)
    {
      
      e.printStackTrace();
      
    }
    
  }
  
  public static void centerMouse(PApplet app)
  {
    
    try
    {

      Point windLoc = new Point(((GLWindow) app.getSurface().getNative()).getX(), ((GLWindow) app.getSurface().getNative()).getY());
      
      new Robot().mouseMove((int)(windLoc.getX() + (app.width / 2)), (int)(windLoc.getY() + (app.height / 2)));
    
    }
    catch (AWTException e)
    {
      
      e.printStackTrace();

    }
    
  }
  
  public static void setupGLWIN(PApplet app)
  {
    
    ((GLWindow) app.getSurface().getNative()).setPosition(160, 228);
        
  }
  
  private static Point subP(Point p1, Point p2)
  {
    
    return new Point(p1.x - p2.x, p1.y - p2.y);
    
  }

}
