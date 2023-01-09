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
            "REMINDER: \n\nAs a trusted member of the Reapo Depot family, you are expected to harvest your assigned souls in a timely fashion. \n\n";
    String explainQuotas = "REMINDER: \n\nYour daily quotas will appear in the upper right portion of your screen.\n\n" +
            "You are required to harvest all souls from your assigned Heaven OR Hell quota during your shift based on the features provided.\n\n" +
            "You are encouraged to harvest as many souls as possible during your shift, but your assigned souls must be reaped before the end of each day.\n\n" +
            "Your shift will end upon successful harvest of the day's Heaven or Hell quota.";
    String explainBoard = "REMINDER: \n\nClick and hold to engage your scythe's harvest mode. \n\n" +
            "Release your mouse when your scythe is above the soul you wish to harvest. \n\n";

    String explainTimer = "REMINDER: \n\n" +
            "If you encounter rocks or other obstacles in the harvesting path to a given soul, consider breaking them with your scythe to harvest on your next \npass, or approach the soul from a different side of the field.\n\n "+
            "Stay aware of the time: failing to fulfill a quota within your shift's \n allotted time may incur further disciplinary action and/or condemn you to\n eternal purgation.\n\n"
            ;
    String wrapup = "REMINDER: \n\nYour scythe's range may not be sufficient to reach all souls from all directions.\n\n" +
            "If you are not able to reap a desired soul from a given location, you are encouraged to try again from different side of the field or use the\nprovided powerups to extend your scythe's reach.\n\n" +
            "You may also encounter various forms of cryptocurrency during your shift. You are free to collect these tokens for points, but be aware that they offer no meaningful value. Just like in real life.\n\n" +
            "Happy harvesting!";


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
