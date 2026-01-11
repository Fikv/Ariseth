package fikv.ariseth.services;

import java.util.List;

import fikv.ariseth.entities.User;
import fikv.ariseth.records.UserRequestDTO;

public interface UserService {

	User create(UserRequestDTO user);

	User getById(Long id);

	List<User> getAll();

	User update(Long id, User user);

	void delete(Long id);
}
