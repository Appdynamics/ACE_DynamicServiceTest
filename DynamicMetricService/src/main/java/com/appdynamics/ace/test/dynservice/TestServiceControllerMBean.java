package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.kernel.spi.IDynamicMBeanBase;

/**
 * Created by stefan.marx on 27.02.14.
 */
public interface TestServiceControllerMBean extends IDynamicMBeanBase {
    public String reportMetric();

    public String testMBeans();
}
