package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.kernel.spi.IMetricHandler;
import com.singularity.ee.agent.commonservices.metricgeneration.metrics.*;
import com.singularity.ee.controller.api.constants.MetricClusterRollupType;
import com.singularity.ee.controller.api.constants.MetricHoleHandlingType;
import com.singularity.ee.controller.api.constants.MetricTimeRollupType;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * Created by stefan.marx on 16.02.15.
 */
public class JMXReporter extends  Thread{
    private boolean _running;

    public JMXReporter () {
        super("JMXReporterSpecial");
    }

    @Override
    public void run() {
        while(isRunning()) {
            long l = System.currentTimeMillis();
            collectJMX();
            try {
                Thread.sleep(1000-(System.currentTimeMillis()-l) );
            } catch (InterruptedException e) {

            }
        }
    }

    private void collectJMX() {
        IMetricHandler mh = JavaAgent.getInstance().getMetricHandler();

        try {
            IMetricReporter metricRep = mh.getMetricReporter("Server|Component:"+getComponentID()+"|JMX|ThreadRatio|TEST1",
                    MetricAggregatorType.AVERAGE,
                    MetricTimeRollupType.AVERAGE,
                    MetricClusterRollupType.INDIVIDUAL,
                    MetricHoleHandlingType.REGULAR_COUNTER);

            metricRep.report(getMetric());

        } catch (InvalidMetricTypeException e) {
            e.printStackTrace();
        } catch (MetricReporterTypeMismatchException e) {
            e.printStackTrace();
        } catch (MetricOverflowException e) {
            e.printStackTrace();
        }

    }

    private long getMetric() {
        MBeanServer ms = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName on = new ObjectName("java.lang:type=Threading");

            long tc = Long.parseLong(ms.getAttribute(on, "DaemonThreadCount").toString());
            long ttc = Long.parseLong(ms.getAttribute(on, "ThreadCount").toString());
            return (ttc-tc);

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

        return 0;
    }

    public void stopReporting() {
        this.interrupt();
        setRunning(false);

    }

    public void setRunning(boolean running) {
        _running = running;
    }

    public boolean isRunning() {
        return _running;
    }

    public void startReporting() {
        setRunning(true);
        this.start();

    }

    private long getComponentID() {
        return JavaAgent.getInstance().getKernel().getConfigManager().getIConfigChannel().getComponentID();
    }
}
