package Pyro.ShapeViewer3D.Utilities;


public class Box3D
{
  
  private int x;
  private int y;
  private int z;
  
  private int w;
  private int h;
  private int d;
  
  public Box3D(int x, int y, int z, int w, int h, int d)
  {
    
    this.x = x;
    this.y = y;
    this.z = z;
    
    this.w = w;
    this.h = h;
    this.d = d;
    
  }
  
  public Box3D()
  {
    
    this(0, 0, 0, 0, 0, 0);
    
  }
  
  public int getX()
  {
    
    return this.x;
    
  }
  
  public int getY()
  {
    
    return this.y;
    
  }
  
  public int getZ()
  {
    
    return this.z;
    
  }
  
  public int getWidth()
  {
    
    return this.w;
    
  }
  
  public int getHeight()
  {
    
    return this.h;
    
  }
  
  public int getDepth()
  {
    
    return this.d;
    
  }
  
  public int getMaxX()
  {
    
    return this.x + this.w;
    
  }
  
  public int getMaxY()
  {
    
    return this.y + this.h;
    
  }
  
  public int getMaxZ()
  {
    
    return this.z + this.d;
    
  }

}
