package com.seeme.domain.covid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CovidRegionalResDto {
    private final Integer newCoronic;
    private final Integer totalCoronic;
    private final List<CoronicList> coronicList;
}

@Data
public class CoronicList {
    private final String day;
    private final Integer coronic;
}