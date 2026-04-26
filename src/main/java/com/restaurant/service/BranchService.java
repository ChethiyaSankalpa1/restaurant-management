package com.restaurant.service;

import com.restaurant.model.Branch;
import com.restaurant.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public List<Branch> getAllBranches() { return branchRepository.findAll(); }
    public Optional<Branch> findById(String id) { return branchRepository.findById(id); }
    public Branch save(Branch b) { return branchRepository.save(b); }
    public void delete(String id) { branchRepository.deleteById(id); }
    public List<Branch> getActiveBranches() { return branchRepository.findByStatus("active"); }
}
