/*
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.parser.impl.message.update.extended.communities.route.target;

import io.netty.buffer.ByteBuf;
import org.opendaylight.protocol.util.Ipv4Util;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.types.rev200120.route.target.ipv4.grouping.RouteTargetIpv4;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.types.rev200120.route.target.ipv4.grouping.RouteTargetIpv4Builder;
import org.opendaylight.yangtools.yang.common.netty.ByteBufUtils;

/**
 * Route Target Ipv4 Handler.
 *
 * @author Claudio D. Gasparini
 */
public final class RouteTargetIpv4Handler {
    private RouteTargetIpv4Handler() {
        // Hidden on purpose
    }

    public static void serialize(final RouteTargetIpv4 routeTarget, final ByteBuf byteAggregator) {
        Ipv4Util.writeIpv4Address(routeTarget.getGlobalAdministrator(), byteAggregator);
        ByteBufUtils.writeOrZero(byteAggregator, routeTarget.getLocalAdministrator());
    }

    public static RouteTargetIpv4 parse(final ByteBuf buffer) {
        return new RouteTargetIpv4Builder()
                .setGlobalAdministrator(Ipv4Util.addressForByteBuf(buffer))
                .setLocalAdministrator(ByteBufUtils.readUint16(buffer))
                .build();
    }
}
