package com.digotsoft.uatc.sim;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MaSte
 * @created 23-Feb-18
 */
@AllArgsConstructor
@Getter
public class Taxiway {
    private String name;
    private double fx;
    private double fy;
    private double sx;
    private double sy;
}
