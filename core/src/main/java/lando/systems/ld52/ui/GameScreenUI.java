package lando.systems.ld52.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.ui.widget.VisTable;
import lando.systems.ld52.Assets;

public class GameScreenUI extends VisTable {
    public LeftSideUI leftSideUI;
    public RightSideUI rightSideUI;
    public GameScreenUI(OrthographicCamera windowCamera, Assets assets) {
        setFillParent(true);
        setSize(windowCamera.viewportWidth, windowCamera.viewportHeight);
        setPosition(0, 0);
        leftSideUI = new LeftSideUI(assets, 0, 0, 280f, windowCamera.viewportHeight);
        rightSideUI = new RightSideUI(assets, windowCamera.viewportWidth - 280f, 0, 280f, windowCamera.viewportHeight);
        addActor(leftSideUI);
        addActor(rightSideUI);
    }
}
