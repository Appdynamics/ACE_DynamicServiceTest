package com.appdynamics.ace.test.dynservice;

import javax.management.*;

/**
 * Created by stefan.marx on 27.02.14.
 */
public class InstrumentationServiceController implements InstrumentationServiceControllerMBean {
    private final TestInstrumentationService _svc;
    private ObjectName _objectName;
    private boolean _enabled;

    public InstrumentationServiceController(TestInstrumentationService testService) {
        _svc = testService;
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
