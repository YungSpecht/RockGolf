package com.rock.golf;

import com.rock.golf.Input.InputModule;

public class Sandpit {
    private double uK;
    private double uS;
    private double xStart, xEnd;
    private double yStart, yEnd;

    PhysicsEngine physics = new PhysicsEngine();
    RockGolf golf = new RockGolf();

    public Sandpit() {

    }

    public void changeFriction() {
        // if the ball is in the sandpit, then we change the coefficient of the entire
        // ball until the ball leaves the sandpit input.get_input()[6] &&
        // input.get_input()[7]
        if (physics.sandpitCollision((float) InputModule.get_input()[6], 100, (float) InputModule.get_input()[7], 10,
                (int) golf.ballRadius, 3) == true
                || physics.sandpitCollision((float) InputModule.get_input()[6], 600, (float) InputModule.get_input()[7],
                        800, (int) golf.ballRadius, 3) == true) {
            InputModule.set_new_friction((float) 0.03, (float) 0.1);
        } else {
            InputModule.set_new_friction((float) 0.08, (float) 0.2);

        }
    }
}
