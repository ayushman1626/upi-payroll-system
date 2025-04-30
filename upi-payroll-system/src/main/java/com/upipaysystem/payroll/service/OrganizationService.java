package com.upipaysystem.payroll.service;

import com.upipaysystem.payroll.dtos.organization.OrganizationDetailsDTO;
import com.upipaysystem.payroll.dtos.organization.OrganizationSummaryDTO;
import com.upipaysystem.payroll.dtos.organization.OrganizationRegisterRequest;
import com.upipaysystem.payroll.exceptions.OrganizationExistException;
import com.upipaysystem.payroll.model.Address;
import com.upipaysystem.payroll.model.Organization;
import com.upipaysystem.payroll.model.User;
import com.upipaysystem.payroll.model.UserPrinciple;
import com.upipaysystem.payroll.repo.OrganizationRepository;
import com.upipaysystem.payroll.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    private OrganizationRepository organizationRepository;
    private UserRepository userRepository;
    public OrganizationService(
            OrganizationRepository organizationRepository,
            UserRepository userRepository
        ){
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrganizationSummaryDTO registerOrganization(
            OrganizationRegisterRequest request,
            UserPrinciple userPrinciple
        )throws UsernameNotFoundException {

        Optional<User> OptionalUser = userRepository.findByEmail(userPrinciple.getUsername());
        if(OptionalUser.isEmpty())
            throw new UsernameNotFoundException("User not found ");

        User user = OptionalUser.get();

        if(user.getOrganization() != null){
            throw new OrganizationExistException("Organization Already Exist.");
        }

        Organization organization = new Organization();
        Address address = new Address();

        organization.setName(request.getName());
        organization.setOrganizationLocation(request.getOrganizationLocation());
        organization.setIndustry(request.getIndustry());
        organization.setGstNumber(request.getGstNumber());
        organization.setRegisteredEmail(request.getRegisteredEmail());
        organization.setCreatedAt(LocalDateTime.now());

        address.setAddressLineOne(request.getAddress().getAddressLineOne());
        address.setAddressLineTwo(request.getAddress().getAddressLineTwo());
        address.setCity(request.getAddress().getCity());
        address.setState(request.getAddress().getState());
        address.setPinCode(request.getAddress().getPinCode());

        address.setOrganization(organization); // This is essential!
        organization.setAddress(address);

        organizationRepository.save(organization);

        user.setRole(User.Role.ADMIN);
        user.setOrganization(organization);
        userRepository.save(user);

        return new OrganizationSummaryDTO(organization);
    }

    public OrganizationDetailsDTO getOrganization(UserPrinciple userPrinciple){

        User user = userRepository.findByEmail(userPrinciple.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email"));

        Organization organization = organizationRepository.findById(userPrinciple.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found for the user"));

        return new OrganizationDetailsDTO(organization);

    }
}
