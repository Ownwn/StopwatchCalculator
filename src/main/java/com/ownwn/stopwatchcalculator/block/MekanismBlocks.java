package com.ownwn.stopwatchcalculator.block;

import java.util.HashMap;
import java.util.Map;

public class MekanismBlocks {
    public static final String[] TEN_SECOND_MACHINES = { // machines that take 10 seconds to produce one item
            "infusing_factory",
            "metallurgic_infuser",
            "crusher",
            "crushing_factory",
            "enrichment_chamber",
            "enriching_factory"
    };

    public static final String[] STRONG_UPGRADE_MACHINES = { // machines that are strongly affected by speed upgrade
            "chemical_washer",
            "chemical_infuser",
            "electrolytic_seperator"
    };

    public static final Map<String, Integer> MACHINE_TIERS = new HashMap<>(); // number of slots in a machine
    public static final Map<String, Double> MILLIBUCKET_MACHINES = new HashMap<>();


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
        map.put("pressurized_reaction_chamber", 0.2);



        map.put("rotary_condensentrator", 20.0);
        map.put("chemical_washer", 20.0);

        map.put("chemical_infuser", 40.0);

        map.put("electrolytic_separator", 60.0);


    }
}
