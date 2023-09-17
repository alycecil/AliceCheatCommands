package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.impl.campaign.fleets.RouteManager;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.github.alycecil.econ.ai.patrol.JumpGatePatrolAssignmentAI;
import com.github.alycecil.econ.impl.common.MilitaryIndustry;
import com.github.alycecil.econ.model.PopulationCommodityDemand;

public class JumpDefense extends MilitaryIndustry {

    public static final String JUMP_DEFENSE_FORCE = "Gates & Customs Navy";

    public JumpDefense() {
        super(0.01f,
                new PopulationCommodityDemand(Commodities.SHIPS, 1, JUMP_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.FUEL, 4, JUMP_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.MARINES, 5, JUMP_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.HAND_WEAPONS, 5, JUMP_DEFENSE_FORCE),
                new PopulationCommodityDemand(Commodities.CREW, 2, JUMP_DEFENSE_FORCE)
        );
    }

    @Override
    protected boolean isFullMilitaryBase() {
        return true;
    }

    @Override
    protected int getHeavy() {
        return market.getSize() > 5 ? 1 : 0;
    }

    @Override
    protected int getMedium() {
        return market.getSize() > 5 ? 1 : 2;
    }

    @Override
    protected int getLight() {
        return Math.min(market.getSize()/2, 3);
    }

    @Override
    protected String getDescription() {
        return JUMP_DEFENSE_FORCE;
    }

    @Override
    protected EveryFrameScript getScript(RouteManager.RouteData route, CampaignFleetAPI fleet) {
        return new JumpGatePatrolAssignmentAI(fleet, route);
    }
}
