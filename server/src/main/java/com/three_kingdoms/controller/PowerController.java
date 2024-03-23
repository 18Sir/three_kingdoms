package com.three_kingdoms.controller;

import com.three_kingdoms.domain.Power;
import com.three_kingdoms.services.impl.PowerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/powers")
public class PowerController {
    @Autowired
    private PowerServiceImpl powerService;

    @GetMapping("")
    public Result getPowerList(){
        List<Power> powers = powerService.findPowerAll();
        if(powers.size() > 0){
            return Result.selectSuccess(powers);
        }else{
            return Result.selectError();
        }
    }

    @GetMapping("/{pid}")
    public Result getPowerById(@PathVariable Long pid){
        Power power = powerService.findPowerById(pid);
        if(power != null){
            return Result.selectSuccess(power);
        }else{
            return Result.selectError();
        }
    }

    @DeleteMapping("/{pid}")
    public Result delPower(@PathVariable Long pid){
        Integer i = powerService.delPower(pid);
        if(i > 0){
            return Result.deleteSuccess();
        }else{
            return Result.deleteError();
        }
    }

    @PostMapping("")
    public Result savePower(@RequestBody Power power){
        Integer i = powerService.savePower(power);
        if(i > 0){
            return Result.saveSuccess();
        }else{
            return Result.saveError();
        }
    }

    @PutMapping("")
    public Result updatePower(@RequestBody Power power){
        Integer i = powerService.updatePower(power);
        if(i > 0){
            return Result.updateSuccess();
        }else{
            return Result.updateError();
        }
    }

}
