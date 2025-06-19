package com.ohgiraffers.associationmapping.section01.manytoone;

import org.springframework.stereotype.Service;

@Service
public class ManyToOneService {

    private ManyToOneRepository manyToOneRepository;

    public ManyToOneService(ManyToOneRepository manyToOneRepository) {
        this.manyToOneRepository = manyToOneRepository;
    }

    public Menu findMenu(int menuCode) {
        return manyToOneRepository.find(menuCode);
    }

    public String findCategoryNameByJpql(int menuCode) {
        return manyToOneRepository.findCategoryName(menuCode);
    }
}
