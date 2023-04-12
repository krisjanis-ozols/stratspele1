/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kihakik
 */
package First;

public class Unit implements Cloneable{
    private String type;
    private int coord;
    private int hp;
    private int mvmt;
    private int range;
    private boolean bTeam;
    
    public Unit(String type,int coord,int hp,int mvmt,int range,boolean bTeam){
        this.type = type;
        this.coord = coord;
        this.hp = hp;
        this.mvmt = mvmt;
        this.range = range;
        this.bTeam = bTeam;
    }

    @Override
    public Unit clone() {
        try {
            return (Unit) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String getType(){
        return type;
    }
    public int getCoord(){
        return coord;
    }
    public int getHp(){
        return hp;
    }    
    public int getMvmt(){
        return mvmt;
    }
    public int getRange(){
        return range;
    }
    public boolean getBTeam(){
        return bTeam;
    }
    
    public void setCoord(int coord){
        this.coord=coord;
    }
    public void setHp(int hp){
        this.hp=hp;
    }
}
