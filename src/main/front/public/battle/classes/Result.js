/**
 * @Class
 * Used to represent a robot's battle result
 */
export class Result{

    /**
     * 
     * @param {string} rank The robot's final battle rank
     * @param {string} robotName The name of a robot
     * @param {string} totalScore The total score of a robot
     */
    constructor(rank,robotName, totalScore){
            this.rank = rank;
            this.robotName = robotName;
            this.totalScore = totalScore;
            this.survival = 0;
            this.lastSurvivorBonus = 0;
            this.bulletDamage = 0;
            this.bulletDamageBonus = 0;
            this.ramDamage = 0;
            this.ramDamageBonus = 0;
            this.firsts = 0;
            this.seconds = 0;
            this.thirds = 0;
    }

    setRank(rank){
        this.rank = rank;
    }

    setSurvival(survival){
        this.survival = survival;
    }

    setBulletDamage(bulletDamage){
        this.bulletDamage = bulletDamage;
    }

    setBulletDamageBonus(bulletDamageBonus){
        this.bulletDamageBonus = bulletDamageBonus;
    }

    setRamDamage(ramDamage){
        this.ramDamage = ramDamage;
    }

    setRamDamageBonus(ramDamageBonus){
        this.ramDamageBonus = ramDamageBonus;
    }

    setFirsts(firsts){
        this.firsts = firsts;
    }

    setSeconds(seconds){
        this.seconds = seconds;
    }

    setThirds(thirds){
        this.thirds = thirds;
    }
}