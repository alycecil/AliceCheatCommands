package com.github.alycecil.econ.cheats;

import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

public class StablePointCheat
    extends BaseSystemCommand {

    @Override
    public CommandResult doOnSystem(
        String args,
        SectorEntityToken fleet,
        StarSystemAPI sys,
        Vector2f playerLocation
    ) {
        SectorEntityToken stable = sys.addCustomEntity(null, null, "stable_location", "neutral");

        float orbitRadius = Misc.getDistance(fleet, sys.getCenter());
        float orbitDays = orbitRadius / (20f + new Random().nextFloat() * 5f);
        float angle = Misc.getAngleInDegrees(sys
            .getCenter()
            .getLocation(), fleet.getLocation());
        stable.setCircularOrbit(sys.getCenter(), angle, orbitRadius, orbitDays);
        return CommandResult.SUCCESS;
    }
}
