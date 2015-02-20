package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.kernel.spi.IDynamicService;
import com.singularity.ee.agent.appagent.kernel.spi.IDynamicServiceManager;
import com.singularity.ee.agent.appagent.kernel.spi.IServiceContext;
import com.singularity.ee.agent.appagent.kernel.spi.data.IServiceConfig;
import com.singularity.ee.agent.appagent.kernel.spi.exception.ConfigException;
import com.singularity.ee.agent.appagent.kernel.spi.exception.ServiceStartException;
import com.singularity.ee.agent.appagent.kernel.spi.exception.ServiceStopException;
import com.singularity.util.org.apache.log4j.Logger;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;


/**
 * Created by stefan.marx on 27.02.14.
 */
public class TestInstrumentationService implements IDynamicService {
    private IServiceConfig _cfg;

    public IDynamicServiceManager getSvcManager() {
        return _svcManager;
    }

    public IServiceContext getCtx() {
        return _ctx;
    }

    public Logger getLogger() {
        return logger;
    }

    private IDynamicServiceManager _svcManager;
    private IServiceContext _ctx;
    private Logger logger;
    private InstrumentationServiceController _controller;

    @Override
    public void setDynamicServiceManager(IDynamicServiceManager iDynamicServiceManager) {
        _svcManager = iDynamicServiceManager;
        logger = _svcManager.createLogger(getClass().getName());

    }

    @Override
    public String getName() {
        return "Test Service";
    }

    @Override
    public void setServiceContext(IServiceContext iServiceContext) {
        _ctx = iServiceContext;

    }

    @Override
    public void configure(IServiceConfig iServiceConfig) throws ConfigException {
        _cfg = iServiceConfig;



    }

    @Override
    public void start() throws ServiceStartException {
        _controller = new InstrumentationServiceController(this);

        try
        {
            _svcManager.registerMBean(_controller,
                    createObjectName(),
                    this);
        }
        catch (MalformedObjectNameException e)
        {
            logger.error(e.toString(),
                    e);
        }

    }

    private ObjectName createObjectName() throws MalformedObjectNameException {
        Hashtable<String, String> onProps = new Hashtable<String, String>();

        onProps.put(IDynamicServiceManager.MBEAN_TYPE_KEY,
                "InstrumentationService");

        ObjectName on = new ObjectName(IDynamicServiceManager.DOMAIN_NAME,      // create the ObjectName for the MBean
                onProps);

        return on;

    }

    @Override
    public void allServicesStarted() {

    }

    @Override
    public void stop() throws ServiceStopException {

    }

    @Override
    public void hotDisable() {

    }

    @Override
    public void hotEnable() {

    }
}
