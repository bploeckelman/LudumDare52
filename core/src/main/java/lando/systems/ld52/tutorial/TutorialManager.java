package lando.systems.ld52.tutorial;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class TutorialManager {

    public static boolean SHOW_TUTORIAL = false;

    Array<TutorialPart> parts;
    String welcomeTutorial = "Good morning, Reapo-Man.\n\n" +
            "As a reminder, agents with one or more active disciplinary citations \n" +
            "are required to review the standards and procedures checklist before each shift.\n\n";

    String explainPlayer =
            "REMINDER: \nAs a trusted member of the Reapo Depot family, you are expected to harvest your assigned souls in a timely fashion. \n\n";
    String explainQuotas = "REMINDER: \nYour daily quotas will appear in the upper right portion of your screen.\n\n" +
            "You are required to harvest at least ONE soul from your assigned Heaven OR Hell quota during your shift.\n\n" +
            "You are encouraged to harvest as many souls as possible during your shift, but your assigned soul must be reaped before the end of each day.\n\n" +
            "Your shift will end upon successful harvest of the day's Heaven or Hell quota.";
    String explainBoard = "REMINDER: \nClick and hold to engage your scythe's harvest mode. \n\n" +
            "Release your mouse when your scythe is above the soul you wish to harvest. \n\n";

    String explainTimer = "REMINDER: \nFailing to reap a soul from an assigned quota within your shift's allotted time may incur further disciplinary action.";
    String wrapup = "REMINDER: \n\nYour scythe's range may not be able to reach all souls from all directions.\n\n" +
            "If you are not able to reap your desired soul from a given location, you are encouraged to try again from different side of the field. \n\n" +
            "Happy reaping! (harvesting)";


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
