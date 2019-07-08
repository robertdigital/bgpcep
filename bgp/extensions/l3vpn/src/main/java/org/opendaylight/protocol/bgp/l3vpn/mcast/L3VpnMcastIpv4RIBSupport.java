/*
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.l3vpn.mcast;

import static com.google.common.base.Verify.verify;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.protocol.bgp.l3vpn.mcast.nlri.L3vpnMcastNlriSerializer;
import org.opendaylight.protocol.bgp.parser.spi.PathIdUtil;
import org.opendaylight.protocol.util.ByteArray;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpPrefix;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Ipv4Prefix;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.bgp.rib.rib.loc.rib.tables.routes.L3vpnMcastRoutesIpv4Case;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.l3vpn.mcast.destination.L3vpnMcastDestination;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.l3vpn.mcast.routes.L3vpnMcastRoute;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.l3vpn.mcast.routes.ipv4.L3vpnMcastRoutesIpv4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.l3vpn.mcast.routes.ipv4.L3vpnMcastRoutesIpv4Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.reach.nlri.advertized.routes.destination.type.DestinationL3vpnMcastIpv4AdvertizedCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.reach.nlri.advertized.routes.destination.type.DestinationL3vpnMcastIpv4AdvertizedCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.reach.nlri.advertized.routes.destination.type.destination.l3vpn.mcast.ipv4.advertized._case.DestinationIpv4L3vpnMcast;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.reach.nlri.advertized.routes.destination.type.destination.l3vpn.mcast.ipv4.advertized._case.DestinationIpv4L3vpnMcastBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.unreach.nlri.withdrawn.routes.destination.type.DestinationL3vpnMcastIpv4WithdrawnCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update.attributes.mp.unreach.nlri.withdrawn.routes.destination.type.DestinationL3vpnMcastIpv4WithdrawnCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.rib.rev180329.rib.tables.Routes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.types.rev180329.Ipv4AddressFamily;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.NodeIdentifierWithPredicates;
import org.opendaylight.yangtools.yang.data.api.YangInstanceIdentifier.PathArgument;
import org.opendaylight.yangtools.yang.data.api.schema.DataContainerChild;
import org.opendaylight.yangtools.yang.data.api.schema.MapEntryNode;
import org.opendaylight.yangtools.yang.data.api.schema.UnkeyedListEntryNode;

/**
 * Ipv4 L3VPN Multicast RIBSupport.
 *
 * @author Claudio D. Gasparini
 */
public final class L3VpnMcastIpv4RIBSupport
        extends AbstractL3vpnMcastIpRIBSupport<L3vpnMcastRoutesIpv4Case, L3vpnMcastRoutesIpv4> {
    private static final L3vpnMcastRoutesIpv4 EMPTY_CONTAINER
            = new L3vpnMcastRoutesIpv4Builder().setL3vpnMcastRoute(Collections.emptyList()).build();
    private static L3VpnMcastIpv4RIBSupport SINGLETON;


    private L3VpnMcastIpv4RIBSupport(final BindingNormalizedNodeSerializer mappingService) {
        super(mappingService,
                L3vpnMcastRoutesIpv4Case.class,
                L3vpnMcastRoutesIpv4.class,
                Ipv4AddressFamily.class,
                DestinationIpv4L3vpnMcast.QNAME,
                L3vpnMcastDestination.QNAME);
    }

    public static synchronized L3VpnMcastIpv4RIBSupport getInstance(
            final BindingNormalizedNodeSerializer mappingService) {
        if (SINGLETON == null) {
            SINGLETON = new L3VpnMcastIpv4RIBSupport(mappingService);
        }
        return SINGLETON;
    }


    @Override
    protected DestinationL3vpnMcastIpv4AdvertizedCase buildDestination(final Collection<MapEntryNode> routes) {
        return new DestinationL3vpnMcastIpv4AdvertizedCaseBuilder().setDestinationIpv4L3vpnMcast(
                new DestinationIpv4L3vpnMcastBuilder().setL3vpnMcastDestination(extractRoutes(routes)).build()).build();
    }

    @Override
    protected DestinationL3vpnMcastIpv4WithdrawnCase buildWithdrawnDestination(final Collection<MapEntryNode> routes) {
        return new DestinationL3vpnMcastIpv4WithdrawnCaseBuilder().setDestinationIpv4L3vpnMcast(
                new org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417.update
                        .attributes.mp.unreach.nlri.withdrawn.routes.destination.type.destination.l3vpn.mcast.ipv4
                        .withdrawn._case.DestinationIpv4L3vpnMcastBuilder()
                        .setL3vpnMcastDestination(extractRoutes(routes)).build()).build();
    }

    @Override
    public L3vpnMcastRoutesIpv4 emptyRoutesContainer() {
        return EMPTY_CONTAINER;
    }

    @Override
    protected IpPrefix createPrefix(final String prefix) {
        return new IpPrefix(new Ipv4Prefix(prefix));
    }

    @Override
    public NodeIdentifierWithPredicates createRouteKey(final UnkeyedListEntryNode l3vpn) {
        final ByteBuf buffer = Unpooled.buffer();
        final L3vpnMcastDestination dest = extractDestinations(l3vpn);
        L3vpnMcastNlriSerializer.serializeNlri(Collections.singletonList(dest), buffer);
        final Optional<DataContainerChild<? extends PathArgument, ?>> maybePathIdLeaf =
                l3vpn.getChild(routePathIdNid());
        return PathIdUtil.createNidKey(routeQName(), routeKeyQName(),
                pathIdQName(), ByteArray.encodeBase64(buffer), maybePathIdLeaf);
    }

    @Override
    public List<L3vpnMcastRoute> extractAdjRibInRoutes(final Routes routes) {
        verify(routes instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast
            .rev180417.bgp.rib.rib.peer.adj.rib.in.tables.routes.L3vpnMcastRoutesIpv4Case, "Unrecognized routes %s",
            routes);
        return ((org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.l3vpn.mcast.rev180417
                .bgp.rib.rib.peer.adj.rib.in.tables.routes.L3vpnMcastRoutesIpv4Case) routes).getL3vpnMcastRoutesIpv4()
                .nonnullL3vpnMcastRoute();
    }
}
