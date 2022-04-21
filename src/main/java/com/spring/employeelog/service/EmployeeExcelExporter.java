package com.spring.employeelog.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.spring.employeelog.model.EmployeeEntity;







public class EmployeeExcelExporter {

	private XSSFWorkbook theWorkBook;
	private XSSFSheet theSheet;
	
	private List<EmployeeEntity> listEmployee;
	
	public EmployeeExcelExporter(List<EmployeeEntity> listStudent) {
		this.listEmployee = listStudent;
		theWorkBook = new XSSFWorkbook();
		theSheet = theWorkBook.createSheet("Employees");
	}

	public void writeHeaderRow() {
		Row theRow = theSheet.createRow(0);
		
		Cell theCell = theRow.createCell(0);
		theCell.setCellValue("First Name");
		
		theCell = theRow.createCell(1);
		theCell.setCellValue("Last Name");
		
		theCell = theRow.createCell(2);
		theCell.setCellValue("Email");
		
		/*
		 * theCell = theRow.createCell(3); theCell.setCellValue("ID");
		 */
	}
	
	public void writeDataRow() {
		int rowCount = 1;
		for(EmployeeEntity theStudent : listEmployee) {
			Row theRow = theSheet.createRow(rowCount++);
			
			Cell theCell = theRow.createCell(0);
			theCell.setCellValue(theStudent.getFirstName());
			
			theCell = theRow.createCell(1);
			theCell.setCellValue(theStudent.getLastName());
			
			theCell = theRow.createCell(2);
			theCell.setCellValue(theStudent.getEmail());
			
			/*
			 * theCell = theRow.createCell(3); theCell.setCellValue(theStudent.getRoll());
			 */
		}
	}
	
	public void export(HttpServletResponse response) throws IOException {
		writeHeaderRow();
		writeDataRow();
		
		ServletOutputStream theOutputStream = response.getOutputStream();
		theWorkBook.write(theOutputStream);
		theWorkBook.close();
		theOutputStream.close();
	}
	
}
