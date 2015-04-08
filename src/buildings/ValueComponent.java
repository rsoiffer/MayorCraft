/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package buildings;

import core.AbstractComponent;

/**
 *
 * @author CGoodman16
 */
public class ValueComponent extends AbstractComponent{
    double val;//the amount of money earned by the building
    double improve;//The chance for a buliding's value to grow by 10%. From 0 to 1
    public ValueComponent(){
        val=100;
        improve=0.1;
    }
    public ValueComponent(double v, double i){
        val=v;
        improve=i;
    }
    public double getValue(){
        return val;
    }
    public void improve(){
        if (Math.random()>improve){
            val*=1.1;
        }
    }
}
