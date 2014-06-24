package com.obc.csrg.icefaces;

/* MPL License text (see http://www.mozilla.org/MPL/) */

public class PanelToolTipModel {

	private String hideOn = "mouseout";

    private String hoverDelay ="500";

    private boolean draggable;

    private String displayOn = "hover";

    private boolean moveWithMouse = false;

    public String getHideOn() {
        return hideOn;
    }

    public void setHideOn(String hideOn) {
        this.hideOn = hideOn;
    }

    public String getHoverDelay() {
        return hoverDelay;
    }

    public int getHoverDelayTime(){
        try {
            return Integer.parseInt(hoverDelay);
        } catch (NumberFormatException e) { // ICE-4753
            return 500;
        }
    }

    public void setHoverDelay(String hoverDelay) {
        this.hoverDelay = hoverDelay;
    }

    public boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getDisplayOn() {
        return displayOn;
    }

    public void setDisplayOn(String displayOn) {
        this.displayOn = displayOn;
    }

    public boolean isMoveWithMouse() {
        return moveWithMouse;
    }

    public void setMoveWithMouse(boolean moveWithMouse) {
        this.moveWithMouse = moveWithMouse;
    }
}
