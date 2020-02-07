package org.batfish.datamodel;

import static org.batfish.datamodel.acl.AclLineMatchExprs.match;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import org.batfish.common.bdd.BDDFlowConstraintGenerator.FlowPreference;
import org.batfish.common.bdd.BDDPacket;
import org.batfish.common.bdd.BDDSourceManager;
import org.batfish.common.bdd.IpAccessListToBddImpl;
import org.batfish.datamodel.acl.AclLineMatchExpr;

/** Class for picking a representative flow from a header space. */
public final class HeaderSpaceToFlow {

  public HeaderSpaceToFlow(Map<String, IpSpace> ipSpaces, FlowPreference preference) {
    _headerSpaceToBDD =
        new IpAccessListToBddImpl(
            BDD_PACKET, BDDSourceManager.empty(BDD_PACKET), ImmutableMap.of(), ipSpaces);
    _preference = preference;
  }

  /** Get a representative flow from a header space according to a flow preference. */
  public Optional<Flow.Builder> getRepresentativeFlow(HeaderSpace hs) {
    return BDD_PACKET.getFlow(_headerSpaceToBDD.toBdd(match(hs)), _preference);
  }

  /** Get a representative flow from an {@link AclLineMatchExpr} according to a flow preference. */
  public Optional<Flow.Builder> getRepresentativeFlow(AclLineMatchExpr expr) {
    return BDD_PACKET.getFlow(_headerSpaceToBDD.toBdd(expr), _preference);
  }

  private static final BDDPacket BDD_PACKET = new BDDPacket();
  private final IpAccessListToBddImpl _headerSpaceToBDD;
  private final FlowPreference _preference;
}
