# AndriodBalls
Example for animating and interacting with images.  Balls bounce around.

So for I have 4 large balls that bounce off the edges of the screen and have a uniform acceleration toward the bottom (gravity).  
The balls also bounce off each other.  Collisions are elastic, but the balls do slow down due to some numerical effects.

Touch events are setup so a ball is captured (follows the finger) when touched.  The velocity changes with the touch so the 
captured ball can be thrown.  

The simulation can get out of normal operating bounds and all balls get stuck at 0,0   I have yet to debug this.
