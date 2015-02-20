package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.kernel.spi.IDynamicMBeanBase;

/**
 * Created by stefan.marx on 27.02.14.
 */
public interface InstrumentationServiceControllerMBean extends IDynamicMBeanBase {

    public String showServices();
    public boolean instrumentClass();
    public boolean instrumentExitClass();

}
