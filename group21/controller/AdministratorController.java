package com.cpt202.group21.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.web.csrf.CsrfToken;
import com.cpt202.group21.model.Administrator;
import com.cpt202.group21.service.AdministratorService;
import com.cpt202.group21.service.MusicManageService;
import com.cpt202.group21.service.UserManageService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdministratorController {
    @Autowired
    private AdministratorService adminService;
    @Autowired
    private MusicManageService musicManageService;
    @Autowired
    private UserManageService userManageService;
    

    // Management of music and user
    @GetMapping("/administratorDashboard")
    public String dashboard(Model model) {
        model.addAttribute("pendingMusicCount", musicManageService.countPendingApproval());
        model.addAttribute("activeUserCount", userManageService.countActiveUsers());
        model.addAttribute("lockedUserCount", userManageService.countLockedUsers());
        return "AdministratorDashboard";
    }
    @GetMapping("/musicApproval/approvalQueue")
    public String approvalQueue(Model model) {
        model.addAttribute("pendingMusic", musicManageService.getPendingApproval());
        return "MusicApproval";
    } 
    @GetMapping("/musicApproval/blockedContent")
    public String blockedContent(Model model) {
        model.addAttribute("blockedMusic", musicManageService.getBlockedContent());
        return "MusicApproval";
    }
    @GetMapping("/userManagement")
    public String userManagement(Model model, HttpServletRequest request) {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
            model.addAttribute("_csrf", csrfToken);
            model.addAttribute("users", userManageService.getAllUsers());
            return "UserManagement";
    }
    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id) {
        musicManageService.approveMusic(id);
        return "redirect:/MusicApproval";
    }
    @PostMapping("/block/{id}")
    public String block(@PathVariable Long id) {
        musicManageService.blockMusic(id);
        return "redirect:/MusicApproval";
    }
    @PostMapping("admin/lock/{id}")
    public String lockUser(@PathVariable Long id) {
        userManageService.lockUser(id);
        return "redirect:/userManagement";
    }
    @PostMapping("admin/unlock/{id}")
    public String unlockUser(@PathVariable Long id) {
        userManageService.unlockUser(id);
        return "redirect:/userManagement";
    }


    // Register and login of admin
    @GetMapping("/administratorRegister")
    public String getRegisterAdminForm(Model model){
        model.addAttribute("admin", new Administrator());
        return "AdministratorRegister"; 
    }
    @PostMapping("/administratorRegister")
    public String registerAdmin(@ModelAttribute Administrator admin){
        adminService.saveAdmin(admin);
        return "redirect:/AdministratorLogin"; 
    }
    @GetMapping("/administratorLogin")
    public String getLoginAdminForm(Model model, HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("_csrf", token);
        model.addAttribute("admin", new Administrator());
        return "AdministratorLogin";
    }
    @PostMapping("/AdministratorLogin")
    public String loginAdmin(@ModelAttribute Administrator admin, Model model){
        String result = adminService.loginAdmin(admin);
        switch(result){
            case "Administrator Not Found!" -> {
                model.addAttribute("error", "Administrator Not Found!");
                return "AdministratorLogin";
            }
            case "Password Mismatch!" -> {
                model.addAttribute("error", "Password Mismatch!");
                return "AdministratorLogin";
            } 
            case "Login Success!" -> {
                return "redirect:/AdministratorHome";
            }
            default -> {
                model.addAttribute("error", "Unknown Error!");
                return "AdministratorLogin";
            }
        }
    }
    @GetMapping("/adminHome")
    public String showAdminHomePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Administrator admin = adminService.getAdminByEmail(email);
    
        model.addAttribute("adminId", admin.getId());
        model.addAttribute("adminName", admin.getName());
        model.addAttribute("adminEmail", admin.getEmail());
    
        return "AdministratorHome";
    }
    @GetMapping("/musicApproval")
    public String redirectToApprovalQueue() {
        return "redirect:/musicApproval/approvalQueue";
    }
}