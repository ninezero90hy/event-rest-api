package com.example.demorestapi.excel.style.custom;

import com.example.demorestapi.excel.style.CustomExcelCellStyle;
import com.example.demorestapi.excel.style.align.DefaultExcelAlign;
import com.example.demorestapi.excel.style.border.DefaultExcelBorders;
import com.example.demorestapi.excel.style.border.ExcelBorderStyle;
import com.example.demorestapi.excel.style.configurer.ExcelCellStyleConfigurer;

public class BlueHeaderStyle extends CustomExcelCellStyle {

	@Override
	public void configure(ExcelCellStyleConfigurer configurer) {

		configurer.foregroundColor(223, 225, 246)
				.excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
				.excelAlign(DefaultExcelAlign.CENTER_CENTER)
		;
	}
}
