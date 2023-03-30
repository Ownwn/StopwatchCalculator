package com.ownwn.stopwatchcalculator.block;

import java.util.HashMap;
import java.util.Map;

public class MekanismBlocks {
    public static final String[] tenSecondTimes = {
            "infusing_factory",
            "metallurgic_infuser",
            "crusher",
            "crushing_factory",
            "enrichment_chamber",
            "enriching_factory"
    };

    public static final Map<String, Integer> machineTiers = new HashMap<>();

    public static final double[] speedUpgrades = {
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

    public static void populateMap(Map<String, Integer> map) {
        map.put("basic", 3);
        map.put("advanced", 5);
        map.put("elite", 7);
        map.put("ultimate", 9);
    }
}
