package com.three_kingdoms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.three_kingdoms.domain.Addr;
import com.three_kingdoms.services.impl.AddrServicesImpl;
import com.three_kingdoms.util.Verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addrs")
public class AddrController {
    @Autowired
    private AddrServicesImpl addrServices;
    @Autowired
    private Verify verify;

    //查询全部
    @GetMapping
    public Result findAll( @RequestParam Long current,
                            @RequestParam Long size) {
        Page p = new Page(current,size);
        IPage page = addrServices.findAll(p);
        if (page.getRecords() != null) {
            return Result.selectSuccess(page);
        } else {
            return Result.selectError();
        }
    }

    //按id查询地点详细信息
    @GetMapping("/{addrId}")
    public Result findById(@PathVariable Long addrId) {
        Addr addr = addrServices.findById(addrId);
        if (addr != null) {
            return Result.selectSuccess(addr);
        } else {
            return Result.selectError();
        }
    }

    //增加地点
    @PostMapping
    public Result save(@RequestHeader(name = "Authorization") String token, @RequestBody Addr addr) {
        if (verify.isAdmin(token)) {
            Integer i = addrServices.save(addr);
            if (i > 0) {
                return Result.saveSuccess();
            } else {
                return Result.saveError();
            }
        } else {
            return Result.error(ResultCode.SAVE_ERR, "无权操作");
        }

    }

    //修改地点
    @PutMapping
    public Result update(@RequestHeader(name = "Authorization") String token, @RequestBody Addr addr) {
        if (verify.isAdmin(token)) {
            Integer i = addrServices.update(addr);
            if (i > 0) {
                return Result.updateSuccess();
            } else {
                return Result.updateError();
            }
        } else {
            return Result.error(ResultCode.UPDATE_ERR, "无权操作");
        }
    }

    //删除地点
    @DeleteMapping("/{addrId}")
    public Result delete(@RequestHeader(name = "Authorization") String token, @PathVariable Long addrId) {
        if (verify.isAdmin(token)) {
            Integer i = addrServices.delete(addrId);
            if (i > 0) {
                return Result.deleteSuccess();
            } else {
                return Result.deleteError();
            }
        } else {
            return Result.error(ResultCode.DELETE_ERR, "无权操作");
        }
    }

}
