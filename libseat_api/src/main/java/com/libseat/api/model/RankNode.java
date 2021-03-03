package com.libseat.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RankNode {
    private Integer id;
    private Double time;
    private Integer rank;
}
