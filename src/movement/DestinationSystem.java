package movement; 

import core.AbstractSystem;
import core.MouseInput;
import core.Vec2;


public class DestinationSystem extends AbstractSystem{

private PositionComponent position;
private VelocityComponent velocity;
private DestinationComponent destination;

public DestinationSystem(PositionComponent position, DestinationComponent destination){


}
public void update(){
   destination.des=MouseInput.mouse();
    
   
}
}