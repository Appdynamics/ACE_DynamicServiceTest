package com.appdynamics.ace.test.dynservice;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.singularity.ee.agent.appagent.entrypoint.bciengine.FastInterceptorClassRegistryBoot;
import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.services.bciengine.AFastTrackedMethodInterceptor;

/**
 * Created by stefan.marx on 16.02.15.
 */
public class SimpleTransactionInterceptor extends AFastTrackedMethodInterceptor{
    private static final int _interceptorId = FastInterceptorClassRegistryBoot.getNewInterceptorId();

    public SimpleTransactionInterceptor() {


    }

    @Override
    public Object onMethodBeginTracked(Object invokedObject, String className, String methodName,
                                       Object[] paramValues, int transformationId) {
        System.out.print("INTERCEPTTED !!!!!!!!!!");
        ITransactionDemarcator dem = JavaAgent.getInstance().getTransactionDelegate();

        String threadName = Thread.currentThread().getName();
        int num = Integer.parseInt(threadName.split("-")[1]);
        int n2 = (num/5);


        String name = "TransBlock-"+(n2*5)+"--"+((n2+1)*5);
        System.out.print("Starting Trans: " + name);
        dem.beginOriginatingTransactionAndAddCurrentThread(name, null);
        System.out.println("   --- ST:" + dem.getUniqueIdentifierForTransaction());

        return "STATE";
    }

    @Override
    public  void onMethodEndTracked(Object state, Object invokedObject, String className, String methodName,
                                            Object[] paramValues, Throwable thrownException, Object returnValue, int transformationId){


        ITransactionDemarcator dem = JavaAgent.getInstance().getTransactionDelegate();
        System.out.println("EN:"+dem.getUniqueIdentifierForTransaction());

        dem.endOriginatingTransactionAndRemoveCurrentThread();

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
        return "SimpleTransactionInterceptor";
    }

    @Override
    public String getDescription() {
        return "SimpleTransactionInterceptor : NOPE";
    }


    @Override
    public boolean shouldCallOnMethodEnd() {
        return true;
    }
}
