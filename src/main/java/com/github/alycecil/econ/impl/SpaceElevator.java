package com.github.alycecil.econ.impl;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.population.PopulationComposition;
import com.github.alycecil.econ.impl.common.SupportInfrastructure;
import com.github.alycecil.econ.util.Incoming;

public class SpaceElevator extends SupportInfrastructure implements MarketImmigrationModifier {

    public static final int DEMAND = 4;

    public SpaceElevator() {
        super(0.5f);
    }

    @Override
    protected String getCommodity() {
        return Commodities.HEAVY_MACHINERY;
    }

    @Override
    public int getDemand() {
        return DEMAND + market.getSize();
    }

    @Override
    public String getDescription() {
        return "Space Elevator";
    }

    @Override
    public void modifyIncoming(MarketAPI market, PopulationComposition incoming) {
        Incoming.modifyIncoming(market, incoming, getModId(), 10 * getEffectiveness(), getDescription());
    }
}
