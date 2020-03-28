package org.batfish.common.util;

import org.batfish.datamodel.Ip;

/** Contains well known IPs */
public final class WellKnownIps {

  public static final Ip GOOGLE_DNS = Ip.parse("8.8.8.8");
  public static final Ip CLOUDFLARE_DNS = Ip.parse("1.1.1.1");
  public static final Ip DOC_RANGE_203 = Ip.parse("203.0.113.1");
}
