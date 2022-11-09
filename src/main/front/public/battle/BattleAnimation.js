import { Turn } from './classes/Turn.js';
import { Bullet } from './classes/Bullet.js';
import { Record } from './classes/Record.js';
import { RecordInfo } from './classes/RecordInfo.js';
import { Rule } from './classes/Rule.js';
import { Result } from './classes/Result.js';
import { RobotData } from './classes/RobotData.js';
import { RobotImages } from './classes/RobotImages.js';
import { RobotAttributes } from './classes/RobotAttributes.js';
import { AnimationControl } from './classes/AnimationControl.js';

const hitVictimSound = new Audio();
hitVictimSound.src = "sounds/bullet.wav";
hitVictimSound.autoplay = true;

const hitWallSound = new Audio();
hitWallSound.src = "sounds/wall.wav";
hitWallSound.autoplay = true;

const explodedSound = new Audio();
explodedSound.src = "sounds/explode.wav";
explodedSound.autoplay = true;

let battleXml;
let battleParticipantes;

function obtieneJson() {
    fetch("./json/" + getURLParameters("token") + ".json")
        .then(function (resp) {
            return resp.json();
        })
        .then(function (data) {
            battleXml = data.battleXml;
            battleParticipantes = data.battleParticipantes;
            init(data.battleFecha);
        });
}

function getURLParameters(paramName)
{
    var sURL = window.document.URL.toString();

    if (sURL.indexOf("?") > 0)
    {
        var arrParams = sURL.split("?");
        var arrURLParams = arrParams[1].split("&");
        var arrParamNames = new Array(arrURLParams.length);
        var arrParamValues = new Array(arrURLParams.length);

        var i = 0;
        for (i = 0; i<arrURLParams.length; i++)
        {
            var sParam =  arrURLParams[i].split("=");
            arrParamNames[i] = sParam[0];
            if (sParam[1] != "")
                arrParamValues[i] = unescape(sParam[1]);
            else
                arrParamValues[i] = "No Value";
        }

        for (i=0; i<arrURLParams.length; i++)
        {
            if (arrParamNames[i] == paramName)
            {
                return arrParamValues[i];
            }
        }
        return "No Parameters Found";
    }
}

/*
    Prevents user accidentally leaving or refreshing tab
    Will show pop up asking for confirmation
*/
window.addEventListener('beforeunload', function (e) {
    e.preventDefault();
    e.returnValue = '';
});

/**
 * Shows or hides results' table whether it is displayed or not
 *
 * @param {Result[]} resultsArray Array of Result, containing a result for each robot that battled
 * @return
 */
function showResults(resultsArray){
    if (document.getElementById("resultsTable") === null) {
        let tableContainer = createCustomElement("TABLE", "", "resultsTable", "");
        document.getElementById("tableContainer").appendChild(tableContainer);
    }
    if (document.getElementById("tableContainer").style.display.localeCompare("flex") !== 0) {

        document.getElementById("dataContainer").style.opacity = "0.6";
        document.getElementById("animationContainer").style.opacity = "0.6";
        document.getElementById("tableContainer").style.display = "flex";
        if (document.getElementById("resultsTable").childElementCount === 0) {
            createResults(resultsArray);
        }
    }else{
        document.getElementById("dataContainer").style.opacity = "1";
        document.getElementById("animationContainer").style.opacity = "1";
        document.getElementById("tableContainer").style.display = "none";
    }
}


/**
 * Creates a table, headers and cells based on the battle results
 *
 * @param {Result[]} resultsArray Array of Result, containing a result for each robot that battled
 * @return
 */

function createResults(resultsArray){

    //string consisting of Result's properties, for headers
    let propertyNames = Object.getOwnPropertyNames(resultsArray[0]);
    //put blank spaces between uppercase char occurrences in property names
    let headerNames = propertyNames.map(function(item){
        return item.replace(/([a-z])([A-Z])/g, '$1 $2').trim();
    });

    let refResultsTable = document.getElementById("resultsTable");
    let refTbody = createCustomElement("TBODY", "", "", "");
    let refTrH = createCustomElement("TR", "", "", "resultsRowHeader");


    //create headers
    for (let headerData of headerNames) {
        let newHeaderData = headerData.charAt(0).toUpperCase() + headerData.slice(1);
        if(newHeaderData.localeCompare("Firsts") === 0){
            newHeaderData = "1sts";
        }else{
            if(headerData.localeCompare("Seconds") === 0){
                newHeaderData = "2nds";
            }else{
                if(newHeaderData.localeCompare("Thirds") === 0){
                    newHeaderData = "3rds";
                }
            }
        }
        let refTh = createCustomElement("TH", newHeaderData, "", "resultTableHeader");
        refTrH.appendChild(refTh);
    }
    refTbody.appendChild(refTrH);

    //create cells
    for (let result of resultsArray){
        let refTr = createCustomElement("TR", "", "", "");
        for(const prop in result){
            let data = result[prop];
            let refCell = createCustomElement("TD", data, "", "resultTableCell");
            refTr.appendChild(refCell);
        }
        refTbody.appendChild(refTr);
    }
    refResultsTable.appendChild(refTbody);
}

/**
 * Creates a custom element with specific tag, value, id and className
 *
 * @param {string} tag Indicates what kind of HTML5 element will be created
 * @param {string} value Element's new value (if any)
 * @param {string} id Element's unique id (if any)
 * @param {string} className Element's class name (if any)
 * @return {HTMLElement} Customized HTML5 element
 */

function createCustomElement(tag, value, id, className){
    let element = document.createElement(tag);
    element.innerHTML = value;
    element.id = id;
    element.className = className;
    return element;
}

/**
 * Based on specific battle date, it determines whether to show a battle's date information,
 * a loading screen, or initiate the battle animation
 */

function init(battleFecha){
    let battleDate = new Date(battleFecha);
    let battleDateDisplay = new Date(battleFecha);
    let interval = 400;
    let minutesOffset = 3;
    let timer = window.setInterval(function () {
        let now = new Date().getTime();
        let distance = battleDate - now;
        // Time calculations for days, hours, minutes and seconds
        let days = Math.floor(distance / (1000 * 60 * 60 * 24));
        let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        let seconds = Math.floor((distance % (1000 * 60)) / 1000);
        //we reached battle date

        if(now == battleDate) {
            window.location.reload();
        }

        if(now >= battleDate){
            //x minutes after battle date = replay, else it's a live battle
            if(now >= battleDate.setMinutes(battleDate.getMinutes() + minutesOffset)){
                document.getElementById("streamMsg").innerHTML = "REPETICIÓN";
                document.getElementById("resultsButton").style.display = "flex";
                document.getElementById("repeatInfoMsg").innerHTML = "Esta batalla tuvo lugar el día  " +
                    battleDate.toLocaleDateString() + " a las " + battleDateDisplay.toLocaleTimeString() + " horas."
                document.getElementById("participantsInfo").innerHTML = "Los participantes fueron: " + battleParticipantes.toString().replaceAll(",", ", ") + ".";
            }else{
                document.getElementById("streamMsg").innerHTML = "EN VIVO";
            }
            window.clearInterval(timer);
            document.getElementById("stateMsg").innerHTML = "CARGANDO...";
            initBattle();
        }else{
            //x minutes before battle date
            if (battleDate < new Date().setMinutes(new Date().getMinutes() + minutesOffset)){
                document.getElementById("stateMsg").innerHTML = "CARGANDO...";
            }
            else {
                //battle date info
                document.getElementById("stateMsg").innerHTML = "La batalla entre los participantes:<br/>"+
                    battleParticipantes.toString().replaceAll(",", "<br/>") +
                    "<br/><br/>Esta programada para el día:<br/>"+
                    battleDate.getDate() + " / " + (battleDate.getMonth()+1) + " / " + battleDate.getUTCFullYear() + " a las "+battleDate.getHours()
                    +":"+battleDate.getMinutes()+" horas<br /><br />" +
                    "La batalla iniciará en:<br/>"  +
                    days + "d " + hours + "h " + minutes + "m " + seconds + "s ";
            }
        }
    }, interval);
}

/**
 * Assuming there's a need to implement a way to display more than 1 battle,
 * it should be handled here
 */
function initBattle() {
    getResponse(showResults);
}


/**
 * Issues HTTP request in order to retrieve the xml file which contains the battle information
 * needed to display its animation
 *
 * @param {function} callback showResults, since it must be called when battle ends
 * @return
 */
function getResponse(callback) {

    const battleFiles = [battleXml];

    let battleNumber = 0;
    let xhr = new XMLHttpRequest();
    let url = battleFiles[battleNumber];
    document.getElementById("stateScreen").style.display = "flex";
    xhr.open("GET", url, true);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let xmlDoc = xhr.responseXML;
            document.getElementById("repeatInfoDiv").style.display = "flex";
            document.getElementById("gameContainer").style.display = "flex";
            document.getElementById("stateScreen").style.display = "none";
            prepareAnimation(xmlDoc, callback);
        }
    };
    xhr.send();
}

/**
 * Creates arrays based on the retrieved XML document's data
 * The XML structure is the following:
 * <record>
 *      <recordInfo>
 *          <rules> battle specifications
 *          <rounds> total rounds
 *              <turns> total turns in 1 round
 *          <results> results of all robots
 *              <result> individual result of a robot
 *      <turns>
 *          <turn>
 *              <robots> data of all robots
 *                  <robot>
 *                      <debugProperties>
 *                      <score>
 *              <bullets> data of all bullets in this specific turn
 * @param {Document} xmlDoc An XML Document form
 * @returns {Record} A Record object with all the XML document data
 */
function getRecordArray(xmlDoc) {
    let i, j, k;

    let recordInfoDom = xmlDoc.getElementsByTagName('record')[0].children[0];

    let rule = new Rule(
        recordInfoDom.children[0].getAttribute('battlefieldWidth'),
        recordInfoDom.children[0].getAttribute('battlefieldHeight'),
        recordInfoDom.children[0].getAttribute('numRounds'),
        recordInfoDom.children[0].getAttribute('gunCoolingRate'),
        recordInfoDom.children[0].getAttribute('inactivityTime'),
        recordInfoDom.children[0].getAttribute('ver'),
        recordInfoDom.getAttribute('robotCount'),

    )

    let numTurnsArray = [];
    for (i = 0; i < recordInfoDom.children[1].children.length; i++) {
        numTurnsArray.push(
            parseInt(recordInfoDom.children[1].children[i].getAttribute('value')));
    }

    let resultsArray = [];
    for (i = 0; i < recordInfoDom.children[2].children.length; i++) {
        let result = new Result(recordInfoDom.children[2].children[i].getAttribute('rank'),
            recordInfoDom.children[2].children[i].getAttribute('teamLeaderName'),
            recordInfoDom.children[2].children[i].getAttribute('score'),
        );
        result.setSurvival(recordInfoDom.children[2].children[i].getAttribute('survival'));
        result.setBulletDamage(recordInfoDom.children[2].children[i].getAttribute('bulletDamage'));
        result.setBulletDamageBonus(recordInfoDom.children[2].children[i].getAttribute('bulletDamageBonus'));
        result.setRamDamage(recordInfoDom.children[2].children[i].getAttribute('ramDamage'));
        result.setRamDamageBonus(recordInfoDom.children[2].children[i].getAttribute('ramDamageBonus'));
        result.setFirsts(recordInfoDom.children[2].children[i].getAttribute('firsts'));
        result.setSeconds(recordInfoDom.children[2].children[i].getAttribute('seconds'));
        result.setThirds(recordInfoDom.children[2].children[i].getAttribute('thirds'));
        resultsArray.push(result);
    }

    //battle results are not in order
    resultsArray.sort((a,b) => b.totalScore - a.totalScore); // b - a for reverse sort
    let resultRank = "1";
    resultsArray.forEach(element => {
        element.setRank(resultRank);
        resultRank++;
    });


    let turnsDom = xmlDoc.getElementsByTagName('record')[0].children[1];
    let recordInfo = new RecordInfo(rule);
    recordInfo.setResults(resultsArray);
    recordInfo.setNumTurns(numTurnsArray);
    let turnDom;
    let turnsArray = [];
    let robotsArray = [];
    let bulletsArray = [];

    let robotDataArray = [];
    let debrisImage = new Image();
    debrisImage.src = "ground/explode_debris.png";

    //iterate through all the turns
    for (i = 0; i < turnsDom.children.length; i++) {
        //a turn
        turnDom = turnsDom.children[i];
        //iterate through a turn
        for (j = 0; j < turnDom.children[0].children.length; j++) {
            //we need to create the robots' static data just once
            if (i === 0) {
                /*
                    colors might not be set within the first turn, this happens in teams,
                    so we obtain the last turn in which the robots will surely have all their colors set
                    colors shouldn't be changing through the battle
                */
                let turnColor = turnsDom.children[turnsDom.children.length-1];
                let robotImages = new RobotImages(
                    selectPartImage(turnColor.children[0].children[j].getAttribute('bodyColor'), "body"),
                    selectPartImage(turnColor.children[0].children[j].getAttribute('bodyColor'), "body"),
                    selectPartImage(turnColor.children[0].children[j].getAttribute('gunColor'), "gun"),
                    selectPartImage(turnColor.children[0].children[j].getAttribute('gunColor'), "gun"),
                    selectPartImage(turnColor.children[0].children[j].getAttribute('radarColor'), "radar"),
                    selectPartImage(turnColor.children[0].children[j].getAttribute('radarColor'), "radar"),
                    debrisImage);
                let robotData = new RobotData(
                    turnDom.children[0].children[j].getAttribute('id'),
                    turnDom.children[0].children[j].getAttribute('vsName'),
                    turnDom.children[0].children[j].getAttribute('teamName'),
                    turnColor.children[0].children[j].getAttribute('bodyColor'),
                    turnColor.children[0].children[j].getAttribute('gunColor'),
                    turnColor.children[0].children[j].getAttribute('radarColor'),
                    turnDom.children[0].children[j].getAttribute('energy'),
                );
                robotData.setRobotImages(robotImages);
                robotDataArray.push(robotData);
            }
            //dynamic robot attributes
            robotsArray.push(new RobotAttributes(
                turnDom.children[0].children[j].getAttribute('state'),
                turnDom.children[0].children[j].getAttribute('energy'),
                turnDom.children[0].children[j].getAttribute('x'),
                turnDom.children[0].children[j].getAttribute('y'),
                turnDom.children[0].children[j].getAttribute('bodyHeading'),
                turnDom.children[0].children[j].getAttribute('gunHeading'),
                turnDom.children[0].children[j].getAttribute('radarHeading')
            ));
        }
        //iterate through the bullets of current turn
        for (k = 0; k < turnDom.children[1].children.length; k++) {
            bulletsArray.push(new Bullet(
                turnDom.children[1].children[k].getAttribute('id'),
                turnDom.children[1].children[k].getAttribute('state'),
                turnDom.children[1].children[k].getAttribute('x'),
                turnDom.children[1].children[k].getAttribute('y'),
                turnDom.children[1].children[k].getAttribute('frame')
            ));
        }

        //create turn with current turn data
        let newTurn = new Turn(turnsDom.children[i].getAttribute('round'), turnsDom.children[i].getAttribute('turn'));
        newTurn.getRobots().push(robotsArray);
        newTurn.getBullets().push(bulletsArray);
        //reset arrays for next turn
        robotsArray = [];
        bulletsArray = [];
        turnsArray.push(newTurn);
    }
    let record = new Record(recordInfo);
    //set record properties
    record.setTurns(turnsArray);
    record.setRobotData(robotDataArray);

    return record;
}

/**
 * Initiates variables, data arrays, and prepares the canvas to display the animation
 *
 * @param {Document} xmlDoc Document containing he XML retrieved by the request
 * @param {function} callback showResults, since it must be called when battle ends
 * @return
 */
function prepareAnimation(xmlDoc, callback) {

    //XML related
    let record = getRecordArray(xmlDoc);
    createPlayerList(record.getRobotData());
    document.getElementById("resultsButton").addEventListener('click', function(){
        showResults(record.getRecordInfo().getResults())
    });

    document.getElementById("closeIcon").addEventListener("click", function(){
        showResults(record.getRecordInfo().getResults());
    });

    let totalTurns = record.getRecordInfo().getNumTurns().reduce((a, b) => a + b, 0)
    let fps = 24, fpsInterval = 1000 / fps, then = Date.now(), now = 0, elapsed = 0;
    let sumTurnsArr = record.getRecordInfo().getNumTurns().map((sum => value => sum += value)(0));
    let control = new AnimationControl(sumTurnsArr, totalTurns, fpsInterval, then, now, elapsed);
    //Canvas related
    let canvas = document.getElementById('canvas1');
    canvas.width = parseInt(record.getRecordInfo().getRule().getBattlefieldWidth());
    canvas.height = parseInt(record.getRecordInfo().getRule().getBattlefieldHeight());
    let ctx = canvas.getContext('2d');
    ctx.font = 'normal 11px Arial';
    ctx.fillStyle = 'black';
    ctx.textAlign = 'center';


    //images related
    let bulletImage = new Image();
    bulletImage.src = "bullets/bullet.png";
    let goneImage = new Image();
    goneImage.src = "";
    let explosionFramesArr = createFramesArr("explosion/explosion2_", 68);
    let hitTargetFramesArr = createFramesArr("explosion/explosion1_", 16);
    let attackImagesArray = [];
    attackImagesArray.push(bulletImage);
    attackImagesArray.push(goneImage);
    attackImagesArray.push(explosionFramesArr);
    attackImagesArray.push(hitTargetFramesArr);
    window.requestAnimationFrame(function () {
        animate(record, control, ctx, attackImagesArray, callback);

    });

}

/**
 * Paints the canvas based on data obtained from the record data arrays
 * Uses Window.requestAnimationFrame() to handle animations
 *
 * @param {Record} record Contains a record about the battle
 * @param {AnimationControl} control Used to control the animation lifetime
 * @param {CanvasRenderingContext2D} ctx Canvas Context
 * @param {[]} attackImagesArray Array of images and frames, used for bullets impact animation
 * @param {Function} callback Function to show results after battle ends
 *
 * @return
 */

function animate(record, control, ctx, attackImagesArray, callback){

    //finish current round
    if (control.getCurrentTurn() === control.getSumTurnsArr()[control.getCurrentRound()]) {
        control.incrementCurrentRound();
        resetRobotImage(record.getRobotData());
    }

    //finish battle
    if (control.getCurrentTurn() === control.getTotalTurns()) {
        callback(record.getRecordInfo().getResults());
    } else {

        //calculates time elapsed since last time, determines if it draws a frame or not
        control.setTimeNow(Date.now());
        control.setTimeElapsed(control.getTimeNow() - control.getTimeThen());

        if (control.getTimeElapsed() > control.getFpsInterval()) {

            control.setTimeThen(control.getTimeNow() - (control.getTimeElapsed() % control.getFpsInterval()));
            ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);
            drawAllRobots(record, control, ctx, attackImagesArray);
            drawBullets(record, control, ctx, attackImagesArray);
            control.incrementCurrentTurn();
        }
        //request next frame to draw
        window.requestAnimationFrame(function () {
            animate(record, control,
                ctx, attackImagesArray, callback);
        })
    }
}

function drawAllRobots(record, control, ctx, attackImagesArray){
    //iterate through current turn to draw every element
    for (let j = 0; j < record.getTurns()[control.getCurrentTurn()].getRobots()[0].length; j++) {

        let robot = record.getTurns()[control.getCurrentTurn()].getRobots()[0][j];
        ctx.save();
        document.getElementById("roundNumber").innerHTML = `Round ${control.getCurrentRound() + 1}`;
        updateHealthBar(record.getRobotData()[j].getVsName(), robot.getEnergy());
        //drawing a robot, depending on its state or explosion frame if it's dead
        if (robot.getState().localeCompare("DEAD") === 0) {
            drawRobotText(ctx, "", "", robot);
            if(record.getRobotData()[j].getExplosionFrame() === 0) {
                explodedSound.cloneNode(true).play();
            }
            if (record.getRobotData()[j].getExplosionFrame() <= 67) {
                record.getRobotData()[j].getRobotImages().setCurrentBodyImage(attackImagesArray[2][record.getRobotData()[j].getExplosionFrame()]);
                record.getRobotData()[j].getRobotImages().setCurrentGunImage(attackImagesArray[1]);
                record.getRobotData()[j].getRobotImages().setCurrentRadarImage(attackImagesArray[1]);
                drawExplosion(robot, ctx, record.getRobotData()[j].getRobotImages().getCurrentBodyImage(), 128, 128);
                record.getRobotData()[j].setExplosionFrame(record.getRobotData()[j].getExplosionFrame() + 1);
            }else{
                record.getRobotData()[j].getRobotImages().setCurrentBodyImage(record.getRobotData()[j].getRobotImages().getDebrisImage());
                record.getRobotData()[j].getRobotImages().setCurrentGunImage(attackImagesArray[1]);
                record.getRobotData()[j].getRobotImages().setCurrentRadarImage(attackImagesArray[1]);
                drawRobotText(ctx, "", "", robot);
                drawRobot(ctx, record.getRobotData()[j], robot);
            }
        }else {
            if (robot.getState().localeCompare("ACTIVE") !== 0) {
                hitWallSound.cloneNode(true).play();
            }
            drawRobotText(ctx, parseFloat(robot.getEnergy()).toFixed(2), record.getRobotData()[j].getVsName(), robot);
            drawRobot(ctx, record.getRobotData()[j], robot);
        }
        ctx.restore();
    }
}

function drawBullets(record, control, ctx, attackImagesArray){
    //draw bullets
    for (let bullet of record.getTurns()[control.getCurrentTurn()].getBullets()[0]) {
        if (bullet.getState().localeCompare("MOVING") === 0) {
            drawBullet(bullet, ctx, attackImagesArray[0], 5, 5);
        }else{
            if (bullet.getFrame() == null) {
                hitVictimSound.cloneNode(true).play();
            } else {
                if (parseInt(bullet.getFrame()) < 16) {
                    drawBullet(bullet, ctx, attackImagesArray[3][bullet.getFrame()], 24, 24);
                }
            }
        }
        ctx.restore();
    }
}

/**
 * Returns Image array of n frames used for animation
 *
 * @param {string} framePath The path, including png resource, where the frames are located at
 * @param {int} totalFrames Total number of frames used for this animation
 * @return {Image[]} Array of Images, with different .src
 */
function createFramesArr(framePath, totalFrames) {
    let animationFramesArr = [];
    for (let i = 0; i < totalFrames; i++) {
        let frame = new Image();
        frame.src = framePath + (i + 1).toString() + ".png";
        animationFramesArr.push(frame);
    }
    return animationFramesArr;
}

/**
 * Creates the player list displayed on the left of the canvas
 *
 * @param {RobotData[]} robotDataArray
 * @return
 */
function createPlayerList(robotDataArray){
    let dataContainer = document.getElementById("dataContainer");
    for (let robotData of robotDataArray) {
        let playerBox = createPlayerBox(robotData.getVsName(), robotData.getMaxEnergy());
        dataContainer.append(playerBox);
    }
}


/**
 * Creates a player box, which indicates a player's name and health bar within the player list
 *
 * @param {string} name Player's robot's name
 * @param {string} maxEnergy Player's robot's maximum energy points
 * @return {HTMLElement} DIV containing the name and health bar
 */
function createPlayerBox(name, maxEnergy){
    let playerBox = document.createElement("DIV");
    let playerBar = document.createElement("PROGRESS");
    let playerName = document.createElement("DIV");
    playerBox.className = "playerBox";
    playerName.innerHTML = name;
    playerBar.max = maxEnergy;
    playerBar.value = 0;
    playerBar.id = name+"_bar";
    playerBar.className = "healthBar";
    playerBox.append(playerName);
    playerBox.append(playerBar);
    return playerBox;
}

/**
 * Resets current part image of all robots to their original image
 * @param {RobotData[]} robotDataArray Array containing RobotData objects, robot static data (except their images)
 * @return
 */
function resetRobotImage(robotDataArray) {
    for (let robotData of robotDataArray) {
        robotData.getRobotImages().setCurrentBodyImage(robotData.getRobotImages().getBodyImage());
        robotData.getRobotImages().setCurrentGunImage(robotData.getRobotImages().getGunImage());
        robotData.getRobotImages().setCurrentRadarImage(robotData.getRobotImages().getRadarImage());
        robotData.setExplosionFrame(0);
    }
}


/**
 * Updates a robot's health bar
 * @param {string} name Robot's name, displayed on screen
 * @param {string} energy Robot's current energy, displayed on screen
 * @return
 */
function updateHealthBar(name, energy){
    document.getElementById(name+"_bar").value = energy;
}


/**
 * Draws a robot
 * @param {RobotAttributes} robot Robot's dynamic attributes
 * @param {CanvasRenderingContext2D} ctx Canvas context
 * @param {HTMLImageElement} body Robot's body to be drawn
 * @param {HTMLImageElement} gun Robot's gun to be drawn
 * @param {HTMLImageElement} radar Robot's radar to be drawn
 * @return
 */
function drawRobotParts(robot, ctx, body, gun, radar) {
    ctx.translate(parseFloat(robot.getX()), (ctx.canvas.height) - parseFloat(robot.getY()));
    drawParts(parseFloat(robot.getBodyHeading()), parseFloat(robot.getBodyHeading()), ctx, body, 36, 38);
    drawParts(parseFloat(robot.getBodyHeading()), parseFloat(robot.getGunHeading()), ctx, gun, 20, 54);
    drawParts(parseFloat(robot.getBodyHeading()), parseFloat(robot.getRadarHeading()), ctx, radar, 22, 16);
}

function drawRobot(ctx, robotData, robot){
    drawRobotParts(robot, ctx, robotData.getRobotImages().getCurrentBodyImage(),
        robotData.getRobotImages().getCurrentGunImage(), robotData.getRobotImages().getCurrentRadarImage());

}

function drawRobotText(ctx, upperText, lowerText, robot){
    ctx.fillText(upperText, parseInt(robot.getX()), (ctx.canvas.height + 40) - parseInt(robot.getY()));
    ctx.fillText(lowerText, parseInt(robot.getX()), (ctx.canvas.height - 30) - parseInt(robot.getY()));
}



/**
 * Draws a robot's part in canvas
 * @param {Number} bodyHeading Robot's body's heading in radians
 * @param {Number} otherHeading Robot's part's heading in radians
 * @param {CanvasRenderingContext2D} ctx Canvas context
 * @param {HTMLImageElement} part Robot's part to be drawn
 * @param {Number} width Robot's part's width
 * @param {Number} height Robot's part's height
 * @return
 */
function drawParts(bodyHeading, otherHeading, ctx, part, width, height) {
    if (part.src.includes("body")) {
        ctx.rotate(bodyHeading);
    } else {
        if (bodyHeading !== otherHeading) {
            ctx.rotate(otherHeading - bodyHeading);
        }
    }

    //var imageObj1 = new Image();
    //part.src = part.src.includes("body")
    /*part.onload = function() {
        ctx.drawImage(part, (width * -1) / 2, (height * -1) / 2, width, height);
    }*/

    ctx.drawImage(part, (width * -1) / 2, (height * -1) / 2, width, height);
}


/**
 * Draws a bullet in canvas
 * @param {Bullet} bullet Bullet's dynamic attributes
 * @param {HTMLImageElement} bulletImage Bullet to be drawn
 * @param {CanvasRenderingContext2D} ctx Canvas context
 * @param {Number} width Bullet's width
 * @param {Number} height Bullet's height
 * @return
 */
function drawBullet(bullet, ctx, bulletImage, width, height) {
    ctx.drawImage(bulletImage, parseFloat(bullet.getX()), (ctx.canvas.height) - parseFloat(bullet.getY()), width, height);
}


/**
 * Draws a robot's explosion in canvas
 * @param {RobotAttributes} robot Robot's dynamic attributes
 * @param {Object} ctx Canvas context
 * @param {Image} explosion Explosion image to be drawn
 * @param {Number} width Explosion width
 * @param {Number} height Explosion height
 * @return
 */
function drawExplosion(robot, ctx, explosion, width, height) {
    ctx.translate(parseFloat(robot.getX()), (ctx.canvas.height) - parseFloat(robot.getY()));
    ctx.drawImage(explosion, (width * -1) / 2, (height * -1) / 2, width, height);
}

/**
 * Selects a robot's part image according to its hex color obtained from XML file
 * @param {string} color Hex color of a robot's part
 * @param {string} part A robot's part
 * @return {HTMLImageElement}
 */
function selectPartImage(color, part) {
    let image = new Image();

    switch (color) {
        case "FFFF0000":
            image.src = "tanks/" + part + "Red.png";
            break;

        case "FF00C800":
            image.src = "tanks/" + part + "Green.png";
            break;


        case "FFC0C0C0":
            image.src = "tanks/" + part + "Gray.png";
            break;


        case "FF000000":
            image.src = "tanks/" + part + "Black.png";
            break;

        //debería ser pink
        case "FFFFAFAF":
            image.src = "tanks/" + part + "Orange.png";
            break;


        case "FF808032":
            image.src = "tanks/" + part + "Brown.png";
            break;


        case "FFFFFF00":
            image.src = "tanks/" + part + "Yellow.png";
            break;


        case "FF0000FF":
            image.src = "tanks/" + part + "Blue.png";
            break;

        default:
            image.src = "tanks/" + part + "Blue.png";
            break;
    }

    return image;
}

window.addEventListener('load', () => obtieneJson());
