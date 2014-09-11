package org.pseudosystems.geodaisia;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;

public class Constants {

    public static final Charset         utf8                = Charset.forName("UTF-8");
    public static final Pattern         ws                  = Pattern.compile("\\s+"),
                                        dot                 = Pattern.compile("\\.");
    public static final LargeInteger    minusOne            = LargeInteger.ONE.opposite(),
                                        minLong             = LargeInteger.valueOf(Long.MIN_VALUE),
                                        maxLong             = LargeInteger.valueOf(Long.MAX_VALUE),
                                        thousand            = LargeInteger.valueOf(1000),
                                        ten                 = LargeInteger.valueOf(10),
                                        zero                = LargeInteger.ZERO;
    public static final Real            comparisonAccuracy  = Real.valueOf(1e-9);
    public static final Rational        half                = Rational.valueOf(1l, 2l);
}
