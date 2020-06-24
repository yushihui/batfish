package org.batfish.representation.palo_alto;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.batfish.datamodel.Ip;
import org.batfish.datamodel.Prefix;

/** Palo Alto ipv4 prefix structure preserving initial (not canonical) ip address. */
public class InterfacePrefix implements Serializable {

  /** A "0.0.0.0/0" prefix */
  public static final InterfacePrefix ZERO = new InterfacePrefix(Ip.ZERO, 0);

  private Prefix _prefix;
  private Ip _ip;

  InterfacePrefix(Ip ip, int prefixLength) {
    _prefix = Prefix.create(ip, prefixLength);
    _ip = ip;
  }

  /** Parse a {@link Prefix} from a string. */
  @Nonnull
  @JsonCreator
  public static InterfacePrefix parse(@Nullable String text) {
    checkArgument(text != null, "Invalid IPv4 prefix %s", text);
    String[] parts = text.split("/");
    checkArgument(parts.length == 2, "Invalid prefix string: \"%s\"", text);
    Ip ip = Ip.parse(parts[0]);
    int prefixLength;
    try {
      prefixLength = Integer.parseInt(parts[1]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid prefix length: \"" + parts[1] + "\"", e);
    }
    return new InterfacePrefix(ip, prefixLength);
  }

  public Ip getIp() {
    return _ip;
  }

  public Prefix getPrefix() {
    return _prefix;
  }
}
