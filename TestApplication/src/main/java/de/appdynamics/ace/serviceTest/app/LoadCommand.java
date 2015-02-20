package de.appdynamics.ace.serviceTest.app;

import com.appdynamics.ace.util.cli.api.api.AbstractCommand;
import com.appdynamics.ace.util.cli.api.api.Command;
import com.appdynamics.ace.util.cli.api.api.CommandException;
import com.appdynamics.ace.util.cli.api.api.OptionWrapper;
import org.apache.commons.cli.Option;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by stefan.marx on 15.02.15.
 */
public class LoadCommand extends AbstractCommand {
    private final String _name;

    public LoadCommand(String name) {
         _name = name;

    }

    @Override
    protected List<Option> getCLIOptionsImpl() {

        List<Option> options = new ArrayList<Option>();

        Option o;
        options.add(o = new Option("numThreads",true,"Num of parallel Threads executed. (Default: 5)"));

        o.setRequired(false);
        return options;
    }

    @Override
    protected int executeImpl(OptionWrapper optionWrapper) throws CommandException {
        int numThreads = Integer.parseInt(optionWrapper.getOptionValue("numThreads", String.valueOf(10)));
        ThreadGroup tg = new ThreadGroup("executor");

        for (int i = 0; i <numThreads; i++)  {
            Thread t = new Thread(tg, new WorkloadRunnable());
            System.out.println("Starting Thread:"+t.getName());
            t.start();
        }
        return 0;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String getDescription() {
        return "Starts some Basic load for simple instrumentor testing !";
    }

    private class WorkloadRunnable implements Runnable {
        @Override
        public void run() {
            int counter = 0;
            while( true) {
                long s = System.currentTimeMillis();
                ApplicationWorkload wl = new ApplicationWorkload(5, 20, 180);
                wl.execute();
                long f = System.currentTimeMillis();
                counter++;
                System.out.println(Thread.currentThread().getName()+" } Executed "+counter+ "  --> "+(f-s)+" ms");

                try {
                    Thread.sleep(new Random().nextInt(1000)+500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
