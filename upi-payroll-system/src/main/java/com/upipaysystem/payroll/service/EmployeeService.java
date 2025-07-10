package com.upipaysystem.payroll.service;

import com.upipaysystem.payroll.dtos.auth.RegistrationResponse;
import com.upipaysystem.payroll.dtos.employee.*;
import com.upipaysystem.payroll.exceptions.RazorpayServiceException;
import com.upipaysystem.payroll.exceptions.UserAlreadyExistsException;
import com.upipaysystem.payroll.exceptions.VerificationTokenExpiredException;
import com.upipaysystem.payroll.model.*;
import com.upipaysystem.payroll.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.upipaysystem.payroll.model.EmployeeDetails.Gender.*;
import static com.upipaysystem.payroll.model.User.Role.EMPLOYEE;


//ADMIN access only except verify-email
//api/
@Service
public class EmployeeService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final PendingUserRepository pendingUserRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder encoder;
    private final EmployeeDetailsRepository employeeDetailsRepository;
    private final PendingEmployeeDetailsRepository pendingEmployeeDetailsRepository;
   // private final RazorpayService razorpayService;

    public EmployeeService(UserRepository userRepository,
                           PendingUserRepository pendingUserRepository,
                           EmailService emailService,
                           OrganizationRepository organizationRepository,
                           PendingEmployeeDetailsRepository pendingEmployeeDetailsRepository,
                           EmployeeDetailsRepository employeeDetailsRepository
                          // RazorpayService razorpayService

    ) {
        this.userRepository = userRepository;
        this.pendingUserRepository = pendingUserRepository;
        this.emailService = emailService;
        this.encoder = new BCryptPasswordEncoder(12); // Initialize BCryptPasswordEncoder here
        this.organizationRepository = organizationRepository;
        this.pendingEmployeeDetailsRepository = pendingEmployeeDetailsRepository;
        this.employeeDetailsRepository = employeeDetailsRepository;
     //   this.razorpayService = razorpayService;
    }


    public RegistrationResponse registerEmployee (
            EmployeeRegisterRequest request,
            UserPrinciple userPrinciple
    ) throws UserAlreadyExistsException,UsernameNotFoundException {

        String token = UUID.randomUUID().toString();
        String baseUrl = "https://localhost:8080/api/employee/verify-email";
        String verificationLink = baseUrl + "?token=" + token;
        String htmlContent = getVerificationLinkEmailTemplate(verificationLink);

        //--> user with email already exist or not

        String email = request.getEmail().toLowerCase().trim();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User exist with the email");  // Custom Exception Handled by GEH
        }

        //Check if the organization exist or not
        Long organizationId = userPrinciple.getOrganizationId();
        Optional<Organization> optionalOrg = organizationRepository.findById(organizationId);
        if(optionalOrg.isEmpty()){
            throw new UsernameNotFoundException("Organization not found.");
        }

        Organization organization = optionalOrg.get();

        PendingUser pendingUser = new PendingUser();
        pendingUser.setFullname(request.getFullName());
        pendingUser.setEmail(email);
        pendingUser.setPassword(encoder.encode(request.getPassword()));
        pendingUser.setVerificationToken(token);
        pendingUser.setTokenExpiry(LocalDateTime.now().plusHours(24));
        pendingUser.setCreatedAt(LocalDateTime.now());
        pendingUser.setOrganizationId(organizationId);
        pendingUserRepository.save(pendingUser);

        PendingEmployeeDetails pendingEmployeeDetails = new PendingEmployeeDetails();

        pendingEmployeeDetails.setDepartment(request.getDepartment());
        pendingEmployeeDetails.setDesignation(request.getDesignation());
        pendingEmployeeDetails.setGender(request.getGender());
        pendingEmployeeDetails.setSalary(request.getSalary());
        pendingEmployeeDetails.setJoiningDate(request.getJoiningDate());
        pendingEmployeeDetails.setPendingUser(pendingUser);
        pendingEmployeeDetails.setCreatedAt(LocalDateTime.now());
        pendingEmployeeDetailsRepository.save(pendingEmployeeDetails);

        //Sending Email
        try {
            emailService.sendEmail(
                    email,
                    "Email Verification link for StremPay employee",
                    htmlContent
            );

            return new RegistrationResponse(email, "Registration initiated. Verification email sent.", true);
        } catch (Exception e) {
            pendingUserRepository.delete(pendingUser);
            pendingEmployeeDetailsRepository.delete(pendingEmployeeDetails);
            return new RegistrationResponse(email, "Registration failed. Could not send verification email.", false);
        }
    }

    private String getVerificationLinkEmailTemplate(String baseUrl) throws RuntimeException {
        try{
            File templateFile = ResourceUtils.getFile("classpath:templates/verification_link_template.html");
            String htmlTemplate = new String(Files.readAllBytes(templateFile.toPath()), StandardCharsets.UTF_8);
            return htmlTemplate.replace("[[VERIFICATION_LINK]]", baseUrl);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    @Transactional
    public EmployeeDTO verifyEmployee(String token) throws VerificationTokenExpiredException {

        //checking if is there any pending user with this token
        PendingUser pendingUser = pendingUserRepository.findByVerificationToken(token)
                .orElseThrow(() -> new VerificationTokenExpiredException("Invalid verification token."));

        if (pendingUser.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenExpiredException("Verification token expired");
        }

        Organization organization = organizationRepository.findById(pendingUser.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("No Organization found"));

        PendingEmployeeDetails pendingEmployeeDetails = pendingEmployeeDetailsRepository
                .findByPendingUser_VerificationToken(token);
        if (pendingEmployeeDetails == null) {
            throw new RuntimeException("Pending employee details not found");
        }

        User newUser = new User();
        newUser.setFullName(pendingUser.getFullname());
        newUser.setEmail(pendingUser.getEmail());
        newUser.setOrganization(organization);
        newUser.setRole(EMPLOYEE);
        newUser.setPassword(pendingUser.getPassword());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setActive(false);

        User savedUser = userRepository.save(newUser);

        EmployeeDetails employeeDetails = new EmployeeDetails();
        employeeDetails.setUser(savedUser);
        employeeDetails.setGender(pendingEmployeeDetails.getGender());
        employeeDetails.setDepartment(pendingEmployeeDetails.getDepartment());
        employeeDetails.setSalary(pendingEmployeeDetails.getSalary());
        employeeDetails.setDesignation(pendingEmployeeDetails.getDesignation());
        employeeDetails.setJoiningDate(pendingEmployeeDetails.getJoiningDate());
        employeeDetails.setGender(pendingEmployeeDetails.getGender());

        employeeDetailsRepository.save(employeeDetails);


        pendingEmployeeDetailsRepository.delete(pendingEmployeeDetails);
        pendingUserRepository.delete(pendingUser);

        return new EmployeeDTO(savedUser, employeeDetails);
    }
    /*
Key Points and Suggestions:
        The code is well-structured, readable, and follows Spring best practices.
        Error handling is robust, with custom exceptions and appropriate use of Optional.
        The use of BCryptPasswordEncoder for password hashing is secure.
        The transactional management of the verifyEmployee method ensures data consistency.
        Email sending is handled by an EmailService, promoting modularity.
        The getAllEmployee method efficiently retrieves and maps employee data.
Suggestions for Potential Enhancements:
    Further Optimize getAllEmployee:   -> Consider using a JOIN fetch in your repository query in the getAllEmployee method.
        This might reduce the number of database queries. For example, you could add a method to your UserRepository
        @Query("SELECT u FROM User u JOIN FETCH u.employeeDetails WHERE u.organization = :organization AND u.role = :role")
        List<User> findAllByOrganizationAndRoleWithDetails(@Param("organization") Organization organization, @Param("role")
        Role role);
        and then adjust the service method. This is database-specific and might not be needed for smaller datasets.
    Centralized Configuration:   -> Externalize the base URL (https://localhost:8080/api/employee/verify-email) to a
        configuration file (e.g., application.properties or application.yml).
    Logging:  ->  Implement logging (e.g., using Spring's slf4j) to track important events such as successful
        registrations, failed verifications, and errors.
    Asynchronous Email:  ->  For improved performance, make the email sending asynchronous.
    Use of Enums:  ->  If you have a fixed set of roles, you could use an enum for the employee role.


    *  */



    public List<EmployeeDetailsDTO> getAllEmployee(UserPrinciple userPrinciple) {
        Optional<Organization> optionalOrg = userPrinciple.getOrganization();
        if(optionalOrg.isEmpty()){
            throw new RuntimeException("Organization Not found");
        }
        Organization organization = optionalOrg.get();

        List<User> employees = userRepository.findAllByOrganizationAndRole(organization, EMPLOYEE);
        if (employees.isEmpty()) {
            return Collections.emptyList(); // Return empty list if no employees found
        }

        // Step 2: Get all employee details
        List<Long> employeeIds = employees.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        List<EmployeeDetails> employeeDetailsList = employeeDetailsRepository.findAllByUserIdIn(employeeIds);

        // Step 3: Map User and EmployeeDetails to EmployeeDetailsDTO
        Map<Long, User> userMap = employees.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<EmployeeDetailsDTO> employeeDetailsDTOs = employeeDetailsList.stream()
                .map(details -> {
                    User user = userMap.get(details.getUser().getId());
                    return new EmployeeDetailsDTO(user, details);
                })
                .collect(Collectors.toList());

        return employeeDetailsDTOs;
    }

//    @Transactional
//    public EmployeePaymentAccountDto setPaymentAccount(
//            UserPrinciple userPrinciple, EmployeeAccountRequest request) {
//      User user = userRepository.findByEmail(userPrinciple.getUsername()).orElseThrow(
//                () -> new UserAlreadyExistsException("User not found"));
//      String contactId = razorpayService.createContact(user.getEmail(),user.getFullName(),user.getId().toString());
//
//    }
}
