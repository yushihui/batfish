package org.batfish.representation.azure;

import java.util.List;
import org.batfish.common.VendorConversionException;
import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.ConfigurationFormat;
import org.batfish.vendor.VendorConfiguration;

public class AzureNetworkConfiguration extends VendorConfiguration {

  @Override public String getHostname() {
    return null;
  }

  @Override public void setHostname(String hostname) {

  }

  @Override public void setVendor(ConfigurationFormat format) {

  }

  @Override public List<Configuration> toVendorIndependentConfigurations()
      throws VendorConversionException {
    return null;
  }
}
