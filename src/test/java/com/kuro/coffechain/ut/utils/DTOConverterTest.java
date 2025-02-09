package com.kuro.coffechain.ut.utils;

import com.kuro.coffechain.utils.DTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DTOConverterTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DTOConverter dtoConverter;

    private TestEntity testEntity;
    private TestDTO testDTO;

    @BeforeEach
    void setUp() {
        testEntity = new TestEntity(1L, "John Doe");
        testDTO = new TestDTO(1L, "John Doe");
    }

    @Test
    void testConvertToDTO() {
        when(modelMapper.map(testEntity, TestDTO.class)).thenReturn(testDTO);

        TestDTO result = dtoConverter.convertToDTO(testEntity, TestDTO.class);

        assertEquals(testDTO.getId(), result.getId());
        assertEquals(testDTO.getName(), result.getName());
    }

    @Test
    void testConvertToEntity() {
        when(modelMapper.map(testDTO, TestEntity.class)).thenReturn(testEntity);

        TestEntity result = dtoConverter.convertToEntity(testDTO, TestEntity.class);

        assertEquals(testEntity.getId(), result.getId());
        assertEquals(testEntity.getName(), result.getName());
    }

    // Sample DTO and Entity classes for testing
    static class TestEntity {
        private Long id;
        private String name;

        public TestEntity(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }

    static class TestDTO {
        private Long id;
        private String name;

        public TestDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
    }
}

