package com.axeane.service.util;

import java.math.BigDecimal;

public final class MontantUtil {

    public static BigDecimal montantRounding(BigDecimal montant) {

        return new BigDecimal(montant.intValue()).movePointLeft(3);

    }
}
