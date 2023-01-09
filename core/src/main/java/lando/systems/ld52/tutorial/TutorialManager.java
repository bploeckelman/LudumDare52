package lando.systems.ld52.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TutorialManager {

    public static boolean SHOW_TUTORIAL = false;

    Array<TutorialPart> parts;
    String welcomeTutorial = "Welcome to Reapo-man.\nYour goal is to harvest souls.  Heaven and Hell will send you quotas to fill each day.";
    String explainPlayer = "This is you. These all still need copy";
    String explainQuotas = "These are your quotas";
    String explainBoard = "This is the board, click and hold to charge your scythe";
    String explainTimer = "This is the timer\nDon't let it run out";
    String wrapup = "Good Luck\nWe hope you enjoy our game.";

    public TutorialManager() {
        parts = new Array<>();
        parts.add(new TutorialPart(-10,-10,0,0, welcomeTutorial));
        parts.add(new TutorialPart(372, 631, 441-372, 705-631, explainPlayer));
        parts.add(new TutorialPart(1032, 257, 1252-1032, 706-257, explainQuotas));
        parts.add(new TutorialPart(369, 100, 911-369, 616-100, explainBoard));
        parts.add(new TutorialPart(1032, 30, 1251-1032, 250-30, explainTimer));
        parts.add(new TutorialPart(-10,-10,0,0, wrapup));

    }

    public void update(float dt) {
        if (SHOW_TUTORIAL && parts.size > 0) {
            parts.get(0).update(dt);
            if (parts.get(0).complete){
                parts.removeIndex(0);
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (SHOW_TUTORIAL && parts.size > 0) {
            parts.get(0).render(batch);
        }
    }

    public boolean tutorialActive() {
        return SHOW_TUTORIAL && parts.size > 0;
    }
}
