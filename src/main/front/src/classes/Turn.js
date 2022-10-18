/**
 * @Class
 * Used to represent a round's turn
 */
export class Turn{
    /**
     * 
     * @param {string} round 
     * @param {string} turn 
     */
    constructor(round, turn){
        this.round = round;
        this.turn = turn;
        this.robots = [];
        this.bullets = []
    }

    getRobots(){
        return this.robots;
    }

    getBullets(){
        return this.bullets;
    }

}