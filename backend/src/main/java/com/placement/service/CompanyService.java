package com.placement.service;

import com.placement.dto.CompanyRequest;
import com.placement.entity.Company;
import com.placement.entity.Coordinator;
import com.placement.exception.ResourceNotFoundException;
import com.placement.repository.CompanyRepository;
import com.placement.repository.CoordinatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CoordinatorRepository coordinatorRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));
    }

    public Company createCompany(Long coordinatorUserId, CompanyRequest request) {
        Coordinator coordinator = coordinatorRepository.findById(coordinatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Coordinator not found"));

        Company company = Company.builder()
                .name(request.name())
                .description(request.description())
                .website(request.website())
                .industry(request.industry())
                .hrContactName(request.hrContactName())
                .hrContactEmail(request.hrContactEmail())
                .hrContactPhone(request.hrContactPhone())
                .createdBy(coordinator)
                .build();
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, CompanyRequest request) {
        Company company = getCompanyById(id);
        company.setName(request.name());
        company.setDescription(request.description());
        company.setWebsite(request.website());
        company.setIndustry(request.industry());
        company.setHrContactName(request.hrContactName());
        company.setHrContactEmail(request.hrContactEmail());
        company.setHrContactPhone(request.hrContactPhone());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found");
        }
        companyRepository.deleteById(id);
    }
}
