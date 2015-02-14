# AndriodBalls
Bouncing balls.
A project to learn android programming.
 
- Setting up an animation loop
- Drawing images on the canvas
- Rotating and scaling images
- Touch events: start, drag, pinch
- Accelerometer events for gravity

So for I have 4 balls that bounce off the edges of the screen and have acceleration due to gravity.  
The balls also bounce off each other.  Collisions are elastic, but the balls eventually do slow 
down due to some numerical effects.  Linear momentum for the mass of balls is computed correctly 
Balls do get rotation when they hit the walls and each other, but there is no real angular momentum.

Touch events are set up so a ball is captured (follows the finger) when touched.  The velocity changes with the touch so the 
captured ball can be thrown.  Pinch with a captured ball changes its radius.

TODO: 
- Improve the physics computation with real angular momentum.
- Make collision computation more efficient so we can have more balls.
- Power management with pause and resume events in Main activity.
- Change method names to start with lower case letters.