/**
 * @Class
 * Used to represent a battle and its information
 */
export class Record{
    /**
     * 
     * @param {RecordInfo} recordInfo Specific info regarding the battle 
     */
    constructor(recordInfo){
        this.recordInfo = recordInfo;
        this.turns = []; //Array containing all the battle turns
        this.robotData = []; //RobotData array containing all the static robot data
    }

    getRecordInfo(){
        return this.recordInfo;
    }

    getTurns(){
        return this.turns;
    }

    setTurns(turns){
        this.turns = turns;
    }

    getRobotData(){
        return this.robotData;
    }

    setRobotData(robotData){
        this.robotData = robotData;
    }
}