/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buildings;

import core.AbstractSystem;
import core.Main;
import movement.PositionComponent;
import units.Unit;

/**
 *
 * @author CGoodman16
 */
public class HouseSystem extends AbstractSystem{
    private PositionComponent pc;
    private int counter=0;
    public HouseSystem(PositionComponent pc){
        this.pc=pc;
    }
    public void update(){
        if (counter%1000==0 && Main.gameManager.elc.list.size() < Main.gameManager.rc.food ){
        new Unit(pc.pos, BuildingType.HOUSE);
        }
        counter++;
    }
}
