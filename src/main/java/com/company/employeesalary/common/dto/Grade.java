package com.company.employeesalary.common.dto;

public enum Grade {
    GRADE_1(1),
    GRADE_2(2),
    GRADE_3(3),
    GRADE_4(4),
    GRADE_5(5),
    GRADE_6(6);

    private final int level;

    Grade(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getStepsFromBase() {
        return GRADE_6.level - this.level;
    }
}
