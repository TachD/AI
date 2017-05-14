package edu.BarSU.AI.GUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Govor Alexander on 30.04.2017.
 */
public class MethodModel {
    private final SimpleStringProperty nameMethod;
    private final SimpleIntegerProperty coastMethod;
    private final SimpleStringProperty routeMethod;
    private final SimpleIntegerProperty iterationMethod;
    private final SimpleLongProperty timeMethod;

    public MethodModel(String nameMethod,
                       int coastMethod,
                       String routeMethod,
                       int iterationMethod,
                       long timeMethod) {
        this.nameMethod = new SimpleStringProperty(nameMethod);
        this.coastMethod = new SimpleIntegerProperty(coastMethod);
        this.routeMethod = new SimpleStringProperty(routeMethod);
        this.iterationMethod = new SimpleIntegerProperty(iterationMethod);
        this.timeMethod = new SimpleLongProperty(timeMethod);
    }

    public String getNameMethod() {
        return nameMethod.get();
    }

    public Integer getCostMethod() {
        return coastMethod.get();
    }

    public String getWayMethod() {
        return routeMethod.get();
    }

    public Integer getIterationMethod() {
        return iterationMethod.get();
    }

    public Long getTimeMethod() {
        return timeMethod.get();
    }
}
