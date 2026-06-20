package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaTreatmentDto;
import com.fundamentals.spa.entity.SpaTreatment;
import com.fundamentals.spa.service.SpaTreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final SpaTreatmentService spaTreatmentService;

    @GetMapping
    public String index(Model model){
        List<SpaTreatmentDto> treatmentsList = spaTreatmentService.getAllNotDeleted();
        model.addAttribute("treatments", treatmentsList);
        return "index";
    }
}
