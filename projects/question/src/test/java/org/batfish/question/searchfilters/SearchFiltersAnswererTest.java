package org.batfish.question.searchfilters;

import static org.batfish.datamodel.IpProtocol.TCP;
import static org.batfish.datamodel.acl.AclLineMatchExprs.matchDstIp;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.batfish.common.plugin.IBatfish;
import org.batfish.datamodel.Configuration;
import org.batfish.datamodel.ConfigurationFormat;
import org.batfish.datamodel.ExprAclLine;
import org.batfish.datamodel.HeaderSpace;
import org.batfish.datamodel.IpAccessList;
import org.batfish.datamodel.IpProtocol;
import org.batfish.datamodel.NetworkFactory;
import org.batfish.datamodel.PacketHeaderConstraints;
import org.batfish.datamodel.SubRange;
import org.batfish.datamodel.TcpFlags;
import org.batfish.datamodel.TcpFlagsMatchConditions;
import org.batfish.datamodel.table.TableAnswerElement;
import org.junit.Test;

/** Tests of {@link SearchFiltersAnswerer}. */
public class SearchFiltersAnswererTest {
  private static final IpAccessList ACL1 =
      IpAccessList.builder()
          .setName("acl1")
          .setLines(
              ImmutableList.of(
                  ExprAclLine.accepting().setMatchCondition(matchDstIp("1.1.1.1")).build()))
          .build();
  private static final IpAccessList ACL2 =
      IpAccessList.builder()
          .setName("acl2")
          .setLines(
              ImmutableList.of(
                  ExprAclLine.accepting().setMatchCondition(matchDstIp("2.2.2.2")).build()))
          .build();

  @Test
  public void testGetSpecifiedAcls_includeAll() {
    Configuration c = createConfigWithAcls(ACL1, ACL2);
    IBatfish bf = new MockBatfish(c);
    SearchFiltersQuestion question = SearchFiltersQuestion.builder().build();
    SearchFiltersAnswerer answerer = new SearchFiltersAnswerer(question, bf);
    assertThat(
        answerer.getSpecifiedAcls(bf.getSnapshot(), question),
        equalTo(ImmutableMap.of(c.getHostname(), c.getIpAccessLists())));
  }

  @Test
  public void testGetSpecifiedAcls_onlyAcl1() {
    Configuration c = createConfigWithAcls(ACL1, ACL2);
    IBatfish bf = new MockBatfish(c);
    SearchFiltersQuestion question =
        SearchFiltersQuestion.builder()
            .setNodeSpecifier(c.getHostname())
            .setFilterSpecifier(ACL1.getName())
            .build();
    SearchFiltersAnswerer answerer = new SearchFiltersAnswerer(question, bf);
    assertThat(
        answerer.getSpecifiedAcls(bf.getSnapshot(), question),
        equalTo(ImmutableMap.of(c.getHostname(), ImmutableMap.of(ACL1.getName(), ACL1))));
  }

  @Test
  public void testGetSpecifiedAcls_noHostnameMatch() {
    Configuration c = createConfigWithAcls(ACL1);
    IBatfish bf = new MockBatfish(c);
    SearchFiltersQuestion question =
        SearchFiltersQuestion.builder().setNodeSpecifier("unknown_hostname").build();
    SearchFiltersAnswerer answerer = new SearchFiltersAnswerer(question, bf);
    assertThat(answerer.getSpecifiedAcls(bf.getSnapshot(), question), anEmptyMap());
  }

  @Test
  public void testGetSpecifiedAcls_noFilterMatch() {
    Configuration c = createConfigWithAcls(ACL1);
    IBatfish bf = new MockBatfish(c);
    SearchFiltersQuestion question =
        SearchFiltersQuestion.builder().setFilterSpecifier("unknown_filter").build();
    SearchFiltersAnswerer answerer = new SearchFiltersAnswerer(question, bf);
    assertThat(
        answerer.getSpecifiedAcls(bf.getSnapshot(), question),
        equalTo(ImmutableMap.of(c.getHostname(), ImmutableMap.of())));
  }

  @Test
  public void test() {
    IpAccessList acl =
        IpAccessList.builder()
            .setName("acl")
            .setLines(
                ExprAclLine.acceptingHeaderSpace(
                    HeaderSpace.builder()
                        .setIpProtocols(IpProtocol.UDP)
                        .setDstPorts(SubRange.singleton(259))
                        .build()))
            .build();
    Configuration c = createConfigWithAcls(acl);
    IBatfish bf = new MockBatfish(c);
    SearchFiltersQuestion question =
        SearchFiltersQuestion.builder()
            .setHeaders(
                PacketHeaderConstraints.builder()
                    .setIpProtocols(ImmutableSet.of(TCP))
                    .setTcpFlags(
                        ImmutableSet.of(
                            TcpFlagsMatchConditions.matchAllFlags(
                                TcpFlags.builder().setAck(false).setRst(false).build())))
                    .build())
            .build();
    SearchFiltersAnswerer answerer = new SearchFiltersAnswerer(question, bf);
    TableAnswerElement answer = (TableAnswerElement) answerer.answer(bf.getSnapshot());
    assertThat(answer.getRows().getData(), empty());
  }

  private static Configuration createConfigWithAcls(IpAccessList... acls) {
    NetworkFactory nf = new NetworkFactory();
    Configuration c =
        nf.configurationBuilder().setConfigurationFormat(ConfigurationFormat.CISCO_IOS).build();
    for (IpAccessList acl : acls) {
      c.getIpAccessLists().put(acl.getName(), acl);
    }
    return c;
  }
}
