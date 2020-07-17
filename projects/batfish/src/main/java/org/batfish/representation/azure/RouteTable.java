package org.batfish.representation.azure;

import javax.annotation.Nonnull;

public class RouteTable {
  @Nonnull private final String _id;
  @Nonnull private final String _name;

  public RouteTable(@Nonnull String id, @Nonnull String name) {
    _id = id;
    _name = name;
  }
}
