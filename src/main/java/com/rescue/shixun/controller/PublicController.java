package com.rescue.shixun.controller;

import com.rescue.shixun.model.AdoptionApplication;
import com.rescue.shixun.model.Animal;
import com.rescue.shixun.model.Donation;
import com.rescue.shixun.model.RescueCase;
import com.rescue.shixun.model.Volunteer;
import com.rescue.shixun.repository.AdoptionApplicationRepository;
import com.rescue.shixun.repository.AnimalRepository;
import com.rescue.shixun.repository.DonationRepository;
import com.rescue.shixun.repository.RescueCaseRepository;
import com.rescue.shixun.repository.VolunteerRepository;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {
    private final AnimalRepository animals;
    private final RescueCaseRepository cases;
    private final AdoptionApplicationRepository applications;
    private final VolunteerRepository volunteers;
    private final DonationRepository donations;

    public PublicController(AnimalRepository animals, RescueCaseRepository cases,
                            AdoptionApplicationRepository applications, VolunteerRepository volunteers,
                            DonationRepository donations) {
        this.animals = animals;
        this.cases = cases;
        this.applications = applications;
        this.volunteers = volunteers;
        this.donations = donations;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("animalCount", animals.count());
        model.addAttribute("waitingCount", animals.findByStatusOrderByCreatedAtDesc("待领养").size());
        model.addAttribute("caseCount", cases.findAll().stream().filter(item -> !"已完成".equals(item.getStatus())).count());
        model.addAttribute("volunteerCount", volunteers.count());
        model.addAttribute("donationTotal", donations.totalAmount());
        model.addAttribute("animals", animals.findByStatusOrderByCreatedAtDesc("待领养"));
        model.addAttribute("cases", cases.findAllByOrderByReportedAtDesc());
        return "index";
    }

    @GetMapping("/animals")
    public String animalList(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String status,
                             Model model) {
        List<Animal> result;
        if (status != null && !status.trim().isEmpty()) {
            result = animals.findByStatusOrderByCreatedAtDesc(status);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            result = animals.findByNameContainingIgnoreCaseOrSpeciesContainingIgnoreCaseOrBreedContainingIgnoreCaseOrLocationContainingIgnoreCaseOrderByCreatedAtDesc(
                    keyword, keyword, keyword, keyword);
        } else {
            result = animals.findAll();
        }
        model.addAttribute("animals", result);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "animals";
    }

    @GetMapping("/animals/{id}")
    public String animalDetail(@PathVariable Long id, Model model) {
        model.addAttribute("animal", animals.findById(id).orElseThrow(IllegalArgumentException::new));
        return "animal-detail";
    }

    @GetMapping("/adoptions/apply/{animalId}")
    public String adoptionForm(@PathVariable Long animalId, Model model) {
        model.addAttribute("animal", animals.findById(animalId).orElseThrow(IllegalArgumentException::new));
        model.addAttribute("application", new AdoptionApplication());
        return "adoption-form";
    }

    @PostMapping("/adoptions/apply/{animalId}")
    public String submitAdoption(@PathVariable Long animalId, @Valid @ModelAttribute("application") AdoptionApplication application,
                                 BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        Animal animal = animals.findById(animalId).orElseThrow(IllegalArgumentException::new);
        if (result.hasErrors()) {
            model.addAttribute("animal", animal);
            return "adoption-form";
        }
        application.setAnimal(animal);
        application.setStatus("待审核");
        applications.save(application);
        redirectAttributes.addFlashAttribute("success", "领养申请已提交，管理员会尽快联系您。");
        return "redirect:/animals/" + animalId;
    }

    @GetMapping("/rescue-cases")
    public String rescueCases(Model model) {
        model.addAttribute("cases", cases.findAllByOrderByReportedAtDesc());
        return "cases";
    }

    @GetMapping("/rescue-cases/report")
    public String reportCaseForm(Model model) {
        model.addAttribute("caseForm", new RescueCase());
        return "report-case";
    }

    @PostMapping("/rescue-cases/report")
    public String reportCase(@Valid @ModelAttribute("caseForm") RescueCase rescueCase,
                             BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "report-case";
        }
        rescueCase.setStatus("待处理");
        cases.save(rescueCase);
        redirectAttributes.addFlashAttribute("success", "救助线索已登记，志愿者会尽快核实。");
        return "redirect:/rescue-cases/report";
    }

    @GetMapping("/volunteers/apply")
    public String volunteerForm(Model model) {
        model.addAttribute("volunteer", new Volunteer());
        return "volunteer-form";
    }

    @PostMapping("/volunteers/apply")
    public String volunteerApply(@Valid @ModelAttribute Volunteer volunteer, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "volunteer-form";
        }
        volunteer.setStatus("待联系");
        volunteers.save(volunteer);
        redirectAttributes.addFlashAttribute("success", "志愿者报名已提交。");
        return "redirect:/volunteers/apply";
    }

    @GetMapping("/donations/create")
    public String donationForm(Model model) {
        Donation donation = new Donation();
        donation.setDonorName("匿名");
        donation.setDonationType("资金");
        model.addAttribute("donation", donation);
        return "donation-form";
    }

    @PostMapping("/donations/create")
    public String createDonation(@Valid @ModelAttribute Donation donation, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if ("资金".equals(donation.getDonationType()) && donation.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            result.rejectValue("amount", "amount.invalid", "资金捐赠金额必须大于 0");
        }
        if (result.hasErrors()) {
            return "donation-form";
        }
        donations.save(donation);
        redirectAttributes.addFlashAttribute("success", "感谢您的爱心支持，捐赠记录已登记。");
        return "redirect:/donations/create";
    }
}
