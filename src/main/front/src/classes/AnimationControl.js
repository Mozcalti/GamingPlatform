/**
 * @Class
 * Used to represent a round's turn
 */
export class AnimationControl{

    /**
     *
     * @param {Number[]} sumTurnsArr Sum Array of Turns
     * @param {Number} totalTurns All battle turns
     * @param {Number} fpsInterval Animation Frame Rate Interval, must be achieved in order to draw next frame
     * @param {Number} timeThen Time since last animation frame was drawn
     * @param {Number} timeNow Current time
     * @param {Number} timeElapsed Elapsed time since last loop
     */
    constructor(sumTurnsArr, totalTurns, fpsInterval, timeThen, timeNow, timeElapsed){
        this.currentRound = 0;
        this.currentTurn = 0;
        this.sumTurnsArr = sumTurnsArr;
        this.totalTurns = totalTurns;
        this.fpsInterval = fpsInterval;
        this.timeThen = timeThen;
        this.timeNow = timeNow;
        this.timeElapsed = timeElapsed;
    }

    getCurrentRound(){
        return this.currentRound;
    }

    incrementCurrentRound(){
        this.currentRound++;
    }

    getCurrentTurn(){
        return this.currentTurn;
    }

    incrementCurrentTurn(){
        this.currentTurn++;
    }
    getSumTurnsArr(){
        return this.sumTurnsArr;
    }

    getTotalTurns(){
        return this.totalTurns;
    }

    getFpsInterval(){
        return this.fpsInterval;
    }

    getTimeThen(){
        return this.timeThen;
    }

    setTimeThen(timeThen){
        this.timeThen = timeThen;
    }

    getTimeNow(){
        return this.timeNow;
    }

    setTimeNow(timeNow){
        this.timeNow = timeNow;
    }

    getTimeElapsed(){
        return this.timeElapsed;
    }

    setTimeElapsed(timeElapsed){
        this.timeElapsed = timeElapsed;
    }
}