package oit.is.quizknockn.yonhaya.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login
        .permitAll())
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")) // ログアウト後に / にリダイレクト
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(AntPathRequestMatcher.antMatcher("/yonhaya/**"))
            .authenticated() // /sample3/以下は認証済みであること
            .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
            .permitAll())// 上記以外は全員アクセス可能
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")))// h2-console用にCSRF対策を無効化
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));
    return http.build();
  }

  /**
   * 認証処理に関する設定（誰がどのようなロールでログインできるか）
   *
   * @return
   */
  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    // ユーザ名，パスワード，ロールを指定してbuildする
    // このときパスワードはBCryptでハッシュ化されているため，{bcrypt}とつける
    // ハッシュ化せずに平文でパスワードを指定する場合は{noop}をつける
    // user1~4/isdev, owner/isdev24

    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2y$05$0BbAzcvroHopgw15fC9sMult03PtKZB42eiHuDNBVH5sbeepKIgna").roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2y$05$zpQ9uf4IQLTzXv0lT0Eile6U1B/vcC49Ar7pmJsGC1j0b1TX4NflO").roles("USER").build();
    UserDetails user3 = User.withUsername("user3")
        .password("{bcrypt}$2y$05$rqIxG6nPTvCdI.s2E4tno.0/zyC25EtOdm.rUQpwh0QVBske5UB7m").roles("USER").build();
    UserDetails user4 = User.withUsername("user4")
        .password("{bcrypt}$2y$05$AWqGLaZDQL0JFGxjfKq49.tcbd51kja1U6fCx2dpalde06iC6d.0O").roles("USER").build();
    UserDetails owner = User.withUsername("owner")
        .password("{bcrypt}$2y$05$H0OkImCYjg3.h8GK/.2Za.SIE9YYpsX1owWtr.CvedI0YZrZtRhiS").roles("OWNER").build();
    // 生成したユーザをImMemoryUserDetailsManagerに渡す（いくつでも良い）
    return new InMemoryUserDetailsManager(user1, user2, user3, user4, owner);
  }
}
