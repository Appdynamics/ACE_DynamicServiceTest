package com.appdynamics.ace.test.dynservice;

import com.appdynamics.apm.appagent.api.ITransactionDemarcator;
import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.kernel.spi.IConfigManager;
import com.singularity.ee.agent.appagent.kernel.spi.IMetricHandler;
import com.singularity.ee.agent.appagent.kernel.spi.Service;
import com.singularity.ee.agent.appagent.services.bciengine.BCIEngineService;
import com.singularity.ee.agent.commonservices.metricgeneration.metrics.*;
import com.singularity.ee.controller.api.constants.MetricClusterRollupType;
import com.singularity.ee.controller.api.constants.MetricHoleHandlingType;
import com.singularity.ee.controller.api.constants.MetricTimeRollupType;

import javax.management.*;
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


    public String analyzeClass(String classname) {

        Map<String, Service> map = JavaAgent.getInstance().getKernel().getLifeCycleManager().getRunningServices();
        IMetricHandler hh = JavaAgent.getInstance().getMetricHandler();

        ITransactionDemarcator dem = JavaAgent.getInstance().getTransactionDelegate();





        String s = "";
        for (Map.Entry e : map.entrySet()) {

            s+= "  "+e.getKey()+"  .. "+e.getValue();

        }
        return s;
    }


    public String dump() {
        StringBuffer buffer = new StringBuffer("DEBUG \n");
        buffer.append(_svc.getCtx().getNodeDirName())
            .append("\n");
        MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName on = new ObjectName("java.lang:type=Threading");

            long tc = Long.parseLong(ms.getAttribute(on, "DaemonThreadCount").toString());
            long ttc = Long.parseLong(ms.getAttribute(on, "ThreadCount").toString());
            return "THREADRatio:"+(ttc-tc);

        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (AttributeNotFoundException e) {
            e.printStackTrace();
        } catch (MBeanException e) {
            e.printStackTrace();
        } catch (ReflectionException e) {
            e.printStackTrace();
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
        }
         return "failed";
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


    @Override
    public String reportMetric() {
        IMetricHandler mh = JavaAgent.getInstance().getMetricHandler();

        try {
            IMetricReporter metricRep = mh.getMetricReporter("Server|Component:"+getComponentID()+"|JMX|MASTER|TEST1",
                    MetricAggregatorType.AVERAGE,
                    MetricTimeRollupType.AVERAGE,
                    MetricClusterRollupType.INDIVIDUAL,
                    MetricHoleHandlingType.REGULAR_COUNTER);

            metricRep.report(23);
            return "Server|Component:"+getComponentID()+"|JMX|MASTER|TEST1  ---> Value "+23;
        } catch (InvalidMetricTypeException e) {
            e.printStackTrace();
            return e.toString();
        } catch (MetricReporterTypeMismatchException e) {
            e.printStackTrace();
            return e.toString();
        } catch (MetricOverflowException e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    private long getComponentID() {
        return JavaAgent.getInstance().getKernel().getConfigManager().getIConfigChannel().getComponentID();
    }

    @Override
    public String testMBeans() {
        String nn = "CC:"+JavaAgent.getInstance().getKernel().getConfigManager().getIConfigChannel().getComponentID();
        IConfigManager cm = JavaAgent.getInstance().getKernel().getConfigManager();



        return dump();
    }
}
