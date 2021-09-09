package com.github.alycecil.econ.model;

import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;

public abstract class QuantityCommodityBonus extends QuantityCommodityChange {


    public QuantityCommodityBonus(String commodityId, String desc) {
        super(commodityId, desc);
    }

    @Override
    public MutableCommodityQuantity getModifier(Industry industry) {
        return industry.getSupply(commodityId);
    }
}
