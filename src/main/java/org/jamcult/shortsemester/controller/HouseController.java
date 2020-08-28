package org.jamcult.shortsemester.controller;

import org.jamcult.shortsemester.model.House;
import org.jamcult.shortsemester.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller //TODO Integerate with View & Validation
@RequestMapping("/house")
public class HouseController {
    @Autowired
    private HouseRepository repository;

    @GetMapping(path = {"/", ""})
    public String get(Model model) {
        Iterable<House> houses = repository.findAll();
        model.addAttribute("houses", houses);
        return "house-index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {
        Optional<House> house = repository.findById(id);
        if (house.isPresent()) {
            model.addAttribute(house);
            return "house-detail";
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = {"/", ""})
    @ResponseStatus(HttpStatus.CREATED)
    public String create(House house) {
        repository.save(house);
        return "house-index";
    }

    @GetMapping("/create")
    public String createPage() {
        return "house-create";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable int id, Model model) {
        Optional<House> house = repository.findById(id);
        if (house.isPresent()) {
            model.addAttribute(house);
            return "house-create";
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}")
    public String update(@PathVariable int id, House updatedHouse) {
        Optional<House> house = repository.findById(id);
        if (house.isPresent() && id == updatedHouse.getId()) {
            System.out.println(updatedHouse);
            repository.save(updatedHouse);
            return "house-index";
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        try {
            repository.deleteById(id);
            return "house-index";
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
