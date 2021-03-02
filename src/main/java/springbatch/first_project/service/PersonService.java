//package springbatch.first_project.service;
//
//
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import springbatch.first_project.dao.PersonRepository;
//import springbatch.first_project.entity.Person;
//@Service
//public class PersonService {
//
//	@Autowired
//	private PersonRepository personRepository;
//	
//	
//	public List<Person> getAllPerson() {
//		List<Person> persons = personRepository.findAll();
//		return persons;
//	}
//	
//	public void saveAllPerson(List<Person> persons ) {
//		personRepository.saveAll(persons);
//	}
//}
