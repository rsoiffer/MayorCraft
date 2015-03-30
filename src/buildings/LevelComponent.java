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
public class LevelComponent {
    int level=0;
    
    //The conditions for a level up can be implemented here, or not. 
    public void levelUp(){
    level++;
    }
    public int getLevel(){
        return level;
    }
}
