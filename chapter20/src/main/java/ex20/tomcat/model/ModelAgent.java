package ex20.tomcat.model;

import javax.management.*;
import javax.management.modelmbean.*;

public class ModelAgent {

    private final String MANAGED_CLASS_NAME = "ex20.tomcat.model.Car";

    private MBeanServer mBeanServer = null;

    public ModelAgent() {
        mBeanServer = MBeanServerFactory.createMBeanServer();
    }

    public MBeanServer getmBeanServer() {
        return mBeanServer;
    }

    private ObjectName createObjectName(String name) throws MalformedObjectNameException {
        ObjectName objectName = null;
        objectName = new ObjectName(name);
        return objectName;
    }

    private ModelMBean createMBean(ObjectName objectName, String mbeanName) throws MBeanException {
        ModelMBeanInfo modelMBeanInfo = createModelMBeanInfo(objectName, mbeanName);
        RequiredModelMBean modelMBean = null;
        modelMBean = new RequiredModelMBean(modelMBeanInfo);
        return modelMBean;
    }

    private ModelMBeanInfo createModelMBeanInfo(ObjectName objectName, String mbeanName) {
        ModelMBeanInfo mBeanInfo = null;
        ModelMBeanAttributeInfo[] attributes = new ModelMBeanAttributeInfo[1];
        ModelMBeanOperationInfo[] operations = new ModelMBeanOperationInfo[3];

        attributes[0] = new ModelMBeanAttributeInfo("Color", "java.lang.String", "the color.",
                true, true, false, null);
        operations[0] = new ModelMBeanOperationInfo("drive", "the drive method", null, "void", MBeanOperationInfo.ACTION, null);
        operations[1] = new ModelMBeanOperationInfo("getColor", "get color attribute", null, "java.lang.String", MBeanOperationInfo.ACTION, null);

        Descriptor setColorDesc = new DescriptorSupport(new String[] {"name=setColor", "descriptorType=operation",
        "class=" + MANAGED_CLASS_NAME, "role=operation"});
        MBeanParameterInfo[] setColorParams = new MBeanParameterInfo[] {
                (new MBeanParameterInfo("new color", "java.lang.String", "new Color value"))
        };
        operations[2] = new ModelMBeanOperationInfo("setColor", "set Color Attribute", setColorParams,
                "void", MBeanOperationInfo.ACTION, setColorDesc);

        mBeanInfo = new ModelMBeanInfoSupport(MANAGED_CLASS_NAME, null, attributes, null, operations, null);

        return mBeanInfo;
    }

    public static void main(String[] args) throws Exception{
        ModelAgent agent = new ModelAgent();
        MBeanServer mBeanServer = agent.getmBeanServer();

        Car car = new Car();
        String domain = mBeanServer.getDefaultDomain();
        ObjectName objectName = agent.createObjectName(domain + ":type=MyCar");
        String mBeanName = "myBean";
        ModelMBean modelMBean = agent.createMBean(objectName, mBeanName);
        modelMBean.setManagedResource(car, "ObjectReference");
        mBeanServer.registerMBean(modelMBean, objectName);

        Attribute attribute = new Attribute("Color", "green");
        mBeanServer.setAttribute(objectName, attribute);
        String color = (String) mBeanServer.getAttribute(objectName, "Color");
        System.out.println("Color: " + color);

        attribute = new Attribute("Color", "blue");
        mBeanServer.setAttribute(objectName, attribute);
        color = (String) mBeanServer.getAttribute(objectName, "Color");
        System.out.println("Color: " + color);

        mBeanServer.invoke(objectName, "drive", null, null);
    }
}
