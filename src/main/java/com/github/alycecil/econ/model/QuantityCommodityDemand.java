package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.combat.MutableStat;
import com.github.alycecil.econ.model.aicore.AICoreEffect;

public abstract class QuantityCommodityDemand extends QuantityCommodityChange implements IndustryDemand {

    public QuantityCommodityDemand(String commodityId, String desc, AICoreEffect aiCoreEffect) {
        super(commodityId, desc, aiCoreEffect);
    }

    @Override
    public MutableCommodityQuantity getModifier(Industry industry) {
        return industry.getDemand(commodityId);
    }

    @Override
    public void doChange(String modId, float mult, String desc, MutableStat quantityStat, int quantityActual) {
        quantityStat.modifyFlat(modId, quantityActual, desc);
    }
}
