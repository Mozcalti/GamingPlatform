/**
 * @Class
 * Used to represent a robot's attributes, which are dynamic
 * */

export class RobotAttributes {

    /**
     * 
     * @param {string} state The state of a robot (ACTIVE, DEAD, HIT_ROBOT, HIT_WALL)
     * @param {string} energy The current energy of a robot
     * @param {string} x The current X position of a robot
     * @param {string} y The current Y position of a robot
     * @param {string} bodyHeading The current body heading of a robot, in radians
     * @param {string} gunHeading The current gun heading of a robot, in radians
     * @param {string} radarHeading The current radar heading of a robot, in radians
     */
    constructor(state, energy, 
        x, y, bodyHeading, gunHeading, radarHeading) {
        this.state = state;
        this.energy = energy;
        this.x = x;
        this.y = y;
        this.bodyHeading = bodyHeading
        this.gunHeading = gunHeading
        this.radarHeading = radarHeading
    }

    
    getState(){
        return this.state;
    }
    
    setState(state){
        this.state = state;
    }

    getEnergy() {
        return this.energy;
    }

    getX() {
        return this.x;
    }

    getY() {
        return this.y;
    }   

    getBodyHeading() {
        return this.bodyHeading;
    }

    getGunHeading() {
        return this.gunHeading;
    }

    getRadarHeading() {
        return this.radarHeading;
    }

}