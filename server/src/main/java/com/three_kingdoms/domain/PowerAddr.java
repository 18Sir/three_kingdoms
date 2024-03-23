package com.three_kingdoms.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerAddr {
    private List<String> addr;
    private Integer type;
}
