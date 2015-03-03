package game; 

import core.AbstractEntity;
import core.MouseInput;
import movement.*;

public class Unit extends AbstractEntity {
	public Unit(){
		PositionComponent pc = add(new PositionComponent());
		RotationComponent rc = add(new RotationComponent());
		DestinationComponent dc = add(new DestinationComponent());
	}
        //I'm not sure when stuff like this is called, but this handles clicks 
	public void stuff(){
            if (MouseInput.isDown(1)){
               DestinationComponent comp= new DestinationComponent(MouseInput.mouse());
            }
        }



}
