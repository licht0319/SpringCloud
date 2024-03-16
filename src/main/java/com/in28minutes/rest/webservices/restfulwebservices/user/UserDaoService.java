package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	
	private static List<User> users = new ArrayList<>();
	private static Integer userCount = 0;
	
	static {
		users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(25)));
		users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(20)));
	}
	
	public List<User> findAll(){
		return users;
	}
	
	public User findById(Integer id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		return users.stream().filter(predicate).findFirst().orElse(null);
//        Optional<User> userOptional = users.stream()
//                .filter(user -> user.getId() == id)
//                .findFirst();
//        return userOptional.orElse(null);
    }
	
	public User save(User user){
		user.setId(++userCount);
		users.add(user);
		return user;
	}
	
	public void deleteById(Integer id) {
		Predicate<? super User> predicate = user -> user.getId().equals(id);
		users.removeIf(predicate);
    }
}
