package ex20.tomcat.standard;

import javax.management.*;

public class StandardAgent {

    private MBeanServer mBeanServer;

    public StandardAgent() {
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getmBeanServer() {
        return mBeanServer;
    }

    public ObjectName createObjectName(String name) throws MalformedObjectNameException {
        ObjectName objectName = new ObjectName(name);
        return objectName;
    }

    private void createStandardBean(ObjectName objectName, String managedResourceClassName) throws Exception{
        mBeanServer.createMBean(managedResourceClassName, objectName);
    }

    public static void main(String[] args) throws Exception{
        StandardAgent agent = new StandardAgent();
        MBeanServer mBeanServer = agent.getmBeanServer();
        String domain = mBeanServer.getDefaultDomain();
        String managedResourceClassName = "ex20.tomcat.standard.Car";
        ObjectName objectName = agent.createObjectName(domain + ":type=" + managedResourceClassName);
        agent.createStandardBean(objectName, managedResourceClassName);

        Attribute colorAttribute = new Attribute("Color", "blue");
        mBeanServer.setAttribute(objectName, colorAttribute);
        System.out.println(mBeanServer.getAttribute(objectName, "Color"));
        mBeanServer.invoke(objectName, "drive", null, null);
    }
}
