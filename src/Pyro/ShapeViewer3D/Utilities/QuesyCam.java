package Pyro.ShapeViewer3D.Utilities;

/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

import java.awt.Point;
import java.util.HashMap;
import processing.core.*;
import processing.event.KeyEvent;

public class QuesyCam 
{
  public final static String VERSION = "##library.prettyVersion##";

  public boolean controllable;
  public float speed;
  public float sensitivity;
  public PVector position;
  public float pan;
  public float tilt;
  public PVector velocity;
  public float friction;

  private PApplet applet;
  private PVector center;
  private PVector up;
  private PVector right;
  private PVector forward;
  private PVector target;
  private Point mouse;
  private HashMap<Character, Boolean> keys;
  
  private boolean SHFT = false;
  private Box3D box;

  public QuesyCam(PApplet applet){
    
    this(applet, 0.01f, 1000f);

  }
  
  public QuesyCam(PApplet applet, float near, float far, Box3D box)
  {
    
    this(applet, near, far);
    
    this.box = box;
    
  }

  public QuesyCam(PApplet applet, float near, float far){
    this.applet = applet;
    applet.registerMethod("draw", this);
    applet.registerMethod("keyEvent", this);

    controllable = true;
    speed = 3f;
    sensitivity = 2f;
    position = new PVector(0f, 0f, 0f);
    up = new PVector(0f, 1f, 0f);
    right = new PVector(1f, 0f, 0f);
    forward = new PVector(0f, 0f, 1f);
    velocity = new PVector(0f, 0f, 0f);
    pan = 0f;
    tilt = 0f;
    friction = 0.75f;
    keys = new HashMap<Character, Boolean>();

    MouseHandler.centerMouse(this.applet);
    
    applet.perspective(PConstants.PI/3f, (float)applet.width/(float)applet.height, near, far);
  }

  public void draw(){
    if (!controllable) return;
    
    mouse = MouseHandler.getMouseDiff(this.applet);
    
    MouseHandler.wrapMouse(this.applet, new Point(100, 100));

    pan += PApplet.map(mouse.x, 0, applet.width, 0, PConstants.TWO_PI) * sensitivity;
    tilt += PApplet.map(mouse.y, 0, applet.height, 0, PConstants.PI) * sensitivity;
    tilt = clamp(tilt, -PConstants.PI/2.01f, PConstants.PI/2.01f);

    if (tilt == PConstants.PI/2) tilt += 0.001f;

    forward = new PVector(PApplet.cos(pan), PApplet.tan(tilt), PApplet.sin(pan));
    forward.normalize();
    right = new PVector(PApplet.cos(pan - PConstants.PI/2), 0, PApplet.sin(pan - PConstants.PI/2));

    target = PVector.add(position, forward);

    if (keys.containsKey('a') && keys.get('a')) velocity.add(PVector.mult(right, speed));
    if (keys.containsKey('d') && keys.get('d')) velocity.sub(PVector.mult(right, speed));
    if (keys.containsKey('w') && keys.get('w')) velocity.add(PVector.mult(forward, speed));
    if (keys.containsKey('s') && keys.get('s')) velocity.sub(PVector.mult(forward, speed));
    if (keys.containsKey(' ') && keys.get(' ')) velocity.sub(PVector.mult(up, speed));
    if (SHFT) velocity.add(PVector.mult(up, speed));
    
    velocity.mult(friction);
    
    if (this.box != null)
    {
      
      position.x = position.x < this.box.getX() ? this.box.getX() : position.x;
      position.y = position.y > this.box.getY() ? this.box.getY() : position.y;
      position.z = position.z < this.box.getZ() ? this.box.getZ() : position.z;
      
      position.x = position.x > this.box.getMaxX() ? this.box.getMaxX() : position.x;
      position.y = position.y < this.box.getMaxY() ? this.box.getMaxY() : position.y;
      position.z = position.z > this.box.getMaxZ() ? this.box.getMaxZ() : position.z;
      
      velocity.x = pVAdd(position, velocity).x < this.box.getX() ? this.box.getX() - this.position.x : velocity.x;
      velocity.y = pVAdd(position, velocity).y > this.box.getY() ? this.box.getY() - this.position.y : velocity.y;
      velocity.z = pVAdd(position, velocity).z < this.box.getZ() ? this.box.getZ() - this.position.z : velocity.z;
      
      velocity.x = pVAdd(position, velocity).x > this.box.getMaxX() ? this.box.getMaxX() - this.position.x : velocity.x;
      velocity.y = pVAdd(position, velocity).y < this.box.getMaxY() ? this.box.getMaxY() - this.position.y : velocity.y;
      velocity.z = pVAdd(position, velocity).z > this.box.getMaxZ() ? this.box.getMaxZ() - this.position.z : velocity.z;
      
    }
    
    position.add(velocity);
    center = PVector.add(position, forward);
    applet.camera(position.x, position.y, position.z, center.x, center.y, center.z, up.x, up.y, up.z);
  }

  public void keyEvent(KeyEvent event){
    char key = event.getKey();

    switch (event.getAction()){
      case KeyEvent.PRESS:
        keys.put(Character.toLowerCase(key), true);
        break;
      case KeyEvent.RELEASE:
        keys.put(Character.toLowerCase(key), false);
        break;
    }
    
    if (event.getKeyCode() == 16)
    {
      
      SHFT = SHFT ? false : true;
      
    }
    
    if (event.getAction() == KeyEvent.PRESS && event.getKeyCode() == 16 && !SHFT)
    {
      
      SHFT = true;
      
    }
    
  }

  private float clamp(float x, float min, float max){
    if (x > max) return max;
    if (x < min) return min;
    return x;
  }
  
  private PVector pVAdd(PVector v1, PVector v2)
  {
    
    return new PVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    
  }

  public PVector getForward(){
    return forward;
  }

  public PVector getUp(){
    return up;
  }

  public PVector getRight(){
    return right;
  }

  public PVector getTarget(){
    return target;
  }
  
  public PVector getCenter()
  {
    
    return this.center;
    
  }
  
  public void toggleControllable()
  {
    
    if (!this.controllable)
    {
      
      mouse = new Point(0, 0);
      
      MouseHandler.centerMouse(this.applet);
      
    }
    
    this.controllable = this.controllable ? false : true;
    
  }

}