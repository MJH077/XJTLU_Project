package com.cpt202.group21.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfigurationSource;
import com.cpt202.group21.service.AdministratorService;
import com.cpt202.group21.service.UserManageService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    // 👤 普通用户加密器
    @Bean
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 👮 管理员明文密码比对器
    @Bean
    public PasswordEncoder adminPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString(); // 不加密
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    // ✅ 普通用户认证器
    @Bean
    public AuthenticationProvider userAuthenticationProvider(UserManageService userManageService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userManageService);
        provider.setPasswordEncoder(userPasswordEncoder());
        return provider;
    }

    // ✅ 管理员认证器
    @Bean
    public AuthenticationProvider adminAuthenticationProvider(AdministratorService administratorService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(administratorService);
        provider.setPasswordEncoder(adminPasswordEncoder());
        return provider;
    }

    // 🔐 管理员安全链
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http,
                                                         AuthenticationProvider adminAuthenticationProvider) throws Exception {
        http
        .securityMatcher("/admin/**", "/administratorLogin", "/processAdminLogin", "/adminHome", "/administratorDashboard", "/userManagement/**", "/musicApproval/**")
            .authenticationProvider(adminAuthenticationProvider)
            .cors(cors -> cors.configurationSource(corsConfigurationSource)) 
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/administratorLogin").permitAll()
                .anyRequest().hasRole("ADMIN"))
            .formLogin(login -> login
                .loginPage("/administratorLogin")
                .loginProcessingUrl("/processAdminLogin")
                .usernameParameter("email")
                .defaultSuccessUrl("/adminHome", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/administratorLogout")
                .logoutSuccessUrl("/administratorLogin")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        return http.build();
    }

    // 用户安全链配置
    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http,
                                                    AuthenticationProvider userAuthenticationProvider) throws Exception {
        http
            // 1. 认证提供器配置
            .authenticationProvider(userAuthenticationProvider)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            // 2. CSRF配置（豁免API和静态资源）
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/api/**",               // 豁免所有API端点
                    "/userRegister/**",      // 豁免注册相关端点
                    "/css/**",              // 豁免CSS静态资源
                    "/js/**",               // 豁免JS静态资源
                    "/music/**",            // 豁免音乐文件访问
                    "/api/music/**"         // 豁免音乐播放器API交互
                )
            )
            
            // 3. 授权配置
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/userLogin", 
                    "/userRegister/**",
                    "/api/**",
                    "/css/**",
                    "/js/**",
                    "/userLocked",
                    "/music/**",            // 豁免音乐文件访问
                    "/api/music/**",         // 豁免音乐播放器API交互
                    "/static/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            
            // 4. 表单登录配置
            .formLogin(login -> login
                .loginPage("/userLogin")
                .loginProcessingUrl("/processLogin")
                .failureHandler(userAuthenticationFailureHandler())
                .usernameParameter("email")
                .defaultSuccessUrl("/userDashboard", true)
                .permitAll()
            )
            
            // 5. 注销配置
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/userLogin")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );
        
        return http.build();
    }

    private AuthenticationFailureHandler userAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            String redirectUrl = "/userLogin?error";
            
            // 解包异常根源
            Throwable cause = exception.getCause();
            if (cause instanceof LockedException) {
                redirectUrl = "/userLocked";
            } else if (exception instanceof BadCredentialsException || cause instanceof BadCredentialsException) {
                redirectUrl += "&message=Invalid%20credentials";
            } else if (cause instanceof UsernameNotFoundException) {
                redirectUrl += "&message=User%20not%20found";
            }
            
            response.sendRedirect(redirectUrl);
        };
    }
}