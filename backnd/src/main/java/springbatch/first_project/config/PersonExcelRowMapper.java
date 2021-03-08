package springbatch.first_project.config;

import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import springbatch.first_project.entity.Person;

public class PersonExcelRowMapper implements RowMapper<Person> {

	@Override
	public Person mapRow(RowSet rowSet) throws Exception {
		Person person = new Person();

		person.setName(rowSet.getColumnValue(1));
		person.setBirthday(rowSet.getColumnValue(2));
		person.setEmail(rowSet.getColumnValue(3));
		person.setRevenue(NumberFormat.getNumberInstance(Locale.UK).parse(rowSet.getColumnValue(4)).intValue());
		person.setCustomer(isCustomer(rowSet.getColumnValue(5)));

		return person;
	}

	public boolean isCustomer(String s) {
		if (s.equals("1.0")) {
			return true;
		} else {
			return false;

		}
	}
}