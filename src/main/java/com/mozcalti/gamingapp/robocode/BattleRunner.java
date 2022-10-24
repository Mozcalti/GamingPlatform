package com.mozcalti.gamingapp.robocode;

import com.mozcalti.gamingapp.exceptions.RobotValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.robocode.battle.events.BattleEventDispatcher;
import net.sf.robocode.recording.RecordManager;
import net.sf.robocode.settings.SettingsManager;
import net.sf.robocode.version.VersionManager;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class BattleRunner {

    private Robocode robocode;
    private String xmlFileId;
    private boolean isRecorded;
    private int battleFieldWidth;
    private int battleFieldHeight;
    private String robots;
    private int numberOfRounds;

    public void prepareRobocode(String robocodePath, String recordingFormat) {
        RobocodeEngine.setLogMessagesEnabled(false);
        robocode.setEngine(new RobocodeEngine(new java.io.File(robocodePath)));
        robocode.setProperties(new SettingsManager());
        robocode.getProperties().setOptionsCommonRecordingFormat(recordingFormat);
        robocode.getProperties().setOptionsCommonEnableAutoRecording(isRecorded);
        robocode.setVersionManager(new VersionManager(robocode.getProperties()));
        robocode.setRecordManager(new RecordManager(robocode.getProperties(), robocode.getVersionManager()));
        robocode.setBattleEventDispatcher(new BattleEventDispatcher());
        robocode.getRecordManager().attachRecorder(robocode.getBattleEventDispatcher());
        robocode.setBattleField(new BattlefieldSpecification(battleFieldWidth, battleFieldHeight));
    }

    public void runBattle(String src) throws IOException {
        RobotSpecification[] selectedRobots;
        BattleSpecification battleSpec;
        try {
            selectedRobots = robocode.getEngine().getLocalRepository(robots);
            battleSpec = new BattleSpecification(numberOfRounds, robocode.getBattleField(), selectedRobots);
            robocode.getEngine().runBattle(battleSpec, true);
            robocode.getEngine().close();
            selectedRobots = robocode.getEngine().getLocalRepository("sample.Fire,sample.Fire");
            battleSpec = new BattleSpecification(numberOfRounds, robocode.getBattleField(), selectedRobots);
            robocode.getEngine().runBattle(battleSpec, true);
            robocode.getEngine().close();
        }catch(RuntimeException e) {
            /*
                Si el robot es invalido, se debe crear una mini batalla que lo libere para poder borrarlo, ya que es imposible
                borrarlo, cambiar su nombre o moverlo de directorio mientras antes de lanzar la excepcion que arroja Robocode.
                Después de esa excepción no encontré la manera de borrarlo, ya que inclusive intenté borrarlo manualmente y no me permite
                "La acción no se puede completar porque Java(TM) Platform SE binary tiene abierto el archivo.
            */
            selectedRobots = robocode.getEngine().getLocalRepository("sample.Fire,sample.Fire");
            battleSpec = new BattleSpecification(numberOfRounds, robocode.getBattleField(), selectedRobots);
            robocode.getEngine().runBattle(battleSpec, true);
            robocode.getEngine().close();
            Files.delete(Paths.get(src));
            throw new RobotValidationException("El robot no es válido. Debe de ser implementado de acuerdo al procedimiento sugerido y compilado con Java 18.");
        }
    }
}
