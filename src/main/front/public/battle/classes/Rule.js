/**
 * @Class
 * Used to represent the rules of 1 battle 
 */
export class Rule{
    
    /**
     * 
     * @param {string} battlefieldWidth 
     * @param {string} battlefieldHeight 
     * @param {string} numRounds 
     * @param {string} gunCoolingRate 
     * @param {string} inactivityTime 
     * @param {string} ver 
     * @param {string} robotCount 
     */
    constructor(battlefieldWidth, battlefieldHeight, numRounds, gunCoolingRate, inactivityTime, ver, robotCount){
        this.battlefieldWidth = battlefieldWidth;
        this.battlefieldHeight = battlefieldHeight;
        this.numRounds = numRounds;
        this.gunCoolingRate = gunCoolingRate;
        this.inactivityTime = inactivityTime;
        this.ver = ver;
        this.robotCount = robotCount;
    }
    
    getBattlefieldWidth(){
        return this.battlefieldWidth;
    }

    getBattlefieldHeight(){
        return this.battlefieldHeight;
    }

}