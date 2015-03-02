package game; 

import core.AbstarctEntity; 
import core.Keys;
import core.MouseInput;
import movement.*;

public class Unit extends AbstractEntity {
	public Unit(){
		PositionComponent pc = add(new PositionComponent());
		RotationComponent rc = add(new RotationComponent());
		
	}
	public void followOrders(){


	}



}
