package com.narxoz.rpg.battle;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        this.random = new Random(1L);
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        validateTeam = (teamA,"teamA");
        validateTeam = (teamB,"teamB");

        List<Combatant> aliveA = new ArrayList<>(teamA);
        List<Combatant> aliveB = new ArrayList<>(teamB);

        EncounterResult result = new EncounterResult();
        result.setWinner("TBD");
        result.setRounds(0);
        result.addLog("TODO: implement battle simulation");

        int round = 0;
        while(!aliveA.isEmpty() && !aliveB.isEmpty()){
            round++;
            result.addLog("-Round" + round);

            performTurn(aliveA,aliveB,result,"Team A");
            if(aliveB.isEmpty()){
                break;
            }
            performTurn(aliveB,aliveA,result,"Team B")
        }

        result.setRounds(round);
        result.setWinner(aliveA.isEmpty() ? "Team B" : "Team A");
        result.addLog("Battle finished. Winner: " + result.getWinner());

        return result;
    }

    private void validateTeam(List<Combatant> team, String teamName) {
        if (team == null || team.isEmpty()) {
            throw new IllegalArgumentException(teamName + " must contain at least one combatant.");
        }
        for (Combatant combatant : team) {
            if (combatant == null) {
                throw new IllegalArgumentException(teamName + " contains null combatant.");
            }
        }
    }

    private void performTurn(List<Combatant> attackers,List<Combatant> defenders,EncounterResult result,String teamName){
        for (Combatant attacker : new ArrayList<>(attackers)) {
            if (!attacker.isAlive() || defenders.isEmpty()) {
                continue;
            }

            Combatant target = defenders.get(random.nextInt(defenders.size()));
            int damage = Math.max(0, attacker.getAttackPower());

            target.takeDamage(damage);
            result.addLog(teamName + ": " + attacker.getName() + " hits " + target.getName() + " for " + damage + " dmg.");

            if (!target.isAlive()) {
                defenders.remove(target);
                result.addLog("✖ " + target.getName() + " is defeated.");
            }
        }
    }
}