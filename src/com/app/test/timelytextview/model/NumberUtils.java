package com.app.test.timelytextview.model;

import com.app.test.timelytextview.model.number.Eight;
import com.app.test.timelytextview.model.number.Five;
import com.app.test.timelytextview.model.number.Four;
import com.app.test.timelytextview.model.number.Nine;
import com.app.test.timelytextview.model.number.Null;
import com.app.test.timelytextview.model.number.One;
import com.app.test.timelytextview.model.number.Seven;
import com.app.test.timelytextview.model.number.Six;
import com.app.test.timelytextview.model.number.Three;
import com.app.test.timelytextview.model.number.Two;
import com.app.test.timelytextview.model.number.Zero;

import java.security.InvalidParameterException;

public class NumberUtils {

    public static float[][] getControlPointsFor(int start) {
        switch (start) {
            case (-1):
                return Null.getInstance().getControlPoints();
            case 0:
                return Zero.getInstance().getControlPoints();
            case 1:
                return One.getInstance().getControlPoints();
            case 2:
                return Two.getInstance().getControlPoints();
            case 3:
                return Three.getInstance().getControlPoints();
            case 4:
                return Four.getInstance().getControlPoints();
            case 5:
                return Five.getInstance().getControlPoints();
            case 6:
                return Six.getInstance().getControlPoints();
            case 7:
                return Seven.getInstance().getControlPoints();
            case 8:
                return Eight.getInstance().getControlPoints();
            case 9:
                return Nine.getInstance().getControlPoints();
            default:
                throw new InvalidParameterException("Unsupported number requested");
        }
    }
}
