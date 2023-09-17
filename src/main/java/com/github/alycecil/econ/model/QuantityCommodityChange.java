package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.combat.MutableStat;
import com.github.alycecil.econ.model.aicore.AICoreEffect;

public abstract class QuantityCommodityChange implements IndustryEffect, HasCommodity {
    protected String commodityId;
    protected String desc;
    private AICoreEffect aiCoreEffect;

    public QuantityCommodityChange(String commodityId, String desc, AICoreEffect aiCoreEffect) {
        this.commodityId = commodityId;
        this.desc = desc;
        this.aiCoreEffect = aiCoreEffect;
    }

    @Override
    public void apply(Industry industry, String modId, float mult) {
        if (industry == null) return;
        String desc = this.desc +" for "+ industry.getNameForModifier();
        MutableCommodityQuantity modifier = getModifier(industry);
        if (modifier == null) return;
        MutableStat quantityStat = modifier.getQuantity();
        if (quantityStat == null) return;
        int quantityActual = getQuantity(industry);
        quantityActual = applyAICoreEffect(industry, quantityActual);
        doChange(modId, mult, desc, quantityStat, quantityActual);
    }

    protected int applyAICoreEffect(Industry industry, int quantityActual) {
        if(getAICoreEffect() == null){
            setAiCoreEffect(getDefaultAICoreEffect());
        }
        if(industry.getAICoreId() != null) {
            quantityActual = aiCoreEffect.getEffect(quantityActual, industry.getAICoreId());
        }
        return quantityActual;
    }

    protected abstract AICoreEffect getDefaultAICoreEffect();

    protected abstract void doChange(String modId, float mult, String desc, MutableStat quantityStat, int quantityActual);

    public abstract MutableCommodityQuantity getModifier(Industry industry);

    @Override
    public void unapply(Industry industry, String modId) {
        getModifier(industry).getQuantity().unmodifyFlat(modId);
    }

    abstract int getQuantity(Industry industry);

    public String getCommodityId() {
        return commodityId;
    }

    public AICoreEffect getAICoreEffect(){
        return aiCoreEffect;
    }

    public void setAiCoreEffect(AICoreEffect aiCoreEffect) {
        this.aiCoreEffect = aiCoreEffect;
    }
}
