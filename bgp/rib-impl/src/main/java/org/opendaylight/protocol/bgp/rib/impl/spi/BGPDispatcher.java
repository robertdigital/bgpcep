/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.protocol.bgp.rib.impl.spi;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import java.net.InetSocketAddress;
import org.opendaylight.protocol.concepts.KeyMapping;

/**
 * Dispatcher class for creating BGP clients.
 */
public interface BGPDispatcher {
    /**
     * Creates Reconnecting client.
     *
     * @param remoteAddress remote Peer Address
     * @param retryTimer    Retry timer
     * @param keys          for TCPMD5
     * @return Future promising a client session
     */
    Future<Void> createReconnectingClient(InetSocketAddress remoteAddress, int retryTimer, KeyMapping keys);

    /**
     * Create new BGP server to accept incoming bgp connections (bound to provided socket localAddress).
     *
     * @param localAddress Peer localAddress
     * @return ChannelFuture promising a client session
     */
    ChannelFuture createServer(InetSocketAddress localAddress);

    /**
     * Return BGP Peer Registry
     *
     * @return BGPPeerRegistry
     */
    BGPPeerRegistry getBGPPeerRegistry();
}
