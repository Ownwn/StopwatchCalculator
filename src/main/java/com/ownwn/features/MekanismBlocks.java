package com.ownwn.features;

import java.util.HashMap;
import java.util.Map;

public class MekanismBlocks {

    public static final String[] STRONG_UPGRADE_MACHINES = { // machines that are strongly affected by speed upgrade
            "chemical_washer",
            "chemical_infuser",
            "electrolytic_seperator"
    };

    public static final Map<String, Integer> MEKANISM_MACHINE_TIERS = new HashMap<>(); // number of slots in a machine
    public static final Map<String, Double> MEKANISM_MILLIBUCKET_MACHINES = new HashMap<>();
    public static final Map<String, Double> MEKANISM_ITEM_MACHINES = new HashMap<>();


    public static final double[] SLOW_SPEED_UPGRADES = { // items per 10 seconds
            1,
            1.342,
            1.786,
            2.381,
            3.175,
            4.255,
            5.714,
            7.692,
            10
    };

    public static final double[] FAST_SPEED_UPGRADES = { // millibuckets per 10 seconds
            1,
            2,
            4,
            8,
            16,
            32,
            64,
            128,
            256
    };

    public static void populateMachineTiers(Map<String, Integer> map) { // number of slots in a machine
        map.put("basic", 3);
        map.put("advanced", 5);
        map.put("elite", 7);
        map.put("ultimate", 9);
    }

    public static void populateMilibucketMachines(Map<String, Double> map) {




        map.put("rotary_condensentrator", 20.0);
        map.put("chemical_washer", 20.0);
        map.put("chemical_oxidizer", 20.0);

        map.put("chemical_infuser", 40.0);
        map.put("nutritional_liquifier", 40.0);

        map.put("electrolytic_separator", 60.0);

        map.put("chemical_dissolution_chamber", 400.0);


    }

    public static void populateItemMachines(Map<String, Double> map) {
        map.put("metallurgic_infuser", 0.1);
        map.put("infusing_factory", 0.1);

        map.put("crusher", 0.1);
        map.put("crushing_factory", 0.1);

        map.put("enrichment_chamber", 0.1);
        map.put("enriching_factory", 0.1);

        map.put("energized_smelter", 0.1);
        map.put("smelting_factory", 0.1);

        map.put("purification_chamber", 0.1);
        map.put("purifying_factory", 0.1);

        map.put("chemical_injection_chamber", 0.1);
        map.put("injecting_factory", 0.1);

        map.put("combiner", 0.1);
        map.put("combining_factory", 0.1);

        map.put("osmium_compressor", 0.1);
        map.put("compressing_factory", 0.1);

        map.put("precision_sawmill", 0.1);
        map.put("sawing_factory", 0.1);


        map.put("chemical_crystallizer", 0.1);


        map.put("pressurized_reaction_chamber", 0.2);

    }
}
