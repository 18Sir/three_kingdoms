package com.three_kingdoms.controller;

import com.three_kingdoms.domain.Power;
import com.three_kingdoms.services.PowerService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/powers")
@Slf4j
public class PowerController {
    @Resource
    private PowerService powerService;

    @GetMapping("")
    public Result getPowerList() {
        return Result.selectSuccess(powerService.findPowerAll());
    }

    @GetMapping("/{pid}")
    public Result getPowerById(@PathVariable Long pid) {
        Power power = powerService.findPowerById(pid);
        if (power != null) {
            return Result.selectSuccess(power);
        } else {
            return Result.selectError();
        }
    }

    @DeleteMapping("/{pid}")
    public Result delPower(@PathVariable Long pid) {
        Integer i = powerService.delPower(pid);
        if (i > 0) {
            return Result.deleteSuccess();
        } else {
            return Result.deleteError();
        }
    }

    @DeleteMapping("/more")
    public Result<Integer> delPowerMore(@RequestBody List<Long> pids) {
        return powerService.delPowerMore(pids);
    }


    @PostMapping("")
    public Result savePower(@RequestBody Power power) {
        log.info("传入的power参数：" + power.toString());
        Integer i = powerService.savePower(power);
        if (i > 0) {
            return Result.saveSuccess("势力添加成功");
        } else {
            return Result.saveError("势力添加失败");
        }
    }

    @PutMapping("")
    public Result updatePower(@RequestBody Power power) {
        Integer i = powerService.updatePower(power);
        if (i > 0) {
            return Result.updateSuccess("势力修改成功");
        } else {
            return Result.updateError("势力修改失败");
        }
    }

}
