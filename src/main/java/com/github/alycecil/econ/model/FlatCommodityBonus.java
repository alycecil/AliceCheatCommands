package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.github.alycecil.econ.model.aicore.AICoreEffect;
import com.github.alycecil.econ.model.aicore.StandardAICoreBonusEffect;

public class FlatCommodityBonus extends QuantityCommodityBonus {
    private int quantity;

    public FlatCommodityBonus(String commodityId, int quantity, String desc) {
        this(commodityId, quantity, desc, StandardAICoreBonusEffect.INSTANCE);
    }

    public FlatCommodityBonus(String commodityId, int quantity, String desc, AICoreEffect aiCoreEffect) {
        super(commodityId, desc, aiCoreEffect);
        this.quantity = quantity;
    }

    @Override
    int getQuantity(Industry industry) {
        return quantity;
    }

    @Override
    protected AICoreEffect getDefaultAICoreEffect() {
        return StandardAICoreBonusEffect.INSTANCE;
    }
}
