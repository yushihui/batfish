package org.batfish.representation.azure;

import org.batfish.datamodel.Configuration;

abstract class NetworkingEntity {
  abstract Configuration toConfigurationNodes(ConvertedConfiguration convertedConfiguration);
}
