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
@RequestMapping("/admin/treatments")
@RequiredArgsConstructor
public class AdminTreatmentController {
    private final SpaTreatmentService spaTreatmentService;

    @GetMapping
    public String allTreatments(Model model){
        List<SpaTreatmentDto> treatmentsList = spaTreatmentService.getAll();
        model.addAttribute("treatments", treatmentsList);
        return "admin/treatments/list";
    }

    @GetMapping("/create")
    public String addTreatmentForm(Model model){
        model.addAttribute("treatmentDto", new SpaTreatmentDto());
        return "admin/treatments/form";
    }

    @PostMapping("/create")
    public String addTreatment(@Valid @ModelAttribute("treatmentDto") SpaTreatmentDto treatmentDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "admin/treatments/form";
        }
        spaTreatmentService.create(treatmentDto);
        redirectAttributes.addFlashAttribute("success", "Treatment created successfully");
        return "redirect:/admin/treatments";
    }

    @GetMapping("/edit/{id}")
    public String editTreatmentForm(@PathVariable UUID id, Model model){
        model.addAttribute("treatmentDto", spaTreatmentService.getById(id));
        return "admin/treatments/form";
    }

    @PostMapping("/edit/{id}")
    public String editTreatment(@Valid @ModelAttribute("treatmentDto") SpaTreatmentDto treatmentDto,
                                BindingResult result, @PathVariable UUID id,
                                RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "admin/treatments/form";
        }
        spaTreatmentService.update(treatmentDto, id);
        redirectAttributes.addFlashAttribute("success", "Treatment updated successfully");
        return "redirect:/admin/treatments";
    }

    @PostMapping("/delete/{id}")
    public String deleteTreatment(@PathVariable UUID id,
                                  RedirectAttributes redirectAttributes){
        try {
            spaTreatmentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Treatment deleted successfully");
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Cound not remove treatment");
        }
        return "redirect:/admin/treatments";
    }

    @PostMapping("/restore/{id}")
    public String restoreTreatment(@PathVariable UUID id,
                                   RedirectAttributes redirectAttributes){
        spaTreatmentService.restore(id);
        redirectAttributes.addFlashAttribute("success", "Treatment restored");
        return "redirect:/admin/treatments";
    }
}
