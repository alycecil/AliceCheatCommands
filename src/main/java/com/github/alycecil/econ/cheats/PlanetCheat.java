package com.github.alycecil.econ.cheats;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.PlanetSpecAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.Console;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;
import java.util.UUID;

import static com.fs.starfarer.api.impl.campaign.procgen.PlanetConditionGenerator.generateConditionsForPlanet;

public class PlanetCheat
    extends BaseSystemCommand {

    @Override
    public CommandResult runCommand(
        String args,
        CommandContext context
    ) {
        args = args.trim();
        if (args.isEmpty()) {
            return doList();
        } else {
            return super.runCommand(args, context);
        }
    }

    @Override
    public CommandResult doOnSystem(
        String args,
        SectorEntityToken fleet,
        StarSystemAPI sys,
        Vector2f playerLocation
    ) {

        String planetId = "cc_" + UUID
            .randomUUID()
            .toString()
            .replaceAll("-", "x");

        String planetName = null;
        String planetClass = null;

        if (args.equalsIgnoreCase("default")) {

            Console.showMessage("Creating a default water world");

            planetName = "Water World";
            planetClass = "water";

        } else {
            String[] splits = args.split(" ");
            if (splits.length == 1) {
                planetClass = args;
            } else if (splits.length > 2) {
                planetClass = splits[0];
                planetName = args.substring(planetClass.length());
            } else {
                Console.showMessage("Unable to parse args [" + args + "]");
                return CommandResult.BAD_SYNTAX;
            }
        }

        PlanetSpecAPI found = getPlanetSpec(planetClass);

        if (found == null) {
            Console.showMessage("Unable to find spec run `PlanetCheat` without args to see the list of planet types");
            return CommandResult.ERROR;
        }

        if(planetName == null || planetName.isEmpty()){
            planetName = found.getName()+" World";
        }

        return createPlanet(sys, playerLocation, planetId, planetName, planetClass, found);
    }

    private BaseCommand.CommandResult createPlanet(
        StarSystemAPI sys,
        Vector2f playerLocation,
        String planetId,
        String planetName,
        String planetClass,
        PlanetSpecAPI found
    ) {
        PlanetAPI star = sys.getStar();

        PlanetAPI planet = sys.addPlanet(planetId, //planet id
            star, //which star do we orbit?
            planetName, //Planet Name
            planetClass, //planet class
            VectorUtils.getAngle(star.getLocation(), playerLocation), 200,
            MathUtils.getDistance(star.getLocation(), playerLocation), 120 //TODO Realism
        );

        StarAge starAge = StarAge.AVERAGE;//if i feel like make this a setting or random
        
        generateConditionsForPlanet(null, planet, starAge);

        return CommandResult.SUCCESS;
    }

    private CommandResult doList() {
        List<PlanetSpecAPI> allPlanetSpecs = Global
            .getSettings()
            .getAllPlanetSpecs();

        if (allPlanetSpecs == null) {
            Console.showMessage("No planet classes?!");
            return CommandResult.ERROR;
        }

        Console.showMessage("List of planet classes:");

        for (PlanetSpecAPI spec : allPlanetSpecs) {
            String type = spec.isStar() ? " (star)"//
                : spec.isBlackHole() ? " (blackhole)"//
                    : spec.isPulsar() ? " (pulsar)"//
                        : spec.isNebulaCenter() ? " (nebula center)"//
                            : "";
            Console.showMessage("- " + spec.getPlanetType() + " [" + spec.getName() + "]" + type);
        }

        return CommandResult.SUCCESS;
    }

    private PlanetSpecAPI getPlanetSpec(String planetClass) {
        List<PlanetSpecAPI> allPlanetSpecs = Global
            .getSettings()
            .getAllPlanetSpecs();

        if (allPlanetSpecs == null) { return null; }

        PlanetSpecAPI found = null;

        for (PlanetSpecAPI spec : allPlanetSpecs) {
            String type = spec.getPlanetType();
            if (planetClass.equalsIgnoreCase(type)) {
                found = spec;
            }
        }
        return found;
    }
}
