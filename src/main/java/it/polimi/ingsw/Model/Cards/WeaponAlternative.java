package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Exception.NotThisKindOfWeapon;
import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class WeaponAlternative extends Weapon {

    private ArrayList<Effect> alternativeEffect;

    public WeaponAlternative(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<Effect> alternativeEffect) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.alternativeEffect=alternativeEffect;
    }


    @Override
    public void fireOptional(TargetParameter target, int which) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

    public void fireAlternative(TargetParameter target) throws InvalidTargetExcepion {
        for (Effect e : alternativeEffect){
            if(e.constraintsCheck(target)){
                e.apply(target);
            }
        }
        return;
    }
}
