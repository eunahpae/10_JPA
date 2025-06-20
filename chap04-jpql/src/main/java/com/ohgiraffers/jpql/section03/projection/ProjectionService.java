package com.ohgiraffers.jpql.section03.projection;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectionService {

    private ProjectionRepository projectionRepository;

    public ProjectionService(ProjectionRepository projectionRepository) {
        this.projectionRepository = projectionRepository;
    }

    @Transactional
    public List<Menu> singleEntityProjection() {
        List<Menu> menuList = projectionRepository.singleEntityProjection();
        menuList.get(0).setMenuName("맛있는 삼겹살");
        return menuList;
    }
}
