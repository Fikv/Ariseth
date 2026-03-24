package fikv.ariseth.controllers;

import org.hibernate.query.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/planner")
public class Planner {

    ResponseEntity<Page> getTasksForAPeriod() {


        return
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createTask() {

    }

}
