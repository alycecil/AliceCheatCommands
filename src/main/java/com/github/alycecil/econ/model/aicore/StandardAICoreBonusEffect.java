package com.github.alycecil.econ.model.aicore;

import com.fs.starfarer.api.impl.campaign.ids.Commodities;

public class StandardAICoreBonusEffect implements AICoreEffect {
    public static StandardAICoreBonusEffect INSTANCE = new StandardAICoreBonusEffect();

    @Override
    public int getEffect(int quantityActual, String aiCoreId) {

        if(aiCoreId.equals(Commodities.ALPHA_CORE)){
            quantityActual+=1;
        }else if(aiCoreId.equals(Commodities.OMEGA_CORE)){
            quantityActual+=2;
        }
        return quantityActual;
    }
}
