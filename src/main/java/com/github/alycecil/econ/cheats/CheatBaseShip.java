package com.github.alycecil.econ.cheats;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.util.DynamicStatsAPI;

import java.util.HashMap;
import java.util.Map;

import static com.fs.starfarer.api.impl.hullmods.AdvancedGroundSupport.GROUND_BONUS;

public class CheatBaseShip
    extends BaseLogisticsHullMod {

    public static final float NUM_BAYS = 4f;
    public static float MAINTENANCE_MULT = 0.5f;
    public static float SMOD_MODIFIER = 0.25f;

    public static float MIN_FRACTION = 2f;

    public static float REPAIR_RATE_BONUS = 50f;
    public static float CR_RECOVERY_BONUS = 50f;

    public static final float ROF_BONUS = 2f;
    public static final float DAMAGE_REDUCTION = 0.95f;
    public static final int AMMO_BONUS = 20;

    private static Map<ShipAPI.HullSize, Float> mag = new HashMap<>();

    static {
        mag.put(ShipAPI.HullSize.FRIGATE, 250f);
        mag.put(ShipAPI.HullSize.DESTROYER, 500f);
        mag.put(ShipAPI.HullSize.CRUISER, 1600f);
        mag.put(ShipAPI.HullSize.CAPITAL_SHIP, 4000f);
    }

    @Override
    public void applyEffectsBeforeShipCreation(
        ShipAPI.HullSize hullSize,
        MutableShipStatsAPI stats,
        String id
    ) {
        boolean sMod = isSMod(stats);

        super.applyEffectsBeforeShipCreation(hullSize, stats, id);

        addBays(stats, id, sMod);

        addDynamics(stats, id, sMod);

        efficiency(stats, id, sMod);

        stats
            .getMaxBurnLevel()
            .modifyFlat(id, 5);


        weaponBonus(stats, id);

        shieldDefense(stats, id);

        addCrew(hullSize, stats, id);
        addFuel(hullSize, stats, id);
        addCargo(hullSize, stats, id);

    }

    protected void efficiency(
        MutableShipStatsAPI stats,
        String id,
        boolean sMod
    ) {
        stats
            .getMaxCrewMod()
            .modifyMult(id, 2);

        stats
            .getMinCrewMod()
            .modifyMult(id, MAINTENANCE_MULT - (sMod ? SMOD_MODIFIER : 0));

        stats
            .getSuppliesPerMonth()
            .modifyMult(id, MAINTENANCE_MULT - (sMod ? SMOD_MODIFIER : 0));

        stats
            .getFuelUseMod()
            .modifyMult(id, MAINTENANCE_MULT - (sMod ? SMOD_MODIFIER : 0));

        stats
            .getBaseCRRecoveryRatePercentPerDay()
            .modifyPercent(id, CR_RECOVERY_BONUS);

        stats
            .getSuppliesToRecover()
            .modifyMult(id, 0.25f);

        stats
            .getRepairRatePercentPerDay()
            .modifyPercent(id, REPAIR_RATE_BONUS);

        stats
            .getCRPerDeploymentPercent()
            .modifyMult(id, 0.25f);
    }

    protected void weaponBonus(
        MutableShipStatsAPI stats,
        String id
    ) {
        stats
            .getAutofireAimAccuracy()
            .modifyFlat(id, 2f);

        stats
            .getMissileAmmoBonus()
            .modifyFlat(id, AMMO_BONUS);
        stats
            .getMissileRoFMult()
            .modifyMult(id, ROF_BONUS);
        stats
            .getMissileWeaponDamageMult()
            .modifyMult(id, DAMAGE_REDUCTION);
        stats
            .getMissileWeaponRangeBonus()
            .modifyFlat(id, 5000f);

        stats
            .getWeaponTurnRateBonus()
            .modifyMult(id, 2f);
        stats
            .getBallisticAmmoBonus()
            .modifyFlat(id, AMMO_BONUS);
        stats
            .getBallisticRoFMult()
            .modifyMult(id, ROF_BONUS);
        stats
            .getBallisticWeaponDamageMult()
            .modifyMult(id, DAMAGE_REDUCTION);
        stats
            .getBallisticWeaponRangeBonus()
            .modifyFlat(id, 500f);

        stats
            .getBeamWeaponTurnRateBonus()
            .modifyMult(id, 2f);
        stats
            .getEnergyAmmoBonus()
            .modifyFlat(id, AMMO_BONUS);
        stats
            .getEnergyRoFMult()
            .modifyMult(id, ROF_BONUS);
        stats
            .getEnergyWeaponDamageMult()
            .modifyMult(id, DAMAGE_REDUCTION);
        stats
            .getEnergyWeaponRangeBonus()
            .modifyFlat(id, 500f);
    }

    protected void shieldDefense(
        MutableShipStatsAPI stats,
        String id
    ) {
        stats
            .getShieldArcBonus()
            .modifyMult(id, 2f);
        stats
            .getBeamShieldDamageTakenMult()
            .modifyMult(id, 0.5f);
        stats
            .getProjectileShieldDamageTakenMult()
            .modifyMult(id, 0.5f);
    }

    protected void addDynamics(
        MutableShipStatsAPI stats,
        String id,
        boolean sMod
    ) {
        DynamicStatsAPI dynamic = stats.getDynamic();

        fighterDynamics(id, dynamic);

        opCosts(id, dynamic);

        dynamic
            .getMod(Stats.DEPLOYMENT_POINTS_MOD)
            .modifyMult(id, MAINTENANCE_MULT - (sMod ? SMOD_MODIFIER : 0));

        stats
            .getDynamic()
            .getMod(Stats.FLEET_GROUND_SUPPORT)
            .modifyFlat(id, GROUND_BONUS, "Drop Pods");

        stats
            .getDynamic()
            .getMod(Stats.FLEET_BOMBARD_COST_REDUCTION)
            .modifyMult(id, 0.5f, "Drop Pods");

        stats
            .getDynamic()
            .getMod(Stats.getSurveyCostReductionId(Commodities.HEAVY_MACHINERY))
            .modifyMult(id, 0.5f, "Savage Efficiency");

        stats
            .getDynamic()
            .getMod(Stats.getSurveyCostReductionId(Commodities.SUPPLIES))
            .modifyMult(id, 0.5f, "Savage Efficiency");


        dynamic
            .getStat(Stats.SURVEY_COST_MULT)
            .modifyMult(id, 0.5f, "Savage Efficiency");

        //salvage
        stats
            .getDynamic()
            .getMod(Stats.SALVAGE_VALUE_MULT_MOD)
            .modifyFlat(id, (Float) (2.5f));

        //game view
        stats
            .getDynamic()
            .getMod(Stats.HRS_SENSOR_RANGE_MOD)
            .modifyFlat(id, 2000f, "Over-sized Scanning Array");
    }

    private void opCosts(
        String id,
        DynamicStatsAPI dynamic
    ) {
        //PD cost reduction
        dynamic.getMod(Stats.SMALL_PD_MOD).modifyFlat(id, -3);
        dynamic.getMod(Stats.SMALL_BALLISTIC_MOD).modifyFlat(id, -3);
        dynamic.getMod(Stats.MEDIUM_BALLISTIC_MOD).modifyFlat(id, -5);
        dynamic.getMod(Stats.LARGE_BALLISTIC_MOD).modifyFlat(id, -10);

        dynamic.getMod(Stats.SMALL_ENERGY_MOD).modifyFlat(id, -3);
        dynamic.getMod(Stats.MEDIUM_ENERGY_MOD).modifyFlat(id, -5);
        dynamic.getMod(Stats.LARGE_ENERGY_MOD).modifyFlat(id, -10);

        dynamic.getMod(Stats.SMALL_MISSILE_MOD).modifyFlat(id, -3);
        dynamic.getMod(Stats.MEDIUM_MISSILE_MOD).modifyFlat(id, -5);
        dynamic.getMod(Stats.LARGE_MISSILE_MOD).modifyFlat(id, -10);
    }

    private void fighterDynamics(
        String id,
        DynamicStatsAPI dynamic
    ) {
        dynamic
            .getMod(Stats.BOMBER_COST_MOD)
            .modifyMult(id, 0.1f);

        dynamic
            .getMod(Stats.FIGHTER_COST_MOD)
            .modifyMult(id, 0.1f);

        dynamic
            .getMod(Stats.INTERCEPTOR_COST_MOD)
            .modifyMult(id, 0.1f);

        dynamic
            .getStat(Stats.FIGHTER_CREW_LOSS_MULT)
            .modifyMult(id, 0.1f);
    }

    protected void addBays(
        MutableShipStatsAPI stats,
        String id,
        boolean sMod
    ) {
        float numBays = NUM_BAYS;
        numBays += stats
            .getDynamic()
            .getMod(Stats.CONVERTED_HANGAR_MOD)
            .computeEffective(0f);

        stats
            .getNumFighterBays()
            .modifyFlat(id, numBays);

        stats
            .getFighterRefitTimeMult()
            .modifyMult(id, MAINTENANCE_MULT - (sMod ? SMOD_MODIFIER : 0));
    }

    public void addCargo(
        ShipAPI.HullSize hullSize,
        MutableShipStatsAPI stats,
        String id
    ) {
        float mod = (Float) mag.get(hullSize);
        if (stats.getVariant() != null) {
            mod = Math.max(stats
                .getVariant()
                .getHullSpec()
                .getCargo() * MIN_FRACTION, mod);
        }
        stats
            .getCargoMod()
            .modifyFlat(id, mod);
    }

    public void addFuel(
        ShipAPI.HullSize hullSize,
        MutableShipStatsAPI stats,
        String id
    ) {
        float mod = mag.get(hullSize);
        if (stats.getVariant() != null) {
            mod = Math.max(stats
                .getVariant()
                .getHullSpec()
                .getFuel() * MIN_FRACTION, mod);
        }
        stats
            .getFuelMod()
            .modifyFlat(id, mod);
    }

    public void addCrew(
        ShipAPI.HullSize hullSize,
        MutableShipStatsAPI stats,
        String id
    ) {
        float mod = mag.get(hullSize);
        if (stats.getVariant() != null) {
            mod = Math.max(stats
                .getVariant()
                .getHullSpec()
                .getMaxCrew() * MIN_FRACTION, mod);
        }
        stats
            .getMaxCrewMod()
            .modifyFlat(id, mod);
    }

    public String getSModDescriptionParam(
        int index,
        ShipAPI.HullSize hullSize,
        ShipAPI ship
    ) {
        if (index == 0) { return "" + (int) Math.round(SMOD_MODIFIER * 100f) + "%"; }
        if (index == 1) { return "" + ((Float) mag.get(ShipAPI.HullSize.FRIGATE)).intValue(); }
        if (index == 2) { return "" + ((Float) mag.get(ShipAPI.HullSize.DESTROYER)).intValue(); }
        if (index == 3) { return "" + ((Float) mag.get(ShipAPI.HullSize.CRUISER)).intValue(); }
        if (index == 4) { return "" + ((Float) mag.get(ShipAPI.HullSize.CAPITAL_SHIP)).intValue(); }
        if (index == 5) { return "" + (int) Math.round(MIN_FRACTION * 100f) + "%"; }
        return null;
    }

    public String getDescriptionParam(
        int index,
        ShipAPI.HullSize hullSize,
        ShipAPI ship
    ) {
        if (index == 0) { return "" + NUM_BAYS; }
        if (index == 1) { return "" + (int) Math.round((1f - MAINTENANCE_MULT) * 100f) + "%"; }
        if (index == 2) { return "" + (int) Math.round(CR_RECOVERY_BONUS) + "%"; }
        return null;
    }

    @Override
    public boolean affectsOPCosts() {
        return true;
    }
}
