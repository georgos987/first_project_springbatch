package springbatch.first_project.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import springbatch.first_project.entity.AnonymizePerson;


public interface AnonymizePersonRepository extends JpaRepository<AnonymizePerson, Integer>{

	


}
