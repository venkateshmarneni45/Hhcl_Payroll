package com.example.HHCL_PAYROLL.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.HHCL_PAYROLL.Dao.PayrollDao;
import com.example.HHCL_PAYROLL.Model.LeftEmployeeData;

@Service
public class PayrollService {
	@Autowired
	private PayrollDao payrollDao;

	public ResponseEntity<String> generateLTAReport(int yearmonth) {
		try {
			ResultSet rs = payrollDao.generateLTAReport(yearmonth);
			ResultSetMetaData rsmd = rs.getMetaData();
			int rowNum = 0;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("LTA_REPORT");

			CellStyle headerRowStyle = workbook.createCellStyle();
			HSSFFont headerRowFont = workbook.createFont();
			headerRowFont.setFontHeightInPoints((short) 11);
			headerRowFont.setFontName("Calibri");
			headerRowFont.setBold(true);
			headerRowStyle.setFont(headerRowFont);

			HSSFRow headerRow = sheet.createRow(rowNum++);
			for (int j = 1; j <= rsmd.getColumnCount(); j++) {
				HSSFCell cell = headerRow.createCell(j - 1);
				cell.setCellValue(rsmd.getColumnLabel(j));
				cell.setCellStyle(headerRowStyle);
			}

			CellStyle dataRowStyle = workbook.createCellStyle();
			HSSFFont dataRowFont = workbook.createFont();
			dataRowFont.setFontHeightInPoints((short) 11);
			dataRowFont.setFontName("Calibri");
			dataRowStyle.setFont(dataRowFont);

			while (rs.next()) {
				HSSFRow dataRow = sheet.createRow(rowNum++);
				for (int j = 1; j <= rsmd.getColumnCount(); j++) {
					HSSFCell cell = dataRow.createCell(j - 1);
					cell.setCellValue(rs.getString(j));
					cell.setCellStyle(dataRowStyle);
				}
			}

			String filePath = "C:\\PAYROLL\\PAYROLL FOR MUMBAI\\LTA\\" + yearmonth + "_LTA_REPORT" + ".csv";
			FileOutputStream fileOut = new FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();
			return new ResponseEntity<String>("REPORT EXPORTED", HttpStatus.OK);

		} catch (SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("EXCEPTION DURING DATA RETRIEVAL", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("REPORT AVAILABLE IN THE PATH", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("EMPTY RESULTSET", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("REPORT NOT EXPORTED", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<String> updateLeftEmployees() {
		try {
			Map<Integer, String> empNo_Lwd = new LinkedHashMap<>();
			FileInputStream inputStream = new FileInputStream(
					"C:\\Users\\venkateswarlu.marnen\\Desktop\\leftemployee.xls");
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(1);
			for (Row row : sheet) {
				empNo_Lwd.put((int) (row.getCell(0).getNumericCellValue()),
						new SimpleDateFormat("yyyy-MM-dd").format(row.getCell(1).getDateCellValue()));
			}
			System.err.println(empNo_Lwd);

			List<Integer> employeeSequenceNos = new ArrayList<>(empNo_Lwd.keySet());
			ResultSet empSequenceNo_empId = payrollDao.getEmpSeqNo_EmpId(employeeSequenceNos);

			Map<Integer, Integer> empNo_empId = new LinkedHashMap<>();
			while (empSequenceNo_empId.next()) {
				empNo_empId.put(empSequenceNo_empId.getInt(1), empSequenceNo_empId.getInt(2));
			}

			String[] headerRowData = { "EMPLOYEEID", "DOCUMENTTYPEID", "DOCUMENTNO", "DOCUMENTDATE", "ACTIONBY",
					"ACTIONDATE", "NOTES", "LASTWORKINGDATE", "DOCSTATUS", "LOGID", "LUPDATE" };

			Map<Integer, LeftEmployeeData> final_data = new LinkedHashMap<>();
			for (int employeeSequenceNo : employeeSequenceNos) {
				final_data.put(employeeSequenceNo,
						new LeftEmployeeData(empNo_empId.get(employeeSequenceNo), 1, 1,
								LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), 12699,
								LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), "RESIGNED",
								empNo_Lwd.get(employeeSequenceNo), 1001, 12699,
								LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));
			}

			int rowNum = 0;
			HSSFWorkbook final_workbook = new HSSFWorkbook();
			HSSFSheet final_sheet = final_workbook
					.createSheet(LocalDate.now().getMonth() + "'" + LocalDate.now().getYear());

			Row headerRow = final_sheet.createRow(rowNum++);
			for (int i = 0; i < headerRowData.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headerRowData[i]);
			}

			for (int employeeSequenceNo : employeeSequenceNos) {
				Row dataRow = final_sheet.createRow(rowNum++);
				LeftEmployeeData employee = final_data.get(employeeSequenceNo);
				for (int i = 0; i < LeftEmployeeData.class.getDeclaredFields().length; i++) {
					Cell cell = dataRow.createCell(i);
					switch (i) {
					case 0:
						cell.setCellValue(employee.getEmployeeId());
						break;
					case 1:
						cell.setCellValue(employee.getDocumentTypeId());
						break;
					case 2:
						cell.setCellValue(employee.getDocumentNo());
						break;
					case 3:
						cell.setCellValue(employee.getDocumentDate());
						break;
					case 4:
						cell.setCellValue(employee.getActionBy());
						break;
					case 5:
						cell.setCellValue(employee.getActionDate());
						break;
					case 6:
						cell.setCellValue(employee.getNotes());
						break;
					case 7:
						cell.setCellValue(employee.getLastWorkingDate());
						break;
					case 8:
						cell.setCellValue(employee.getDocStatus());
						break;
					case 9:
						cell.setCellValue(employee.getLogId());
						break;
					case 10:
						cell.setCellValue(employee.getLupDate());
						break;
					}
				}
			}
			for (int i = 0; i < headerRowData.length; i++) {
				final_sheet.autoSizeColumn(i);
			}

			String filePath = "C:\\Users\\venkateswarlu.marnen\\Desktop\\"
					+ LocalDate.now().getMonth().toString().substring(0, 3) + "'" + LocalDate.now().getYear()
					+ "_LEFT_EMP_UPDATION" + ".csv";
			FileOutputStream fileOut = new FileOutputStream(filePath);
			final_workbook.write(fileOut);
			fileOut.close();
			final_workbook.close();
			inputStream.close();
			workbook.close();

			if(payrollDao.addEmployeesToHrActions()>0) {
				payrollDao.updateEmpStatus(employeeSequenceNos);
				return new ResponseEntity<String>("Data added to hr cations and updated in primary table", HttpStatus.OK);
			}
			else {
				return new ResponseEntity<String>("Data Not updated", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("FILE NOT FOUND IN THE PATH SPECIFIED", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("REPORT AVAILABLE IN THE PATH", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("EXCEPTION DURING DATA RETRIEVAL", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (NullPointerException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("EMPTY RESULT SET", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void generateLateHoursReport(){
		
	}
}
