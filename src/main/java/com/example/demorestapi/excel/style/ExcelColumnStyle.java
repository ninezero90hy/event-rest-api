package com.example.demorestapi.excel.style;



public @interface ExcelColumnStyle {

	/**
	 * Enum implements {@link com.example.demorestapi.excel.style}
	 * Also, can use just class.
	 * If not use Enum, enumName will be ignored
	 * @see com.example.demorestapi.excel.style.DefaultExcelCellStyle
	 * @see com.example.demorestapi.excel.style.CustomExcelCellStyle
	 */
	Class<? extends ExcelCellStyle> excelCellStyleClass();

	/**
	 * name of Enum implements {@link com.example.demorestapi.excel.style.ExcelCellStyle}
	 * if not use Enum, enumName will be ignored
	 */
	String enumName() default "";

}
