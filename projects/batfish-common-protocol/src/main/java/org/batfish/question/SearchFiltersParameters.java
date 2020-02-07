package org.batfish.question;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.util.Objects.requireNonNull;
import static org.batfish.datamodel.acl.AclLineMatchExprs.and;
import static org.batfish.datamodel.acl.AclLineMatchExprs.matchDst;
import static org.batfish.datamodel.acl.AclLineMatchExprs.matchSrc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import javax.annotation.Nonnull;
import org.batfish.datamodel.AclIpSpace;
import org.batfish.datamodel.EmptyIpSpace;
import org.batfish.datamodel.IpSpace;
import org.batfish.datamodel.acl.AclLineMatchExpr;
import org.batfish.specifier.IpSpaceAssignment.Entry;
import org.batfish.specifier.IpSpaceSpecifier;
import org.batfish.specifier.LocationSpecifier;
import org.batfish.specifier.SpecifierContext;

/** A set of parameters for ACL filter analysis that uses high-level specifiers. */
public final class SearchFiltersParameters {
  private final @Nonnull IpSpaceSpecifier _destinationIpSpaceSpecifier;
  private final @Nonnull AclLineMatchExpr _headerSpace;
  private final @Nonnull LocationSpecifier _startLocationSpecifier;
  private final @Nonnull IpSpaceSpecifier _sourceIpSpaceSpecifier;

  private SearchFiltersParameters(
      @Nonnull IpSpaceSpecifier destinationIpSpaceSpecifier,
      @Nonnull LocationSpecifier startLocationSpecifier,
      @Nonnull IpSpaceSpecifier sourceIpSpaceSpecifier,
      @Nonnull AclLineMatchExpr headerSpace) {
    _destinationIpSpaceSpecifier = destinationIpSpaceSpecifier;
    _headerSpace = headerSpace;
    _startLocationSpecifier = startLocationSpecifier;
    _sourceIpSpaceSpecifier = sourceIpSpaceSpecifier;
  }

  @Nonnull
  public IpSpaceSpecifier getDestinationIpSpaceSpecifier() {
    return _destinationIpSpaceSpecifier;
  }

  @Nonnull
  public LocationSpecifier getStartLocationSpecifier() {
    return _startLocationSpecifier;
  }

  @Nonnull
  public IpSpaceSpecifier getSourceIpSpaceSpecifier() {
    return _sourceIpSpaceSpecifier;
  }

  /** Resolve all parameters and update the underlying headerspace. */
  public AclLineMatchExpr resolveHeaderspace(SpecifierContext ctx) {
    return and(
        _headerSpace,
        matchSrc(resolveIpSpaceSpecifier(_sourceIpSpaceSpecifier, ctx)),
        matchDst(resolveIpSpaceSpecifier(_destinationIpSpaceSpecifier, ctx)));
  }

  @Nonnull
  private static IpSpace resolveIpSpaceSpecifier(IpSpaceSpecifier specifier, SpecifierContext ctx) {
    return firstNonNull(
        AclIpSpace.union(
            specifier.resolve(ImmutableSet.of(), ctx).getEntries().stream()
                .map(Entry::getIpSpace)
                .collect(ImmutableList.toImmutableList())),
        EmptyIpSpace.INSTANCE);
  }

  public Builder toBuilder() {
    return new Builder()
        .setDestinationIpSpaceSpecifier(_destinationIpSpaceSpecifier)
        .setHeaderSpace(_headerSpace)
        .setStartLocationSpecifier(_startLocationSpecifier)
        .setSourceIpSpaceSpecifier(_sourceIpSpaceSpecifier);
  }

  public static final class Builder {
    private IpSpaceSpecifier _destinationIpSpaceSpecifier;
    private AclLineMatchExpr _headerSpace;
    private LocationSpecifier _startLocationSpecifier;
    private IpSpaceSpecifier _sourceIpSpaceSpecifier;

    private Builder() {}

    public Builder setDestinationIpSpaceSpecifier(
        @Nonnull IpSpaceSpecifier destinationIpSpaceSpecifier) {
      _destinationIpSpaceSpecifier = destinationIpSpaceSpecifier;
      return this;
    }

    public Builder setHeaderSpace(@Nonnull AclLineMatchExpr headerSpace) {
      _headerSpace = headerSpace;
      return this;
    }

    public Builder setSourceIpSpaceSpecifier(@Nonnull IpSpaceSpecifier sourceIpSpaceSpecifier) {
      _sourceIpSpaceSpecifier = sourceIpSpaceSpecifier;
      return this;
    }

    public SearchFiltersParameters build() {
      return new SearchFiltersParameters(
          requireNonNull(_destinationIpSpaceSpecifier),
          requireNonNull(_startLocationSpecifier),
          requireNonNull(_sourceIpSpaceSpecifier),
          requireNonNull(_headerSpace));
    }

    public Builder setStartLocationSpecifier(@Nonnull LocationSpecifier startLocationSpecifier) {
      _startLocationSpecifier = startLocationSpecifier;
      return this;
    }
  }

  public static Builder builder() {
    return new Builder();
  }
}
