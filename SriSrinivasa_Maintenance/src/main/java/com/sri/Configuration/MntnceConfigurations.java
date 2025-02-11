package com.sri.Configuration;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sri.Filters.JWTRequestFilter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class MntnceConfigurations {
	
	@Autowired
	JWTRequestFilter jwtFilter;

    @Autowired
    HttpServletRequest request;
    
	//For Authentication and Authorization
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(request->
		request
		.requestMatchers("/maintenance/eqpmnt/**","/maintenance/equipmentDashboard","/maintenance/maintenanceDashboard","/maintenance/",		
					"/maintenance/mntnce/getWorkOrder/**","/maintenance/mntnce/breakdown","/maintenance/mntnce/workOrderList",
					"/maintenance/sprvsr/viewTeam/**","/maintenance/sprvsr/viewTeammatePage","/maintenance/sprvsr/feedbackAndStatus"
				).hasAnyAuthority("MAINTENANCE_MANAGER","MAINTENANCE_SUPERVISOR","GENERAL_MANAGER")
		
		.requestMatchers("/maintenance/mntnce/mntncTasks","/maintenance/mntnce/registerWorkOrder/**","/maintenance/mntnce/registerMaterialPage/**","/maintenance/mntnce/registerMaterialQuantityPage",
				"/maintenance/mntnce/registerMaterialQuantity","/maintenance/mntnce/approveBreakdown/**","/maintenance/mntnce/requestNewMaterialPage/**","/maintenance/mntnce/requestNewMaterial","/maintenance/mntnce/breakdownlist",
				"/maintenance/sprvsr/team/**","/maintenance/sprvsr/assignSprvsr","/maintenance/mntnce/newProductReqList","/maintenance/sprvsr/empList"
				).hasAnyAuthority("MAINTENANCE_MANAGER","GENERAL_MANAGER")	
		
		.anyRequest().authenticated()
		).csrf(csrf->csrf.disable())
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
		.exceptionHandling(exception->exception.accessDeniedPage("/maintenance/error-403"));
		
		return http.build();
	}
	
	//to add token cookie to Feign client requests
    @Bean
    RequestInterceptor tokenInterceptor() {
	    return new RequestInterceptor() {
	        @Override
	        public void apply(RequestTemplate requestTemplate) {
 	            String jwtToken = getJwtTokenFromCookie();
	            
 	            if (jwtToken != null) {
	            	 requestTemplate.header("Cookie", "Authorization=" + jwtToken);	            
	            	 }
	        }
	        private String getJwtTokenFromCookie() {
	            if (request.getCookies() != null) {
	                for (Cookie cookie : request.getCookies()) {
	                    if (cookie.getName().equals("Authorization")) {
	                        return cookie.getValue(); // JWT token stored in the cookie
	                    }
	                }
	            }
	            return null;  
	        }
	    };
	}
	
	
}
