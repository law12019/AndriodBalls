# AndriodBalls
Bouncing balls.
A project to learn android programming.

- Setting up an animation loop
- Drawing images on the canvas
- Rotating and scaling images
- Touch events: start, drag, pinch.


So for I have 4 large balls that bounce off the edges of the screen and have a uniform acceleration toward the bottom (gravity).  
The balls also bounce off each other.  Collisions are elastic, but the balls do slow down due to some numerical effects.
Balls do get rotation when they hit the walls and each other, but there is no real angular momentum.

Touch events are setup so a ball is captured (follows the finger) when touched.  The velocity changes with the touch so the 
captured ball can be thrown.  Pinch with a captured ball changes its radius.

TODO: 
- Improve the physics computation with real angular momentum and balls with different masses (computed from radius).
- Make collision computation more efficient so we can have more balls.