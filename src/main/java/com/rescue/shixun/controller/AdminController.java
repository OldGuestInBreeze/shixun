package com.rescue.shixun.controller;

import com.rescue.shixun.config.PasswordUtil;
import com.rescue.shixun.model.AdminUser;
import com.rescue.shixun.model.Animal;
import com.rescue.shixun.model.RescueCase;
import com.rescue.shixun.repository.AdminUserRepository;
import com.rescue.shixun.repository.AdoptionApplicationRepository;
import com.rescue.shixun.repository.AnimalRepository;
import com.rescue.shixun.repository.DonationRepository;
import com.rescue.shixun.repository.RescueCaseRepository;
import com.rescue.shixun.repository.VolunteerRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private static final String APPLICATION_PENDING = "待审核";
    private static final String APPLICATION_APPROVED = "已通过";
    private static final String APPLICATION_REJECTED = "已拒绝";
    private static final String ANIMAL_WAITING = "待领养";
    private static final String ANIMAL_ADOPTED = "已领养";
    private static final Set<String> APPLICATION_STATUSES = new HashSet<>(
            Arrays.asList(APPLICATION_PENDING, APPLICATION_APPROVED, APPLICATION_REJECTED));

    private final AdminUserRepository users;
    private final AnimalRepository animals;
    private final RescueCaseRepository cases;
    private final AdoptionApplicationRepository applications;
    private final VolunteerRepository volunteers;
    private final DonationRepository donations;

    public AdminController(AdminUserRepository users, AnimalRepository animals, RescueCaseRepository cases,
                           AdoptionApplicationRepository applications, VolunteerRepository volunteers,
                           DonationRepository donations) {
        this.users = users;
        this.animals = animals;
        this.cases = cases;
        this.applications = applications;
        this.volunteers = volunteers;
        this.donations = donations;
    }

    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        AdminUser user = users.findByUsername(username).orElse(null);
        if (user == null || !user.getPassword().equals(PasswordUtil.sha256(password))) {
            model.addAttribute("error", "用户名或密码错误");
            return "admin/login";
        }
        session.setAttribute("adminUser", user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String dashboard(HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("animalCount", animals.count());
        model.addAttribute("waitingCount", animals.findByStatusOrderByCreatedAtDesc("待领养").size());
        model.addAttribute("caseCount", cases.findAll().stream().filter(item -> !"已完成".equals(item.getStatus())).count());
        model.addAttribute("pendingCount", applications.countByStatus("待审核"));
        model.addAttribute("donationTotal", donations.totalAmount());
        return "admin/dashboard";
    }

    @GetMapping("/admin/animals")
    public String animalManage(@RequestParam(required = false) Long editId, HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("animals", animals.findAll());
        model.addAttribute("animal", editId == null ? new Animal() : animals.findById(editId).orElse(new Animal()));
        return "admin/animals";
    }

    @PostMapping("/admin/animals")
    public String saveAnimal(@Valid @ModelAttribute Animal animal, BindingResult result, HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("animals", animals.findAll());
            return "admin/animals";
        }
        animals.save(animal);
        return "redirect:/admin/animals";
    }

    @PostMapping("/admin/animals/{id}/delete")
    public String deleteAnimal(@PathVariable Long id, HttpSession session) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        animals.deleteById(id);
        return "redirect:/admin/animals";
    }

    @GetMapping("/admin/cases")
    public String caseManage(@RequestParam(required = false) Long editId, HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("cases", cases.findAllByOrderByReportedAtDesc());
        model.addAttribute("caseForm", editId == null ? new RescueCase() : cases.findById(editId).orElse(new RescueCase()));
        return "admin/cases";
    }

    @PostMapping("/admin/cases")
    public String saveCase(@Valid @ModelAttribute("caseForm") RescueCase rescueCase, BindingResult result, HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("cases", cases.findAllByOrderByReportedAtDesc());
            return "admin/cases";
        }
        cases.save(rescueCase);
        return "redirect:/admin/cases";
    }

    @PostMapping("/admin/cases/{id}/delete")
    public String deleteCase(@PathVariable Long id, HttpSession session) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        cases.deleteById(id);
        return "redirect:/admin/cases";
    }

    @GetMapping("/admin/applications")
    public String applicationManage(HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("applications", applications.findAllByOrderByCreatedAtDesc());
        return "admin/applications";
    }

    @PostMapping("/admin/applications/{id}/status")
    public String updateApplication(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        if (!APPLICATION_STATUSES.contains(status)) {
            return "redirect:/admin/applications";
        }
        applications.findById(id).ifPresent(application -> {
            application.setStatus(status);
            applications.save(application);
            Animal animal = application.getAnimal();
            if (animal != null) {
                boolean hasApprovedApplication = applications.countByAnimalAndStatus(animal, APPLICATION_APPROVED) > 0;
                if (hasApprovedApplication) {
                    animal.setStatus(ANIMAL_ADOPTED);
                } else if (ANIMAL_ADOPTED.equals(animal.getStatus())) {
                    animal.setStatus(ANIMAL_WAITING);
                }
                animals.save(animal);
            }
        });
        return "redirect:/admin/applications";
    }

    @GetMapping("/admin/volunteers")
    public String volunteerManage(HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("volunteers", volunteers.findAllByOrderByCreatedAtDesc());
        return "admin/volunteers";
    }

    @PostMapping("/admin/volunteers/{id}/status")
    public String updateVolunteer(@PathVariable Long id, @RequestParam String status, HttpSession session) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        volunteers.findById(id).ifPresent(volunteer -> {
            volunteer.setStatus(status);
            volunteers.save(volunteer);
        });
        return "redirect:/admin/volunteers";
    }

    @GetMapping("/admin/donations")
    public String donationManage(HttpSession session, Model model) {
        if (!loggedIn(session)) {
            return "redirect:/admin/login";
        }
        model.addAttribute("donations", donations.findAllByOrderByCreatedAtDesc());
        model.addAttribute("donationTotal", donations.totalAmount());
        return "admin/donations";
    }

    private boolean loggedIn(HttpSession session) {
        return session.getAttribute("adminUser") != null;
    }
}
