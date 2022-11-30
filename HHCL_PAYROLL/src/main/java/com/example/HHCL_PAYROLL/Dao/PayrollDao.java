package com.example.HHCL_PAYROLL.Dao;

import java.sql.ResultSet;
import java.util.List;

public interface PayrollDao {
	public ResultSet generateLTAReport(int yearmonth);
	public ResultSet getEmpSeqNo_EmpId(List<Integer> employeeSequenceNos);
	public int addEmployeesToHrActions();
	public int updateEmpStatus(List<Integer> employeeSequenceNos);
}
