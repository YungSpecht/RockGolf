package com.rock.golf;

import com.rock.golf.Bot.AngleBot;
import com.rock.golf.Bot.Bot;
import java.util.Arrays;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.rock.golf.Bot.Bruteforce;
import com.rock.golf.Bot.RuleBasedBot;
import com.rock.golf.Bot.HillClimb;
import com.rock.golf.Bot.ImplicitPathBot;
import com.rock.golf.Bot.PathBot;
import com.rock.golf.Bot.StochasticBot;
import com.rock.golf.Input.InputModule;
import com.rock.golf.Physics.Engine.PhysicsEngine;

public class BotHandler implements InputProcessor {

    private Bot bot;
    private RockGolf golf;
    private PhysicsEngine botEngine;

    public BotHandler(RockGolf rockGolf, PhysicsEngine engine) {
        this.golf = rockGolf;
        botEngine = engine;
    }

    @Override
    public boolean keyDown(int keycode) {

        double[] shot;
        golf.switchState();
        botEngine.tolerance = 0.1;

        if (keycode == Input.Keys.S) {
            bot = new StochasticBot(botEngine, 1000);

        } else if (keycode == Input.Keys.B) {
            bot = new Bruteforce(botEngine, 1);

        } else if (keycode == Input.Keys.H) {
            bot = new HillClimb(botEngine, 800, 100);

        } else if (keycode == Input.Keys.A) {
            bot = new AngleBot(botEngine);

        } else if (keycode == Input.Keys.R) {
            bot = new RuleBasedBot(botEngine);

        } else if(keycode == Input.Keys.P){
            if(RockGolf.currentAstarPath == null){
                System.out.println("ERROR: You haven't searched for a path yet");
            } else{
                bot = new PathBot(botEngine, RockGolf.currentAstarPath);
            }
        } else if(keycode == Input.Keys.I){
            if(RockGolf.currentBFSPath == null){
                System.out.println("ERROR: You haven't searched for a path yet");
            } else{
                bot = new ImplicitPathBot(botEngine, RockGolf.currentBFSPath);
            }
        }


        if (bot != null) {
            shot = bot.getMove();
            System.out.println("Best shot found: [x-velocity, y-velocity] = " + Arrays.toString(shot));
            System.out.println("Shot found in " + bot.getTime() + "ms");
            System.out.println("Amount of simulated shots: " + bot.getIterations());
            InputModule.setNewVelocity(shot[0], shot[1]);
            golf.prepareNewShot();
            golf.executor.execute(botEngine);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
