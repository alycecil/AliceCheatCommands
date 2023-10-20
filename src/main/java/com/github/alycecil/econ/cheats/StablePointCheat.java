package com.github.alycecil.econ.cheats;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.util.Misc;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lwjgl.util.vector.Vector2f;

import java.util.Random;

public class StablePointCheat
    implements BaseCommand {
    @Override
    public CommandResult runCommand(
        String args,
        CommandContext context
    ) {
        // This command should only be usable in combat
        if (!context.isInCampaign()) {
            // Show a default error message
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            // Return the 'wrong context' result, this will alert the player by playing a special sound
            return CommandResult.WRONG_CONTEXT;
        }

        return doCommand(args);

    }

    protected CommandResult doCommand(String args) {
        SectorEntityToken fleet = Global
            .getSector()
            .getPlayerFleet();

        StarSystemAPI sys = (StarSystemAPI) fleet.getContainingLocation();

        if (sys == null) {
            Console.showMessage("Not in a system");
            return CommandResult.ERROR;
        }

        for (PlanetAPI planet : sys.getPlanets()) {

            Vector2f loc = planet.getLocation();


            float dis = Misc.getDistance(loc, fleet.getLocation());

            if (dis < 10) {
                Console.showMessage("Distance to a planet is too small.");
                return CommandResult.ERROR;
            }

        }

        SectorEntityToken stable = sys.addCustomEntity(null, null, "stable_location", "neutral");

        float orbitRadius = com.fs.starfarer.api.util.Misc.getDistance(fleet, sys.getCenter());
        float orbitDays = orbitRadius / (20f + new Random().nextFloat() * 5f);
        float angle = com.fs.starfarer.api.util.Misc.getAngleInDegrees(sys
            .getCenter()
            .getLocation(), fleet.getLocation());
        stable.setCircularOrbit(sys.getCenter(), angle, orbitRadius, orbitDays);
        return CommandResult.SUCCESS;
    }
}
