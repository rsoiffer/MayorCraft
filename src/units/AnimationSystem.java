package units;

import core.AbstractSystem;
import core.Color4d;
import core.Vec2;
import graphics.Graphics;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import movement.PositionComponent;
import movement.RotationComponent;
import movement.VelocityComponent;

public class AnimationSystem extends AbstractSystem {

    private AnimationComponent ac;
    private PositionComponent pc;
    private VelocityComponent vc;
    private RotationComponent rc;

    public AnimationSystem(AnimationComponent ac, PositionComponent pc, VelocityComponent vc, RotationComponent rc) {
        this.ac = ac;
        this.pc = pc;
        this.vc = vc;
        this.rc = rc;
    }

    @Override
    public void update() {
        double speed = vc.vel.length();
        Vec2 direction = vc.vel.multiply(1 / speed);

        //optimization code
        double s_d = direction.y;
        double c_d = direction.x;

        double rotation_speed = 10;

        double r = Math.max(1, speed * 2.3);

        double target_direction = direction.direction();
        double diff_direction = Math.abs(target_direction - rc.rot + 4 * Math.PI) % 2 * Math.PI;
        if (diff_direction < rotation_speed) {
            rc.rot = target_direction;
        } else if (diff_direction < 180) {
            rc.rot += rotation_speed;
        } else {
            rc.rot -= rotation_speed;
        }

        //arm_direction = rc.rot;
        ac.legL.target = ac.legL.target.setX(pc.pos.x - 8 * s_d + c_d * speed * 30 / r);
        ac.legL.target = ac.legL.target.setY(pc.pos.y - 8 * c_d + s_d * speed * 30 / r);
        ac.legR.target = ac.legR.target.setX(pc.pos.x + 8 * s_d + c_d * speed * 30 / r);
        ac.legR.target = ac.legR.target.setY(pc.pos.y + 8 * c_d + s_d * speed * 30 / r);

        Vec2 dxy = ac.legL.pos.subtract(ac.legL.target);
        double ldist = dxy.length() + 0.1;
        boolean foot_down = false;
        if (speed < 1 && ldist < 2) {
            //foot_down = 0;
        } else if (ldist > ac.d) {
            foot_down = true;
        }

        dxy = ac.legR.pos.subtract(ac.legR.target);
        double rdist = dxy.length() + 0.1;
        if (speed < 1 && rdist < 2) {
            foot_down = true;
        } else if (ldist > ac.d) {
            foot_down = false;
        }

        if (foot_down) {
            ac.legL.pos = ac.legL.pos.add(ac.legL.target.subtract(ac.legL.pos).multiply(Math.min(1, r / ldist)));
        } else {
            ac.legR.pos = ac.legR.pos.add(ac.legR.target.subtract(ac.legR.pos).multiply(Math.min(1, r / rdist)));
        }
        //makes the character look like it's running if it's going fast enough
        if (speed > 2) {
            ac.legL.pos = ac.legL.pos.add(vc.vel.multiply(.5));
            ac.legR.pos = ac.legR.pos.add(vc.vel.multiply(.5));
        }

        if (ldist > ac.d && rdist > ac.d) {
            ac.legL.pos = new Vec2(pc.pos.x - 8 * s_d, pc.pos.y - 8 * c_d);
            ac.legR.pos = new Vec2(pc.pos.x + 8 * s_d, pc.pos.y + 8 * c_d);
        }

        //RENDER CODE
        ac.legL.draw(rc.rot);
        ac.legR.draw(rc.rot);
        //draw_sprite_ext(foot_sprite,0,lfoot_x,lfoot_y,1,1,facing_direction,c_white,1);
        //draw_sprite_ext(foot_sprite,0,rfoot_x,rfoot_y,1,1,facing_direction,c_white,1);

//        lleg_x = x - 6 * sin(facing_direction * pi / 180);
//        lleg_y = y - 6 * cos(facing_direction * pi / 180);
//        rleg_x = x + 6 * sin(facing_direction * pi / 180);
//        rleg_y = y + 6 * cos(facing_direction * pi / 180);
        /*
         lknee_x=(lleg_x+lfoot_x)/2+6*cos(facing_direction*pi/180);
         lknee_y=(lleg_y+lfoot_y)/2-6*sin(facing_direction*pi/180);
         rknee_x=(rleg_x+rfoot_x)/2+6*cos(facing_direction*pi/180);
         rknee_y=(rleg_y+rfoot_y)/2-6*sin(facing_direction*pi/180);

         draw_set_color(rleg_color);
         draw_line_width(rfoot_x-2*cos(facing_direction*pi/180), rfoot_y+2*sin(facing_direction*pi/180), rknee_x, rknee_y, 8);
         draw_circle(rknee_x, rknee_y, 4, false);
         draw_line_width(rleg_x, rleg_y, rknee_x, rknee_y, 8);
         //draw_line_width(rfoot_x, rfoot_y, rleg_x, rleg_y, 4);
         if(has_pegleg){
         draw_set_color(c_white);
         draw_sprite_ext(pegleg,0,lleg_x,lleg_y,sqrt(sqr(lleg_x-lfoot_x)+sqr(lleg_y-lfoot_y))/32,1,arctan2(lleg_y-lfoot_y,lfoot_x-lleg_x)*180/pi,c_white,1);
         }
         else{
         draw_set_color(lleg_color);
         draw_line_width(lfoot_x-2*cos(facing_direction*pi/180), lfoot_y+2*sin(facing_direction*pi/180), lknee_x, lknee_y, 8);
         draw_circle(lknee_x, lknee_y, 4, false);
         draw_line_width(lleg_x, lleg_y, lknee_x, lknee_y, 8);
         }
         //draw_sprite_ext(leg_sprite,0,rleg_x,rleg_y,sqrt(sqr(rleg_x-rfoot_x)+sqr(rleg_y-rfoot_y))/32,1,arctan2(rleg_y-rfoot_y,rfoot_x-rleg_x)*180/pi,c_white,1);
         //draw_sprite_ext(feet,0,rfoot_x,rfoot_y,1,1,facing_direction,c_white,1);
         if(arm_pose==0){
         larm_x=(x-18*sin(facing_direction*pi/180))+(x-lfoot_x)*0.3;
         larm_y=(y-18*cos(facing_direction*pi/180))+(y-lfoot_y)*0.3;
         rarm_x=(x+18*sin(facing_direction*pi/180))+(x-rfoot_x)*0.3;
         rarm_y=(y+18*cos(facing_direction*pi/180))+(y-rfoot_y)*0.3;
    
         lelbow_x=(lleg_x+larm_x)/2-6*cos(facing_direction*pi/180);
         lelbow_y=(lleg_y+larm_y)/2+6*sin(facing_direction*pi/180);
         relbow_x=(rleg_x+rarm_x)/2-6*cos(facing_direction*pi/180);
         relbow_y=(rleg_y+rarm_y)/2+6*sin(facing_direction*pi/180);
         }
         if(arm_pose==1){
         //1-handed ranged weapon
         larm_x=x+12*cos((facing_direction+60)*pi/180);
         larm_y=y-12*sin((facing_direction+60)*pi/180);
         rarm_x=x+18*cos((facing_direction-20)*pi/180);
         rarm_y=y-18*sin((facing_direction-20)*pi/180);
         }
         if(arm_pose==1){
         //1-handed ranged weapon
         larm_x=x+12*cos((facing_direction+60)*pi/180);
         larm_y=y-12*sin((facing_direction+60)*pi/180);
         rarm_x=x+18*cos((facing_direction-20)*pi/180);
         rarm_y=y-18*sin((facing_direction-20)*pi/180); 
         }
         if(arm_pose==2){
         //psychic arm pose
         larm_x=x+18*cos((facing_direction+20)*pi/180);
         larm_y=y-18*sin((facing_direction+20)*pi/180);
         rarm_x=x+12*cos((facing_direction-60)*pi/180);
         rarm_y=y-12*sin((facing_direction-60)*pi/180); 
         }
         if(arm_pose==3){
         //2-handed ranged weapon
         larm_x=x+18*cos((facing_direction)*pi/180);
         larm_y=y-18*sin((facing_direction)*pi/180);
         rarm_x=x+10*cos((facing_direction)*pi/180);
         rarm_y=y-10*sin((facing_direction)*pi/180);
    
         lelbow_x=x+10*cos((facing_direction+50)*pi/180);
         lelbow_y=y-10*sin((facing_direction+50)*pi/180);
         relbow_x=x+10*cos((facing_direction-80)*pi/180);
         relbow_y=y-10*sin((facing_direction-80)*pi/180);
         }
         else{
         lelbow_x=(lleg_x+larm_x)/2-6*cos(facing_direction*pi/180);
         lelbow_y=(lleg_y+larm_y)/2+6*sin(facing_direction*pi/180);
         relbow_x=(rleg_x+rarm_x)/2-6*cos(facing_direction*pi/180);
         relbow_y=(rleg_y+rarm_y)/2+6*sin(facing_direction*pi/180);
         }


         draw_sprite_ext(lhand_sprite,0,larm_x,larm_y,1,1,facing_direction,c_white,1);
         draw_sprite_ext(rhand_sprite,0,rarm_x,rarm_y,1,1,facing_direction,c_white,1);
         //elbow code



         draw_set_color(larm_color1);
         draw_line_width(larm_x-2*cos(facing_direction*pi/180), larm_y+2*sin(facing_direction*pi/180), lelbow_x, lelbow_y, 8);
         draw_set_color(larm_color2);
         draw_circle(lelbow_x, lelbow_y, 4, false);
         draw_line_width(lleg_x, lleg_y, lelbow_x, lelbow_y, 8);


         draw_set_color(rarm_color1);
         draw_line_width(rarm_x-2*cos(facing_direction*pi/180), rarm_y+2*sin(facing_direction*pi/180), relbow_x, relbow_y, 8);
         draw_set_color(rarm_color2);
         draw_circle(relbow_x, relbow_y, 4, false);
         draw_line_width(rleg_x, rleg_y, relbow_x, relbow_y, 8);
         //if(arm_pose>0){
         if weapon_sprite != noone {
         draw_sprite_ext(weapon_sprite,0,rarm_x,rarm_y,1,1,facing_direction,c_white,1);
         }
         //draw_sprite_ext(arm_sprite,1,x,y,1,1,arm_direction,c_white,1);
         draw_sprite_ext(head_sprite,0,x,y,1,1,facing_direction,c_white,1);
         */
    }
}
