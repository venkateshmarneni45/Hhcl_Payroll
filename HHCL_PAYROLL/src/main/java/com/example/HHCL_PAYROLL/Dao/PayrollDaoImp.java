package com.example.HHCL_PAYROLL.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PayrollDaoImp implements PayrollDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public ResultSet generateLTAReport(int yearmonth) {
		try {
			final StringBuffer sql = new StringBuffer();
			sql.append(" SELECT A.EMPLOYEESEQUENCENO Code,A.CALLNAME EmpName, ");
			sql.append(
					" DATE_FORMAT(B.FROM_DATE,'%d.%m.%Y') as From_Period ,DATE_FORMAT(B.TODATE,'%d.%m.%Y') as Period_To, ");
			sql.append(
					" DATE_FORMAT(STR_TO_DATE(B.SNO,'%Y%m'),'%m/%Y') as Pay_Month ,CONSIDER_LTA as Amt,C.SHORTNAME as Division, ");
			sql.append(" B.COMMENT as Remarks ");
			sql.append(" FROM HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY A ");
			sql.append(" JOIN HCLHRM_PROD_OTHERS.TBL_MUM_LTA B ON A.EMPLOYEESEQUENCENO=B.EMPLOYEESEQUENCENO ");
			sql.append(" LEFT JOIN HCLADM_PROD.TBL_BUSINESSUNIT C ON A.COMPANYID=C.BUSINESSUNITID ");
			sql.append(" where b.sno=?  and b.status=1001; ");

			PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
					.prepareStatement(sql.toString());
			preparedStatement.setInt(1, yearmonth);
			return preparedStatement.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ResultSet getEmpSeqNo_EmpId(List<Integer> employeeSequenceNos) {
		try {
			final StringBuffer sql = new StringBuffer();
			sql.append(" SELECT EMPLOYEESEQUENCENO,EMPLOYEEID FROM ");
			sql.append(" HCLHRM_PROD.TBL_EMPLOYEE_PRIMARY WHERE EMPLOYEESEQUENCENO IN (");
			for (int i = 0; i < employeeSequenceNos.size(); i++) {
				@SuppressWarnings("unused")
				StringBuffer latest = (i < employeeSequenceNos.size() - 1) ? sql.append("?,") : sql.append("?)");
			}
			System.err.println(sql);
			PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
					.prepareStatement(sql.toString());
			for (int i = 1; i <= employeeSequenceNos.size(); i++) {
				preparedStatement.setInt(i, employeeSequenceNos.get(i - 1));
			}
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int addEmployeesToHrActions() {
		try {
			final StringBuffer sql = new StringBuffer();
			sql.append(" LOAD DATA LOCAL INFILE 'C:\\Users\\venkateswarlu.marnen\\Desktop\\ "
					+ LocalDate.now().getMonth().toString().substring(0, 3) + "'"
					+ LocalDate.now().getYear() + "_LEFT_EMP_UPDATION" + ".csv'");
			sql.append(" INTO TABLE hclhrm_prod.tbl_employee_hractions ");
			sql.append(" FIELDS TERMINATED BY ',' ENCLOSED BY '\"' ");
			sql.append(" LINES TERMINATED BY '\\r\\n' ");
			sql.append(" IGNORE 1 LINES; ");

			return jdbcTemplate.update(sql.toString());
		}catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public int updateEmpStatus(List<Integer> employeeSequenceNos) {
		try {
			final StringBuffer sql=new StringBuffer();
			sql.append("update hclhrm_prod.tbl_employee_primary set status=1061 where employeesequenceno in(");
			for (int i = 0; i < employeeSequenceNos.size(); i++) {
				@SuppressWarnings("unused")
				StringBuffer latest = (i < employeeSequenceNos.size() - 1) ? sql.append("?,") : sql.append("?)");
			}
			System.err.println(sql);
			PreparedStatement preparedStatement = jdbcTemplate.getDataSource().getConnection()
					.prepareStatement(sql.toString());
			for (int i = 1; i <= employeeSequenceNos.size(); i++) {
				preparedStatement.setInt(i, employeeSequenceNos.get(i - 1));
			}
			return preparedStatement.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
