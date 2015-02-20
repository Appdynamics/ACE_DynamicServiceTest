package com.appdynamics.ace.test.dynservice;

import com.singularity.ee.agent.appagent.entrypoint.bciengine.FastInterceptorClassRegistryBoot;
import com.singularity.ee.agent.appagent.kernel.JavaAgent;
import com.singularity.ee.agent.appagent.kernel.spi.Service;
import com.singularity.ee.agent.appagent.services.bciengine.custom.interceptors.BCIMatchRule;
import com.singularity.ee.agent.appagent.services.bciengine.pojo.util.BCIRuleApplier;
import com.singularity.ee.agent.appagent.services.bciengine.pojo.util.UnableToRetransformException;
import com.singularity.ee.agent.appagent.services.bciengine.spi.IBCIEngineService;
import com.singularity.ee.agent.appagent.services.bciengine.spi.TransformationRule;
import com.singularity.ee.controller.api.dto.transactionmonitor.transactiondefinition.POJOMethodDefinition;
import com.singularity.ee.controller.api.dto.transactionmonitor.transactiondefinition.pojo.POJOMatchType;

import javax.management.*;
import java.util.Map;

/**
 * Created by stefan.marx on 27.02.14.
 */
public class InstrumentationServiceController implements InstrumentationServiceControllerMBean {
    private final TestInstrumentationService _svc;
    private final BCIRuleApplier _applier;
    private boolean _newApplier;
    private ObjectName _objectName;
    private boolean _enabled;

    public InstrumentationServiceController(TestInstrumentationService testService) {

        _svc = testService;
        IBCIEngineService bci = (IBCIEngineService) JavaAgent.getInstance().getKernel().getLifeCycleManager().getRunningServices().get("BCIEngine");

        _applier = new BCIRuleApplier(_svc.getLogger(),bci);

        int id = FastInterceptorClassRegistryBoot.getNewInterceptorId();

        FastInterceptorClassRegistryBoot.bindFastInterceptorInstance(new SimpleTransactionInterceptor());
        FastInterceptorClassRegistryBoot.bindFastInterceptorInstance(new SimpleExitInterceptor());

        _svc.getLogger().warn("Interceptor registered with ID:"+SimpleTransactionInterceptor.getStaticInterceptorId());

        _newApplier = true;

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
    public String showServices() {

        StringBuffer b = new StringBuffer();

        Map<String, Service> map = JavaAgent.getInstance().getKernel().getLifeCycleManager().getRunningServices();
        for (Map.Entry<String,Service> e : map.entrySet()) {
            b.append(e.getKey()+"  -->")
                    .append(e.getValue().getName())
                    .append("(")
                    .append(e.getValue().getClass().getCanonicalName())
                    .append(")\n");
        }
        b.append("Services :: \n");

        _svc.getLogger().warn("TEST MESSAGE WARN!");
        _svc.getLogger().debug("DEBUG MESSAGE");
        return b.toString();
    }

    @Override
    public boolean instrumentClass() {
        if (_newApplier) {
            _applier.startApplyingNewSet();
        } else {
            _applier.startModifyingCurrentSet();
        }

        POJOMethodDefinition pojo = null;
        pojo = new POJOMethodDefinition(POJOMatchType.MATCHES_CLASS,
                            "de.appdynamics.ace.serviceTest.app.ApplicationWorkload",
                            "execute");


        registerInstrumentation(pojo,SimpleTransactionInterceptor.class);

        try {
            _newApplier = false;
            return _applier.finishApplyingNewSet();
        } catch (UnableToRetransformException e) {
            _svc.getLogger().error("Error while applying set",e);
            return false;

        }
    }

    private void registerInstrumentation(POJOMethodDefinition pojo,Class clazz) {
        BCIMatchRule bciMatchRule = BCIMatchRule.fromPOJOMethodDefinition(pojo,clazz.getName());

        _svc.getLogger().warn("Registered POJO for class :"+pojo.getClassName());


        TransformationRule newRule = _applier.getTransformationRuleForBCIMatchRule(
                bciMatchRule); // convert BCIMatchRule to a TransformationRule


        _applier.applyInterceptor(newRule);
    }

    @Override
    public boolean instrumentExitClass() {
        if (_newApplier) {
            _applier.startApplyingNewSet();
        } else {
            _applier.startModifyingCurrentSet();
        }

        POJOMethodDefinition pojo = null;

        pojo = new POJOMethodDefinition(POJOMatchType.MATCHES_CLASS,
                "de.appdynamics.ace.serviceTest.app.ApplicationWorkload",
                "execute");
        registerInstrumentation(pojo,SimpleTransactionInterceptor.class);


        pojo = new POJOMethodDefinition(POJOMatchType.MATCHES_CLASS,
                "de.appdynamics.ace.serviceTest.app.ApplicationWorkload",
                "callWebSite");
        registerInstrumentation(pojo,SimpleExitInterceptor.class);



        try {
            _newApplier = false;
            return _applier.finishApplyingNewSet();
        } catch (UnableToRetransformException e) {
            _svc.getLogger().error("Error while applying set",e);
            return false;

        }
    }
}
