//package springbatch.first_project.service;
//
//
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import springbatch.first_project.dao.AnonymizePersonRepository;
//import springbatch.first_project.dao.PersonRepository;
//import springbatch.first_project.entity.AnonymizePerson;
//import springbatch.first_project.entity.Person;
//@Service
//public class AnonymizePersonService {
//
//	@Autowired
//	private AnonymizePersonRepository anonymizePersonRepository;
//	
//	
//	public List<AnonymizePerson> getAllAnonymizePerson() {
//		List<AnonymizePerson> anonymizePersons = anonymizePersonRepository.findAll();
//		return anonymizePersons;
//	}
//	
//	public void saveAllAnonymizePerson(List<AnonymizePerson> anonymizePersons ) {
//		anonymizePersonRepository.saveAll(anonymizePersons);
//	}
//}
