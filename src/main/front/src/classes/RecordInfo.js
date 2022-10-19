/**
 * @Class
 * Used to represent an XML Robocode battle file
 */
export class RecordInfo{

    /**
     * 
     * @param {Rule} rule The rule specification of a battle 
     */
    constructor(rule){
        this.rule = rule;
        this.numTurns = []; //Total number of turns of battle
        this.results = []; //The results of a battle
    }

    getRule(){
        return this.rule;
    }

    getNumTurns(){
        return this.numTurns;
    }

    setNumTurns(numTurns){
        this.numTurns = numTurns;
    }

    getResults(){
        return this.results;
    }
 
    setResults(results){
        this.results = results;
    }
}