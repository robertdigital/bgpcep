/*
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.route.targetcontrain.spi.nlri;

import io.netty.buffer.ByteBuf;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.bgp.route.target.constrain.rev180618.route.target.constrain.RouteTargetConstrainChoice;

public interface RouteTargetConstrainParser<T extends RouteTargetConstrainChoice> {
    /**
     * Decode input buffer to BGP Route Target.
     *
     * @param buffer encoded RouteTarget body in Bytebuf
     * @return Route Target
     */
    @NonNull T parseRouteTargetConstrain(@NonNull ByteBuf buffer);

    /**
     * Returns NlriType handled by parser.
     *
     * @return NlriType
     */
    Integer getType();
}
