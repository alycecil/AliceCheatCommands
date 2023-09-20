package com.github.alycecil.econ.cheats;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import indevo.dialogue.research.ResearchProject;
import indevo.dialogue.research.ResearchProjectTemplateRepo;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

import java.util.Map;

public class ResearchCheat
        implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        // This command should only be usable in combat
        if (!context.isInCampaign())
        {
            // Show a default error message
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            // Return the 'wrong context' result, this will alert the player by playing a special sound
            return CommandResult.WRONG_CONTEXT;
        }

        if (args.isEmpty())
        {
            doList();
            return CommandResult.SUCCESS;
        }

        return doCommand(args);

    }

    protected CommandResult doCommand(String args) {
        int count;
        String name;

        try {

            final String[] tmp = args.split(" ", 2);
            if (tmp.length == 1) {
                count = 1;
                name = args;
            } else {
                name = tmp[0];
                count = Integer.parseInt(tmp[1]);
            }
        }catch (Exception e){
            Console.showMessage("unable to parse args "+e);
            return CommandResult.BAD_SYNTAX;
        }
        try{
            for (Map.Entry<String, ResearchProject> stringResearchProjectEntry : ResearchProjectTemplateRepo.RESEARCH_PROJECTS.entrySet()) {
                if(stringResearchProjectEntry.getKey() != null && stringResearchProjectEntry.getKey().equals(name) || name.equalsIgnoreCase("all")){
                    doGive(count, stringResearchProjectEntry);
                }
            }
            return CommandResult.SUCCESS;
        }catch (Exception e){
            Console.showMessage("failed "+e);
            return CommandResult.ERROR;
        }
    }

    private void doGive(int count, Map.Entry<String, ResearchProject> stringResearchProjectEntry) {
        if(count < 1){
            count = 1;
            Console.showMessage("Set count to 1");
        }
        if(count > 100){
            count = 100;
            Console.showMessage("Limiting count to 100");
        }

        CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
        for (int i = 0; i < count; i++) {
            CargoAPI rewards = stringResearchProjectEntry.getValue().getRewards();

            cargo.addAll(rewards.createCopy());
        }
    }

    private void doList() {
        StringBuilder s = new StringBuilder(1000);
        s.append("Research Projects:");
        for (Map.Entry<String, ResearchProject> stringResearchProjectEntry : ResearchProjectTemplateRepo.RESEARCH_PROJECTS.entrySet()) {
            s.append("\n   ").append(stringResearchProjectEntry.getKey());
        }

        Console.showMessage(s.toString());

    }
}
