package com.kkuk.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.SiteUser;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<SiteUser, Long>{

	public Optional<SiteUser> findByUsername(String username);
	
}
