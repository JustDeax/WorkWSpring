package com.justdeax.WorkXSpring;
import org.apache.catalina.Engine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

@SuppressWarnings("ALL") //Аннотация игнорирует все не используемые фрагменты
@Configuration
public class AutowiredAndBean {
    Engine engine;
    Integer wheal;
    String string;

    @Autowired //Spring автоматически внедрит бин Engine в поле engine.
    AutowiredAndBean(Engine engine, Integer wheal, String string) {
        this.engine = engine;
        this.wheal = wheal;
        this.string = string;
    }

    @Bean("wheal1") //Должен находится в классе с configuration можно внедрить в другие место, удобно
    Integer wheal() {
        return 10;
    }
}

interface Vehicle {}

@Primary
@SuppressWarnings("ALL")
class Bike implements Vehicle {
    Vehicle vehicle;

    @Autowired
    void setVehicle(@Qualifier("bike") Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

@SuppressWarnings("ALL")
class Car implements Vehicle {
    Vehicle vehicle;
    int cylinderCount;
    String color;

    @Autowired //          \/ Выбрать Vehicle из Car
    void setVehicle(@Qualifier("car") Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Autowired //          \/ По умолчанию значение "8"
    void setCylinderCount(@Value("8") int cylinderCount) {
        this.cylinderCount = cylinderCount;
    }

    @Value("${red}") //Из application properties
    void setColor(String color) {
        this.color = color;
    }
}

class Driver {
    @Autowired
    Vehicle vehicle; //Так как Bike имеет аннотацию Primary, он будет использован по умолчанию

}


