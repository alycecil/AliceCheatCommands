package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.combat.MutableStat;
import com.github.alycecil.econ.model.aicore.AICoreEffect;
import com.github.alycecil.econ.model.aicore.StandardAICoreBonusEffect;
import com.github.alycecil.econ.model.aicore.StandardAICoreDemandEffect;

public abstract class QuantityCommodityBonus extends QuantityCommodityChange implements IndustryBonus {

    public QuantityCommodityBonus(String commodityId, String desc, AICoreEffect aiCoreEffect) {
        super(commodityId, desc, aiCoreEffect);
    }

    @Override
    public MutableCommodityQuantity getModifier(Industry industry) {
        return industry.getSupply(commodityId);
    }

    @Override
    public void doChange(String modId, float mult, String desc, MutableStat quantityStat, int quantityActual) {
        quantityStat.modifyFlat(modId, Math.round(quantityActual*mult), desc);
    }
}
