package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.github.alycecil.econ.model.aicore.AICoreEffect;
import com.github.alycecil.econ.model.aicore.StandardAICoreBonusEffect;

public class PopulationCommodityBonus extends QuantityCommodityBonus {
    private int reduction;

    public PopulationCommodityBonus(String commodityId, int reduction, String desc) {
        this(commodityId, reduction, desc, StandardAICoreBonusEffect.INSTANCE);
    }

    public PopulationCommodityBonus(String commodityId, int reduction, String desc, AICoreEffect aiCoreEffect) {
        super(commodityId, desc, aiCoreEffect);
        this.reduction = reduction;
    }

    @Override
    int getQuantity(Industry industry) {
        return Math.max(1, industry.getMarket().getSize() - reduction);
    }

    @Override
    protected AICoreEffect getDefaultAICoreEffect() {
        return StandardAICoreBonusEffect.INSTANCE;
    }
}
