package it.polimi.ingsw.Map;

import java.util.ArrayList;

public class Room {

    private int size;
    private ArrayList<Square> squares= new ArrayList<Square>();
    private Board board;

    public Room(int size, ArrayList<Integer> x, ArrayList<Integer> y, Board board) {
        this.size = size;
        this.board= board;
        // TODO
    }

    public Square find(int x, int y){

        return null;
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Square> getSquares() {
        return squares;
    }

    public Board getBoard() {
        return board;
    }
}
