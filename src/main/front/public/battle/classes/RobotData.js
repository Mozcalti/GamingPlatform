/**
 * @Class
 * Used to represent a robot's data, which is static (except current body part images)
 */
export class RobotData {
    /**
     * 
     * @param {string} id The unique id of a robot 
     * @param {string} vsName The name of a robot
     * @param {string} teamName The name of the robot's team
     * @param {string} bodyColor The color of a robot's body 
     * @param {string} gunColor The color of a robot's gun
     * @param {string} radarColor The color of a robot's radar
     * @param {string} maxEnergy The robot's maximum energy points
     */

    constructor(id, vsName, teamName, bodyColor,
        gunColor, radarColor, maxEnergy) {
        this.id = id;
        this.vsName = vsName;
        this.teamName = teamName;
        this.bodyColor = bodyColor;
        this.gunColor = gunColor;
        this.radarColor = radarColor;
        this.explosionFrame = 0; // Current explosion frame of a destroyed robot, must start at 0 to properly display animation
        this.maxEnergy = maxEnergy;
        this.robotImages = [];
    }

    getVsName() {
        return this.vsName;
    }


    getExplosionFrame(){
        return this.explosionFrame;
    } 

    setExplosionFrame(explosionFrame){
        this.explosionFrame = explosionFrame;
    }

    getMaxEnergy(){
        return this.maxEnergy;
    }

    getRobotImages(){
        return this.robotImages;
    }
    setRobotImages(robotImages){
        this.robotImages = robotImages;
    }
}