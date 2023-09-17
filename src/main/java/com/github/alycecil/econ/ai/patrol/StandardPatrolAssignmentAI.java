package com.github.alycecil.econ.ai.patrol;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetActionTextProvider;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;

import static com.github.alycecil.econ.impl.IndustrialDefenseForce.INDUSTRIAL_DEFENSE_FORCE;

public class StandardPatrolAssignmentAI extends BasePatrolAssignmentAI implements FleetActionTextProvider {
    public StandardPatrolAssignmentAI(CampaignFleetAPI fleet, RouteManager.RouteData route) {
        super(fleet, route);
    }

    @Override
    public String getActionText(CampaignFleetAPI fleet) {
        return INDUSTRIAL_DEFENSE_FORCE + " " + super.getActionText(fleet);
    }

}
