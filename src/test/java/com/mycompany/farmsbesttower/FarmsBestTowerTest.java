/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.farmsbesttower;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.mockito.Mockito;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 *
 * @author elena
 */
public class FarmsBestTowerTest {
    private FarmsBestTower fetchBestTower;

    @BeforeEach
    void setUp() {
        fetchBestTower = new FarmsBestTower();
    }
    
    @Test
    public void testFetchBestTowerNoData() throws IOException {
        // This test checks the scenario when the data set is empty
        // Mock the HttpURLConnection to return an empty data set
        HttpURLConnection mockConnection = Mockito.mock(HttpURLConnection.class);
        InputStream stream = new ByteArrayInputStream("[]".getBytes());
        when(mockConnection.getInputStream()).thenReturn(stream);
        
        // Mock URL.openConnection() to return mockConnection
        URL url = Mockito.mock(URL.class);
        when(url.openConnection()).thenReturn(mockConnection);
        
        String bestTower = FarmsBestTower.fetchBestTower("some-farm-id");

        assertEquals("", bestTower);
    }
    
    @Test
    void testValidFarmId() {
        /* This test only works in the context of this technical task
        In a real-life scenario it would likely not work due to the volatile data */
        String farmId = "06fc1b97-d26a-4081-b527-50184c387d89";
        String expectedBestTower = "9c25ee42-8b79-4133-a11a-b6989293f927";
        try {
            String result = FarmsBestTower.fetchBestTower(farmId);
            assertEquals(expectedBestTower, result);
        } catch (IOException e) {
            fail("Test failed due to IOException.");
        }
    }
    @Test
    void testInvalidFarmId() {
        // This test checks the scenario when the farmId is invalid
        String farmId = "invalid-farm-id";
        String expectedBestTower = "";
        try {
            String result = FarmsBestTower.fetchBestTower(farmId);
            assertEquals(expectedBestTower, result);
        } catch (IOException e) {
            fail("Test failed due to IOException.");
        }
    }
}
