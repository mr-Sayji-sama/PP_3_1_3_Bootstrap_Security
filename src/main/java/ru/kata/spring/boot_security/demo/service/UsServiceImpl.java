package ru.kata.spring.boot_security.demo.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.Model.Role;
import ru.kata.spring.boot_security.demo.Model.User;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class UsServiceImpl implements UsService {
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(new HashSet<>(roleRepository.findAll()));
		userRepository.save(user);
	}

	@Override
	public Optional<User> findUserByHisName(String name) {
		return em.createQuery("select u from User u where u.username =:name", User.class)
				.setParameter("name", name)
				.getResultStream().findAny();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return em.createQuery("select u from User u where u.email =:email", User.class)
				.setParameter("email", email)
				.getResultStream().findAny();
	}
}
