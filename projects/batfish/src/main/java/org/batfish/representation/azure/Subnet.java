package org.batfish.representation.azure;

import javax.annotation.Nonnull;
import org.batfish.datamodel.Prefix;

public class Subnet {
  @Nonnull private final String _id;
  @Nonnull private final String _name;
  @Nonnull private final String _vnetId;
  @Nonnull private final Prefix _cidrBlock;

  public Subnet(@Nonnull String id, @Nonnull String name, @Nonnull String vnetId,
      @Nonnull Prefix cidrBlock) {
    _id = id;
    _name = name;
    _vnetId = vnetId;
    _cidrBlock = cidrBlock;
  }
}
