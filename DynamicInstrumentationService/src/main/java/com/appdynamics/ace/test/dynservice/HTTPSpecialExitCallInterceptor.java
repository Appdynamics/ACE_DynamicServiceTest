package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.services.transactionmonitor.http.correlation.AHTTPCorrelationInterceptor;
import com.singularity.ee.agent.appagent.services.transactionmonitor.http.correlation.HTTPHost;
import com.singularity.ee.agent.appagent.services.transactionmonitor.spi.ICurrentTransactionContext;

/**
 * Created by stefan.marx on 16.02.15.
 */
public class HTTPSpecialExitCallInterceptor extends AHTTPCorrelationInterceptor{
    @Override
    protected HTTPHost getHostAndPortInfo(Object o, String s, String s2, Object[] objects) {
        return null;
    }

    @Override
    public int getInterceptorId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected void propogateTransactionContext(Object o, String s, String s2, Object[] objects, Throwable throwable, Object o2, ICurrentTransactionContext iCurrentTransactionContext, int i) {

    }
}
