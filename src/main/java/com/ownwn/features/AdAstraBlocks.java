package com.ownwn.features;

import java.util.HashMap;
import java.util.Map;

public class AdAstraBlocks {

    public static final Map<String, Double> AD_ASTRA_MACHINES = new HashMap<>();


    public static void populateAstraMachines() { // list of machines and their respective speeds
        new AdAstraMachine("oxygen_loader", 400.0);
    }





    public static class AdAstraMachine {
        public AdAstraMachine(String machineName, double machineSpeed) {
            AdAstraBlocks.AD_ASTRA_MACHINES.put(machineName, machineSpeed);
        }
    }
}
