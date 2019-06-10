package it.polimi.ingsw.communication.client;

import it.polimi.ingsw.view.ViewInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MessageHandler {
    private String toSend, toShow;
    private ArrayList<String> slowSend;
    private String[] bigReceive;
    private ViewInterface view;
    private Client client;
    private Timer timer;
    private State state;

    private static final int startFirstEtiquette = 0, endFirstEtiquette = 3, timerDuration = 2, instantTimerResponse = 1, nameEtiquette = 4;

    public enum State{
        LOGIN, MENU, WAIT, BOARD;
    }

    public MessageHandler(ViewInterface view, Client client) {
        this.view = view;
        this.slowSend = new ArrayList<>();
        this.client = client;
        this.state = State.LOGIN;
    }

    public String correctToSend(){
        if(this.slowSend.isEmpty()){
            return toSend;
        }
        String sendable = slowSend.get(0);
        this.slowSend.remove(0);
        if(this.slowSend.isEmpty()){
            client.setGo(false);
        }
        return sendable;
    }

    protected synchronized void understandMessage(String msg){

        if(msg.startsWith(">>>")){
            this.toShow = msg;
            view.showMessage();
        }
        else {
            switch (this.state) {
                case LOGIN: {
                    loginUnderstand(msg);
                    break;
                }
                case MENU: {
                    menuUnderstand(msg);
                    break;
                }
                case WAIT: {
                    waitUnderstand(msg);
                }
                case BOARD: {

                }
            }
        }
    }

    public void understandReceived(String msg){
        if(msg == null){
            client.setToStop(true);
            view.stopView();
        }
        else{
            switch (msg.substring(startFirstEtiquette,endFirstEtiquette)){
                case ">>>":{
                    this.toShow = msg;
                    view.showMessage();
                    break;
                }
                case "§§§":{
                    bigReceive = msg.substring(nameEtiquette).split("-");
                    break;
                }
                case "+++":{
                    view.dataShow(msg.substring(3));
                    break;
                }
                default:

            }
        }
    }

    private void loginUnderstand(String msg){

        switch (msg){
            case "Login":{
                view.loginView();
                break;
            }
            case "Now you are in the waiting room":{
                this.state = State.WAIT;
                view.waitingRoomView();
                break;
            }
            case "setting":{
                this.state = State.MENU;
                break;
            }
            default:
        }
    }

    private void menuUnderstand(String msg){
        String[] stringo = msg.split("-");
        switch (stringo[0]){
            case "Select a board":{
                view.boardSettingView(stringToArrayList(stringo[1]),"Board");
                break;
            }
            case "Select the number of skulls":{
                view.boardSettingView(stringToArrayList(stringo[1]),"Skull");
                break;
            }
            case "Do you like to play with frenzy? Y/N":{
                view.boardSettingView(new ArrayList<>(Arrays.asList("Y","N")),"Frenzy");
                break;
            }
            case "Now you are in the waiting room":{
                this.state = State.WAIT;
                view.waitingRoomView();
                break;
            }
            default:
        }
    }

    private void waitUnderstand(String msg){
        if(msg.substring(startFirstEtiquette,endFirstEtiquette).equals("§§§")){
            bigReceive = msg.substring(nameEtiquette).split("-");
            view.waitingRoomView();
        }
    }

    public void update(){
        this.toSend = "Ok";

        this.timer.cancel();
    }

    private ArrayList<String> stringToArrayList(String msg){
        ArrayList<String> data = new ArrayList<>();
        for(String s: msg.substring(1,msg.length()-1).split(",")){
            data.add(s);
        }
        return data;
    }

    private static TimerTask wrap(Runnable r){
        return new TimerTask() {
            @Override
            public void run() {
                r.run();
            }
        };
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String[] getBigReceive() {
        return bigReceive;
    }

    public String getToShow() {
        return toShow;
    }

    public void setReceive(String receive) {
        this.toShow = receive;
    }

    public void slowSendAdd(String msg){
        this.slowSend.add(msg);
    }
}
