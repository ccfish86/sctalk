package net.ccfish.talk.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@MapperScan(basePackages = {"com.webjava.kernel.mybatis.mapper"})
@ComponentScan(basePackages = {"com.webjava"})
@SpringBootApplication
public class TalkAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkAdminApplication.class, args);
	}

    /**
     * 用户密码加密/解密类
     * 
     * @return 用户密码处理相关类
     * @since 1.0
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}
