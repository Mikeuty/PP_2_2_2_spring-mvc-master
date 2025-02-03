package web.service;

import org.springframework.stereotype.Service;
import web.model.Car;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private List<Car> cars;

    public CarServiceImpl() {
        cars = new ArrayList<>();
        cars.add(new Car("Toyota", "Red", 2022));
        cars.add(new Car("Honda", "Blue", 2021));
        cars.add(new Car("Nissan", "Silver", 2023));
        cars.add(new Car("Mazda", "Red", 2022));
        cars.add(new Car("Subaru", "Blue", 2023));
    }


    @Override
    public List<Car> getCarsList(int count) {
        if (count >= 5 || count <= 0) {
            return cars;
        }
        return cars.subList(0, count);
    }
}
