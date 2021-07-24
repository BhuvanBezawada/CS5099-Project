package main;

import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.numbers.IntColumnType;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.BoxPlot;
import tech.tablesaw.plotly.api.Histogram;
import tech.tablesaw.plotly.api.Histogram2D;
import tech.tablesaw.plotly.api.VerticalBarPlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Marker;
import tech.tablesaw.plotly.traces.HistogramTrace;

import java.util.List;
import java.util.stream.IntStream;

public class Vis {
    public static void main(String[] args) {
        Table dataTable = Table.create("dataTable");
        StringColumn grades = StringColumn.create("grades");
        IntColumn nums = IntColumn.create("num");

        grades.append("1.0");
        grades.append("2.0");
        grades.append("3.0");
        grades.append("4.0");
        grades.append("5.0");

        nums.append(5);
        nums.append(6);
        nums.append(7);
        nums.append(8);
        nums.append(9);

        dataTable.addColumns(grades, nums);

        System.out.println(dataTable.columns());

        Plot.show(VerticalBarPlot.create("Grade distribution", dataTable, "grades", "num"));
    }

    public static void createBarChart(List<Integer> gradeValues) {
        Table dataTable = Table.create("dataTable");

        StringColumn gradesLabels = StringColumn.create("gradeLabels");
        for (double i = 0.0; i <= 20.0; i+= 0.5) {
            gradesLabels.append(String.valueOf(i));
        }

        DoubleColumn grades = DoubleColumn.create("gradeValues", gradeValues);

        dataTable.addColumns(gradesLabels, grades);

        Plot.show(VerticalBarPlot.create("Grade distribution", dataTable, "gradeLabels", "gradeValues"));
    }
}
