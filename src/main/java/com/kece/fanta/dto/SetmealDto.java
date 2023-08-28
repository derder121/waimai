package com.kece.fanta.dto;


import com.kece.fanta.entity.Setmeal;
import com.kece.fanta.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
