package com.mozcalti.gamingapp.robocode;

import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

import net.sf.robocode.battle.events.BattleEventDispatcher;
import net.sf.robocode.recording.RecordManager;
import net.sf.robocode.settings.ISettingsManager;
import net.sf.robocode.version.IVersionManager;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

@Getter
@Setter
@NoArgsConstructor
public class Robocode {

    private RobocodeEngine engine;
    private ISettingsManager properties;
    private IVersionManager versionManager;
    private RecordManager recordManager;
    private BattleEventDispatcher battleEventDispatcher;
    private BattlefieldSpecification battleField;
    private RobotSpecification[] selectedRobots;
    private BattleSpecification battleSpec;

}