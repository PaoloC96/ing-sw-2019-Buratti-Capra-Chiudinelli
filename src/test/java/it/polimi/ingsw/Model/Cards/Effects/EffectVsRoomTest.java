package it.polimi.ingsw.Model.Cards.Effects;

import it.polimi.ingsw.Exception.InvalidTargetException;
import it.polimi.ingsw.Exception.NotFoundException;
import it.polimi.ingsw.Model.Cards.Constraints.AdjacentRoom;
import it.polimi.ingsw.Model.Cards.Constraints.Constraint;
import it.polimi.ingsw.Model.Cards.Constraints.NotSameSquare;
import it.polimi.ingsw.Model.Map.Board;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.TargetParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EffectVsRoomTest {

    EffectVsRoom test;
    Player enemy, enemy2, enemy3;
    Player owner;
    TargetParameter target;
    Board board;
    ArrayList<Constraint> constraints;
    NotSameSquare notSameSquare;
    AdjacentRoom adjacentRoom;
    ArrayList<Boolean> constrainPositivity;

    @BeforeEach
    public void setup(){
        owner = new Player(true, "blue", "Franco");
        enemy = new Player(true, "green", "Lucio");
        enemy2 = new Player(true, "red", "Fabio");
        enemy3 = new Player(true, "yellow", "Gino");
        board = new Board(null, "./resources/Board/Board1.json");
        notSameSquare = new NotSameSquare();
        adjacentRoom = new AdjacentRoom();
        constraints = new ArrayList<Constraint>(Arrays.asList(notSameSquare,adjacentRoom));
        constrainPositivity = new ArrayList<Boolean>();
        target = new TargetParameter(null, owner, null, null, null);
        test = new EffectVsRoom(0,0,0,"Vulcanizzatore",constraints,constrainPositivity,1,0);

    }


    @Test
    void apply() {

        board.getRooms().get(0).getSquares().get(0).arrives(enemy);
        enemy.setPosition(board.getRooms().get(0).getSquares().get(0));
        board.getRooms().get(0).getSquares().get(1).arrives(enemy2);
        enemy2.setPosition(board.getRooms().get(0).getSquares().get(1));
        board.getRooms().get(0).getSquares().get(2).arrives(enemy3);
        enemy3.setPosition(board.getRooms().get(0).getSquares().get(2));

        target.setTargetRoom(board.getRooms().get(0));
        target.getConstraintSquareList().add(enemy.getPosition());
        target.getConstraintSquareList().add(enemy2.getPosition());
        target.getConstraintSquareList().add(enemy3.getPosition());

        try {
            board.find(1,2).arrives(owner);
            owner.setPosition(board.find(1,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            test.apply(target,null);
        } catch (InvalidTargetException invalidTargetException) {
            invalidTargetException.printStackTrace();
        }
        assertEquals(1, enemy.getDamageCounter());
        assertEquals(target.getOwner(),enemy.getDamage().get(0));
        assertEquals(1, enemy2.getDamageCounter());
        assertEquals(target.getOwner(),enemy2.getDamage().get(0));
        assertEquals(1, enemy3.getDamageCounter());
        assertEquals(target.getOwner(),enemy3.getDamage().get(0));
    }

    @Test
    void applyNotNearRoom() {

        board.getRooms().get(0).getSquares().get(0).arrives(enemy);
        enemy.setPosition(board.getRooms().get(0).getSquares().get(0));
        board.getRooms().get(0).getSquares().get(1).arrives(enemy2);
        enemy2.setPosition(board.getRooms().get(0).getSquares().get(1));
        board.getRooms().get(0).getSquares().get(2).arrives(enemy3);
        enemy3.setPosition(board.getRooms().get(0).getSquares().get(2));

        target.setTargetRoom(board.getRooms().get(0));
        target.getConstraintSquareList().add(enemy.getPosition());
        target.getConstraintSquareList().add(enemy2.getPosition());
        target.getConstraintSquareList().add(enemy3.getPosition());
        try {
            board.find(2,2).arrives(owner);
            owner.setPosition(board.find(2,2));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        assertThrows(InvalidTargetException.class,()->test.apply(target,null));
    }
}