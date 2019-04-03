package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Player;
import it.polimi.ingsw.Map.Square;

import java.util.ArrayList;

public class MovementEffect extends Effect {

    public MovementEffect(int costBlue, int costRed, int costYellow, String name, ArrayList<Constraint> constraints, ArrayList<Boolean> constraintPositivity, int distance, boolean linear) {
        super(costBlue, costRed, costYellow, name, constraints, constraintPositivity);
        this.distance = distance;
        this.linear = linear;
    }

    private int distance;
    private boolean linear;

    public void apply(Square destination, Player target, Player owner){

        return;
    }
}