package org.batfish.representation.azure;

import javax.annotation.Nonnull;
import org.batfish.datamodel.Ip;

public class PublicIPAddress {
  @Nonnull private final String _id;
  @Nonnull private final String _name;
  @Nonnull private final String _location;
  @Nonnull private final Ip _ip;

  public PublicIPAddress(@Nonnull String id, @Nonnull String name, @Nonnull String location,
      @Nonnull Ip ip) {
    _id = id;
    _name = name;
    _location = location;
    _ip = ip;
  }
}
