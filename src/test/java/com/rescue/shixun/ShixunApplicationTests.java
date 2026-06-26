package com.rescue.shixun;

import static org.assertj.core.api.Assertions.assertThat;

import com.rescue.shixun.config.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShixunApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    void hashesDefaultAdminPassword() {
        assertThat(PasswordUtil.sha256("admin123"))
                .isEqualTo("240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
    }
}
