/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import buildings.Building;
import core.AbstractComponent;
import world.Terrain;

/**
 *
 * @author CGoodman16
 */
public class ResourceComponent extends AbstractComponent{
    private Terrain ter;
    private Building build;
    public ResourceComponent(Terrain t, Building b){
        ter=t;
        build=b;
               
    }
}
