package com.example.HHCL_PAYROLL.Controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.HHCL_PAYROLL.Service.PayrollService;

@RestController
@RequestMapping("/payroll")
public class PayrollController {
	@Autowired
	private PayrollService payrollService;

	@GetMapping(value = "/LTAReport/{yearmonth}", produces = "application/json")
	public ResponseEntity<String> generateLTAReport(@PathVariable("yearmonth") int yearmonth) {
		return payrollService.generateLTAReport(yearmonth);
	}

	@PutMapping(value = "/leftEmpReport", produces = "application/json")
	public ResponseEntity<String> updateLeftEmployees() throws SQLException {
		return payrollService.updateLeftEmployees();
	}
}
