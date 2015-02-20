package de.appdynamics.ace.serviceTest.app;

import com.appdynamics.ace.util.cli.api.api.CommandlineExecution;

/**
 * Created by stefan.marx on 15.02.15.
 */
public class Main {
    public static void main(String[] args) {
        CommandlineExecution cle = new CommandlineExecution("TestApp");
        cle.setHelpVerboseEnabled(true);

        cle.addCommand(new LoadCommand("startLoad"));


        cle.execute(args);
    }
}
