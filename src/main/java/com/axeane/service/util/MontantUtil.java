package com.axeane.service.util;

import java.math.BigDecimal;

/**
 * Created by lenovo on 31/03/2018.
 */
public final class MontantUtil {

    public static BigDecimal montantRounding(BigDecimal montant){

        return new BigDecimal(montant.intValue()).movePointLeft(3);

    }
}
