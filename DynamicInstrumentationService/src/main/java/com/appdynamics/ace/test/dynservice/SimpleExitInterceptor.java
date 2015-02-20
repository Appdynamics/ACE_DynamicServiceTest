package com.appdynamics.ace.test.dynservice;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.FastInterceptorClassRegistryBoot;
import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.services.bciengine.AFastTrackedMethodInterceptor;
import com.singularity.ee.agent.appagent.services.transactionmonitor.common.CorrelationHeader;
import com.singularity.ee.agent.appagent.services.transactionmonitor.common.CurrentExitCall;
import com.singularity.ee.agent.appagent.services.transactionmonitor.http.correlation.AHTTPCorrelationInterceptor;
import com.singularity.ee.agent.appagent.services.transactionmonitor.http.correlation.HTTPHost;
import com.singularity.ee.agent.appagent.services.transactionmonitor.http.correlation.HttpHostUtil;
import com.singularity.ee.agent.appagent.services.transactionmonitor.spi.ICurrentTransactionContext;
import com.singularity.ee.agent.util.reflect.ReflectionException;
import com.singularity.ee.agent.util.reflect.ReflectionUtility;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by stefan.marx on 16.02.15.
 */
public class SimpleExitInterceptor extends AHTTPCorrelationInterceptor{
    private static final int _interceptorId = FastInterceptorClassRegistryBoot.getNewInterceptorId();

    public SimpleExitInterceptor() {


    }

    @Override
    protected HTTPHost getHostAndPortInfo(Object o, String s, String s2, Object[] objects) {
        System.out.println("EXIT");
        return new HTTPHost("http","myHostNAme.com",80,"nope/nodder/nuun","nope=kdk");
    }

    @Override
    protected void propogateTransactionContext(Object o, String s, String s2, Object[] objects, Throwable throwable, Object o2, ICurrentTransactionContext iCurrentTransactionContext, int i) {
        return;
    }


    @Override
    public int getInterceptorId() {
        return _interceptorId;
    }

    public static int getStaticInterceptorId() {
        return _interceptorId;
    }

    @Override
    public String getName() {
        return "SimpleTransactionExitInterceptor";
    }

    @Override
    public String getDescription() {
        return "SimpleTransactionExitInterceptor : NOPE";
    }

    @Override
    protected boolean isResponseTimeAndErrorReporter() {
        return true;
    }



}
