package de.appdynamics.ace.serviceTest.app;

import java.util.Random;

/**
 * Created by stefan.marx on 15.02.15.
 */
public class ApplicationWorkload {
    private final Random _rnd;
    private int _depth;
    private int _minWait;
    private int _maxWait;

    public ApplicationWorkload(int depth, int minWait, int maxWait) {
        setDepth(depth);
        setMinWait(minWait);
        setMaxWait(maxWait);
        _rnd = new Random();




    }

    public void setDepth(int depth) {
        _depth = depth;
    }

    public int getDepth() {
        return _depth;
    }

    public void setMinWait(int minWait) {
        _minWait = minWait;
    }

    public int getMinWait() {
        return _minWait;
    }

    public void setMaxWait(int maxWait) {
        _maxWait = maxWait;
    }

    public int getMaxWait() {
        return _maxWait;
    }

    public void execute() {
        execLoad(getDepth(),getMinWait(),getMaxWait());


    }

    private void execLoad(int depth, int minWait, int maxWait) {
        if (depth == 0)  {
            callWebSite();
            return;
        }
        try {
            Thread.sleep(_rnd.nextInt((maxWait-minWait)+minWait));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execLoad(depth-1,minWait,maxWait);


    }

    public void callWebSite() {
        try {
            Thread.sleep(82);
            System.out.println("E");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return;


    }
}
