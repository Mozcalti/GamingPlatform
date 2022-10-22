package com.mozcalti.gamingapp.robocode;



import com.mozcalti.gamingapp.exceptions.RobotValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.robocode.battle.events.BattleEventDispatcher;
import net.sf.robocode.recording.RecordManager;
import net.sf.robocode.settings.SettingsManager;
import net.sf.robocode.version.VersionManager;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

@Getter
@Setter
@AllArgsConstructor
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
        robocode.getEngine().addBattleListener(robocode.getRecordManager().getRecorder().getBattleObserver());
        robocode.getRecordManager().getRecorder().setFileId(xmlFileId);
        robocode.setBattleField(new BattlefieldSpecification(battleFieldWidth, battleFieldHeight));
    }


    public void runBattle() {
        try {
            RobotSpecification[] selectedRobots = robocode.getEngine().getLocalRepository(robots);
            BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, robocode.getBattleField(), selectedRobots);
            robocode.getEngine().runBattle(battleSpec, true);
            robocode.getEngine().close();
            //robocode.getEngine().finalize();
        }catch(Exception e) {
            throw new RobotValidationException("El robot no es válido. Debe de ser implementado de acuerdo al procedimiento sugerido y compilado con Java 18.");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
