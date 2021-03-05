package springbatch.first_project.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import springbatch.first_project.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>{

	


}
