package springbatch.first_project.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="anonymize_person")
public class AnonymizePerson {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	

	public int id;
	

	public String name;
	

	public String birthday;
	


	public int revenue;
	

	public boolean customer;
	
	
	
	
	public AnonymizePerson() {
		super();
	}
	public AnonymizePerson(String name, String birthday, int revenue, boolean customer) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.revenue = revenue;
		this.customer = customer;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getRevenue() {
		return revenue;
	}
	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}
	public boolean getCustomer() {
		return customer;
	}
	public void setCustomer(boolean customer) {
		this.customer = customer;
	}
	
	
	
}
