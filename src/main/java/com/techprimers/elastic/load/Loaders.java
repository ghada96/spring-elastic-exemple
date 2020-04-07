package com.techprimers.elastic.load;

import com.techprimers.elastic.jparepository.UserJpaRepository;
import com.techprimers.elastic.model.Users;
import com.techprimers.elastic.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class Loaders {

    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    @PostConstruct
    @Transactional
    public void loadAll(){

        operations.putMapping(Users.class);
        System.out.println("Loading Data");
        List<Users> data = getData();
        userJpaRepository.save(data); //saves to H2 DB

        List<Users> usersList = userJpaRepository.findAll(); //Get from H2 DB
        usersRepository.save(usersList); //loads into Elastic
        System.out.printf("Loading Completed");

    }

    private List<Users> getData() {
        List<Users> userses = new ArrayList<>();
        userses.add(new Users("ghada",123L, "dev", 12000L));
        userses.add(new Users("amri",1234L, "prod", 22000L));
        userses.add(new Users("ghaaada",1235L, "test", 12000L));
        return userses;
    }
}
