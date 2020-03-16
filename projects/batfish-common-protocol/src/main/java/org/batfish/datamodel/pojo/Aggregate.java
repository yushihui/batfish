package org.batfish.datamodel.pojo;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/** Represents an aggregate node in the pojo topology */
@ParametersAreNonnullByDefault
public class Aggregate extends BfObject {

  public enum AggregateType {
    CLOUD,
    REGION,
    SUBNET,
    VNET, // "VPC" in AWS
    UNKNOWN
  }

  private static final String PROP_CONTENTS = "contents";
  private static final String PROP_HUMAN_NAME = "humanName";
  private static final String PROP_NAME = "name";
  private static final String PROP_TYPE = "type";

  private Set<String> _contents;
  private final String _name;
  private AggregateType _type;
  private final String _humanName;

  public Aggregate(String name, AggregateType type) {
    this(name, type, name, new HashSet<>());
  }

  public Aggregate(
      String name, AggregateType type, @Nullable String humanName, Set<String> contents) {
    super(makeId(name));
    _name = name;
    _humanName = humanName;
    _type = type;
    _contents = contents;
  }

  @JsonCreator
  private static Aggregate jsonCreator(
      @Nullable @JsonProperty(PROP_ID) String id,
      @Nullable @JsonProperty(PROP_NAME) String name,
      @Nullable @JsonProperty(PROP_CONTENTS) Set<String> contents,
      @Nullable @JsonProperty(PROP_TYPE) AggregateType type,
      @Nullable @JsonProperty(PROP_HUMAN_NAME) String humanName) {
    checkArgument(name != null, "Missing %s", PROP_NAME);
    checkArgument(id != null, "Missing %s", PROP_ID);
    checkArgument(type != null, "Missing %s", PROP_TYPE);
    checkArgument(humanName != null, "Missing %s", PROP_HUMAN_NAME);
    return new Aggregate(
        name, type, humanName, ImmutableSet.copyOf(firstNonNull(contents, ImmutableSet.of())));
  }

  @JsonProperty(PROP_CONTENTS)
  public Set<String> getContents() {
    return _contents;
  }

  @JsonProperty(PROP_NAME)
  public String getName() {
    return _name;
  }

  @JsonProperty(PROP_HUMAN_NAME)
  public String getHumanName() {
    return _humanName;
  }

  @JsonProperty(PROP_TYPE)
  public AggregateType getType() {
    return _type;
  }

  public void setContents(Set<String> contents) {
    _contents = contents;
  }

  public void setType(AggregateType type) {
    _type = type;
  }

  @Nonnull
  static String makeId(@Nonnull String name) {
    return "aggregate-" + name;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof Aggregate)) {
      return false;
    }
    Aggregate a = (Aggregate) o;
    return _type == a._type
        && _name.equals(a._name)
        && _contents.equals(a._contents)
        && Objects.equals(getId(), a.getId())
        && Objects.equals(_humanName, a._humanName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_type.ordinal(), _name, _contents, getId(), _humanName);
  }
}
