package it.polimi.ingsw.Model.Cards.Constraints;

import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Map.Square;
import it.polimi.ingsw.Model.TargetParameter;

import java.util.ArrayList;

public class SameDirection extends Constraint {

    @Override
    public boolean canShoot(TargetParameter target) {

        ArrayList<Integer> positions;
        positions = new ArrayList<Integer>();
        int i;

        for(Square s: target.getConstraintSquareList()){
            if((target.getOwner().getPosition().getY() != s.getY())&&(target.getOwner().getPosition().getX() != s.getX())){
                return false;
            }
            if((target.getOwner().getPosition().getY() == s.getY())&&(target.getOwner().getPosition().getX() == s.getX())){
                positions.add(1);
            }
            if(target.getOwner().getPosition().getX() == s.getX()){
                if(target.getOwner().getPosition().getY() < s.getY()){
                    positions.add(2);
                }
                if(target.getOwner().getPosition().getY() > s.getY()){
                    positions.add(4);
                }
            }
            if(target.getOwner().getPosition().getY() == s.getY()){
                if(target.getOwner().getPosition().getX() < s.getX()){
                    positions.add(5);
                }
                if(target.getOwner().getPosition().getX() > s.getX()){
                    positions.add(3);
                }
            }
        }

        for(i=1;i<positions.size();i++){
            if((positions.get(i)!= 1)&&(positions.get(i-1)!=1)){
                if(positions.get(i)!=positions.get(i-1)){
                    return false;
                }
            }
        }

        return true;
    }

}