package com.github.alycecil.econ.model.aicore;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;

public class StandardAICoreDemandEffect
        implements AICoreEffect {
    public static StandardAICoreDemandEffect INSTANCE = new StandardAICoreDemandEffect();

    @Override
    public int getEffect(int quantityActual, String aiCoreId) {
        boolean omega = aiCoreId.equals(Commodities.OMEGA_CORE);
        if (omega && quantityActual >= 1) {
            quantityActual -= 1;
        }

        if (quantityActual >= 1) {
            quantityActual -= 1;
        }
        return quantityActual;
    }
}
