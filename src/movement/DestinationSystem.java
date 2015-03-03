package movement; 

import core.AbstractSystem;
import core.MouseInput;
import core.Vec2;


public class DestinationSystem extends AbstractSystem{

private PositionComponent position;
private VelocityComponent velocity;
private DestinationComponent destination;

public DestinationSystem(PositionComponent position, VelocityComponent velocity, DestinationComponent destination){


}
public void update(){
velocity.vel=new Vec2(0,0);//Code to move to location of destination that I don't feel like writing yet
}
}