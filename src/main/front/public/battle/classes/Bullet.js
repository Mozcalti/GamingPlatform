/**
 * @Class
 * Used to represent a bullet fired by a robot
 */
export class Bullet{
    
    /**
     * 
     * @param {string} id The unique id of for owner robot 
     * @param {string} state The state of the bullet (EXPLODED,FIRED, HIT_BULLET, HIT_VICTIM, HIT_WALL, INACTIVE, MOVING)
     * @param {string} x The current X position of the bullet
     * @param {string} y The current Y position of the bullet
     * @param {string} frame The current frame of the bullet
     */
    
    constructor(id, state, x, y, frame){
        this.id = id;
        this.state = state;
        this.x = x;
        this.y = y;
        this.frame = frame;
    }

    getState(){
        return this.state;
    }

    setState(state){
        this.state = state;
    }

    getX(){
        return this.x;
    }
    

    getY(){
        return this.y;
    }

    getFrame(){
        return this.frame;
    }
}