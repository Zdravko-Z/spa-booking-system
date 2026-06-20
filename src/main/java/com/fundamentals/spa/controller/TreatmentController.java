package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaTreatmentDto;
import com.fundamentals.spa.service.SpaTreatmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/treatments")
@RequiredArgsConstructor
public class TreatmentController {
    private final SpaTreatmentService spaTreatmentService;

    @GetMapping
    public String allTreatments(Model model){
        List<SpaTreatmentDto> treatmentsList = spaTreatmentService.getAll();
        model.addAttribute("treatments", treatmentsList);
        return "treatments";
    }

    @GetMapping("/create")
    public String addTreatmentForm(Model model){
        model.addAttribute("treatmentDto", new SpaTreatmentDto());
        return "treatments/form";
    }

    @PostMapping("/create")
    public String addTreatment(@Valid @ModelAttribute("treatmentDto") SpaTreatmentDto treatmentDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "treatments/form";
        }
        spaTreatmentService.create(treatmentDto);
        redirectAttributes.addFlashAttribute("success", "Treatment created successfully");
        return "redirect:/treatments";
    }

    @GetMapping("/edit/{id}")
    public String editTreatmentForm(@PathVariable UUID id, Model model){
        model.addAttribute("treatmentDto", spaTreatmentService.getById(id));
        return "treatments/form";
    }

    @PostMapping("/edit/{id}")
    public String editTreatment(@Valid @ModelAttribute("treatmentDto") SpaTreatmentDto treatmentDto,
                                BindingResult result, @PathVariable UUID id,
                                RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "treatments/form";
        }
        spaTreatmentService.update(treatmentDto, id);
        redirectAttributes.addFlashAttribute("success", "Treatment updated successfully");
        return "redirect:/treatments";
    }

    @PostMapping("/delete/{id}")
    public String deleteTreatment(@PathVariable UUID id,
                                  RedirectAttributes redirectAttributes){
        spaTreatmentService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Treatment deleted successfully");
        return "redirect:/treatments";
    }
}
