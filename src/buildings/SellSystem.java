/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buildings;

/**
 *
 * @author CGoodman16
 */
public class SellSystem {
    ValueComponent vc;
    LevelComponent lc;
    public SellSystem(ValueComponent vc, LevelComponent lc){
           this.vc=vc;
           this.lc=lc;
       }
    public void sell(){
        /*Add an amount of money and destroy the building. Resources aren't implenented yet.  */
    }
}
