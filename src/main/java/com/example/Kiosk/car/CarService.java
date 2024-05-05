package com.example.Kiosk.car;

import com.example.Kiosk.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CarService {
    private final CarRepository carRepository;

    public String create(String firstNum, String lastNum){
        Car car = new Car();
        car.setFirstNum(firstNum);
        car.setLastNum(lastNum);
        car.setInTime(LocalDateTime.now());

        this.carRepository.save(car);

        return "redirect:/";
    }

    public List<Car> getList(){
        List<Car> carList = this.carRepository.findAll();

        return carList;
    }

    public List<Car> findList(String searchNum){
        List<Car> carList = this.carRepository.findAll();
        List<Car> findList = new ArrayList<>();

        for (Car car : carList){
            if (car.getLastNum().equals(searchNum)){
                findList.add(car);
            }
        }
        return findList;
    }

    public Car findCar(int id){
        Car car = this.carRepository.findById(id).get();

        return car;
    }

    public void calculate(Car car){
        LocalDateTime inTime = car.getInTime();
        LocalDateTime outTime = inTime.plusHours(1);

        car.setOutTime(outTime);
        this.carRepository.save(car);
    }
}
