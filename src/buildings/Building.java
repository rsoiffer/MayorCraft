/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package buildings;

import core.AbstractEntity;
import movement.PositionComponent;

/**
 *
 * @author CGoodman16
 */
/**
 * Has: position: x, y
 * sell value (ValueComponent): How much money received upon selling
 * units (UnitCreationComponent): What type of unit is created
 * unit storage (HousingComponent): How many units of what type can be stored here.  
 * type (TypeComponent): What it does. Types of buildings: 
 * upkeep (UpkeepComponent): 
 * size (SizeComponet):
 * level(LevelComponent):
 * 
 * Can:
 * be bought and placed
 * be sold
 * produce units
 * be selected
 * generate income/cost resources
 * draw self
 * 
 * 
 * 
 * Types of buildings/hero:
 * House/human
 * Police Station/Officer(Batman): Fights crime. (Fights super crime)
 * fire station/Fireman(Elsa): Puts out fires (Freezes fires)
 * school/teacher(Dr Gorman): Boost education points (Spew complex math at random times during gameplay)
 * bank/banker(alchemist): Holds money, makes more money. 
 * library/scholar(genius): Boost education; Research things. 
 * lab/scientist(mad scientist): Research things. 
 * dock/captain(aquaman):
 * hanger/pilot(astronaut):
 * lumber yard/lumberjack(Paul Bunyan): Turns trees into WOOD. 
 * mine/miner(dwarf): 
 * forge/smith(Masamune): 
 * factory/worker(Karl Marx):
 * Construction site/builder(Bob the Builder):
 * stadium/athlete(Good Athlete?):
 * farm/farmer (Old MCDonald):
 * ranch/cowboy (Clint Eastwood):
 * 
 * 
 * @author CGoodman16
 */
public class Building extends AbstractEntity{
    //components
    PositionComponent pc = add(new PositionComponent()); 
    /**ValueComponent*/ FooSystem vc = add(new FooSystem());
    /**UnitCreationComponent*/FooSystem uc = add(new FooSystem());
    /**HousingComponent*/ FooSystem hc = add(new FooSystem());
    /**TypeComponent*/ FooSystem tc = add(new FooSystem());
    /**UpkeepComponent*/ FooSystem ic = add(new FooSystem());
    /**SizeComponent*/ FooSystem sc = add(new FooSystem());
    /**LevelComponent*/ FooSystem lc = add(new FooSystem());
    //systems
    /**SellSystem*/ FooSystem vs = add(new FooSystem());
    /**UnitCreationSystem*/ FooSystem us = add(new FooSystem());
    /**TypeSystem*/ FooSystem ts = add(new FooSystem());
    /**IncomeSystem*/ FooSystem is = add(new FooSystem());
    /**LevelSystem*/ FooSystem ls = add(new FooSystem());
    
    
}
