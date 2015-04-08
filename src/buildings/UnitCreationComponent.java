/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package buildings;

import core.AbstractComponent;
import core.Vec2;
import graphics.RenderSystem;
import graphics.SpriteComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import movement.PositionComponent;
import movement.PreviousPositionComponent;
import movement.PreviousPositionSystem;
import movement.RotationComponent;
import movement.VelocityComponent;
import movement.VelocitySystem;
import units.AnimationComponent;
import units.AnimationSystem;
import units.CollisionSystem;
import units.DestinationComponent;
import units.DestinationSystem;
import units.SelectableComponent;
import units.SelectableSystem;

/**
 *
 * @author CGoodman16
 */
public class UnitCreationComponent extends AbstractComponent{
    public UnitCreationComponent(){
        
    }
        //Components
       /* PositionComponent pc = add(new PositionComponent());
        VelocityComponent vc = add(new VelocityComponent());
        PreviousPositionComponent ppc = add(new PreviousPositionComponent());
        RotationComponent rc = add(new RotationComponent());
        SpriteComponent sc = add(new SpriteComponent("PoliceHead2"));//("cavemanHead"));
        AnimationComponent ac = add(new AnimationComponent(pos));
        DestinationComponent dc = add(new DestinationComponent());
        SelectableComponent slc = add(new SelectableComponent(16, pc, dc));

        //Systems
        add(new DestinationSystem(pc, vc, dc));
        add(new VelocitySystem(pc, vc));
        add(new CollisionSystem(pc, ppc, vc, slc, dc));
        add(new SelectableSystem(slc));
        add(new RenderSystem(pc, rc, sc));
        add(new AnimationSystem(ac, pc, ppc, rc));
        add(new PreviousPositionSystem(pc, ppc));*/
    
}
