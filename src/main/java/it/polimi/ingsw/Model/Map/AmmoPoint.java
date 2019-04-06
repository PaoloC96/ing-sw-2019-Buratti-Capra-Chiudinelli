package it.polimi.ingsw.Model.Map;

import java.util.ArrayList;

public class AmmoPoint extends Square {

    private AmmoTile ammo;

    public AmmoPoint(int x, int y, String color, Room room) {
        super(x, y, color, room);
    }

    @Override
    public void generate(){
        this.ammo=super.getRoom().getBoard().nextAmmo();
        return;
    }

    @Override
    public boolean require() {
        return ammo==null;
    }
}