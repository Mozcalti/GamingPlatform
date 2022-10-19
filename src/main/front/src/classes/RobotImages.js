/**
 * @Class
 * Used to represent a robot's possible images
 */
export class RobotImages{

    /**
     * @param {HTMLImageElement} currentBodyImage The image currently representing a robot's body in canvas
     * @param {HTMLImageElement} bodyImage The original image representing a robot's body in canvas
     * @param {HTMLImageElement} currentGunImage The image currently representing a robot's gun in canvas
     * @param {HTMLImageElement} gunImage The original image representing a robot's gun in canvas
     * @param {HTMLImageElement} currentRadarImage The image currently representing a robot's radar in canvas
     * @param {HTMLImageElement} radarImage The original image representing a robot's radar in canvas
     * @param {HTMLImageElement} debrisImage The image used to represent a destroyed robot
     */
    constructor(currentBodyImage, bodyImage,
                currentGunImage, gunImage, currentRadarImage, radarImage, debrisImage) {
        this.currentBodyImage = currentBodyImage;
        this.currentGunImage = currentGunImage;
        this.currentRadarImage = currentRadarImage;
        this.bodyImage = bodyImage;
        this.gunImage = gunImage;
        this.radarImage = radarImage;
        this.debrisImage = debrisImage;
    }


    getBodyImage(){
        return this.bodyImage;
    }

    getGunImage(){
        return this.gunImage;
    }

    getRadarImage(){
        return this.radarImage;
    }

    getCurrentBodyImage(){
        return this.currentBodyImage;
    }

    setCurrentBodyImage(currentBodyImage){
        this.currentBodyImage = currentBodyImage;
    }

    getCurrentGunImage(){
        return this.currentGunImage;
    }

    setCurrentGunImage(currentGunImage){
        this.currentGunImage = currentGunImage;
    }

    getCurrentRadarImage(){
        return this.currentRadarImage;
    }

    setCurrentRadarImage(currentRadarImage){
        this.currentRadarImage = currentRadarImage;
    }

    getDebrisImage(){
        return this.debrisImage;
    }

}