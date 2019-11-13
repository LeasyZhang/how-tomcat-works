package ex20.tomcat.standard;

public class Car implements CarMBean{

    private String color = "red";

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void drive() {
        System.out.println("Baby you can drive my " + color + " car.");
    }
}
