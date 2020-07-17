package org.batfish.representation.azure;

import java.io.Serializable;
import org.batfish.datamodel.Configuration;

abstract class NetworkingEntity implements Serializable {
  abstract Configuration toConfigurationNodes(ConvertedConfiguration convertedConfiguration);
}
