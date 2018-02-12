package com.digotsoft.uatc.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MaSte
 * @created 04-Feb-18
 */
@AllArgsConstructor
@Getter
public class WaitingTransmission {
    private String voice;
    private String line;
    private Runnable runnable;
    private boolean waitForResponse;
    private Flight flight;
}
