package com.rems.favorite.service;

import com.rems.favorite.service.impl.FavoriteServiceImpl;
import com.rems.property.dto.PropertyCardDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteServiceImpl favoriteService;

    private PropertyCardDTO mockProperty;

    @BeforeEach
    public void setUp() {
        mockProperty = new PropertyCardDTO();
        mockProperty.setId(1L);
        mockProperty.setTitle("Test Property");
        mockProperty.setAddress("Test Address");
        mockProperty.setPrice(new BigDecimal("100000"));
        mockProperty.setType("SALE");
        mockProperty.setThumbnail("thumbnail.jpg");
    }

    @Test
    public void testAddFavorite() {
        // Arrange
        Long customerId = 1L;
        Long propertyId = 1L;

        // Act & Assert
        assertDoesNotThrow(() -> {
            favoriteService.addFavorite(customerId, propertyId);
        });
    }

    @Test
    public void testRemoveFavorite() {
        // Arrange
        Long customerId = 1L;
        Long propertyId = 1L;

        // Act & Assert
        assertDoesNotThrow(() -> {
            favoriteService.removeFavorite(customerId, propertyId);
        });
    }

    @Test
    public void testIsFavorite() {
        // Arrange
        Long customerId = 1L;
        Long propertyId = 1L;

        // Mock the behavior
        when(favoriteService.isFavorite(customerId, propertyId)).thenReturn(true);

        // Act & Assert
        assertTrue(favoriteService.isFavorite(customerId, propertyId));
    }

    @Test
    public void testGetFavorites() {
        // Arrange
        Long customerId = 1L;
        List<PropertyCardDTO> expectedFavorites = Arrays.asList(mockProperty);

        // Mock the behavior
        when(favoriteService.getFavorites(customerId)).thenReturn(expectedFavorites);

        // Act
        List<PropertyCardDTO> actualFavorites = favoriteService.getFavorites(customerId);

        // Assert
        assertNotNull(actualFavorites);
        assertEquals(1, actualFavorites.size());
        assertEquals(mockProperty.getTitle(), actualFavorites.get(0).getTitle());
    }
}
