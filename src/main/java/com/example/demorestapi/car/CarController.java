package com.example.demorestapi.car;

import com.example.demorestapi.excel.ExcelFile;
import com.example.demorestapi.excel.OneSheetExcelFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CarController {

    @GetMapping("/api/v1/car/excel")
    public void downloadCarInfo2(HttpServletResponse response) throws IOException {
        List<CarExcelDto> carList = getCarList();
        ExcelFile<CarExcelDto> excelFile = new OneSheetExcelFile<>(carList, CarExcelDto.class);

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

        excelFile.write(response.getOutputStream());
    }



    private List<CarExcelDto> getCarList() {
        List carList = new ArrayList<>();
        // dummy data
        CarExcelDto data = new CarExcelDto();
        data.setCompany("현대");
        data.setName("소나타");
        data.setPrice(100000);
        data.setRating(4.8);
        carList.add(data);

        data = new CarExcelDto();
        data.setCompany("르노삼성");
        data.setName("QM6");
        data.setPrice(150000);
        data.setRating(4.5);
        carList.add(data);

        data = new CarExcelDto();
        data.setCompany("기아");
        data.setName("K5");
        data.setPrice(130000);
        data.setRating(4.6);
        carList.add(data);

        return carList;
    }

}
