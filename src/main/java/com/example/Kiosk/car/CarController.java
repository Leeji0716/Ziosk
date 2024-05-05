package com.example.Kiosk.car;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequestMapping("/car")
@RequiredArgsConstructor
@Controller
public class CarController {
    private final CarService carService;

    @GetMapping("/create")
    public String carCreate(CarForm carForm){
        return "car_form";
    }

    @PostMapping("/create")
    public String ProductCreate(@Valid CarForm carForm, BindingResult bindingResult){ //폼 바인딩
        if (bindingResult.hasErrors()){
            return "car_form";
        }

        this.carService.create(carForm.getFirstNum(), carForm.getLastNum());
        return "redirect:/";
    }

    @GetMapping("/find")
    public String findCar(Model model, @RequestParam(value = "searchNum", defaultValue = "0000") String searchNum){
        List<Car> carList = this.carService.findList(searchNum);

        model.addAttribute("carList", carList);
        return "car_calculate";
    }

    @GetMapping("/calculate/{id}")
    public String calculateCar(@PathVariable("id") int id, Model model){
        Car car = this.carService.findCar(id);
        if (car.getOutTime() == null){
            carService.calculate(car);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String formattedOutTime = car.getOutTime().format(formatter);
            model.addAttribute("outTime", formattedOutTime);

        }else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String formattedOutTime = car.getOutTime().format(formatter);
            model.addAttribute("outTime", "이미 " + formattedOutTime);
        }
        return "calculate_success";
    }
}
