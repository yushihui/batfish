package org.batfish.specifier;

import static org.parboiled.common.Preconditions.checkArgument;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.IpSpace;

/** Information about whether/how to treat a location as a source or sink of traffic. */
public final class LocationInfo implements Serializable {
  private final boolean _isSource;
  private final IpSpace _sourceIpSpace;
  private final IpSpace _arpIps;
  private final List<Ip> _preferredSourceIps;

  public LocationInfo(boolean isSource, IpSpace sourceIpSpace, IpSpace arpIps) {
    this(isSource, sourceIpSpace, arpIps, ImmutableList.of());
  }

  public LocationInfo(
      boolean isSource, IpSpace sourceIpSpace, IpSpace arpIps, List<Ip> preferredSourceIps) {
    preferredSourceIps.forEach(
        ip ->
            checkArgument(
                sourceIpSpace.containsIp(ip, ImmutableMap.of()),
                "Source IP space does not contain preferred source IP %s",
                ip));
    _isSource = isSource;
    _sourceIpSpace = sourceIpSpace;
    _arpIps = arpIps;
    _preferredSourceIps = ImmutableList.copyOf(preferredSourceIps);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LocationInfo)) {
      return false;
    }
    LocationInfo other = (LocationInfo) o;
    return _isSource == other._isSource
        && _sourceIpSpace.equals(other._sourceIpSpace)
        && _arpIps.equals(other._arpIps)
        && _preferredSourceIps.equals(other._preferredSourceIps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_isSource, _sourceIpSpace, _arpIps, _preferredSourceIps);
  }

  /**
   * Whether a location should be considered a source of traffic in batfish analyses. When false,
   * the user can still explicitly specify to use the location as a source.
   *
   * <p>For example, this might be false for an infrastructure interface, since most of the time the
   * user wants to analyze end-to-end network behavior. However, they can still explicitly source
   * traffic from that location, which can be useful for debugging in some cases.
   */
  public boolean isSource() {
    return _isSource;
  }

  /**
   * The set of IP addresses to use as source IPs for flows originating from the location by
   * default, when the location is used as a source of traffic. Users can override this via question
   * parameters. For non-sources, these IPs will be used when the user explicitly specifies to use
   * the location as a source but does not explicitly specify the source IPs to use.
   */
  public IpSpace getSourceIpSpace() {
    return _sourceIpSpace;
  }

  /**
   * For {@link InterfaceLinkLocation} only. Used for disposition assignment when a flow terminates
   * at an {@link InterfaceLinkLocation}. Batfish will give a successful disposition for these IPs,
   * as if ARP resolution indicates the presence of a device listening on that address at that
   * location, without modeling the device itself.
   */
  public IpSpace getArpIps() {
    return _arpIps;
  }

  /**
   * Source IP addresses to prefer when this location is used in queries like traceroute that need a
   * single IP. Each preferred IP is guaranteed to be in the source IP space.
   */
  public List<Ip> getPreferredSourceIps() {
    return _preferredSourceIps;
  }
}
