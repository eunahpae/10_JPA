package com.ohgiraffers.springdatajpa.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 공통 Bean 설정 클래스.
 * ModelMapper 등 애플리케이션 전역에서 사용할 공통 빈을 등록한다.
 */
@Configuration
public class BeanConfig {

    /**
     * ModelMapper 빈을 등록한다.
     * - 객체 간 매핑 시, getter/setter 대신 필드 접근을 허용하도록 설정
     * - private 필드에도 접근 가능하게 하여 매핑 유연성 확보
     *
     * @return 설정이 적용된 ModelMapper 인스턴스
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
            .setFieldAccessLevel(AccessLevel.PRIVATE)   // private 필드에도 접근 가능하도록 설정
            .setFieldMatchingEnabled(true);             // 필드 이름 기반 자동 매핑 활성화

        return modelMapper;
    }
}
