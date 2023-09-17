package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.github.alycecil.econ.model.aicore.AICoreEffect;
import com.github.alycecil.econ.model.aicore.StandardAICoreBonusEffect;

public class ConditionalFlatCommodityBonus extends FlatCommodityBonus {
    private final String conditionalId;

    public ConditionalFlatCommodityBonus(String commodityId, String conditionalId, int quantity, String desc) {
        this(commodityId, conditionalId, quantity, desc, StandardAICoreBonusEffect.INSTANCE);
    }

    public ConditionalFlatCommodityBonus(String commodityId, String conditionalId, int quantity, String desc, AICoreEffect aiCoreEffect) {
        super(commodityId, quantity, desc, aiCoreEffect);
        this.conditionalId = conditionalId;
    }

    @Override
    int getQuantity(Industry industry) {
        if (industry.getMarket().hasCondition(conditionalId)) {
            return super.getQuantity(industry);
        }
        return 0;
    }
}
