package com.example.demorestapi.car;

import com.example.demorestapi.excel.ExcelColumn;
import com.example.demorestapi.excel.style.DefaultHeaderStyle;
import com.example.demorestapi.excel.style.ExcelColumnStyle;
import com.example.demorestapi.excel.style.NoExcelCellStyle;
import com.example.demorestapi.excel.style.custom.BlueHeaderStyle;
import com.example.demorestapi.excel.style.custom.GrayHeaderStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@DefaultHeaderStyle(style = @ExcelColumnStyle(excelCellStyleClass = GrayHeaderStyle.class))
public class CarExcelDto {

    @ExcelColumn(
        headerName = "회사",
        headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class)
    )
    private String company;

    @ExcelColumn(
        headerName = "차종",
        headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class))
    private String name;

    @ExcelColumn(
        headerName = "가격",
        headerStyle = @ExcelColumnStyle(excelCellStyleClass = BlueHeaderStyle.class)
    )
    private int price;

    @ExcelColumn(headerName = "평점")
    private double rating;

}
