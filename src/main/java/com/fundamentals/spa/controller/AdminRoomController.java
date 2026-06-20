package com.fundamentals.spa.controller;

import com.fundamentals.spa.dto.SpaRoomDto;
import com.fundamentals.spa.entity.enums.RoomStatus;
import com.fundamentals.spa.service.SpaRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.UUID;

@Controller
@RequestMapping("/admin/rooms")
@RequiredArgsConstructor
public class AdminRoomController {
    private final SpaRoomService spaRoomService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rooms", spaRoomService.getAll());
        return "admin/rooms/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        model.addAttribute("roomDto", spaRoomService.getById(id));
        model.addAttribute("statuses", RoomStatus.values());
        return "admin/rooms/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@Valid @ModelAttribute("roomDto") SpaRoomDto roomDto,
                         BindingResult result,
                         @PathVariable UUID id,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", RoomStatus.values());
            return "admin/rooms/form";
        }
        spaRoomService.update(roomDto, id);
        redirectAttributes.addFlashAttribute("success", "Room updated successfully");
        return "redirect:/admin/rooms";
    }
}
