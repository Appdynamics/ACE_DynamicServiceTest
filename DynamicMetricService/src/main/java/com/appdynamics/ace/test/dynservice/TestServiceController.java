package com.appdynamics.ace.test.dynservice;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.kernel.spi.IMetricHandler;
import com.singularity.ee.agent.appagent.kernel.spi.Service;
import com.singularity.ee.agent.appagent.services.bciengine.BCIEngineService;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by stefan.marx on 27.02.14.
 */
public class TestServiceController implements TestServiceControllerMBean {
    private final TestService _svc;
    private ObjectName _objectName;
    private boolean _enabled;

    public TestServiceController(TestService testService) {
        _svc = testService;
    }

    public String hello(String p1) {

        return p1.toUpperCase();
    }

    @Override
    public String analyzeClass(String classname) {

        Map<String, Service> map = JavaAgent.getInstance().getKernel().getLifeCycleManager().getRunningServices();
        IMetricHandler hh = JavaAgent.getInstance().getMetricHandler();

        ITransactionDemarcator dem = JavaAgent.getInstance().getTransactionDelegate();

        dem.setActivityNameForCurrentThread()



        String s = "";
        for (Map.Entry e : map.entrySet()) {

            s+= "  "+e.getKey()+"  .. "+e.getValue();

        }
        return s;
    }

    @Override
    public String dump() {
        StringBuffer buffer = new StringBuffer("DEBUG \n");
        buffer.append(_svc.getCtx().getNodeDirName())
            .append("\n");
        MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> names = ms.queryNames(null, null);

        for (ObjectName s : names) {
            buffer.append(s.getCanonicalName()).append("\n");
        }

        return buffer.toString();
    }

    @Override
    public ObjectName getObjectName() {
        return _objectName;
    }

    @Override
    public boolean isEnabled() {
        return _enabled;
    }

    @Override
    public void setEnabled(boolean b) {
         _enabled = b;
    }

    @Override
    public void setObjectName(ObjectName objectName) {
        _objectName = objectName;
    }


}
