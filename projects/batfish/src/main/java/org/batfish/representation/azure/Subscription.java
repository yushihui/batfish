package org.batfish.representation.azure;

import java.util.Map;

// top on network resource
public class Subscription implements AzureEntity {

  private Map<String, NatGateway> natGateways;
  private Map<String, VirtualNetwork> vnets;
  private Map<String, VpnGateway> vpnGateways;
  private Map<String, VirtualHub> vhubs;

  public Subscription() {}
}
