package org.batfish.representation.azure;

import javax.annotation.Nonnull;
import org.batfish.datamodel.Configuration;

public class VirtualHub extends NetworkingEntity implements AzureEntity {

  @Nonnull private final String _id;
  @Nonnull private final String _name;

  public VirtualHub(@Nonnull String id, @Nonnull String name) {
    _id = id;
    _name = name;
  }

  @Override public Configuration toConfigurationNodes(
      ConvertedConfiguration convertedConfiguration) {
    return null;
  }
}
