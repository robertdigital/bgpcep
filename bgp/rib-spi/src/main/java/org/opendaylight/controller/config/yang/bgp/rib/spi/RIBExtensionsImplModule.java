/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * Generated file

 * Generated from: yang module name: config-bgp-rib-spi  yang module local name: bgp-rib-extensions-impl
 * Generated by: org.opendaylight.controller.config.yangjmxgenerator.plugin.JMXGenerator
 * Generated at: Wed Nov 27 12:38:57 CET 2013
 *
 * Do not modify this file unless it is present under src/main directory
 */
package org.opendaylight.controller.config.yang.bgp.rib.spi;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import java.lang.reflect.Method;
import org.opendaylight.controller.config.api.osgi.WaitingServiceTracker;
import org.opendaylight.protocol.bgp.rib.spi.RIBExtensionProviderContext;
import org.osgi.framework.BundleContext;

/**
 * @deprecated Replaced by blueprint wiring but remains for backwards compatibility until downstream users
 *             of the provided config system service are converted to blueprint.
 */
@Deprecated
public final class RIBExtensionsImplModule extends org.opendaylight.controller.config.yang.bgp.rib.spi.AbstractRIBExtensionsImplModule {
    private BundleContext bundleContext;

    public RIBExtensionsImplModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver) {
        super(identifier, dependencyResolver);
    }

    public RIBExtensionsImplModule(final org.opendaylight.controller.config.api.ModuleIdentifier identifier,
            final org.opendaylight.controller.config.api.DependencyResolver dependencyResolver, final RIBExtensionsImplModule oldModule,
            final java.lang.AutoCloseable oldInstance) {
        super(identifier, dependencyResolver, oldModule, oldInstance);
    }

    @Override
    protected void customValidation() {
        // Add custom validation for module attributes here.
    }

    @Override
    public AutoCloseable createInstance() {
        // The RIBExtensionProviderContext instance is created and advertised as an OSGi service via blueprint
        // so obtain it here (waiting if necessary).
        final WaitingServiceTracker<RIBExtensionProviderContext> tracker =
                WaitingServiceTracker.create(RIBExtensionProviderContext.class, bundleContext);
        final RIBExtensionProviderContext service = tracker.waitForService(WaitingServiceTracker.FIVE_MINUTES);

        // Create a proxy to override close to close the ServiceTracker. The actual RIBExtensionProviderContext
        // instance will be closed via blueprint.
        return Reflection.newProxy(AutoCloseableRIBExtensionProviderContext.class, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("close")) {
                    tracker.close();
                    return null;
                } else {
                    return method.invoke(service, args);
                }
            }
        });
    }

    void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    private static interface AutoCloseableRIBExtensionProviderContext extends RIBExtensionProviderContext, AutoCloseable {
    }
}
