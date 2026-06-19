package com.rescue.shixun.config;

import com.rescue.shixun.model.AdminUser;
import com.rescue.shixun.model.Animal;
import com.rescue.shixun.model.Donation;
import com.rescue.shixun.model.RescueCase;
import com.rescue.shixun.model.Volunteer;
import com.rescue.shixun.repository.AdminUserRepository;
import com.rescue.shixun.repository.AnimalRepository;
import com.rescue.shixun.repository.DonationRepository;
import com.rescue.shixun.repository.RescueCaseRepository;
import com.rescue.shixun.repository.VolunteerRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initData(AdminUserRepository users, AnimalRepository animals, RescueCaseRepository cases,
                               VolunteerRepository volunteers, DonationRepository donations) {
        return args -> {
            if (users.count() == 0) {
                AdminUser admin = new AdminUser();
                admin.setUsername("admin");
                admin.setPassword(PasswordUtil.sha256("admin123"));
                admin.setDisplayName("救助站管理员");
                admin.setRole("ADMIN");
                users.save(admin);
            }
            if (animals.count() == 0) {
                animals.save(animal("团团", "猫", "中华田园猫", "雌", 1, "待领养",
                        "城南社区救助点", "已驱虫，轻微营养不良恢复中",
                        "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?auto=format&fit=crop&w=900&q=80",
                        "亲人、安静，适合有养猫经验的家庭。"));
                animals.save(animal("豆豆", "狗", "混血犬", "雄", 3, "待领养",
                        "河滨公园临时安置点", "疫苗齐全，后腿旧伤已恢复",
                        "https://images.unsplash.com/photo-1552053831-71594a27632d?auto=format&fit=crop&w=900&q=80",
                        "性格活泼，会基础牵引，适合经常运动的家庭。"));
                animals.save(animal("小橘", "猫", "橘猫", "雄", 2, "康复中",
                        "东门动物医院", "皮肤病治疗中",
                        "https://images.unsplash.com/photo-1574158622682-e40e69881006?auto=format&fit=crop&w=900&q=80",
                        "需要继续药浴和观察，暂不开放领养。"));
            }
            if (cases.count() == 0) {
                cases.save(rescueCase("雨夜受伤幼猫救助", "未命名幼猫", "人民路公交站附近", "高", "处理中",
                        "李同学", "13800000001", "幼猫躲在站牌后，前爪疑似受伤，已联系志愿者前往。"));
                cases.save(rescueCase("校园流浪犬绝育回访", "黑背混血犬", "北区操场", "中", "已完成",
                        "王老师", "13800000002", "完成捕捉、绝育、疫苗和原地放归，后续定期回访。"));
            }
            if (volunteers.count() == 0) {
                Volunteer volunteer = new Volunteer();
                volunteer.setName("赵志愿");
                volunteer.setPhone("13700000001");
                volunteer.setSkill("摄影、接送");
                volunteer.setAvailableTime("周末上午");
                volunteer.setStatus("已联系");
                volunteers.save(volunteer);
            }
            if (donations.count() == 0) {
                Donation donation = new Donation();
                donation.setDonorName("爱心人士A");
                donation.setDonationType("资金");
                donation.setAmount(new BigDecimal("200.00"));
                donation.setMessage("用于购买猫粮");
                donations.save(donation);
            }
        };
    }

    private Animal animal(String name, String species, String breed, String gender, Integer age, String status,
                          String location, String health, String imageUrl, String description) {
        Animal animal = new Animal();
        animal.setName(name);
        animal.setSpecies(species);
        animal.setBreed(breed);
        animal.setGender(gender);
        animal.setAge(age);
        animal.setStatus(status);
        animal.setLocation(location);
        animal.setHealthStatus(health);
        animal.setImageUrl(imageUrl);
        animal.setDescription(description);
        return animal;
    }

    private RescueCase rescueCase(String title, String animalName, String location, String urgency, String status,
                                  String contactName, String contactPhone, String description) {
        RescueCase rescueCase = new RescueCase();
        rescueCase.setTitle(title);
        rescueCase.setAnimalName(animalName);
        rescueCase.setLocation(location);
        rescueCase.setUrgency(urgency);
        rescueCase.setStatus(status);
        rescueCase.setContactName(contactName);
        rescueCase.setContactPhone(contactPhone);
        rescueCase.setDescription(description);
        return rescueCase;
    }
}
