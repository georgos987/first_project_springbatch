package springbatch.first_project.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="person")
public class Person {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "id")
	public int id;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "birthday")
	public String birthday;
	
	@Column(name = "email")
	public String email;
	
	@Column(name = "revenue")
	public int revenue;
	
	@Column(name = "customer")
	public boolean customer;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Person(String name, String birthday, String email, int revenue, boolean customer) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.email = email;
		this.revenue = revenue;
		this.customer = customer;
	}
	
	public Person() {
	
	}

	@Override
	public String toString() {
		return "Person [ name=" + name + ", birthday=" + birthday + ", email=" + email + ", revenue="
				+ revenue + ", customer=" + customer + "]";
	}
	
	

	
}
