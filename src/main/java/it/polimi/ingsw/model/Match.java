package it.polimi.ingsw.model;

import it.polimi.ingsw.model.map.Board;

import java.util.ArrayList;

/**
 * This class represents the match
 */
public class Match {
    /**
     * This attribute is the circular list of the players
     */
    private CircularArrayList<Player> players;
    /**
     * This attribute is the number of the players that are playing
     */
    private int numPlayers;
    /**
     * This attribute is the number of skulls for this game
     */
    private int skulls;
    /**
     * This attribute is the killshot track
     */
    private ArrayList<Player> killShotTrack= new ArrayList<>();
    /**
     * This attribute is the frenzy enable setting
     */
    private boolean frenzyEn;
    /**
     * This attribute is the mode that the players are playing
     */
    private String mode;
    /**
     * This attribute is the turn of a player
     */
    private Turn turn;
    /**
     * This attribute is the board of the game
     */
    private Board board;

    int i;

    /**
     * This class is the circular array list of players
     * @param <Player> This parameter is the player list
     */
    private class CircularArrayList<Player> extends ArrayList<Player>
    {
        public CircularArrayList(ArrayList<Player> players) {
            this.addAll(players);
        }

        public Player get(int index)
        {
            if (index >= players.size())
            {
                index = index%players.size() ;
            }
            return super.get(index);
        }
    }

    /**
     * This constructor instantiates the match
     * @param players This parameter is the list of the players that are playing
     * @param numPlayers This parameter is number of player that are playing
     * @param skulls This parameter is the number of skulls that the game'll have
     * @param frenzyEn This parameter is the frenzy enable, true if the players want the frenzy turn, false otherwise
     * @param mode This parameter is the mode that the players are playing
     */
    public Match(ArrayList<Player> players, int numPlayers, int skulls, boolean frenzyEn, String mode, String type) {
        this.players = new CircularArrayList<>(players);
        this.numPlayers = numPlayers;
        this.skulls = skulls;
        this.frenzyEn = frenzyEn;
        this.mode = mode;
        this.i=0;
        this.turn=new Turn(false,null,this);
        this.board= new Board(this, type);
    }

    /**
     * This method starts the game
     */
    public void start() {
        this.turn.endTurn();
    }

    /**
     * This method starts the turn
     */
    public void startTurn() {
        this.turn.reset(this.frenzyEn, players.get(i));
        i++;
    }

    /**
     * This method sets the turn to a frenzy turn
     * @return True if the number of skulls is 0 and the frenzy enable is set on true, false otherwise
     */
    public boolean setFrenzy(){
        if(this.skulls==0 && this.frenzyEn)
            return true;
        return false;
    }

    /**
     * This method computes the points on the killshot track and control who is the winner
     */
    public void endGame(){
        ArrayList<Player> killPlayer = new ArrayList<>();
        ArrayList<Integer> killCounter = new ArrayList<>();
        ArrayList<Player> killPriority = new ArrayList<>();
        ArrayList<Player> winPlayer = new ArrayList<>();
        int k;
        int max=0;
        int index=0;
        boolean found;
        boolean added;
        boolean equal=true;

        for (i = 0; i < this.killShotTrack.size(); i++) {
            for (k = 0,found=false; k < killPlayer.size() && !found; k++)
                if (this.killShotTrack.get(i) == killPlayer.get(k)) {
                    killCounter.set(k, killCounter.get(k) + 1);
                    found = true;
                }
            if (!found) {
                killPlayer.add(this.killShotTrack.get(i));
                killCounter.add(1);
            }
        }

        for(i=0;!killPlayer.isEmpty();i++) {    // SET POINT FOR ALL KILLER
            for (k = 0,max=0,index=0;k<killCounter.size(); k++)
                if (killCounter.get(k) > max) {
                    max = killCounter.get(k);
                    index = k;
                }
            killPlayer.get(index).setPoints(killPlayer.get(index).getPoints() + getTurn().calcPoints(i));
            killPriority.add(killPlayer.get(index));
            killCounter.remove(index);
            killPlayer.remove(index);
        }

        max=0;
        do {
            for (i = 0,added = false; i < this.numPlayers; i++)
                if (this.players.get(i).getPoints() >= max && !winPlayer.contains(this.players.get(i))) {
                    max = this.players.get(i).getPoints();
                    index = i;
                    added = true;
                }
            if (added)
                winPlayer.add(this.players.get(index));
        }
        while(added);

        //if there are more than one player with the same point, control and remove if there are player that didn't make a kill
        if(winPlayer.size()>1) {
            for (i = 0; i < winPlayer.size(); i++)
                if(killPriority.contains(winPlayer.get(i))) {
                    equal = false;
                    for (i = 0; i < winPlayer.size(); i++)
                        if (!killPriority.contains(winPlayer.get(i))) {
                            winPlayer.remove(i);
                            i--;
                        }
                }

            if(!equal) {
                found = false;
                for (i = 0; i < killPriority.size() && !found; i++)
                    if (winPlayer.contains(killPriority.get(i))) {
                        found = true;
                        winPlayer.clear();
                        winPlayer.add(killPriority.get(i));
                    }
            }
        }
        //print for test
        /*for (i = 0; i < winPlayer.size(); i++)
            System.out.println(winPlayer.get(i).getNickname());
        */
    }

    /**
     * This method returns the board of this match
     * @return The board of this match
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method sets the board of this match
     * @param board This parameter is the board that will be played
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * This method returns the turn of the match
     * @return The turn of the match
     */
    public Turn getTurn() {
        return turn;
    }

    /**
     * This method sets the turn of the match
     * @param turn This parameter is the turn that the match'll have
     */
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    /**
     * This method returns the list of players that are playing
     * @return The list of players that the match has
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * This method return the number of skulls of the match
     * @return The number of the actual skulls of the match
     */
    public int getSkulls() {
        return skulls;
    }

    /**
     * This method sets the skulls of the match
     * @param skulls This parameter is the number of skulls that the match'll have
     */
    public void setSkulls(int skulls) {
        this.skulls = skulls;
    }

    /**
     * This method return the killshot track of the match
     * @return The killshot track that the match'll have
     */
    public ArrayList<Player> getKillShotTrack() {
        return killShotTrack;
    }

    /**
     * This method sets the killshot track of the match
     * @param killShotTrack This parameter is the killshot track that the match'll have
     */
    public void setKillShotTrack(ArrayList<Player> killShotTrack) {
        this.killShotTrack = killShotTrack;
    }

    /**
     * This method return if the frenzy turn is enable or not
     * @return The settings of the frenzy enable
     */
    public boolean isFrenzyEn() {
        return frenzyEn;
    }
}