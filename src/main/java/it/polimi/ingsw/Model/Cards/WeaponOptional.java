package it.polimi.ingsw.Model.Cards;

import it.polimi.ingsw.Exception.InvalidTargetExcepion;
import it.polimi.ingsw.Exception.NotThisKindOfWeapon;
import it.polimi.ingsw.Model.Cards.Effects.Effect;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class WeaponOptional extends Weapon {

    private ArrayList<ArrayList<Effect>> optionalEffect= new ArrayList<ArrayList<Effect>>();

    public WeaponOptional(String color, String name, int costBlue, int costRed, int costYellow, ArrayList<Effect> effect, ArrayList<ArrayList<Effect>> optionalEffect) {
        super(color, name, costBlue, costRed, costYellow, effect);
        this.optionalEffect=optionalEffect;
    }

    @Override
    public void fireOptional(TargetParameter target, int which) throws InvalidTargetExcepion {
        for (Effect e : optionalEffect.get(which)){
            if (e.constraintsCheck(target)){
                e.apply(target);
            }
        }
    }

    @Override
    public void fireAlternative(TargetParameter target) throws NotThisKindOfWeapon {
        throw new NotThisKindOfWeapon();
    }

}
