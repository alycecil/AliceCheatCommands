package com.github.alycecil.econ.impl.common;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.CommoditySpecAPI;
import com.fs.starfarer.api.campaign.econ.MarketImmigrationModifier;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.github.alycecil.econ.model.IndustryDemand;
import com.github.alycecil.econ.model.IndustryEffect;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AliceBaseIndustry extends BaseIndustry {
    List<IndustryEffect> bonuses;

    public AliceBaseIndustry(IndustryEffect... bonuses) {
        this.bonuses = Arrays.asList(bonuses);
    }

    @Override
    public void apply() {
        if (isFunctional() && bonuses != null) {
            float effectiveness = 1f;
            if (this instanceof HasEffectiveness) {
                effectiveness = ((HasEffectiveness) this).getEffectiveness();
            }

            applyForIndustry(effectiveness);


            for (IndustryEffect bonus : bonuses) {
                bonus.apply(this, getModId(), effectiveness);
            }
        }
        if (this instanceof MarketImmigrationModifier) {
            market.addTransientImmigrationModifier((MarketImmigrationModifier) this);
        }
    }

    @Override
    public void unapply() {
        super.unapply();

        for (IndustryEffect bonus : bonuses) {
            bonus.unapply(this, getModId());
        }

        unapplyForIndustry();


    }

    public List<String> getDemanded() {
        List<String> list = new LinkedList<>();
        for (IndustryEffect bonus : bonuses) {
            if (bonus instanceof IndustryDemand) {
                list.add(
                        ((IndustryDemand) bonus).getCommodityId()
                );
            }
        }
        return list;
    }

    public float getEffectiveness() {
        List<String> list = getDemanded();
        float effectiveness = 1f;
        if (list == null || list.isEmpty()) return effectiveness;

        String[] type = new String[list.size()];
        Pair<String, Integer> deficit = getMaxDeficit(list.toArray(type));

        if (deficit != null && deficit.two != null && deficit.two >= 1) {
            int size = market.getSize();
            if (deficit.two >= size) {
                effectiveness = 10;
            } else {
                effectiveness = (float) (size - deficit.two) / size;
            }
        }
        return effectiveness;
    }

    @Override
    protected void addPostSupplySection(TooltipMakerAPI tooltip, boolean hasSupply, IndustryTooltipMode mode) {
        super.addPostSupplySection(tooltip, hasSupply, mode);

        if (this instanceof HasEffectiveness) {
            String pct = "" + (int) (getEffectiveness() * 100f) + "%";
            tooltip.addPara("Currently operating at %s effective load.", 10f, Misc.getHighlightColor(), pct);
        }
    }

    protected abstract void applyForIndustry(float effectiveness);

    protected abstract void unapplyForIndustry();

    @Override
    protected void applyAICoreModifiers() {
        if (aiCoreId == null) {
            applyNoAICoreModifiers();
            return;
        }
        if (aiCoreId.equals(Commodities.ALPHA_CORE)) {
            applyAlphaCoreModifiers();
        } else if (aiCoreId.equals(Commodities.BETA_CORE)) {
            applyBetaCoreModifiers();
        } else if (aiCoreId.equals(Commodities.GAMMA_CORE)) {
            applyGammaCoreModifiers();
        } else if (aiCoreId.equals(Commodities.OMEGA_CORE)) {
            applyOmegaCoreModifiers();
        }
    }

    protected void applyOmegaCoreModifiers() {
        super.applyAlphaCoreModifiers();
    }

    @Override
    public void addAICoreSection(TooltipMakerAPI tooltip, String coreId, AICoreDescriptionMode mode) {
        boolean omega = aiCoreId!=null && aiCoreId.equals(Commodities.OMEGA_CORE);
        if(omega){
            addOmegaCoreDescription(coreId, tooltip, mode);
        }else{
            super.addAICoreSection(tooltip, coreId, mode);
        }
    }

    protected void addOmegaCoreDescription(String coreId, TooltipMakerAPI tooltip, AICoreDescriptionMode mode) {
        float opad = 10f;
        Color highlight = Misc.getHighlightColor();

        String pre = "Omega-level AI core currently assigned. ";
        if (mode == AICoreDescriptionMode.MANAGE_CORE_DIALOG_LIST || mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP) {
            pre = "Omega-level AI core. ";
        }
        if (mode == AICoreDescriptionMode.INDUSTRY_TOOLTIP || mode == AICoreDescriptionMode.MANAGE_CORE_TOOLTIP) {
            CommoditySpecAPI coreSpec = Global
                    .getSettings().getCommoditySpec(aiCoreId);
            TooltipMakerAPI text = tooltip.beginImageWithText(coreSpec.getIconName(), 48);
            text.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by upto 2 unit. " +
                            "Increases production by 2 unit.", 0f, highlight,
                    "" + (int)((1f - UPKEEP_MULT) * 100f) + "%");
            tooltip.addImageWithText(opad);
            return;
        }

        tooltip.addPara(pre + "Reduces upkeep cost by %s. Reduces demand by upto 2 unit. " +
                        "Increases production by 2 unit.", opad, highlight,
                "" + (int)((1f - UPKEEP_MULT) * 100f) + "%");
    }
}
