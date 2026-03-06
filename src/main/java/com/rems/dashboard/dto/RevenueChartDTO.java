package com.rems.dashboard.dto;

import java.util.List;

public class RevenueChartDTO {

    private List<RevenuePointDTO> points;

    public List<RevenuePointDTO> getPoints() {
        return points;
    }

    public void setPoints(List<RevenuePointDTO> points) {
        this.points = points;
    }
}
